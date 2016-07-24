package com.mehmetakiftutuncu.quupnotifications.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mehmetakiftutuncu.quupnotifications.R;
import com.mehmetakiftutuncu.quupnotifications.models.Notification;
import com.mehmetakiftutuncu.quupnotifications.utilities.RequestUtils;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

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

            if (jsonArray.length() > 0) {
                Notification firstNotification = Notification.fromJson(jsonArray.getJSONObject(0)).get();
                Load first = getNotification(firstNotification);

                Intent profileIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(firstNotification.by.profileUrl));
                PendingIntent profilePendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, profileIntent, 0);

                if (jsonArray.length() == 1) {
                    first.button(R.mipmap.ic_person_action, getString(R.string.notifications_profile), profilePendingIntent)
                            .largeIcon(Picasso.with(getApplicationContext()).load(firstNotification.by.avatarUrl).get())
                            .simple()
                            .build();
                } else {
                    int count = jsonArray.length();
                    String[] lines = new String[count];

                    for (int i = 0; i < count; i++) {
                        Notification notification = Notification.fromJson(jsonArray.getJSONObject(i)).get();

                        String title   = getTitle(notification);
                        String message = getString(notification.notificationType.messageResourceId);

                        lines[i] = title + " " + message;
                    }

                    String title = getString(R.string.notifications_title, count);

                    Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RequestUtils.Quup.ALL_NOTIFICATIONS));
                    PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

                    first.number(count)
                            .inboxStyle(lines, title, getString(R.string.app_name))
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

        return PugNotification.with(getApplicationContext())
                .load()
                .autoCancel(true)
                .title(getTitle(notification))
                .message(notification.notificationType.messageResourceId)
                .smallIcon(notification.notificationType.smallIconResourceId)
                .largeIcon(R.mipmap.ic_launcher)
                .click(notificationPendingIntent);
    }

    private String getTitle(Notification notification) {
        String title = String.format("%s [@%s]", notification.by.fullName, notification.by.userName);

        if (notification.others.size() > 0) {
            title += " " + getString(R.string.notifications_otherUsers, notification.others.size());
        }

        return title;
    }
}
