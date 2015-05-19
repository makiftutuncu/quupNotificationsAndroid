package com.mehmetakiftutuncu.models;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuupNotification {
    public String id;
    public NotificationTypes type;
    public QuupUser from;
    public List<QuupUser> otherUsers;

    private QuupNotification(String id, NotificationTypes type, QuupUser from, List<QuupUser> otherUsers) {
        this.id = id;
        this.type = type;
        this.from = from;
        this.otherUsers = otherUsers;
    }

    public static QuupNotification fromJSON(JSONObject json) {
        try {
            String id = json.getString("id");

            NotificationTypes type = NotificationTypes.valueOf(json.getString("type"));

            QuupUser from = QuupUser.fromJSON(json.getJSONObject("from"));

            ArrayList<QuupUser> otherUsers = new ArrayList<>();
            JSONArray otherUsersJson = json.getJSONArray("otherUsers");
            for (int i = 0; i < otherUsersJson.length(); i++) {
                otherUsers.add(QuupUser.fromJSON(otherUsersJson.getJSONObject(i)));
            }

            return new QuupNotification(id, type, from, otherUsers);
        } catch (Throwable t) {
            Logger.e(t, "Failed to create quup notification from json %s", json.toString());
            return null;
        }
    }
}
