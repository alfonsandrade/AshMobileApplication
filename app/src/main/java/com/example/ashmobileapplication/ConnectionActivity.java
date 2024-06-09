package com.example.ashmobileapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

public class ConnectionActivity extends AppCompatActivity {
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 101;
    private MyBluetoothManager bluetoothManager;
    private HttpCommunicator httpCommunicator;
    private boolean isBluetoothConnected = false;
    private boolean isWebServerConnected = false;

    private final ActivityResultLauncher<Intent> enableBluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    checkBluetoothPermissions();
                } else {
                    showToast("Bluetooth enabling is required for this app to function");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_process);

        bluetoothManager = MyBluetoothManager.getInstance();
        httpCommunicator = new HttpCommunicator();

        ImageButton bluetoothButton = findViewById(R.id.ash_bluetooth_icon);
        bluetoothButton.setOnClickListener(v -> connectBluetooth());
    }

    private void connectBluetooth() {
        if (!bluetoothManager.isBluetoothSupported()) {
            showToast("Bluetooth not supported on this device");
            return;
        }

        if (bluetoothManager.isBluetoothEnabled()) {
            checkBluetoothPermissions();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSIONS);
            } else {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBluetoothLauncher.launch(enableIntent);
            }
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
        if (bluetoothManager.isDevicePaired(this)) {
            handleBluetoothConnection();
        } else {
            showToast("Device not paired. Please pair the device first.");
        }
    }

    private void handleBluetoothConnection() {
        bluetoothManager.connectToDevice(this, new MyBluetoothManager.ConnectionCallback() {
            @Override
            public void onConnectionSuccess() {
                runOnUiThread(() -> {
                    showToast("Bluetooth connected successfully");
                    isBluetoothConnected = true;
                    checkWebServerConnection();
                });
            }

            @Override
            public void onConnectionFailed() {
                runOnUiThread(() -> {
                    showToast("Failed to connect to the device");
                    isBluetoothConnected = false;
                    proceedBasedOnConnection();
                });
            }
        });
    }

    private void checkWebServerConnection() {
        String url = "http://192.168.68.200/connection";
        String payload = "{}";

        httpCommunicator.sendMessage(url, payload, new ResponseHandler.ResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                runOnUiThread(() -> {
                    showToast("Web server connected successfully");
                    isWebServerConnected = true;
                    proceedBasedOnConnection();
                });
            }

            @Override
            public void onFailure(int errorCode, String errorMessage) {
                runOnUiThread(() -> {
                    showToast("Failed to connect to the web server: " + errorMessage);
                    isWebServerConnected = false;
                    proceedBasedOnConnection();
                });
            }
        });
    }

    private void proceedBasedOnConnection() {
        if (isBluetoothConnected && isWebServerConnected) {
            Intent intent = new Intent(ConnectionActivity.this, CatchingLiveProcessActivity.class);
            startActivity(intent);
        } else if (isBluetoothConnected) {
            Intent intent = new Intent(ConnectionActivity.this, CatchingMainProcessActivity.class);
            startActivity(intent);
        } else {
            showToast("Connection failed. Please check your Bluetooth and web server.");
        }
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkBluetoothConnection();
            } else {
                showToast("Bluetooth permissions are required to connect");
            }
        }
    }
}
