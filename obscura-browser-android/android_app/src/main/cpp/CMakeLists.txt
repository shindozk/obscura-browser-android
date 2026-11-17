cmake_minimum_required(VERSION 3.18)
project(obscura_browser)

# Set the Android STL
set(ANDROID_STL c++_shared)
set(CMAKE_ANDROID_ARCH_ABI arm64-v8a)

# The NDK will automatically find and build Rust code 
# if Cargo.toml is present in the module paths

# C++ wrapper for the Rust engine
# Name must match Kotlin's System.loadLibrary("obscura_engine")
add_library(obscura_engine SHARED
    src/main/cpp/obscura_engine.cpp
)

target_link_libraries(obscura-engine
    android log
)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)