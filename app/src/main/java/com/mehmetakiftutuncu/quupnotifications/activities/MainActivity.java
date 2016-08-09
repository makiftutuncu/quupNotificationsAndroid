package com.mehmetakiftutuncu.quupnotifications.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mehmetakiftutuncu.quupnotifications.R;
import com.mehmetakiftutuncu.quupnotifications.utilities.GoogleAPIUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.PreferenceUtils;
import com.mehmetakiftutuncu.quupnotifications.utilities.StringUtils;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Remove
        // GoogleAPIUtils.checkGooglePlayServices(this);

        Button mLogin = (Button) findViewById(R.id.button_login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        if (!StringUtils.isEmpty(PreferenceUtils.getUsername())) {
            finish();
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more, menu);

        return true;
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
}
