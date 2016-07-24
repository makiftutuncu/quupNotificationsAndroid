package com.mehmetakiftutuncu.quupnotifications;

import android.app.Application;
import android.content.ContextWrapper;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.pixplicity.easyprefs.library.Prefs;

public class QuupNotificationApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();

        Logger.init("QuupNotifications")
              .setMethodCount(5)
              .hideThreadInfo()
              .setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
