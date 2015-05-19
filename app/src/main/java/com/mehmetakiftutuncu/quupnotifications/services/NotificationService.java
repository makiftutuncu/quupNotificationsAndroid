package com.mehmetakiftutuncu.quupnotifications.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mehmetakiftutuncu.quupnotifications.receivers.NotificationReceiver;
import com.orhanobut.logger.Logger;

public class NotificationService extends IntentService {
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras != null && !extras.isEmpty()) {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            String messageType = gcm.getMessageType(intent);

            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.d("Received a message: %s", extras.toString());
            }
        }

        NotificationReceiver.completeWakefulIntent(intent);
    }
}
