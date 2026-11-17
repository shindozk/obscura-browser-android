#include <jni.h>
#include <string>
#include <android/log.h>
#include <cstdlib>

// Forward declarations for Rust-compiled code
// These will be linked against the rust_browser static library
extern "C" {
    // Rust engine initialization
    void rust_browser_init();
    
    // Rust engine rendering - returns pixel buffer
    // width*height u32 values in RGBA format
    unsigned int* rust_browser_render(int width, int height);
    
    // Rust engine cleanup
    void rust_browser_cleanup();
    
    // Rust engine navigation
    void rust_browser_navigate(const char* url);
    
    // Rust engine stealth mode
    void rust_browser_set_stealth(bool enabled);
    
    // Rust engine content retrieval
    const char* rust_browser_content();
}

// Obscura engine state
static bool g_engineInitialized = false;

/*
 * Class:     com_obscura_browser_ObscuraEngine
 * Method:    nativeInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_obscura_browser_ObscuraEngine_nativeInit
(JNIEnv *env, jobject thiz) {
    __android_log_print(ANDROID_LOG_INFO, "ObscuraEngine", "Initializing obscura browser engine");
    g_engineInitialized = true;
    
    // Initialize the Rust browser engine
    rust_browser_init();
}

/*
 * Class:     com_obscura_browser_ObscuraEngine
 * Method:    nativeRender
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_obscura_browser_ObscuraEngine_nativeRender
(JNIEnv *env, jobject thiz, jint width, jint height) {
    if (!g_engineInitialized) {
        __android_log_print(ANDROID_LOG_WARN, "ObscuraEngine", "Engine not initialized");
        return;
    }
    
    // Render frame from Rust engine
    unsigned int* pixels = rust_browser_render(width, height);
    
    if (pixels) {
        __android_log_print(ANDROID_LOG_INFO, "ObscuraEngine", "Rendering frame %dx%d - %d pixels", width, height, width * height);
        // In a real implementation, these pixels would be
        // uploaded to a SurfaceView/GL surface for display
        // For now, just log the render operation
        
        // Clean up the pixel buffer (Rust allocates it)
        std::free(pixels);
    }
}

/*
 * Class:     com_obscura_browser_ObscuraEngine
 * Method:    nativeVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_obscura_browser_ObscuraEngine_nativeVersion
(JNIEnv *env, jobject thiz) {
    const char* version = "obscura-browser-0.1.0-android";
    return env->NewStringUTF(version);
}

/*
 * Class:     com_obscura_browser_ObscuraEngine
 * Method:    nativeNavigate
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_obscura_browser_ObscuraEngine_nativeNavigate
(JNIEnv *env, jobject thiz, jstring url) {
    if (!g_engineInitialized) {
        __android_log_print(ANDROID_LOG_WARN, "ObscuraEngine", "Engine not initialized");
        return;
    }
    
    // Convert Java string to C-string
    const char* nativeUrl = env->GetStringUTFChars(url, nullptr);
    if (nativeUrl) {
        __android_log_print(ANDROID_LOG_INFO, "ObscuraEngine", "Navigating to: %s", nativeUrl);
        rust_browser_navigate(nativeUrl);
        env->ReleaseStringUTFChars(url, nativeUrl);
    }
}

/*
 * Class:     com_obscura_browser_ObscuraEngine
 * Method:    nativeSetStealth
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_obscura_browser_ObscuraEngine_nativeSetStealth
(JNIEnv *env, jobject thiz, jboolean enabled) {
    if (!g_engineInitialized) {
        __android_log_print(ANDROID_LOG_WARN, "ObscuraEngine", "Engine not initialized");
        return;
    }
    
    __android_log_print(ANDROID_LOG_INFO, "ObscuraEngine", "Setting stealth mode: %s", enabled ? "enabled" : "disabled");
    rust_browser_set_stealth(enabled != 0);
}

/*
 * Class:     com_obscura_browser_ObscuraEngine
 * Method:    nativeContent
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_obscura_browser_ObscuraEngine_nativeContent
(JNIEnv *env, jobject thiz) {
    if (!g_engineInitialized) {
        const char* empty = "";
        return env->NewStringUTF(empty);
    }
    
    // Get content from Rust engine
    const char* content = rust_browser_content();
    if (content) {
        return env->NewStringUTF(content);
    }
    
    const char* empty = "";
    return env->NewStringUTF(empty);
}