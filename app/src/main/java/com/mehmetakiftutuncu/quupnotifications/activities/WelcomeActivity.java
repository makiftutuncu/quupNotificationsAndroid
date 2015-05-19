package com.mehmetakiftutuncu.quupnotifications.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kennyc.view.MultiStateView;
import com.mehmetakiftutuncu.quupnotifications.R;
import com.mehmetakiftutuncu.quupnotifications.tasks.LogoutTask;
import com.mehmetakiftutuncu.quupnotifications.utilities.PreferenceUtils;
import com.orhanobut.logger.Logger;

public class WelcomeActivity extends AppCompatActivity implements LogoutTask.OnLogoutListener {
    private Toolbar mToolbar;
    private MultiStateView multiStateView;
    private TextView mInfo;
    private Button mLogout;
    private Button mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mToolbar = (Toolbar)  findViewById(R.id.toolbar);
        multiStateView = (MultiStateView) findViewById(R.id.multiStateView_logout);
        mInfo    = (TextView) findViewById(R.id.textView_info);
        mLogout  = (Button)   findViewById(R.id.button_logout);

        setSupportActionBar(mToolbar);

        final String username = PreferenceUtils.getUsername();

        mInfo.setText(username);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("Logging in out as %s...", username);

                changeStateTo(MultiStateView.ViewState.LOADING);

                new LogoutTask(WelcomeActivity.this, username, WelcomeActivity.this).execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isErrorState()) {
            changeStateTo(MultiStateView.ViewState.CONTENT);
        } else {
            super.onBackPressed();
        }
    }

    private void changeStateTo(MultiStateView.ViewState newState) {
        if (multiStateView != null && !multiStateView.getViewState().equals(newState)) {
            multiStateView.setViewState(newState);
        }
    }

    private boolean isErrorState() {
        return multiStateView != null && multiStateView.getViewState().equals(MultiStateView.ViewState.ERROR);
    }

    @Override
    public void onLogoutSuccess(String username) {
        Logger.d("Successfully logged out as %s!", username);
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onLogoutFailed() {
        changeStateTo(MultiStateView.ViewState.ERROR);
        mBack = (Button) findViewById(R.id.button_back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStateTo(MultiStateView.ViewState.CONTENT);
            }
        });
    }
}
