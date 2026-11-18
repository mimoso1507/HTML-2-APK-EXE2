# Convertidor HTML → APK / EXE

1. Instala Node.js 18+.
2. `node server.js` y abre http://localhost:3000
3. Elige tu HTML (o carpeta), modo **MINIMA** (solo nombre/icono) o **COMPLETA** (todas las opciones) y pulsa *Generar*.
4. En `salida/<app>/` tendrás `android/` (Capacitor → APK) y `windows/` (Electron → EXE) con `construir.bat`/`construir.sh`.
   Sin Android SDK, sube la carpeta a GitHub: los workflows compilan APK y EXE en la nube.
