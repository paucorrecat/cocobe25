# cocobe25 (Android)

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

Aplicació Android per a ús docent. Permet gestionar llistes d’alumnes i professorat, incidències, importació/exportació de dades i estadístiques. Aquesta versió parteix d’un projecte funcional de l’any passat i en continua l’evolució.

> Nota sobre emmagatzematge: el projecte està adaptat a **Scoped Storage** (Android 11+).

---

## Funcionalitats principals

Manteniment d'incidències i d'actuacions
Manteniment d'alumnes

## Requisits

* **Android Studio** (Narwhal 2025.1.1 o superior).
* **Gradle Wrapper** 8.13 (inclòs al projecte).
* Dispositiu o emulador **Android 11** o superior (projecte provat a SDK 30).

## Instal·lació i arrencada

1. Clona el repositori:

   ```bash
   git clone https://github.com/<USER>/cocobe25.git
   cd cocobe25
   ```
2. Obre el directori a **Android Studio** i fes **Sync** de Gradle.
3. Executa **Run ▶** sobre un dispositiu o emulador.

## Configuració

* `local.properties` és generat per Android Studio (no es versiona).
* El projecte utilitza **AndroidX**.

## Compilació i paquets

* **Debug**:

  ```bash
  ./gradlew assembleDebug
  ```
* **Release** (signat):

  1. Crea/selecciona una keystore a *Build > Generate Signed App Bundle / APK…*
  2. Genera l’`APK` o l’`AAB` de producció.

## Estructura (resum)

```
app/
  src/
    main/
      AndroidManifest.xml
      java/... (Activities, DAOs, models, etc.)
      res/... (layouts, drawables, strings, styles)
```

## Full de ruta (idees)

* Desat automàtic amb **DESFER** (snackbar) per als formularis.
* Tipificació de les actuacions per a una entrada més àgil
* Gestió d'incidències de múltiples alumnes
* Gestió d'actuacions no lligades a cap incidència

## Contribució

1. Obre una **issue** per debatre canvis majors.
2. Crea una branca des de `main` i fes **pull request**.
3. Mantén els commits clars i curts.

## Llicència

Distribuït sota **MIT**. Consulta el fitxer [LICENSE](./LICENSE).
