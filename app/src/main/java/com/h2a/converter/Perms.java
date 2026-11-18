package com.h2a.converter;

import java.util.*;

/** Permisos disponibles (clave de la interfaz -> permiso Android). */
public final class Perms {
  private Perms() {}
  public static final Map<String, String> MAP = new LinkedHashMap<>();
  public static final Set<String> RUNTIME = new HashSet<>();
  static {
    MAP.put("camera", "android.permission.CAMERA");
    MAP.put("microphone", "android.permission.RECORD_AUDIO");
    MAP.put("location", "android.permission.ACCESS_FINE_LOCATION");
    MAP.put("coarseLocation", "android.permission.ACCESS_COARSE_LOCATION");
    MAP.put("backgroundLocation", "android.permission.ACCESS_BACKGROUND_LOCATION");
    MAP.put("storage", "android.permission.READ_EXTERNAL_STORAGE");
    MAP.put("writeStorage", "android.permission.WRITE_EXTERNAL_STORAGE");
    MAP.put("manageStorage", "android.permission.MANAGE_EXTERNAL_STORAGE");
    MAP.put("images", "android.permission.READ_MEDIA_IMAGES");
    MAP.put("videos", "android.permission.READ_MEDIA_VIDEO");
    MAP.put("audio", "android.permission.READ_MEDIA_AUDIO");
    MAP.put("notifications", "android.permission.POST_NOTIFICATIONS");
    MAP.put("contacts", "android.permission.READ_CONTACTS");
    MAP.put("writeContacts", "android.permission.WRITE_CONTACTS");
    MAP.put("calendar", "android.permission.READ_CALENDAR");
    MAP.put("writeCalendar", "android.permission.WRITE_CALENDAR");
    MAP.put("phone", "android.permission.READ_PHONE_STATE");
    MAP.put("callPhone", "android.permission.CALL_PHONE");
    MAP.put("callLog", "android.permission.READ_CALL_LOG");
    MAP.put("sms", "android.permission.READ_SMS");
    MAP.put("sendSms", "android.permission.SEND_SMS");
    MAP.put("receiveSms", "android.permission.RECEIVE_SMS");
    MAP.put("bodySensors", "android.permission.BODY_SENSORS");
    MAP.put("activityRecognition", "android.permission.ACTIVITY_RECOGNITION");
    MAP.put("bluetooth", "android.permission.BLUETOOTH");
    MAP.put("bluetoothConnect", "android.permission.BLUETOOTH_CONNECT");
    MAP.put("bluetoothScan", "android.permission.BLUETOOTH_SCAN");
    MAP.put("nearbyWifi", "android.permission.NEARBY_WIFI_DEVICES");
    MAP.put("wifiState", "android.permission.ACCESS_WIFI_STATE");
    MAP.put("networkState", "android.permission.ACCESS_NETWORK_STATE");
    MAP.put("changeWifi", "android.permission.CHANGE_WIFI_STATE");
    MAP.put("nfc", "android.permission.NFC");
    MAP.put("vibrate", "android.permission.VIBRATE");
    MAP.put("wakeLock", "android.permission.WAKE_LOCK");
    MAP.put("bootCompleted", "android.permission.RECEIVE_BOOT_COMPLETED");
    MAP.put("foregroundService", "android.permission.FOREGROUND_SERVICE");
    MAP.put("overlay", "android.permission.SYSTEM_ALERT_WINDOW");
    MAP.put("flashlight", "android.permission.FLASHLIGHT");
    MAP.put("biometric", "android.permission.USE_BIOMETRIC");
    MAP.put("installPackages", "android.permission.REQUEST_INSTALL_PACKAGES");
    MAP.put("accounts", "android.permission.GET_ACCOUNTS");
    MAP.put("modifyAudio", "android.permission.MODIFY_AUDIO_SETTINGS");
    MAP.put("exactAlarm", "android.permission.SCHEDULE_EXACT_ALARM");
    RUNTIME.add("android.permission.CAMERA");
    RUNTIME.add("android.permission.RECORD_AUDIO");
    RUNTIME.add("android.permission.ACCESS_FINE_LOCATION");
    RUNTIME.add("android.permission.ACCESS_COARSE_LOCATION");
    RUNTIME.add("android.permission.ACCESS_BACKGROUND_LOCATION");
    RUNTIME.add("android.permission.READ_EXTERNAL_STORAGE");
    RUNTIME.add("android.permission.WRITE_EXTERNAL_STORAGE");
    RUNTIME.add("android.permission.READ_MEDIA_IMAGES");
    RUNTIME.add("android.permission.READ_MEDIA_VIDEO");
    RUNTIME.add("android.permission.READ_MEDIA_AUDIO");
    RUNTIME.add("android.permission.POST_NOTIFICATIONS");
    RUNTIME.add("android.permission.READ_CONTACTS");
    RUNTIME.add("android.permission.WRITE_CONTACTS");
    RUNTIME.add("android.permission.READ_CALENDAR");
    RUNTIME.add("android.permission.WRITE_CALENDAR");
    RUNTIME.add("android.permission.READ_PHONE_STATE");
    RUNTIME.add("android.permission.CALL_PHONE");
    RUNTIME.add("android.permission.READ_CALL_LOG");
    RUNTIME.add("android.permission.READ_SMS");
    RUNTIME.add("android.permission.SEND_SMS");
    RUNTIME.add("android.permission.RECEIVE_SMS");
    RUNTIME.add("android.permission.BODY_SENSORS");
    RUNTIME.add("android.permission.ACTIVITY_RECOGNITION");
    RUNTIME.add("android.permission.BLUETOOTH_CONNECT");
    RUNTIME.add("android.permission.BLUETOOTH_SCAN");
    RUNTIME.add("android.permission.NEARBY_WIFI_DEVICES");
    RUNTIME.add("android.permission.GET_ACCOUNTS");
  }

  public static boolean isRuntime(String perm) { return RUNTIME.contains(perm); }
}
