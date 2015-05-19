package com.mehmetakiftutuncu.quupnotifications.utilities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.orhanobut.logger.Logger;
import com.pixplicity.easyprefs.library.Prefs;

public class PreferenceUtils {
    private static final String KEY_REG_ID      = "registration_id";
    private static final String KEY_USERNAME    = "username";
    private static final String KEY_APP_VERSION = "appVersion";

    public static String getRegistrationId(Context context) {
        String registrationId = Prefs.getString(KEY_REG_ID, "");

        if (registrationId.isEmpty()) {
            Logger.e("Registration id is not found!");

            return "";
        } else {
            int registeredVersion = Prefs.getInt(KEY_APP_VERSION, Integer.MIN_VALUE);
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
        int appVersion = getAppVersion(context);

        Logger.d("Saving registration id... id: %s, version %s", regId, appVersion);

        Prefs.putString(KEY_REG_ID, regId);
        Prefs.putInt(KEY_APP_VERSION, appVersion);
    }

    public static String getUsername() {
        return Prefs.getString(KEY_USERNAME, "");
    }

    public static void setUsername(String username) {
        Prefs.putString(KEY_USERNAME, username);
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
