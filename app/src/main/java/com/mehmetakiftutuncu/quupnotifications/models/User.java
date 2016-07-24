package com.mehmetakiftutuncu.quupnotifications.models;

import com.mehmetakiftutuncu.quupnotifications.utilities.RequestUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.option.None;
import com.mehmetakiftutuncu.quupnotifications.utilities.option.Option;
import com.mehmetakiftutuncu.quupnotifications.utilities.option.Some;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.Locale;

public class User {
    public final String id;
    public final String userName;
    public final String fullName;

    public final String profileUrl;
    public final String avatarUrl;

    public User(String id, String userName, String fullName) {
        this.id       = id;
        this.userName = userName;
        this.fullName = fullName;

        profileUrl = RequestUtils.Quup.USER_PAGE + userName;
        avatarUrl  = RequestUtils.Quup.AVATAR + id;
    }

    public static Option<User> fromJson(JSONObject json) {
        if (json == null) {
            return new None<>();
        }

        try {
            String id       = json.getString("id");
            String userName = json.getString("userName");
            String fullName = json.getString("fullName");

            User user = new User(id, userName, fullName);

            return new Some<>(user);
        } catch (Throwable t) {
            Logger.e(t, "Failed to parse User Json %s", json);

            return new None<>();
        }
    }

    public String toJson() {
        return String.format(
                Locale.ENGLISH,
                "{\"id\":\"%s\",\"userName\":\"%s\",\"fullName\":\"%s\"}",
                id,
                userName,
                fullName
        );
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id) && userName.equals(user.userName) && fullName.equals(user.fullName);

    }

    @Override public int hashCode() {
        int prime = 127;
        int result = id.hashCode();
        result = prime * result + userName.hashCode();
        result = prime * result + fullName.hashCode();
        return result;
    }

    @Override public String toString() {
        return toJson();
    }
}
