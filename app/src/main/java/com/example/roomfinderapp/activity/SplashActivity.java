package com.example.roomfinderapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomfinderapp.R;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
    TextView tv_Splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        tv_Splash = findViewById(R.id.tv_Splash);
        callMainActivity();
    }

    private void callMainActivity() {
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        };

        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}