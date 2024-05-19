package com.example.ashmobileapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        ImageButton catchButton = findViewById(R.id.catch_button);
        catchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                boolean webServerConnected = preferences.getBoolean("web_server_connected", false);
                Intent intent;
                if (webServerConnected) {
                    intent = new Intent(HomePageActivity.this, CatchingLiveProcessActivity.class);
                } else {
                    intent = new Intent(HomePageActivity.this, CatchingMainProcessActivity.class);
                }
                startActivity(intent);
            }
        });
        ImageButton schedule_button = findViewById(R.id.schedule_button);
        schedule_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(HomePageActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });
    }
}
