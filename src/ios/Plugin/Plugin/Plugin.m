#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(Geo, "Geo",
           CAP_PLUGIN_METHOD(setOnMessageListener, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(init, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(createCircleFence, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(hasPermission, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(requestPermission, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getCurrentLocation, CAPPluginReturnPromise);
)
