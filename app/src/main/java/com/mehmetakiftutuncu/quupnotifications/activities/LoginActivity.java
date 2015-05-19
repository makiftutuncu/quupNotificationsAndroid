package com.mehmetakiftutuncu.quupnotifications.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kennyc.view.MultiStateView;
import com.mehmetakiftutuncu.quupnotifications.R;
import com.mehmetakiftutuncu.quupnotifications.tasks.LoginTask;
import com.orhanobut.logger.Logger;

public class LoginActivity extends AppCompatActivity implements LoginTask.OnLoginListener {
    private Toolbar mToolbar;
    private MultiStateView multiStateView;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private Button mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar       = (Toolbar)        findViewById(R.id.toolbar);
        multiStateView = (MultiStateView) findViewById(R.id.multiStateView_login);
        mUsername      = (EditText)       findViewById(R.id.editText_login_username);
        mPassword      = (EditText)       findViewById(R.id.editText_login_password);
        mLogin         = (Button)         findViewById(R.id.button_login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        setSupportActionBar(mToolbar);
    }

    @Override
    public void onBackPressed() {
        if (isErrorState()) {
            changeStateTo(MultiStateView.ViewState.CONTENT);
        } else {
            super.onBackPressed();
        }
    }

    private void login() {
        if (mUsername != null && mPassword != null) {
            View focus     = null;
            boolean cancel = false;

            if (isUsernameEmpty()) {
                Logger.d("Failed to login, username is empty!");
                mUsername.setError(getString(R.string.error_nonEmpty));
                focus = mUsername;
                cancel = true;
            } else if (isPasswordEmpty()) {
                Logger.d("Failed to login, password is empty!");
                mPassword.setError(getString(R.string.error_nonEmpty));
                focus = mPassword;
                cancel = true;
            }

            if (cancel) {
                if (focus != null) {
                    focus.requestFocus();
                }
            } else {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                Logger.d("Logging in as %s...", username);

                changeStateTo(MultiStateView.ViewState.LOADING);

                new LoginTask(this, username, password, this).execute();
            }
        }
    }

    private boolean isUsernameEmpty() {
        return mUsername == null || TextUtils.isEmpty(mUsername.getText());
    }

    private boolean isPasswordEmpty() {
        return mPassword == null || TextUtils.isEmpty(mPassword.getText());
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
    public void onLoginSuccess() {
        Logger.d("Successfully logged in as %s!", mUsername.getText().toString());
        finish();
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    @Override
    public void onLoginFailed() {
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
