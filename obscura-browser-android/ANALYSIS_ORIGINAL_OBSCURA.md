# Analysis: Original Obscura Project (h4ckf0r0day/obscura)

## 📋 Project Overview

**Obscura** is an open-source headless browser written in Rust, designed for AI agents and web scraping. It's a **drop-in replacement for headless Chrome** with Puppeteer/Playwright support.

### Key Facts
- **License**: Apache-2.0
- **Releases**: Prebuilt binaries for Linux x86_64, Linux ARM64 (aarch64), macOS, Windows
- **Architecture**: workspace of **9 crates**
- **First commit**: April 2026
- **Latest version**: 0.2.1 (August 2026) - includes Rust layout/paint engine

## 🏗️ Architecture: 9 Crates Breakdown

| Crate | Purpose | Key Features |
|-------|---------|-------------|
| **obscura-cli** | CLI entry point | fetch, serve, scrape, MCP |
| **obscura-cdp** | Chrome DevTools Protocol server | WebSocket, dispatch, domain handlers |
| **obscura-browser** | Page type, navigation, lifecycle | Core browser logic |
| **obscura-js** | V8 runtime via deno_core | JavaScript execution, bootstrap.js + Rust ops |
| **obscura-dom** | DOM tree implementation | html5ever, markup5ever, CSS selectors |
| **obscura-net** | HTTP client, stealth, cookies | Stealth client, cookie jar, tracker blocklist |
| **obscura-mcp** | Model Context Protocol server | AI agent integration |
| **obscura-render** | **CSS cascade, layout, text shaping, CPU paint** | **taffy layout, tiny-skia rasterization, text shaping** |
| **obscura** | **Embeddable API** | `Browser`, `Page`, `Element`, `CookieStore` public types |

## 🎯 Relevant Crates for Browser Adaptation

### 1. **obscura-dom** - DOM Implementation
- **Dependencies**: html5ever, markup5ever, selectors, servo_arc
- **Purpose**: Parse HTML into DOM tree
- **Relevance**: Core HTML parsing for browser engine
- **Android adaptation**: Could replace custom HTML parsing in Android WebView

### 2. **obscura-render** - Layout & Paint Engine
- **Dependencies**: taffy (layout), tiny-skia (rasterization), cosmic-text (text shaping), swash, resvg, usvg
- **Features**: 
  - `default`: Layout core only
  - `paint`: Full rendering with rasterization
- **Purpose**: CSS cascade, taffy layout, text shaping, raster output
- **Relevance**: **Most relevant for adaptation** - this is the layout/paint engine that renders pages without Chromium
- **Android adaptation**: Could replace current Rust engine's rendering pipeline

### 3. **obscura-net** - HTTP Client & Networking
- **Dependencies**: ureq (HTTP), stealth client, cookie jar, robots cache, tracker blocklist
- **Features**: Stealth mode, proxies
- **Purpose**: HTTP client with anti-detection features
- **Relevance**: Networking stack for browser engine
- **Android adaptation**: Could replace OkHttp/Retrofit with this stealth HTTP client

### 4. **obscura-js** - JavaScript Runtime
- **Dependencies**: deno_core, V8 embedded
- **Purpose**: JavaScript execution via V8
- **Relevance**: JS engine for browser
- **Android adaptation**: Could integrate V8 into Android app (similar to what WebView does)

### 5. **obscura** - Public API
- **Public types**: `Browser`, `Page`, `Element`, `CookieStore`
- **Builder pattern**: `Browser::builder().stealth(true).build()?`
- **Async API**: `page.goto()`, `page.content()`, etc.
- **Relevance**: High-level API to integrate

## 📦 Linux ARM64 Support

### Prebuilt Binaries
```bash
# Linux ARM64 (aarch64)
curl -LO https://github.com/h4ckf0r0day/obscura/releases/latest/download/obscura-aarch64-linux.tar.gz
tar xzf obscura-aarch64-linux.tar.gz
./obscura --version
```

### Build from Source
```bash
# Rendering build
cargo build --release -p obscura-cli --bins --features render

# Rendering + stealth
cargo build --release -p obscura-cli --bins --features render,stealth

# No rendering (headless fetch only)
cargo build --release -p obscura-cli --bins --no-default-features
```

### Build Requirements
- Current stable Rust (rustup.rs)
- C compiler (gcc or clang)
- ~5 GB free disk space (V8 compiles from source on first build)
- For stealth: CMake, Clang, BoringSSL, libclang/LLVM dev libraries

### ARM64 Note
- Issue #127 requested prebuilt ARM64 releases
- **Resolved**: May 2026 - GitHub Actions now builds aarch64 binaries natively
- Uses Ubuntu 22.04 ARM runner, no cross-compile needed
- Matches glibc 2.35 floor as x86_64

## 🔄 Adaptation Opportunities for Android App

### High-Impact Adaptations

| Component | Effort | Impact | Integration Path |
|-----------|--------|--------|------------------|
| **obscura-render** | High | Critical | Replace custom rendering pipeline; use taffy + tiny-skia for UI rendering |
| **obscura-dom** | Medium | High | Replace HTML parser; integrate with Android's WebView or custom layout |
| **obscura-net** | Medium | High | Replace OkHttp with stealth HTTP client; add tracker blocking |
| **obscura-js** | Very High | Medium | Embed V8 JS engine (complex but powerful) |
| **obscura** API | Medium | High | Use `Browser::builder()` pattern for engine initialization |

### Integration Options

#### Option A: Full Obscura Integration
- Compile Obscura crates for Android ARM via `cargo-ndk`
- Replace entire Rust engine with Obscura
- Wrap Obscura's API in Kotlin JNI bindings
- **Pros**: Full browser features, active maintenance
- **Cons**: Significant build setup, many dependencies (V8, tiny-skia, etc.)

#### Option B: Selective Component Integration
- Use only `obscura-dom` + `obscura-render` for layout/paint
- Keep current Kotlin UI layer
- Replace only the Rust rendering portion
- **Pros**: Less disruptive, targeted improvements
- **Cons**: Need to integrate DOM + Render separately

#### Option C: Obscura as Headless Service
- Use Obscura's CLI/HTTP API from Android
- Communicate via subprocess or network
- Obscura runs as background process, Android UI displays results
- **Pros**: Less code integration, leverage existing CLI
- **Cons**: Not embedded, different architecture

## 🛠️ Adaptation Steps (Recommended: Option B)

### Step 1: Analyze Current Rust Engine
Current state (from earlier analysis):
- `lib.rs`: Basic ANativeActivity entry with TODO stubs
- `obscura_engine.cpp`: JNI wrappers with basic init/render/version
- `CMakeLists.txt`: Simple shared library build
- `Cargo.toml`: Only `android_logger` + `android_native_app_glue`

### Step 2: Add Obscura Crates to Cargo.toml
```toml
[dependencies]
# Core browser crates
obscura = { path = "../../../../../obscura" }  # or crates.io
obscura-dom = { path = "../../../../../obscura-dom" }
obscura-render = { path = "../../../../../obscura-render", features = ["paint"] }
obscura-net = { path = "../../../../../obscura-net" }

# Graphics (Android NDK)
tiny-skia = { version = "0.7", features = ["gpu"] }  # or software backend
rusttype = "1.2"
```

### Step 3: Update CMakeLists.txt
```cmake
# Add Obscura render library
add_library(obscura-render SHARED
    src/main/cpp/obscura_engine.cpp  # adapt JNI
)
target_link_libraries(obscura-render
    android log
    tiny-skia  # or system library
)
```

### Step 4: Update Kotlin JNI Bindings
Add Kotlin native declarations for Obscura API:
```kotlin
object ObscuraNative {
    @JvmStatic native fun initBrowser(): Boolean
    @JvmStatic native fun navigate(url: String): String
    @JvmStatic native fun renderFrame(): ByteBuffer  // return rendered frame
    @JvmStatic fun cleanupBrowser()
}
```

### Step 5: Update lib.rs Entry Point
Replace current stub with Obscura integration:
```rust
use obscura::Browser;
use obscura_render::Renderer;

#[no_mangle]
pub extern "C" fn ANativeActivity_onCreate(activity: *mut ffi::c_void) {
    let app = NativeAppGlue::new(activity);
    app.setLooper(Looper::create());
    
    // Initialize Obscura browser
    let browser = Browser::builder()
        .stealth(true)
        .build()
        .expect("Failed to init browser");
    
    // Main loop...
}
```

## ⚠️ Challenges & Considerations

### 1. Dependency Overhead
Obscura has many dependencies:
- **V8/js**: ~200MB compile time, deno_core integration
- **tiny-skia**: GPU or software rasterization
- **cosmic-text**: Text shaping (HarfBuzz algorithms)
- **taffy**: CSS flex/grid layout (same as React Native)

### 2. Android NDK Compatibility
- Obscura builds for `aarch64-unknown-linux-gnu`
- Need cross-compilation for `aarch64-linux-android`
- May need `cargo-ndk` or custom toolchain

### 3. GUI Integration
Obscura is headless by design; need to add:
- SurfaceView integration (like current project)
- Render loop into Android's UI thread
- Touch input handling
- Window management

### 4. Stealth Features
Obscura has built-in stealth/anti-detection:
- TLS fingerprint consistency
- Tracker blocklist
- User agent spoofing
- Could be valuable for browser privacy features

## 📋 Recommended Starting Point

### Minimum Viable Adaptation
1. **Start with `obscura-dom` only** - HTML parsing, simple DOM
2. **Add `obscura-render` with `default` feature** (layout only, no paint) - taffy layout
3. **Gradually add paint features** - tiny-skia rasterization
4. **Integrate networking** - replace with obscura-net for stealth HTTP
5. **Keep Kotlin UI** - don't replace the Android framework layer

### Code Structure After Adaptation
```
Android App Layer (Kotlin)
├── MainActivity (unchanged)
├── Fragments (unchanged)
└── ObscuraView (new) - SurfaceView with Rust rendering

Rust Engine Layer (adapted)
├── obscura-dom: HTML parsing
├── obscura-render: Layout + paint (taffy + tiny-skia)
├── obscura-net: HTTP client
└── JNI bridge: Kotlin ↔ Rust communication

Build System
├── CMake (adapted for Obscura dependencies)
├── Gradle (unchanged)
└── Cargo (obscura crates)
```

## 🔮 Long-term Vision

If successfully adapted, the app could feature:
- **Lightweight browser engine** (vs Chromium WebView)
- **Built-in stealth/privacy** (Obscura's anti-detection)
- **CSS Grid/Flex layout** (taffy rendering engine)
- **Modern text shaping** (cosmic-text, HarfBuzz algorithms)
- **SVG support** (resvg/usvg rasterization)
- **PDF/screenshot export** (render feature)
- **AI agent integration** (obscura-cli compatibility)

## 📞 Next Steps

1. **Clone original Obscura repo** to examine full source
2. **Test cargo build for Android** with minimal crate
3. **Prototype DOM parsing** independently
4. **Integrate one crate at a time** (start with obscura-dom)
5. **Update JNI bindings** progressively
6. **Test render loop** with simple HTML page

*Analysis completed: Obscura project structure, crate breakdown, Linux ARM support, and adaptation pathway for Android application.*