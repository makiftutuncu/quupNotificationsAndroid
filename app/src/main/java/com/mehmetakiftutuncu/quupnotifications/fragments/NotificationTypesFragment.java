package com.mehmetakiftutuncu.quupnotifications.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.mehmetakiftutuncu.quupnotifications.R;

public class NotificationTypesFragment extends PreferenceFragment {
    @Override public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.notification_types);
    }
}
