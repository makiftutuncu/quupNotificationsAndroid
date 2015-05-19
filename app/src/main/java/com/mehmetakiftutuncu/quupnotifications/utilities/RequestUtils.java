/*
 * Copyright (C) 2015 Mehmet Akif Tütüncü
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mehmetakiftutuncu.quupnotifications.utilities;

import com.squareup.okhttp.MediaType;

public class RequestUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static class URL {
        public static final String SERVER = "https://quupnotifications.herokuapp.com/";

        public static final String NOTIFICATIONS = SERVER + "notifications";
        public static final String MENTIONS      = SERVER + "mentions";
        public static final String MESSAGES      = SERVER + "messages";
        public static final String LOGIN         = SERVER + "login";
        public static final String LOGOUT        = SERVER + "logout";
    }

    public static class Quup {
        public static final String SERVER = "https://quup.com/";

        public static final String NOTIFICATION      = SERVER + "my/NotificationRedirect/";
        public static final String ALL_NOTIFICATIONS = SERVER + "my/allnotification";
    }
}
