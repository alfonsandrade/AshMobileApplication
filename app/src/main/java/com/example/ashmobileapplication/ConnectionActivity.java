package com.example.ashmobileapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
<<<<<<< Updated upstream
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
=======
>>>>>>> Stashed changes
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ConnectionActivity extends BaseActivity {
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 101;
    private static final int REQUEST_BLUETOOTH_CONNECT_PERMISSION = 101;
    private MyBluetoothManager bluetoothManager;
<<<<<<< Updated upstream
    private static final String ESP32_MAC_ADDRESS = "8C:4B:14:9A:46:C2"; // Store MAC address in a variable
=======

    private final ActivityResultLauncher<Intent> enableBluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    checkBluetoothPermissions();
                } else {
                    showToast("Bluetooth enabling is required for this app to function");
                }
            });
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_process);

        bluetoothManager = MyBluetoothManager.getInstance();

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
            showToast("Bluetooth not supported on this device");
            return;
        }

        if (bluetoothManager.isBluetoothEnabled()) {
            checkBluetoothConnection();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSIONS);
            } else {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBluetoothLauncher.launch(enableIntent);
            }
        }
    }

    private void checkBluetoothConnection() {
<<<<<<< Updated upstream
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
=======
        if (bluetoothManager.isDevicePaired(this)) {
            handleBluetoothConnection();
        } else {
            showToast("Device not paired. Please pair the device first.");
>>>>>>> Stashed changes
        }
    }

    @Override
    protected void handleBluetoothConnection() {
        bluetoothManager.connectToDevice(this, new MyBluetoothManager.ConnectionCallback() {
            @Override
            public void onConnectionSuccess() {
                runOnUiThread(() -> goToHomeScreen());
            }

            @Override
            public void onConnectionFailed() {
                runOnUiThread(() -> showToast("Failed to connect to the device"));
            }
        });
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(ConnectionActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
<<<<<<< Updated upstream
        if (requestCode == REQUEST_BLUETOOTH_CONNECT_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkBluetoothConnection();
        } else {
            Toast.makeText(this, "Bluetooth permission is required to connect", Toast.LENGTH_SHORT).show();
=======
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkBluetoothConnection();
            } else {
                showToast("Bluetooth permissions are required to connect");
            }
>>>>>>> Stashed changes
        }
    }
}
