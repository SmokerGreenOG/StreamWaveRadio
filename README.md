# 🎵 StreamWave Radio

> Een moderne Android radio-app met donker futuristisch design — Kotlin + Jetpack Compose.
> **Gemaakt door SmokerGreenOG** | Powered by [Futuristic Creations](https://futuristiccreations.nl/)

**Status:** ✅ Complete — v1.1 release

---

## 📋 Features

| Feature | Status |
|---------|--------|
| 📻 **Radio Browser API** — 1000+ live stations (NL/DE/US/BE) | ✅ |
| 🌐 **4 talen** — NL 🇳🇱 / EN 🇬🇧 / DE 🇩🇪 / ES 🇪🇸 | ✅ |
| 🎨 **5 thema's** — Paars/Blauw/Groen/Rood/Oranje (donker) | ✅ |
| 🔊 **Volume boost** — 0–200% met slider | ✅ |
| ⏰ **Slaaptimer** — 10 minuten tot 12 uur | ✅ |
| ❤️ **Favorieten** — Toggle + lijstweergave | ✅ |
| ➕ **Persoonlijke stations** — Eigen links toevoegen + stream test | ✅ |
| 🔍 **Zoeken & filters** — Naam, categorie, land, taal | ✅ |
| 📊 **4 visualizer stijlen** — Bars, cirkels, golf, stippen | ✅ |
| 🚀 **Splash screen** — Logo animatie | ✅ |
| 📱 **Background audio** — Foreground service + media notificatie | ✅ |
| 🛡️ **Admin paneel** — Station beheer | ✅ |
| 🔄 **Auto-sync** — Elke 6 uur verse stations via WorkManager | ✅ |
| ✨ **Reclame popup** — Subtiel na 60s → futuristiccreations.nl | ✅ |

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
| Retrofit + Gson | 2.11.0 |
| Coil | 2.6.0 |
| DataStore | 1.1.1 |
| WorkManager | 2.9.0 |
| **minSdk** | **28 (Android 9+)** |
| **compileSdk** | **35** |

## 🚀 Build & Install

### Zelf bouwen
```bash
git clone https://github.com/SmokerGreenOG/StreamWaveRadio.git
cd StreamWaveRadio
export JAVA_HOME="/path/to/jdk-17"
export ANDROID_HOME="$HOME/AppData/Local/Android/Sdk"
./gradlew assembleRelease
# APK: app/build/outputs/apk/release/app-release.apk (~5 MB)
```

### Direct installeren
Download de laatste APK van de [releases](https://github.com/SmokerGreenOG/StreamWaveRadio/releases) of vraag hem aan SmokerGreenOG.

---

## 📂 Structuur

```
app/src/main/java/com/streamwave/radio/
├── StreamWaveApp.kt              # @HiltAndroidApp + API-load
├── MainActivity.kt               # Navigatie + attachBaseContext
├── SplashActivity.kt             # Splash screen
├── core/
│   ├── theme/                    # Color.kt, Theme.kt, Type.kt
│   ├── common/                   # Constants.kt
│   └── localization/             # LanguageManager.kt
├── data/
│   ├── database/                 # Room DB (6 entiteiten)
│   ├── repository/               # 6 repositories
│   └── datastore/                # SettingsDataStore
├── network/
│   └── api/                      # RadioBrowserApi, StationApiService
├── player/                       # RadioPlayer, SleepTimerManager, MediaService
├── ui/
│   ├── home/                     # HomeScreen + HomeViewModel
│   ├── player/                   # MiniPlayer, FullPlayerScreen, SleepTimerDialog
│   ├── settings/                 # SettingsScreen
│   ├── favorites/                # FavoritesScreen
│   ├── personal/                 # AddPersonalStationScreen, MyStationsScreen
│   ├── admin/                    # AdminScreen
│   └── components/               # StationCard, SearchBar, Animations
├── di/                           # AppModule, DatabaseModule, RepositoryModule, NetworkModule
└── sync/                         # SyncWorker
```

---

## 🎨 Thema's

De app heeft 5 donkere thema's, wisselbaar via Instellingen:

| Thema | Primaire kleur |
|-------|---------------|
| 🟣 Paars | `#A855F7` |
| 🔵 Blauw | `#3B82F6` |
| 🟢 Groen | `#22C55E` |
| 🔴 Rood | `#EF4444` |
| 🟠 Oranje | `#F97316` |

---

## 📻 Radio Bron

Stations worden geladen van de gratis [Radio Browser API](https://api.radio-browser.info/):
- `NL` — Nederlandse stations
- `DE` — Duitse stations  
- `US` — Amerikaanse stations
- `BE` — Belgische stations

Geen hardcoded stations in de APK — altijd up-to-date!

---

## 👤 Maker

**SmokerGreenOG**
- GitHub: [@SmokerGreenOG](https://github.com/SmokerGreenOG)
- Website: [Futuristic Creations](https://futuristiccreations.nl/)
- ToolCase: [github.com/SmokerGreenOG/ToolCase](https://github.com/SmokerGreenOG/ToolCase)

---

## 📄 License

**Custom License** — Copyright (c) 2026 SmokerGreenOG. All rights reserved.

- ✅ Personal, non-commercial use allowed
- ❌ No modification, redistribution, or commercial use
- ❌ No reverse engineering
- ❌ No use of branding without permission

Zie [LICENSE](LICENSE) voor de volledige voorwaarden.

Voor commerciële rechten: neem contact op met SmokerGreenOG.
