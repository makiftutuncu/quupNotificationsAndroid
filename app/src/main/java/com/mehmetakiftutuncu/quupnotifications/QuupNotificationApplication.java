package com.mehmetakiftutuncu.quupnotifications;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

public class QuupNotificationApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Logger.init("QuupNotifications")
              .setMethodCount(3)
              .hideThreadInfo()
              .setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
    }
}
