package com.example.ashmobileapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    protected MyBluetoothManager bluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothManager = new MyBluetoothManager(this);
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void handleBluetoothConnection() {
        bluetoothManager.connectToDevice(new MyBluetoothManager.ConnectionCallback() {
            @Override
            public void onConnectionSuccess() {
                runOnUiThread(() -> showToast("Bluetooth connected successfully"));
            }

            @Override
            public void onConnectionFailed() {
                runOnUiThread(() -> showToast("Bluetooth connection failed"));
            }
        });
    }
}
