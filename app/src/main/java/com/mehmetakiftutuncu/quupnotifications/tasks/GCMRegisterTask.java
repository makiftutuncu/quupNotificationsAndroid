package com.mehmetakiftutuncu.quupnotifications.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mehmetakiftutuncu.quupnotifications.utilities.PreferenceUtils;
import com.orhanobut.logger.Logger;

public class GCMRegisterTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    private String SENDER_ID = "SOMETHING_MORE_SECRET_THAN_VICTORIA'S";

    public GCMRegisterTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mContext);

            String regId = gcm.register(SENDER_ID);

            Logger.d("Successfully registered to GCM with id %s", regId);

            PreferenceUtils.setRegistrationId(mContext, regId);
        } catch (Throwable t) {
            Logger.e(t, "Failed to register to GCM!");
        }

        return null;
    }
}
