package com.mehmetakiftutuncu.quupnotifications.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mehmetakiftutuncu.quupnotifications.R;
import com.mehmetakiftutuncu.quupnotifications.tasks.GCMRegisterTask;
import com.mehmetakiftutuncu.quupnotifications.utilities.GCMUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.PreferenceUtils;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mLogin   = (Button)  findViewById(R.id.button_login);

        setSupportActionBar(mToolbar);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        if (!TextUtils.isEmpty(PreferenceUtils.getUsername())) {
            finish();
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (GCMUtils.checkPlayServices(this)) {
            String regId = PreferenceUtils.getRegistrationId(getApplicationContext());

            if (regId.isEmpty()) {
                new GCMRegisterTask(this).execute();
            }
        } else {
            Logger.e("No valid Google Play Services APK found!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_more:
                Intent intent = new Intent(getApplicationContext(), MoreActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
