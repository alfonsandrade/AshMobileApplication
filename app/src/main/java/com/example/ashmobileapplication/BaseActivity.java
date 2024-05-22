package com.example.ashmobileapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    protected MyBluetoothManager bluetoothManager;
    protected ImageView batteryIcon;
    protected RobotStatusHandler robotStatusHandler;
    protected TextView ballsCollectedView;
    protected View ballsCollectedFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothManager = MyBluetoothManager.getInstance();
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

    public void setBallsCollectedView(TextView view) {
        this.ballsCollectedView = view;
    }

    public void setBallsCollectedFrame(View frame) {
        this.ballsCollectedFrame = frame;
    }
}
