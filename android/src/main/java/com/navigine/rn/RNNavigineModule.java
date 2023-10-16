package com.navigine.rn;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.navigine.idl.java.NavigineSdk;
import com.navigine.sdk.Navigine;

import java.util.HashMap;
import java.util.Map;

public class RNNavigineModule extends ReactContextBaseJavaModule {
    private static final String REACT_CLASS = "NavigineModule";

    private NavigineSdk sdk;

    private ReactApplicationContext getContext() {
        return reactContext;
    }

    private static ReactApplicationContext reactContext = null;

    RNNavigineModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public Map<String, Object> getConstants() {
        return new HashMap<>();
    }

    @ReactMethod
    public void init(final String userHash, final String server, final Promise promise) {
        runOnUiThread(new Thread(() -> {
            Throwable initException = null;
            try {
                // In case when android application reloads during development
                // MapKitFactory is already initialized
                // And setting api key leads to crash
                try {
                    Navigine.initialize(reactContext);

                    sdk = NavigineSdk.getInstance();
                    sdk.setUserHash(userHash);
                    sdk.setServer(server);
                } catch (Throwable exception) {
                    initException = exception;
                }
                promise.resolve(null);
            } catch (Exception exception) {
                if(initException != null) {
                    promise.reject(initException);
                    return;
                }
                promise.reject(exception);
            }
        }));
    }

    private static void emitDeviceEvent(String eventName, @Nullable WritableMap eventData) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, eventData);
    }
}
