package com.example.ashmobileapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    protected MyBluetoothManager bluetoothManager;
    protected ImageView batteryIcon;
    protected RobotStatusHandler robotStatusHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothManager = MyBluetoothManager.getInstance(); // Use singleton instance
        robotStatusHandler = new RobotStatusHandler(this);

        // Only start listening if already connected
        if (bluetoothManager.isConnected()) {
            bluetoothManager.startListening(robotStatusHandler);
        }
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void handleBluetoothConnection() {
        if (bluetoothManager == null) {
            bluetoothManager = MyBluetoothManager.getInstance();
        }

        bluetoothManager.connectToDevice(this, new MyBluetoothManager.ConnectionCallback() {
            @Override
            public void onConnectionSuccess() {
                runOnUiThread(() -> showToast("Bluetooth connected successfully"));
                bluetoothManager.startListening(robotStatusHandler);
            }

            @Override
            public void onConnectionFailed() {
                runOnUiThread(() -> showToast("Bluetooth connection failed"));
            }
        });
    }

    public void updateBatteryIcon(double batteryLevel) {
        if (batteryIcon != null) {
            BatteryIconUpdater.updateBatteryIcon(batteryIcon, batteryLevel);
        }
    }
}
