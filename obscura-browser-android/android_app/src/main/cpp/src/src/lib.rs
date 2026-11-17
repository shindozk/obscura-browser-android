#![no_std]
#![no_main]

use android_logger::init_logger;
use android_native_app_glue::Looper;

#[no_mangle]
pub extern "C" fn ANativeActivity_onCreate(activity: *mut std::ffi::c_void) {
    // Initialize the Android native app glue
    let app = android_native_app_glue::NativeAppGlue::new(activity);
    
    // Set up the looper for event handling
    app.setLooper(Looper::create());
    
    // Initialize the obscura browser engine
    init_obscura_engine();
    
    log!("Obscura Browser engine started via NDK");
    
    // Main event loop
    loop {
        if let Some(event) = app.pollEvent() {
            match event.header.type_ {
                android_native_app_glue::APP_CMD_INIT_WINDOW => {
                    // Window created - initialize renderer
                    log!("Window initialized, starting renderer");
                }
                android_native_app_glue::APP_CMD_TERM_WINDOW => {
                    // Window destroyed - clean up
                    log!("Window terminated");
                }
                android_native_app_glue::APP_CMD_INPUT_CHANGED => {
                    // Input event
                }
                _ => {}
            }
        }
        
        // Small delay to prevent busy loop
        std::thread::sleep(std::time::Duration::from_millis(16));
    }
}

fn init_obscura_engine() {
    // Initialize the obscura Rust browser engine
    // This will be connected to the native C++ layer via JNI
    log!("Obscura engine initialization started");
}