package com.mehmetakiftutuncu.quupnotifications.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mehmetakiftutuncu.quupnotifications.utilities.NetworkUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.PreferenceUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.RequestUtils;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class LoginTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private String mUsername;
    private String mPassword;
    private OnLoginListener mListener;

    public LoginTask(Context context, String username, String password, OnLoginListener listener) {
        mContext  = context;
        mUsername = username;
        mPassword = password;
        mListener = listener;
    }

    @Override protected Boolean doInBackground(Void... params) {
        if (mListener == null) {
            Logger.e("Failed to login, listener is null!");
            return false;
        } else if (!NetworkUtils.isConnectedToInternet(mContext)) {
            Logger.e("Failed to login, not connected to internet!");
            return false;
        } else {
            OkHttpClient mClient = new OkHttpClient();

            try {
                String registrationId = FirebaseInstanceId.getInstance().getToken();
                String loginJson = String.format(
                    "{\"registrationId\":\"%s\", \"username\":\"%s\", \"password\":\"%s\"}", registrationId, mUsername, mPassword
                );
                RequestBody body = RequestBody.create(RequestUtils.JSON, loginJson);

                Request request = new Request.Builder()
                        .url(RequestUtils.URL.LOGIN)
                        .post(body)
                        .build();

                Response response = mClient.newCall(request).execute();

                if (response.isSuccessful()) {
                    PreferenceUtils.setUsername(mUsername);
                    return true;
                } else {
                    Logger.e("Failed to login, request failed! status: %d\n\n%s", response.code(), response.body().string());
                    return false;
                }
            } catch (Throwable t) {
                Logger.e(t, "Failed to login!");
                return false;
            }
        }
    }

    @Override protected void onPostExecute(Boolean isSuccessful) {
        super.onPostExecute(isSuccessful);

        if (mListener != null) {
            if (!isSuccessful) {
                mListener.onLoginFailed();
            } else {
                mListener.onLoginSuccess();
            }
        }
    }

    public interface OnLoginListener {
        void onLoginSuccess();

        void onLoginFailed();
    }
}
