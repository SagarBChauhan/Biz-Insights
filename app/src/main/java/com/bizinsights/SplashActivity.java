package com.bizinsights;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.bizinsights.utility.Globals;

public class SplashActivity extends AppCompatActivity {
    private final int DELAY = 5000;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        globals = (Globals) getApplicationContext();
        new Handler().postDelayed(() -> {
            if (globals.getLoginData() != null) {
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, DELAY);
    }
}