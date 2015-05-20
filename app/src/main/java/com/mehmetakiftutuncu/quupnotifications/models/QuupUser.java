package com.mehmetakiftutuncu.quupnotifications.models;

import com.orhanobut.logger.Logger;

import org.json.JSONObject;

public class QuupUser {
    public String id;
    public String username;
    public String name;
    public String avatar;

    private QuupUser(String id, String username, String name, String avatar) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.avatar = avatar;
    }

    public static QuupUser fromJSON(JSONObject json) {
        try {
            String id       = json.getString("id");
            String username = json.getString("username");
            String name     = json.getString("name");
            String avatar   = json.getString("avatar");

            return new QuupUser(id, username, name, avatar);
        } catch (Throwable t) {
            Logger.e(t, "Failed to create quup user from json %s", json.toString());
            return null;
        }
    }
}
