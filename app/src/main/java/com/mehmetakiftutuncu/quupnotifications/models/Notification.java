package com.mehmetakiftutuncu.quupnotifications.models;

import com.mehmetakiftutuncu.quupnotifications.utilities.RequestUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.StringUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.option.None;
import com.mehmetakiftutuncu.quupnotifications.utilities.option.Option;
import com.mehmetakiftutuncu.quupnotifications.utilities.option.Some;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Notification {
    public final Option<String> quupId;
    public final NotificationType notificationType;
    public final long when;
    public final User by;
    public final Set<User> others;

    public final String url;

    public Notification(Option<String> quupId, NotificationType notificationType, long when, User by, Set<User> others) {
        this.quupId = quupId;
        this.notificationType = notificationType;
        this.when = when;
        this.by = by;
        this.others = others;

        url = quupId.isDefined ? (RequestUtils.Quup.QUUP_PAGE + quupId.get()) : by.profileUrl;
    }

    public static Option<Notification> fromJson(JSONObject json) {
        if (json == null) {
            return new None<>();
        }

        try {
            String maybeQuupId = json.optString("quupId");
            Option<String> quupId = maybeQuupId != null && !maybeQuupId.equals("null") ? new Some<>(maybeQuupId) : new None<String>();

            NotificationType notificationType = NotificationType.valueOf(json.getString("type"));

            long when = json.getLong("when");

            Option<User> maybeBy = User.fromJson(json.optJSONObject("by"));

            if (maybeBy.isEmpty) {
                return new None<>();
            }

            User by = maybeBy.get();

            Set<User> others = new HashSet<>();
            JSONArray othersArray = json.optJSONArray("others");
            for (int i = 0, length = othersArray != null ? othersArray.length() : 0; i < length; i++) {
                Option<User> maybeOther = User.fromJson(othersArray.optJSONObject(i));

                if (maybeOther.isEmpty) {
                    return new None<>();
                }

                others.add(maybeOther.get());
            }

            Notification notification = new Notification(quupId, notificationType, when, by, others);

            return new Some<>(notification);
        } catch (Throwable t) {
            Logger.e(t, "Failed to parse Notification Json %s", json);

            return new None<>();
        }
    }

    public String toJson() {
        return String.format(
                Locale.ENGLISH,
                "{\"quupId\":%s,\"notificationType\":\"%s\",\"when\":%d,\"by\":%s,\"others\":%s}",
                (quupId.isDefined ? "\"" + quupId.get() + "\"" : "null"),
                notificationType.toString(),
                when,
                by.toJson(),
                StringUtils.makeString(others, "[", ",", "]")
        );
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        return quupId.equals(that.quupId) && notificationType == that.notificationType && when == that.when && by.equals(that.by) && others.equals(that.others);

    }

    @Override public int hashCode() {
        int prime = 223;
        int result = quupId.hashCode();
        result = prime * result + notificationType.hashCode();
        result = prime * result + (int) (when ^ (when >>> 32));
        result = prime * result + by.hashCode();
        result = prime * result + others.hashCode();
        return result;
    }
}
