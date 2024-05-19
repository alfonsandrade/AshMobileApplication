package com.example.ashmobileapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class HomePageActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        bluetoothManager = MyBluetoothManager.getInstance();
        batteryIcon = findViewById(R.id.battery_icon);

        ImageButton catchButton = findViewById(R.id.catch_button);
        CommandScheduler commandScheduler = new CommandScheduler(bluetoothManager);
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
                commandScheduler.sendStartCommand();
                startActivity(intent);
            }
        });

        ImageButton scheduleButton = findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        // Ensure Bluetooth connection is handled properly
        if (bluetoothManager.isConnected()) {
            bluetoothManager.startListening(robotStatusHandler);
        } else {
            handleBluetoothConnection();
        }
    }

    @Override
    protected void handleBluetoothConnection() {
        super.handleBluetoothConnection();
    }
}
