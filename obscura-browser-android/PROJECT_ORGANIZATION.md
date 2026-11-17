# Obscura Browser - Project Organization

## 🎯 Project Overview

**Obscura Browser** is a privacy-focused Android browser application built with Rust via Android NDK, featuring Material You (Dynamic Color) theming and Opera Mobile visual inspiration.

### Core Philosophy
- Privacy-first browsing experience
- Material You dynamic color theming
- Opera Mobile-inspired UI design
- Rust-powered browser engine for performance and safety

---

## 🏗️ Architecture

### Tech Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| **UI** | Kotlin + Jetpack Compose/Fragments | Android UI components, navigation, theming |
| **Native Bridge** | C++ JNI | Kotlin-Rust communication |
| **Engine** | Rust + Android NDK | Browser rendering engine |
| **Build** | CMake + Gradle | Native compilation and Android packaging |

### Key Components

#### Kotlin Layer (`android_app/src/main/java/`)
- **MainActivity.kt** - Entry point, engine init/cleanup, tab navigation
- **ObscuraNative.kt** - Native interface, JNI function declarations
- **UI Fragments** - BrowserFragment, HomeFragment, BookmarksFragment, SettingsFragment, TabFragment, TabAdapter
- **MainActivity UI** - Toolbar, BottomNavigationView, SurfaceView for engine rendering

#### Rust Layer (`android_app/src/main/cpp/src/src/`)
- **lib.rs** - ANativeActivity entry point, event loop, engine init
- **android_native_app_glue** - Android main loop integration

#### C++ Layer (`android_app/src/main/cpp/`)
- **obscura_engine.cpp** - JNI exports matching Kotlin native function names
- **CMakeLists.txt** - Native build configuration

#### Navigation Layer (`android_app/src/main/navigation/`)
- **nav_graph.xml** - Navigation graph with 5 fragment destinations

---

## 📁 Project Structure

```
obscura-browser-android/
├── android_app/                      # Main Android module
│   ├── build.gradle                   # Android build config with NDK/cmake
│   ├── settings.gradle                # Project root name "ObscuraBrowser"
│   ├── AndroidManifest.xml           # App manifest with activity declaration
│   │
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/
│   │   │   │   └── com/obscura/browser/
│   │   │   │       ├── MainActivity.kt          # Entry point
│   │   │   │       ├── ObscuraNative.kt          # Native interface
│   │   │   │       └── ui/
│   │   │   │           ├── BrowserFragment.kt
│   │   │   │           ├── HomeFragment.kt
│   │   │   │           ├── BookmarksFragment.kt
│   │   │   │           ├── SettingsFragment.kt
│   │   │   │           ├── TabFragment.kt
│   │   │   │           └── TabAdapter.kt
│   │   │   │
│   │   │   ├── cpp/
│   │   │   │   ├── CMakeLists.txt             # Native build config
│   │   │   │   ├── obscura_engine.cpp         # C++ JNI wrapper
│   │   │   │   └── src/
│   │   │   │       └── Cargo.toml             # Rust dependencies
│   │   │   │       └── src/
│   │   │   │           └── lib.rs              # Rust ANativeActivity entry point
│   │   │   │
│   │   │   ├── navigation/
│   │   │   │   └── nav_graph.xml               # Navigation graph
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── layout/                   # 17+ XML layouts
│   │ │   │   │   ├── activity_main.xml         # Main layout (SurfaceView + BottomNav)
│   │   │   │   ├── fragment_browser.xml
│   │   │   │   ├── fragment_home.xml
│   │   │   │   ├── fragment_tabs.xml
│   │   │   │   ├── fragment_bookmarks.xml
│   │   │   │   ├── fragment_settings.xml
│   │   │   │   └── url_bar.xml
│   │   │   │   └── item_tab.xml
│   │   │   │
│   │   │   ├── menu/                         # 2 menu resource files
│   │   │   │   ├── menu_main.xml
│   │   │   │   └── bottom_nav_menu.xml
│   │   │   │
│   │   │   ├── values/                       # strings.xml, colors.xml, themes.xml
│   │   │   │   ├── colors.xml                  # #3F51B5 (Opera Purple), #00ACC1 (Cyan)
│   │   │   │   ├── themes.xml                  # Theme.Material3.DayNight.NoActionBar
│   │   │   │   └── strings.xml                 # All string constants
│   │   │   │
│   │   │   ├── drawable/                     # Vector drawables for icons
│   │   │   │   ├── vic_home.xml, vic_bookmark.xml, etc.
│   │   │   │   ├── vector/                    # Vector drawable definitions
│   │   │   │   └── mipmap/                    # Launcher icons (mdpi-xxxhdpi)
│   │   │   │
│   │   │   └── jniLibs/                      # Prebuilt .so native libraries
│   │   │
│   │   └── debug/                            # Debug folders
│   │
│   └── .git/                               # Git version control
│
├── README.md                               # Project overview (this file)
├── LICENSE                                 # MIT License
└── .gitignore                              # Git ignore rules
```

---

## 🚀 Getting Started

### Prerequisites

- **Android SDK** (compileSdk 34, minSdk 21)
- **Android NDK** (r21+ recommended)
- **CMake** 3.10+
- **JDK 11+**
- **Git**

### Building the Project

```bash
# From the android_app directory
./gradlew assembleDebug

# Or with specific ABI
./gradlew assembleDebug -Pandroid.abi=arm64-v8a

# To run on device/emulator
./gradlew installDebug
```

### Running on Device

```bash
# Install and launch
./gradlew installDebug

# Or directly via ADB
adb install -r android_app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.obscura.browser/.MainActivity
```

---

## 🧭 Navigation & UI Features

### Bottom Navigation Tabs (4 items)

| ID | Destination | Description |
|---|-------------|-------------|
| `nav_home` | `home_fragment` | Browser start page |
| `nav_tabs` | `tab_fragment` | Tab management grid |
| `nav_bookmarks` | `bookmarks_fragment` | Saved bookmarks |
| `nav_settings` | `settings_fragment` | Browser settings |

### Navigation Graph Actions
- `main_fragment_to_home_fragment`
- `main_fragment_to_tab_fragment`
- `main_fragment_to_bookmarks_fragment`
- `main_fragment_to_settings_fragment`

### UI Features
- **Material You (Dynamic Color)** theming with primary color `#3F51B5` (Opera Purple)
- **Bottom Navigation** bar with 4 tabs
- **Floating Action Button** (FAB) for quick access
- **SurfaceView** for Rust engine rendering
- **Toolbar** with back arrow support
- **Options Menu** with New Tab, Settings, Share actions

---

## 🔧 Engine Integration

### JNI Bridge
The obscura Rust browser engine is integrated via JNI with the following function mapping:

| Kotlin Function | C++ JNI Function | Description |
|---|---|---|
| `initEngine()` | `Java_com_obscura_browser_ObscuraEngine_nativeInit` | Engine initialization |
| `renderFrame(w, h)` | `Java_com_obscura_browser_ObscuraEngine_nativeRender` | Render a frame |
| `cleanupEngine()` | `Java_com_obscura_browser_ObscuraEngine_nativeCleanup` | Clean up resources |
| `getVersion()` | `Java_com_obscura_browser_ObscuraEngine_nativeVersion` | Get engine version |

### Rust Engine Lifecycle
1. **initEngine()** - Called in `MainActivity.onCreate()`
2. **renderFrame(width, height)** - Called per render cycle
3. **cleanupEngine()** - Called in `MainActivity.onDestroy()`

### Window Events (lib.rs)
- `APP_CMD_INIT_WINDOW` - Window created, initialize renderer
- `APP_CMD_TERM_WINDOW` - Window destroyed, clean up

---

## 🎨 Theming

### Material You Configuration
- **Base Theme**: `Theme.Material3.DayNight.NoActionBar`
- **Primary Color**: `#3F51B5` (Indigo/Opera Purple)
- **Secondary Color**: `#00ACC1` (Cyan)
- **Dynamic Color**: Automatic extraction from wallpaper
- **Custom Styles**: Toolbar and tab styles with purple theme

### Color Palette
| Color | Hex | Purpose |
|-------|-----|---------|
| Primary | `#3F51B5` | Opera Purple, toolbar, tabs |
| Secondary | `#00ACC1` | Cyan, accents |
| Background | Default | Day/night mode |

---

## 🤝 Contributing

### Guidelines

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/AmazingFeature`)
3. **Commit your changes** (`git commit -m 'Add some AmazingFeature'`)
4. **Push to the branch** (`git push origin feature/AmazingFeature`)
5. **Open a Pull Request**

### Development Workflow

```bash
# Make your changes
git add .
git commit -m "Description of changes"

# Test build
./gradlew assembleDebug

# Run lint checks
./gradlew lint

# Format code
./gradlew ktFormat
```

### Code Conventions
- **Kotlin**: Follow Android Kotlin conventions, usektlint if available
- **Rust**: Follow Rust fmt guidelines, use `cargo fmt`
- **C++**: Follow Google C++ style, use clang-format
- **Commit Messages**: Conventional Commits format recommended

---

## 📦 Versioning

Currently using Semantic Versioning (implied):
- **Major**: Breaking changes to engine or API
- **Minor**: New features, UI improvements
- **Patch**: Bug fixes, internal refactors

Current version: `1.0` (versionCode 1, versionName "1.0")

---

## 🛠️ Build Configuration Details

### CMake Configuration
```cmake
cmake_minimum_required(VERSION 3.18)
project(obscura_browser)

# Android STL
set(ANDROID_STL c++_shared)
set(CMAKE_ANDROID_ARCH_ABI arm64-v8a)

# Rust engine build (via Cargo.toml location)
add_library(obscura-engine SHARED
    src/main/cpp/obscura_engine.cpp
)

target_link_libraries(obscura-engine android log)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
```

### Gradle Configuration
- **namespace**: `com.obscura.browser`
- **compileSdk**: 34
- **minSdk**: 21
- **targetSdk**: 34
- **abiFilters**: `armeabi-v7a`, `arm64-v8a`
- **viewBinding**: enabled

### NDK Module Paths
```
modulePaths 'src/main/cpp'
```

---

## 📋 TODO / Roadmap Ideas

### Short-term
- [ ] Complete Rust engine integration (currently TODO stubs)
- [ ] Implement actual rendering in `obscura_engine.cpp`
- [ ] Add tab management functionality
- [ ] Implement bookmarks save/load
- [ ] Settings UI completion

### Mid-term
- [ ] Performance optimization for engine rendering
- [ ] Extended Material You theming
- [ ] Privacy features (tracking protection, etc.)
- [ ] Extension system

### Long-term
- [ ] Cross-platform support (iOS, desktop)
- [ ] Sync across devices
- [ ] Advanced privacy controls
- [ ] Custom engine enhancements

---

## 📄 License

This project is licensed under the **MIT License** - see the LICENSE file for details.

Full license text in `LICENSE` file.

---

## 📞 Contact & Support

For questions, issues, or contributions:
- **GitHub Issues**: Use the issue tracker
- **Discussions**: GitHub Discussions (if enabled)
- **Email**: Reach out via the project maintainer

---

*Organized for contribution and maintainability. Last updated: November 2026.*