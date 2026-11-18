# HTML2APK-EXE

Convertidor de HTML a APK (Android) y EXE (Windows), con modos **MINIMA** y **COMPLETA**.

- `app/` – APK convertidora (se instala en el celular). Arma la APK del usuario dentro del teléfono: toma la plantilla, mete el HTML, parchea el manifiesto (permisos, id, versión), firma y permite instalar.
- `template/` – APK plantilla (WebView) que se incrusta en la convertidora.
- `desktop/` – versión para PC (`node server.js`).
- Para Windows, la APK genera un proyecto ZIP que GitHub Actions compila a `.exe`.

La APK convertidora se compila con GitHub Actions y se publica en *Releases* (`latest`).
