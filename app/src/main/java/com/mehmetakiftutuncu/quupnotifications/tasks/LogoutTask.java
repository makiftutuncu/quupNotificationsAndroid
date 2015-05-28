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
package com.mehmetakiftutuncu.quupnotifications.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.mehmetakiftutuncu.quupnotifications.utilities.NetworkUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.PreferenceUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.RequestUtils;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class LogoutTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private String mUsername;
    private OnLogoutListener mListener;

    public LogoutTask(Context context, String username, OnLogoutListener listener) {
        mContext  = context;
        mUsername = username;
        mListener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (mListener == null) {
            Logger.e("Failed to logout, listener is null!");
            return false;
        } else if (!NetworkUtils.isConnectedToInternet(mContext)) {
            Logger.e("Failed to logout, not connected to internet!");
            return false;
        } else {
            OkHttpClient mClient = new OkHttpClient();

            try {
                String registrationId = PreferenceUtils.getRegistrationId(mContext);
                String logoutJson     = String.format("{\"registrationId\":\"%s\"}", registrationId);
                RequestBody body      = RequestBody.create(RequestUtils.JSON, logoutJson);

                Request request = new Request.Builder()
                        .url(RequestUtils.URL.LOGOUT)
                        .post(body)
                        .build();

                Response response = mClient.newCall(request).execute();

                if (!response.isSuccessful()) {
                    Logger.e("Failed to logout, request failed! status: %d\n\n%s", response.code(), response.body().string());
                }
            } catch (Throwable t) {
                Logger.e(t, "Failed to logout!");
            }

            PreferenceUtils.setUsername("");
            PreferenceUtils.setRegistrationId(mContext, "");

            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean isSuccessful) {
        super.onPostExecute(isSuccessful);

        if (mListener != null) {
            if (!isSuccessful) {
                mListener.onLogoutFailed();
            } else {
                mListener.onLogoutSuccess(mUsername);
            }
        }
    }

    public interface OnLogoutListener {
        public void onLogoutSuccess(String username);

        public void onLogoutFailed();
    }
}
