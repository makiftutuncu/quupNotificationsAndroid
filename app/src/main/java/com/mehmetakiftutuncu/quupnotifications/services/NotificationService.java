package com.mehmetakiftutuncu.quupnotifications.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mehmetakiftutuncu.quupnotifications.models.QuupNotification;
import com.mehmetakiftutuncu.quupnotifications.R;
import com.mehmetakiftutuncu.quupnotifications.receivers.NotificationReceiver;
import com.mehmetakiftutuncu.quupnotifications.utilities.PreferenceUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.RequestUtils;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;

import br.com.goncalves.pugnotification.notification.Load;
import br.com.goncalves.pugnotification.notification.PugNotification;

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
                String notificationsJsonString = extras.getString("notifications", "");
                String mentionsJsonString      = extras.getString("mentions", "");
                String messagesJsonString      = extras.getString("messages", "");

                if (!TextUtils.isEmpty(notificationsJsonString)) {
                    if (!isSameNotification("notifications", notificationsJsonString)) {
                        parseAndShowNotification(notificationsJsonString);
                        PreferenceUtils.setLastNotification("notifications", notificationsJsonString);
                    }
                }

                if (!TextUtils.isEmpty(mentionsJsonString)) {
                    if (!isSameNotification("mentions", mentionsJsonString)) {
                        parseAndShowNotification(mentionsJsonString);
                        PreferenceUtils.setLastNotification("mentions", mentionsJsonString);
                    }
                }

                if (!TextUtils.isEmpty(messagesJsonString)) {
                    if (!isSameNotification("messages", messagesJsonString)) {
                        parseAndShowNotification(messagesJsonString);
                        PreferenceUtils.setLastNotification("messages", messagesJsonString);
                    }
                }
            }
        }

        NotificationReceiver.completeWakefulIntent(intent);
    }

    private void parseAndShowNotification(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            if (jsonArray.length() > 0) {
                QuupNotification firstQuupNotification = QuupNotification.fromJSON(jsonArray.getJSONObject(0));
                Load firstNotification = getNotification(firstQuupNotification);

                Intent profileIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RequestUtils.Quup.SERVER + firstQuupNotification.from.username));
                PendingIntent profilePendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, profileIntent, 0);

                if (jsonArray.length() == 1) {
                    firstNotification
                            .button(R.mipmap.ic_person_action, getString(R.string.notifications_profile), profilePendingIntent)
                            .largeIcon(Picasso.with(getApplicationContext()).load(firstQuupNotification.from.avatar).get())
                            .simple()
                            .build();
                } else {
                    int count = jsonArray.length();
                    String[] lines = new String[count];

                    for (int i = 0; i < count; i++) {
                        QuupNotification quupNotification = QuupNotification.fromJSON(jsonArray.getJSONObject(i));

                        String title   = getTitle(quupNotification);
                        String message = getString(getMessage(quupNotification));

                        lines[i] = title + " " + message;
                    }

                    String title = getString(R.string.notifications_title, count);

                    Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RequestUtils.Quup.ALL_NOTIFICATIONS));
                    PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

                    firstNotification
                            .number(count)
                            .inboxStyle(lines, title, getString(R.string.app_name))
                            .click(notificationPendingIntent)
                            .simple()
                            .build();
                }
            }
        } catch (Throwable t) {
            Logger.e(t, "Failed to parse GCM message JSON %s", jsonString);
        }
    }

    private Load getNotification(QuupNotification quupNotification) {
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RequestUtils.Quup.NOTIFICATION + quupNotification.id));
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        return PugNotification.with(getApplicationContext())
                .load()
                .autoCancel(true)
                .title(getTitle(quupNotification))
                .message(getMessage(quupNotification))
                .smallIcon(getSmallIcon(quupNotification))
                .largeIcon(R.mipmap.ic_launcher)
                .click(notificationPendingIntent);
    }

    private String getTitle(QuupNotification quupNotification) {
        String title = String.format("%s (@%s)", quupNotification.from.name, quupNotification.from.username);

        if (quupNotification.otherUsers.size() > 0) {
            title += " " + getString(R.string.notifications_otherUsers, quupNotification.otherUsers.size());
        }

        return title;
    }

    private int getMessage(QuupNotification quupNotification) {
        switch (quupNotification.type) {
            case Comment:
                return R.string.notifications_comment;

            case EntryLike:
                return R.string.notifications_like;

            case CommentLike:
                return R.string.notifications_commentLike;

            case Follow:
                return R.string.notifications_follow;

            case Mention:
                return R.string.notifications_mention;

            case DirectMessage:
                return R.string.notifications_message;

            default:
                return -1;
        }
    }

    private int getSmallIcon(QuupNotification quupNotification) {
        switch (quupNotification.type) {
            case Comment:
                return R.mipmap.ic_comment;

            case EntryLike:
            case CommentLike:
                return R.mipmap.ic_like;

            case Follow:
                return R.mipmap.ic_follow;

            case Mention:
                return R.mipmap.ic_mention;

            case DirectMessage:
                return R.mipmap.ic_message;

            default:
                return -1;
        }
    }

    private boolean isSameNotification(String key, String jsonString) {
        String hash = new String(Hex.encodeHex(DigestUtils.sha(jsonString)));
        return hash.equals(PreferenceUtils.getLastNotification(key));
    }
}
