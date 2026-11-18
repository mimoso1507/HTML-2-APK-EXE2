package com.h2a.converter;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Edita un AndroidManifest.xml binario (AXML): reemplaza textos, quita permisos y cambia enteros. */
public final class AxmlPatcher {
    public static final class Edits {
        public final Map<String, String> stringReplace = new HashMap<>();
        /** Permisos que se conservan; el resto de elementos uses-permission se elimina. null = conservar todos. */
        public Set<String> keepPermissions;
        /** Atributos enteros por nombre (versionCode, minSdkVersion, targetSdkVersion). */
        public final Map<String, Integer> intAttrs = new HashMap<>();
    }

    private static final int POOL = 0x0001, START = 0x0102, END = 0x0103;

    public static byte[] patch(byte[] in, Edits e) {
        ByteBuffer b = ByteBuffer.wrap(in).order(ByteOrder.LITTLE_ENDIAN);
        if ((b.getShort(0) & 0xffff) != 0x0003) throw new IllegalArgumentException("No es un AXML");
        int fileSize = b.getInt(4);
        List<byte[]> out = new ArrayList<>();
        List<String> strings = null;
        boolean utf8 = false;
        int poolSlot = -1;
        boolean skipping = false;
        int pos = 8;
        while (pos < fileSize) {
            int type = b.getShort(pos) & 0xffff;
            int size = b.getInt(pos + 4);
            if (type == POOL) {
                int count = b.getInt(pos + 8), styles = b.getInt(pos + 12), flags = b.getInt(pos + 16), start = b.getInt(pos + 20);
                if (styles != 0) throw new IllegalArgumentException("Estilos en el pool no soportados");
                utf8 = (flags & 0x100) != 0;
                strings = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    int off = pos + start + b.getInt(pos + 28 + 4 * i);
                    strings.add(utf8 ? readUtf8(b, off) : readUtf16(b, off));
                }
                poolSlot = out.size();
                out.add(null);
            } else if (type == START) {
                if (strings == null) throw new IllegalStateException("Pool no encontrado");
                byte[] c = new byte[size];
                b.position(pos); b.get(c);
                ByteBuffer cb = ByteBuffer.wrap(c).order(ByteOrder.LITTLE_ENDIAN);
                String el = strings.get(cb.getInt(20));
                int attrStart = cb.getShort(24) & 0xffff, attrSize = cb.getShort(26) & 0xffff, attrCount = cb.getShort(28) & 0xffff;
                boolean drop = false;
                for (int i = 0; i < attrCount; i++) {
                    int a = 16 + attrStart + i * attrSize;
                    String an = strings.get(cb.getInt(a + 4));
                    if (el.equals("uses-permission") && an.equals("name") && e.keepPermissions != null) {
                        int raw = cb.getInt(a + 8);
                        if (raw >= 0 && !e.keepPermissions.contains(strings.get(raw))) drop = true;
                    }
                    Integer iv = e.intAttrs.get(an);
                    if (iv != null && (el.equals("manifest") || el.equals("uses-sdk"))) {
                        cb.putInt(a + 8, -1);
                        cb.put(a + 15, (byte) 0x10);
                        cb.putInt(a + 16, iv);
                    }
                }
                if (drop) skipping = true; else out.add(c);
            } else if (type == END && skipping) {
                skipping = false;
            } else {
                byte[] c = new byte[size];
                b.position(pos); b.get(c);
                out.add(c);
            }
            pos += size;
        }
        if (strings == null) throw new IllegalStateException("Sin pool de cadenas");
        for (int i = 0; i < strings.size(); i++) {
            String r = e.stringReplace.get(strings.get(i));
            if (r != null) strings.set(i, r);
        }
        out.set(poolSlot, buildPool(strings, utf8));
        int total = 8;
        for (byte[] c : out) total += c.length;
        ByteBuffer res = ByteBuffer.allocate(total).order(ByteOrder.LITTLE_ENDIAN);
        res.putShort((short) 0x0003).putShort((short) 8).putInt(total);
        for (byte[] c : out) res.put(c);
        return res.array();
    }

    private static String readUtf8(ByteBuffer b, int off) {
        int l = b.get(off) & 0xff; off++;
        if ((l & 0x80) != 0) { off++; }
        int bl = b.get(off) & 0xff; off++;
        if ((bl & 0x80) != 0) { bl = ((bl & 0x7f) << 8) | (b.get(off) & 0xff); off++; }
        byte[] d = new byte[bl];
        for (int i = 0; i < bl; i++) d[i] = b.get(off + i);
        return new String(d, StandardCharsets.UTF_8);
    }

    private static String readUtf16(ByteBuffer b, int off) {
        int l = b.getShort(off) & 0xffff; off += 2;
        if ((l & 0x8000) != 0) { l = ((l & 0x7fff) << 16) | (b.getShort(off) & 0xffff); off += 2; }
        char[] ch = new char[l];
        for (int i = 0; i < l; i++) ch[i] = (char) (b.getShort(off + 2 * i) & 0xffff);
        return new String(ch);
    }

    private static byte[] buildPool(List<String> strings, boolean utf8) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        int[] offsets = new int[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            offsets[i] = data.size();
            String s = strings.get(i);
            if (utf8) {
                byte[] u = s.getBytes(StandardCharsets.UTF_8);
                writeLen8(data, s.length());
                writeLen8(data, u.length);
                data.write(u, 0, u.length);
                data.write(0);
            } else {
                int l = s.length();
                if (l > 0x7fff) {
                    data.write(((l >> 16) | 0x80) & 0xff); data.write((l >> 24) & 0xff);
                    data.write(l & 0xff); data.write((l >> 8) & 0xff);
                } else { data.write(l & 0xff); data.write((l >> 8) & 0xff); }
                for (int k = 0; k < l; k++) { char c = s.charAt(k); data.write(c & 0xff); data.write((c >> 8) & 0xff); }
                data.write(0); data.write(0);
            }
        }
        while (data.size() % 4 != 0) data.write(0);
        int count = strings.size();
        int stringsStart = 28 + 4 * count;
        int total = stringsStart + data.size();
        ByteBuffer r = ByteBuffer.allocate(total).order(ByteOrder.LITTLE_ENDIAN);
        r.putShort((short) POOL).putShort((short) 28).putInt(total).putInt(count).putInt(0)
                .putInt(utf8 ? 0x100 : 0).putInt(stringsStart).putInt(0);
        for (int o : offsets) r.putInt(o);
        r.put(data.toByteArray());
        return r.array();
    }

    private static void writeLen8(ByteArrayOutputStream o, int l) {
        if (l > 0x7f) { o.write(((l >> 8) | 0x80) & 0xff); o.write(l & 0xff); } else o.write(l);
    }
}
