package com.mehmetakiftutuncu.quupnotifications.utilities;

import com.squareup.okhttp.MediaType;

public class RequestUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static class URL {
        public static final String SERVER = "https://quupnotifications.herokuapp.com/";

        public static final String LOGIN  = SERVER + "login";
        public static final String LOGOUT = SERVER + "logout/";
    }

    public static class Quup {
        public static final String SERVER = "https://quup.com/";

        public static final String QUUP_PAGE         = SERVER + "social/entry/";
        public static final String USER_PAGE         = SERVER + "@";
        public static final String AVATAR            = SERVER + "social/avatar/";
        public static final String ALL_NOTIFICATIONS = SERVER + "social/notification";
    }
}
