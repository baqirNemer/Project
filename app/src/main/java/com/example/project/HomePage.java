package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    private TextView loginText;
    private TextView registerText;
    private boolean soundPlayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        if (!soundPlayed) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    startSoundService();
                    soundPlayed = true;
                }
            }, 3000);
        }

        loginText = findViewById(R.id.loginText);
        registerText = findViewById(R.id.registerText);

        // Set click listener for Login TextView
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity when Login TextView is clicked
                startActivity(new Intent(HomePage.this, LoginActivity.class));
            }
        });

        // Set click listener for Register TextView
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Signup01Activity when Register TextView is clicked
                startActivity(new Intent(HomePage.this, Signup01Activity.class));
            }
        });


    }
    private void startSoundService() {
        Intent intent = new Intent(this, SoundService.class);
        startService(intent);
    }
}
