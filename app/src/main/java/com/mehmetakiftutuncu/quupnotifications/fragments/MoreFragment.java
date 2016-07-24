package com.mehmetakiftutuncu.quupnotifications.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.mehmetakiftutuncu.quupnotifications.R;
import com.mehmetakiftutuncu.quupnotifications.utilities.PreferenceUtils;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

public class MoreFragment extends PreferenceFragment {
    public static final String PREFERENCE_RATE      = "preference_about_rate";
    public static final String PREFERENCE_FEEDBACK  = "preference_about_feedback";
    public static final String PREFERENCE_VERSION   = "preference_about_version";
    public static final String PREFERENCE_LICENSES  = "preference_about_licenses";

    public static final String RATE_URI = "market://details?id=com.mehmetakiftutuncu.quupnotifications";

    public static final String FEEDBACK_CONTACT = "m.akif.tutuncu@gmail.com";

    @Override public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.more);

        initializeData();
    }

    private void initializeData() {
        Preference rate = findPreference(PREFERENCE_RATE);
        rate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(RATE_URI));
                startActivity(Intent.createChooser(intent, getString(R.string.more_about_rate)));
                return true;
            }
        });

        Preference feedback = findPreference(PREFERENCE_FEEDBACK);
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{FEEDBACK_CONTACT});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject));
                startActivity(Intent.createChooser(intent, getString(R.string.more_about_feedback)));
                return true;
            }
        });

        Preference version = findPreference(PREFERENCE_VERSION);

        Preference licenses = findPreference(PREFERENCE_LICENSES);
        licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Notices notices = new Notices();
                notices.addNotice(new Notice("PugNotification",      "https://github.com/halysongoncalves/PugNotification", "Copyright 2013 Halyson L. Gonçalves, Inc.", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("MultiStateView",       "https://github.com/Kennyc1012/MultiStateView",        "Copyright 2015 Kenny Campagna",             new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Logger",               "https://github.com/orhanobut/Logger",                 "Copyright 2015 Orhan Obut",                 new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("EasyPreferences",      "https://github.com/Pixplicity/EasyPreferences",       "Copyright 2014 Pixplicity, bv.",            new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("OkHttp",               "https://github.com/square/okhttp",                    "Copyright 2013 Square, Inc.",               new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Picasso",              "https://github.com/square/picasso",                   "Copyright 2013 Square, Inc.",               new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Apache Commons Codec", "https://commons.apache.org/proper/commons-codec",     "",                                          new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("WaitingDots",          "https://github.com/tajchert/WaitingDots",             "Copyright (c) 2015 Michal Tajchert",        new MITLicense()));

                new LicensesDialog.Builder(getActivity())
                        .setNotices(notices)
                        .setIncludeOwnLicense(true)
                        .build()
                        .show();
                return true;
            }
        });

        String versionName = PreferenceUtils.getAppVersionName(getActivity());
        version.setSummary(versionName);
    }
}
