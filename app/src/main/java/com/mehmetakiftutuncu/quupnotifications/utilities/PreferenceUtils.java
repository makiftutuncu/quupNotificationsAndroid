package com.mehmetakiftutuncu.quupnotifications.utilities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.pixplicity.easyprefs.library.Prefs;

public class PreferenceUtils {
    private static final String KEY_USERNAME = "username";

    public static String getUsername() {
        return Prefs.getString(KEY_USERNAME, "");
    }

    public static void setUsername(String username) {
        Prefs.putString(KEY_USERNAME, username);
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
