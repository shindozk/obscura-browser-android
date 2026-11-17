cmake_minimum_required(VERSION 3.18)
project(obscura_browser)

# Set the Android STL
set(ANDROID_STL c++_shared)
set(CMAKE_ANDROID_ARCH_ABI arm64-v8a)

# Enable Rust support
set(ANDROID_RUST_STATIC_LIBRARIES true)

# C++ wrapper for the Rust engine
# Name must match Kotlin's System.loadLibrary("obscura_engine")
add_library(obscura_engine SHARED
    src/main/cpp/obscura_engine.cpp
)

# Rust browser engine library
# This will be built from the Rust source in the Android module
add_library(rust_browser STATIC)

# Link Rust standard library and native app glue
target_link_libraries(rust_browser
    android
    log
    android_native_app_glue
)

# Set up the Rust toolchain
# The Rust code in src/src/ will be compiled by the NDK toolchain
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Import the Rust crate dependencies
# The Cargo.toml in src/ handles Rust dependencies

# Export the combined library for Kotlin JNI usage
set_target_properties(rust_browser PROPERTIES
    ANDROID_PIE true
)

# Link both native libraries together
target_link_libraries(obscura-engine
    rust_browser
    android
    log
)

# Optional: Add sample usage comment
# JNI functions in obscura_engine.cpp will call into rust_browser
# which uses the adapted Obscura crates (obscura-dom, obscura-render, obscura-net)