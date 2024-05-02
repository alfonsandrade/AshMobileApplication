package com.example.ashmobileapplication;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ConnectionActivity extends AppCompatActivity {
    private NetworkCommunicator networkCommunicator;
    private BluetoothManager bluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_process);

        networkCommunicator = new HttpCommunicator();
        bluetoothManager = new BluetoothManager(this);  // Ensure constructor accepts Context

        ImageButton wifiButton = findViewById(R.id.ash_wifi_icon);
        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createHttpConnection();
            }
        });

        ImageButton bluetoothButton = findViewById(R.id.ash_bluetooth_icon);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectBluetooth();
            }
        });
    }

    private void createHttpConnection() {
        String url = "http://ESP-32IP/connect";
        String payload = "{\"connection\":\"confirm\", \"command\":\"request_connection\"}";
        networkCommunicator.sendMessage(url, payload, new NetworkCommunicator.NetworkCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    Toast.makeText(ConnectionActivity.this, "Connection successful!", Toast.LENGTH_SHORT).show();
                    new android.os.Handler().postDelayed(() -> {
                        Intent intent = new Intent(ConnectionActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                    }, 1000);
                });
            }
            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> Toast.makeText(ConnectionActivity.this, "Unable to connect: " + error, Toast.LENGTH_LONG).show());
            }
        });
    }

    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 101;

    private void connectBluetooth() {
        if (!bluetoothManager.isBluetoothSupported()) {
            Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_PERMISSIONS);
        } else {
            initiateBluetoothProcess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateBluetoothProcess();
            } else {
                Toast.makeText(this, "Bluetooth permissions are required for this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initiateBluetoothProcess() {
        if (!bluetoothManager.isBluetoothEnabled()) {
            bluetoothManager.requestEnableBluetooth();
        } else {
            bluetoothManager.connectToDevice("ESP-32_MAC-ADDRESS");
            bluetoothManager.sendCommand("turn on led");
        }
    }
}
