# Obscura Browser

A privacy-focused Android browser application built with Rust via Android NDK, featuring Material You (Dynamic Color) theming and Opera Mobile visual inspiration.

## Project Overview

**Obscura Browser** replaces the traditional WebView-based implementation with the obscura Rust browser engine rendered through the Android NDK. The app maintains Material You design language and features an Opera Mobile-inspired UI with bottom navigation, FAB, and purple color scheme.

## Architecture

### Tech Stack
- **Kotlin** - Main activity and UI components
- **Rust** - Browser engine via Android NDK
- **C++ JNI** - Bridge between Kotlin and Rust layers
- **CMake** - Native build configuration
- **Android NDK** - Rust compilation for Android arm64-v7a/arm64-v8a

### Key Components
| Component | Location | Description |
|-----------|----------|-------------|
| `MainActivity.kt` | `android_app/src/main/java/` | Entry point, engine init/cleanup, tab navigation |
| `ObscuraNative.kt` | `android_app/src/main/java/` | Kotlin native interface for JNI |
| `lib.rs` | `android_app/src/main/cpp/src/src/` | Rust entry point `ANativeActivity_onCreate` |
| `obscura_engine.cpp` | `android_app/src/main/cpp/` | C++ JNI wrapper functions |
| `CMakeLists.txt` | `android_app/src/main/cpp/` | Native build configuration |
| `nav_graph.xml` | `android_app/src/main/navigation/` | Navigation graph with fragment destinations |
| `activity_main.xml` | `android_app/src/main/res/layout/` | Main layout with SurfaceView + BottomNav |

## UI Features

- **Material You (Dynamic Color)** theming with primary color `#3F51B5` (Opera Purple)
- **Bottom Navigation** with 4 tabs: Home, Tabs, Bookmarks, Settings
- **Floating Action Button** (FAB) for quick access
- **SurfaceView** for Rust engine rendering
- **Toolbar** with back arrow support
- **Options Menu** with New Tab, Settings, Share actions

## Project Structure

```
android_app/
├── build.gradle          # Android project build config with NDK/cmake
├── settings.gradle       # Project root name "ObscuraBrowser"
├── AndroidManifest.xml   # App manifest with activity declaration
│
├── src/
│   ├── main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/
│   │   │   ├── com/obscura/browser/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── ObscuraNative.kt
│   │   │   │   └── ui/
│   │   │   │       ├── BrowserFragment.kt
│   │   │   │       ├── HomeFragment.kt
│   │   │   │       ├── BookmarksFragment.kt
│   │   │   │       ├── SettingsFragment.kt
│   │   │   │       ├── TabFragment.kt
│   │   │   │       └── TabAdapter.kt
│   │   │   └── jniLibs/         # Prebuilt native libraries
│   │   │
│   │   ├── cpp/
│   │   │   ├── CMakeLists.txt        # Native build config
│   │   │   ├── obscura_engine.cpp    # C++ JNI wrapper
│   │   │   └── src/
│   │   │       └── Cargo.toml        # Rust dependencies
│   │   │       └── src/
│   │   │           └── lib.rs        # Rust ANativeActivity entry point
│   │   │
│   │   ├── navigation/
│   │   │   └── nav_graph.xml         # Navigation graph with 5 fragments
│   │   │
│   │   ├── res/
│   │   │   ├── layout/               # 17+ XML layouts
│   │   │   ├── menu/                 # 2 menu resource files
│   │   │   ├── values/                 # strings.xml, colors.xml, themes.xml
│   │   │   ├── drawable/             # Vector drawables for icons
│   │   │   └── mipmap/               # Launcher icons (mdpi-xxxhdpi)
│   │   │
│   │   └── jniLibs/                  # Prebuilt .so native libraries
│   │
│   └── debug/                        # Debug folders
│
├── .git/                             # Git version control
└── README.md                         # This file
```

## Tab Navigation

The bottom navigation enables switching between 4 tabs:

| ID | Destination | Description |
|----|-------------|-------------|
| `nav_home` | `home_fragment` | Browser start page |
| `nav_tabs` | `tab_fragment` | Tab management grid |
| `nav_bookmarks` | `bookmarks_fragment` | Saved bookmarks |
| `nav_settings` | `settings_fragment` | Browser settings |

Navigation is handled via the Navigation Component's Safe Args with the following actions:
- `main_fragment_to_home_fragment`
- `main_fragment_to_tab_fragment`
- `main_fragment_to_bookmarks_fragment`
- `main_fragment_to_settings_fragment`

## Building the Project

### Prerequisites
- **Android SDK** (compileSdk 34, minSdk 21)
- **Android NDK** (r21+ recommended)
- **CMake** 3.10+
- **Java Development Kit** (JDK 11+)

### Build Commands

```bash
# From the android_app directory
./gradlew assembleDebug

# Or with specific ABI
./gradlew assembleDebug -Pandroid.abi=arm64-v8a

# To run on device/emulator
./gradlew installDebug
```

### CMake Configuration
The project uses `externalNativeBuild` with CMake configured for:
- C++17 standard
- `c++_shared` STL (runtime)
- ARM64 and armeabi-v7a ABI filters
- Obscura engine shared library production

## Rust Engine Integration

The obscura Rust browser engine is integrated via:

1. **JNI Bridge**: `obscura_engine.cpp` exports functions matching `Java_com_obscura_browser_ObscuraEngine_*` naming convention
2. **Rust Entry Point**: `lib.rs` implements `ANativeActivity_onCreate` using `android_native_app_glue`
3. **Engine Lifecycle**: 
   - `initEngine()` - Called in `MainActivity.onCreate()`
   - `renderFrame(width, height)` - Called per render cycle
   - `cleanupEngine()` - Called in `MainActivity.onDestroy()`

## Material You Theming

The app uses `Theme.Material3.DayNight.NoActionBar` with dynamic color support:
- Primary color: `#3F51B5` (Indigo/Opera Purple)
- Secondary color: `#00ACC1` (Cyan)
- Automatic color extraction from wallpaper
- Custom toolbar and tab styles

## Opera Mobile Inspiration

Visual design inspired by Opera Mobile:
- **Bottom Navigation** bar
- **Floating Action Button** (FAB) for quick actions
- **Purple color scheme** (`#3F51B5`)
- **Quick Access panel** at bottom-right
- **Tab grid layout** (4 columns in landscape)

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request