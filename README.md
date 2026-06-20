# 🎵 StreamWave Radio

> A modern Android radio app with dark futuristic design — built with Kotlin + Jetpack Compose.

**Status:** 🚧 In development (Fase 0/12 voltooid)

---

## 📋 Features (planned)

- 🎧 **Official Stations** — Curated by admin, visible to all users
- 🔒 **Personal Stations** — User-added private stations
- 🌐 **Multi-language** — NL 🇳🇱 / EN 🇬🇧 / DE 🇩🇪 / ES 🇪🇸
- 🎨 **Dark Futuristic Design** — #140b24 backgrounds, purple neon accents
- 📻 **Media3 ExoPlayer** — MP3, AAC, HLS, Icecast, Shoutcast support
- 💤 **Sleep Timer** — Custom countdown with fade-out
- ⭐ **Favorites** — Reorder, quick play
- 🔍 **Search & Filters** — Category, country, language
- 📱 **Background Audio** — Foreground service + media notification
- 🛡️ **Admin Panel** — Station CRUD, stream testing, submission review

---

## 🏗️ Tech Stack

| Technology | Version |
|-----------|---------|
| Kotlin | 2.0.0 |
| AGP | 8.7.3 |
| Gradle | 8.9 |
| Compose BOM | 2024.06.00 |
| Hilt | 2.51.1 |
| Room | 2.6.1 |
| Media3 / ExoPlayer | 1.4.1 |
| Retrofit | 2.11.0 |
| Coil | 2.6.0 |
| minSdk | 28 (Android 9+) |
| compileSdk | 35 |

---

## 🚀 Build & Run

### Prerequisites
- **JDK 17** (Temurin recommended)
- **Android SDK** (API 35, build-tools 36.x+)
- **Gradle 8.9** (wrapper included)

### Setup

```bash
# Clone
git clone https://github.com/SmokerGreenOG/StreamWaveRadio.git
cd StreamWaveRadio

# Set environment
export JAVA_HOME="/path/to/jdk-17"
export ANDROID_HOME="$HOME/AppData/Local/Android/Sdk"

# Build
./gradlew assembleDebug

# APK location
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 📂 Project Structure

```
app/src/main/java/com/streamwave/radio/
├── StreamWaveApp.kt          # @HiltAndroidApp
├── MainActivity.kt           # @AndroidEntryPoint, Compose
├── core/
│   ├── theme/                # Color.kt, Theme.kt, Type.kt
│   ├── navigation/           # AppNavigation.kt (Fase 4)
│   ├── localization/         # LanguageManager.kt (Fase 2)
│   └── common/               # Constants.kt
├── data/
│   ├── database/             # Room DB (Fase 1)
│   ├── repository/           # Repositories (Fase 1)
│   └── model/                # Domain models
├── network/                  # API client (Fase 9)
├── player/                   # Media3 player (Fase 3)
├── ui/                       # Compose screens (Fase 4-11)
├── di/                       # Hilt modules
└── sync/                     # WorkManager (Fase 9)
```

---

## 👤 Author

**SmokerGreenOG** — Creator & Developer

---

## 📄 License

MIT License — see [LICENSE](LICENSE)
