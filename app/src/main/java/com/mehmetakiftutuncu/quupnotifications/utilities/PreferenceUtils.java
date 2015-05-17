package com.mehmetakiftutuncu.quupnotifications.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.orhanobut.logger.Logger;

public class PreferenceUtils {
    private static final String PROPERTY_REG_ID      = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getPreferences(context);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.isEmpty()) {
            Logger.e("Registration id is not found!");

            return "";
        } else {
            int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
            int currentVersion    = getAppVersion(context);

            if (registeredVersion != currentVersion) {
                Logger.e("App version changed! registered: %d, current: %d", registeredVersion, currentVersion);

                return "";
            } else {
                return registrationId;
            }
        }
    }

    public static void setRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getPreferences(context);
        int appVersion = getAppVersion(context);

        Logger.d("Saving registration id... id: %s, version %s", regId, appVersion);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Should never happen!
            throw new RuntimeException("Could not get package name: " + e.getMessage());
        }
    }
}
