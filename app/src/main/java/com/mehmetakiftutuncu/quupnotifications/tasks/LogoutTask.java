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
                String registrationId = FirebaseInstanceId.getInstance().getToken();
                RequestBody body      = RequestBody.create(RequestUtils.JSON, "{}");

                Request request = new Request.Builder()
                        .url(RequestUtils.URL.LOGOUT + registrationId)
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

            return true;
        }
    }

    @Override protected void onPostExecute(Boolean isSuccessful) {
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
        void onLogoutSuccess(String username);

        void onLogoutFailed();
    }
}
