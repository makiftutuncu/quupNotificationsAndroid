package com.mehmetakiftutuncu.quupnotifications.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kennyc.view.MultiStateView;
import com.mehmetakiftutuncu.quupnotifications.R;
import com.mehmetakiftutuncu.quupnotifications.tasks.LoginTask;
import com.mehmetakiftutuncu.quupnotifications.utilities.StringUtils;
import com.orhanobut.logger.Logger;

public class LoginActivity extends AppCompatActivity implements LoginTask.OnLoginListener {
    private MultiStateView multiStateView;
    private EditText mUsername;
    private EditText mPassword;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        multiStateView = (MultiStateView) findViewById(R.id.multiStateView_login);
        mUsername      = (EditText)       findViewById(R.id.editText_login_username);
        mPassword      = (EditText)       findViewById(R.id.editText_login_password);

        Button mLogin = (Button) findViewById(R.id.button_login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more, menu);

        return true;
    }

    @Override public void onBackPressed() {
        if (isErrorState()) {
            changeStateTo(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            super.onBackPressed();
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_more:
                Intent intent = new Intent(getApplicationContext(), MoreActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void login() {
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
            String username = getUsername();
            String password = getPassword();

            Logger.d("Logging in as %s...", username);

            changeStateTo(MultiStateView.VIEW_STATE_LOADING);

            new LoginTask(this, username, password, this).execute();
        }
    }

    private String getUsername() {
        return mUsername != null ? mUsername.getText().toString().trim() : null;
    }

    private String getPassword() {
        return mPassword != null ? mPassword.getText().toString().trim() : null;
    }

    private boolean isUsernameEmpty() {
        return StringUtils.isEmpty(getUsername());
    }

    private boolean isPasswordEmpty() {
        return StringUtils.isEmpty(getPassword());
    }

    private void changeStateTo(int newState) {
        if (multiStateView != null && multiStateView.getViewState() != newState) {
            multiStateView.setViewState(newState);
        }
    }

    private boolean isErrorState() {
        return multiStateView != null && multiStateView.getViewState() == MultiStateView.VIEW_STATE_ERROR;
    }

    @Override public void onLoginSuccess() {
        Logger.d("Successfully logged in as %s!", getUsername());
        finish();
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    @Override public void onLoginFailed() {
        changeStateTo(MultiStateView.VIEW_STATE_ERROR);
        Button mBack = (Button) findViewById(R.id.button_back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStateTo(MultiStateView.VIEW_STATE_ERROR);
            }
        });
    }
}
