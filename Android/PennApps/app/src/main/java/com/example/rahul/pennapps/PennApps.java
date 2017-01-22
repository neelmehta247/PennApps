package com.example.rahul.pennapps;

import android.app.Application;

/**
 * Created by neel on 22/01/17 at 5:59 AM.
 */

public class PennApps extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // During your first implementation, we recommend you to turn on
        // logging for potential troubleshooting.
        // The logs will be under the tag 'ButtonSDK'
        // adb logcat -s ButtonSDK
        if (BuildConfig.DEBUG) {
            com.usebutton.sdk.Button.enableDebugLogging();
        }
        com.usebutton.sdk.Button.getButton(this).start();
    }
}
