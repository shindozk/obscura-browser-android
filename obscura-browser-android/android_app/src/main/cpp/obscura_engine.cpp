#include <jni.h>
#include <string>
#include <android/log.h>

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
    __android_log_print(ANDROID_LOG_INFO, "ObscuraEngine", "Rendering frame %dx%d", width, height);
    // TODO: Actually render using the obscura Rust engine
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
 * Method:    nativeHandleEvent
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_obscura_browser_ObscuraEngine_nativeHandleEvent
(JNIEnv *env, jobject thiz, jint eventType) {
    __android_log_print(ANDROID_LOG_INFO, "ObscuraEngine", "Handling event: %d", eventType);
    // TODO: Handle Android events in the obscura engine
}