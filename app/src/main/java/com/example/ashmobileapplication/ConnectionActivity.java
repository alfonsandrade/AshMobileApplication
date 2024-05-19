package com.example.ashmobileapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
    private MyBluetoothManager bluetoothManager;
    private static final String ESP32_MAC_ADDRESS = "8C:4B:14:9A:46:C2";

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
            checkBluetoothPermissions();
        } else {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    private void checkBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_ADVERTISE
                    },
                    REQUEST_BLUETOOTH_PERMISSIONS);
        } else {
            checkBluetoothConnection();
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
                            String url = "http://192.168.68.200/connection";
                            String payload = "{}";
                            HttpCommunicator httpCommunicator = new HttpCommunicator();
                            httpCommunicator.sendMessage(url, payload, new NetworkCommunicator.NetworkCallback() {
                                @Override
                                public void onSuccess(String response) {
                                    SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("web_server_connected", true);
                                    editor.apply();
                                    goToHomeScreen();
                                }

                                @Override
                                public void onFailure(String error) {
                                    // Store the unsuccessful connection status
                                    SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("web_server_connected", false);
                                    editor.apply();

                                    goToHomeScreen();
                                }
                            });
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
            Toast.makeText(this, "Device not paired. Please pair the device first.", Toast.LENGTH_SHORT).show();
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
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkBluetoothConnection();
            } else {
                Toast.makeText(this, "Bluetooth permissions are required to connect", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
