package com.example.ashmobileapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MyBluetoothManager {
    private static MyBluetoothManager instance;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String ESP32_MAC_ADDRESS = "8C:4B:14:9A:46:C2";
    private static final String TAG = "MyBluetoothManager";

    private MyBluetoothManager() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static synchronized MyBluetoothManager getInstance() {
        if (instance == null) {
            instance = new MyBluetoothManager();
        }
        return instance;
    }

    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public boolean isDevicePaired(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices != null && !pairedDevices.isEmpty()) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getAddress().equals(ESP32_MAC_ADDRESS)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void connectToDevice(Context context, final ConnectionCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            callback.onConnectionFailed();
            return;
        }
        new Thread(() -> {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(ESP32_MAC_ADDRESS);
            boolean connectionSuccessful = false;

            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothAdapter.cancelDiscovery();
                socket.connect();
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                Log.d(TAG, "Connected to device");
                connectionSuccessful = true;
            } catch (IOException e) {
                Log.e(TAG, "Connection failed", e);
                connectionSuccessful = false;
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
            }

            if (connectionSuccessful) {
                Log.d(TAG, "Output stream: " + outputStream);
                Log.d(TAG, "Input stream: " + inputStream);
                callback.onConnectionSuccess();
            } else {
                callback.onConnectionFailed();
            }
        }).start();
    }

    public void sendMessage(String message, ResponseHandler.ResponseCallback callback) {
        if (outputStream != null) {
            try {
                Log.d(TAG, "Sending message: " + message);
                outputStream.write((message + "\n").getBytes());
                listenForResponse(callback);
            } catch (IOException e) {
                Log.e(TAG, "Failed to send message", e);
                callback.onFailure(ErrorCode.SERVICE_UNAVAILABLE, "Failed to send message");
            }
        } else {
            Log.e(TAG, "Output stream is null");
            callback.onFailure(ErrorCode.SERVICE_UNAVAILABLE, "Output stream is null");
        }
    }

    private void listenForResponse(ResponseHandler.ResponseCallback callback) {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            int bytes;
            try {
                bytes = inputStream.read(buffer);
                String response = new String(buffer, 0, bytes);
                ResponseHandler.handleResponse(response, callback);
            } catch (IOException e) {
                Log.e(TAG, "Error reading from input stream", e);
                callback.onFailure(ErrorCode.SERVICE_UNAVAILABLE, "Error reading from input stream");
            }
        }).start();
    }

    public interface ConnectionCallback {
        void onConnectionSuccess();
        void onConnectionFailed();
    }
}
