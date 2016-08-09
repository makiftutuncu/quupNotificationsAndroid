package com.mehmetakiftutuncu.quupnotifications.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mehmetakiftutuncu.quupnotifications.R;
import com.mehmetakiftutuncu.quupnotifications.models.Notification;
import com.mehmetakiftutuncu.quupnotifications.utilities.PreferenceUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.RequestUtils;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.goncalves.pugnotification.notification.Load;
import br.com.goncalves.pugnotification.notification.PugNotification;

public class NotificationService extends FirebaseMessagingService {
    @Override public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.d("Received notification message!");

        Map<String, String> data = remoteMessage.getData();
        String notificationsString = data.get("notifications");

        if (notificationsString != null) {
            parseAndShowNotification(notificationsString);
        }
    }

    private void parseAndShowNotification(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            if (jsonArray.length() == 1) {
                Notification notification = Notification.fromJson(jsonArray.getJSONObject(0)).get();

                if (PreferenceUtils.Notifications.enabledFor(notification.notificationType)) {
                    Load load = getNotification(notification);

                    Intent profileIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notification.by.profileUrl));
                    PendingIntent profilePendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, profileIntent, 0);

                    load.button(R.mipmap.ic_person_action, getString(R.string.notifications_profile), profilePendingIntent)
                        .largeIcon(Picasso.with(getApplicationContext()).load(notification.by.avatarUrl).get())
                        .simple()
                        .build();
                }
            } else if (jsonArray.length() > 1) {
                int count = jsonArray.length();
                List<String> lines = new ArrayList<>();
                Notification firstNotification = null;

                for (int i = 0; i < count; i++) {
                    Notification notification = Notification.fromJson(jsonArray.getJSONObject(i)).get();

                    if (PreferenceUtils.Notifications.enabledFor(notification.notificationType)) {
                        if (firstNotification == null) {
                            firstNotification = notification;
                        }

                        String title   = getTitle(notification);
                        String message = getString(notification.notificationType.messageResourceId);

                        lines.add(title + " " + message);
                    }
                }

                count = lines.size();

                if (count > 0) {
                    String title = getString(R.string.notifications_title, count);

                    Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RequestUtils.Quup.ALL_NOTIFICATIONS));
                    PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

                    String[] linesArray = new String[count];
                    for (int i = 0, size = lines.size(); i < size; i++) {
                        linesArray[i] = lines.get(i);
                    }

                    Load load = getNotification(firstNotification);

                    load.number(count)
                        .inboxStyle(linesArray, title, getString(R.string.app_name))
                        .click(notificationPendingIntent)
                        .simple()
                        .build();
                }
            }
        } catch (Throwable t) {
            Logger.e(t, "Failed to parse FCM message JSON %s", jsonString);
        }
    }

    private Load getNotification(Notification notification) {
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notification.url));
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        String sound = PreferenceUtils.Notifications.sound();
        boolean vibration = PreferenceUtils.Notifications.vibration();

        Load load = PugNotification.with(getApplicationContext())
                .load()
                .autoCancel(true)
                .title(getTitle(notification))
                .sound(sound != null ? Uri.parse(sound) : Uri.EMPTY)
                .message(notification.notificationType.messageResourceId)
                .smallIcon(notification.notificationType.smallIconResourceId)
                .largeIcon(R.mipmap.ic_launcher)
                .click(notificationPendingIntent);

        return vibration ? load : load.vibrate(new long[]{});
    }

    private String getTitle(Notification notification) {
        String title = String.format("%s [@%s]", notification.by.fullName, notification.by.userName);

        if (notification.others.size() > 0) {
            title += " " + getString(R.string.notifications_otherUsers, notification.others.size());
        }

        return title;
    }
}
