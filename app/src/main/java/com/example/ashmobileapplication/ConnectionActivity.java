package com.example.ashmobileapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ConnectionActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 101;
    private static final int REQUEST_BLUETOOTH_CONNECT_PERMISSION = 101;
    private MyBluetoothManager bluetoothManager;
    private static final String ESP32_MAC_ADDRESS = "8C:4B:14:9A:46:C2"; // Store MAC address in a variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_process);

        bluetoothManager = new MyBluetoothManager(this);

        ImageButton bluetoothButton = findViewById(R.id.ash_bluetooth_icon);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectBluetooth();
            }
        });
    }

    private void connectBluetooth() {
        if (!bluetoothManager.isBluetoothSupported()) {
            Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bluetoothManager.isBluetoothEnabled()) {
            checkBluetoothConnection();
        } else {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    private void checkBluetoothConnection() {
        if (bluetoothManager.isDevicePaired(ESP32_MAC_ADDRESS)) {
            bluetoothManager.connectToDevice(ESP32_MAC_ADDRESS, new MyBluetoothManager.ConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goToHomeScreen();
                        }
                    });
                    bluetoothManager.sendMessage("Hello ESP32");
                    bluetoothManager.listenForMessages();
                }

                @Override
                public void onConnectionFailed() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showErrorToast();
                        }
                    });
                }
            });
        } else {
            requestBluetoothPermissions();
        }
    }

    private void requestBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_PERMISSIONS);
        } else {
            bluetoothManager.connectToDevice(ESP32_MAC_ADDRESS, new MyBluetoothManager.ConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goToHomeScreen();
                        }
                    });
                    bluetoothManager.sendMessage("Hello ESP32");
                    bluetoothManager.listenForMessages();
                }

                @Override
                public void onConnectionFailed() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showErrorToast();
                        }
                    });
                }
            });
        }
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(ConnectionActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    private void showErrorToast() {
        Toast.makeText(this, "Failed to connect to the robot. Please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_CONNECT_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkBluetoothConnection();
        } else {
            Toast.makeText(this, "Bluetooth permission is required to connect", Toast.LENGTH_SHORT).show();
        }
    }
}
