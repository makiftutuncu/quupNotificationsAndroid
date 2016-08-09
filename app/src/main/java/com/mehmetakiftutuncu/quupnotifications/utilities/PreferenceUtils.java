package com.mehmetakiftutuncu.quupnotifications.utilities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mehmetakiftutuncu.quupnotifications.models.NotificationType;
import com.pixplicity.easyprefs.library.Prefs;

public class PreferenceUtils {
    private static final String KEY_USERNAME                = "username";
    private static final String KEY_NOTIFICATIONS_SOUND     = "notifications_sound";
    private static final String KEY_NOTIFICATIONS_VIBRATION = "notifications_vibration";
    private static final String KEY_NOTIFICATIONS_ENABLED   = "notifications_enabled_";

    public static String getUsername() {
        return Prefs.getString(KEY_USERNAME, "");
    }

    public static void setUsername(String username) {
        Prefs.putString(KEY_USERNAME, username);
    }

    public static class Notifications {
        public static String sound() {
            return Prefs.getString(KEY_NOTIFICATIONS_SOUND, null);
        }

        public static boolean vibration() {
            return Prefs.getBoolean(KEY_NOTIFICATIONS_VIBRATION, true);
        }

        public static boolean enabledFor(NotificationType notificationType) {
            return Prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED + notificationType.key, true);
        }
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e.getMessage());
        }
    }
}
