package com.obscura.browser

import android.util.Log

/** 
 * Native interface for the obscura browser engine. 
 * Loads the native .so library and provides bridge to JNI functions. 
 * The native functions must match JNI naming: Java_com_obscura_browser_ObscuraEngine_* 
 */ 
object ObscuraNative {

    // Load the native library
    // The library name "obscura_engine" must match the .so file name
    init {
        System.loadLibrary("obscura_engine")
    }

    /** 
     * Initialize the obscura browser engine. 
     * Should be called when the surface is created. 
     * Corresponds to Java_com_obscura_browser_ObscuraEngine_nativeInit in C++. 
     */ 
    @JvmStatic
    native fun initEngine()

    /** 
     * Render a frame from the obscura engine. 
     * Should be called on each frame render. 
     * Corresponds to Java_com_obscura_browser_ObscuraEngine_nativeRender in C++.
     
     * @param width  Width of the render target
     * @param height Height of the render target
     */ 
    @JvmStatic
    native fun renderFrame(width: Int, height: Int)

    /** 
     * Clean up the obscura engine resources. 
     * Should be called when the surface is destroyed. 
     * Corresponds to Java_com_obscura_browser_ObscuraEngine_nativeCleanup in C++. 
     */ 
    @JvmStatic
    native fun cleanupEngine()

    /** 
     * Get the obscura engine version string. 
     
     * @return Version string or "unknown" 
     */ 
    @JvmStatic
    native fun getVersion(): String

    /** 
     * Navigate to a URL in the obscura engine. 
     
     * @param url URL to navigate to 
     */ 
    @JvmStatic
    native fun navigate(url: String)

    /** 
     * Set stealth mode in the obscura engine. 
     
     * @param enabled Whether stealth mode is enabled 
     */ 
    @JvmStatic
    native fun setStealth(enabled: Boolean)

    /** 
     * Get the current page content from the obscura engine. 
     
     * @return Page content string 
     */ 
    @JvmStatic
    native fun getContent(): String
}