package com.example.ashmobileapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FovActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fov_map);

        batteryIcon = findViewById(R.id.battery_icon);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Initialize the RobotStatusHandler
        robotStatusHandler = new RobotStatusHandler(this, new TextView(this)); // Replace new TextView(this) with the actual TextView for balls collected

        if (bluetoothManager.isConnected()) {
            bluetoothManager.startListening(robotStatusHandler);
        } else {
            handleBluetoothConnection();
        }
    }
}
