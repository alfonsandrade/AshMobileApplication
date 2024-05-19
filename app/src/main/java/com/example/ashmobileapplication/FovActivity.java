package com.example.ashmobileapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FovActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fov_map);

        batteryIcon = findViewById(R.id.battery_icon);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (bluetoothManager.isConnected()) {
            bluetoothManager.startListening(robotStatusHandler);
        } else {
            handleBluetoothConnection();
        }
    }
}
