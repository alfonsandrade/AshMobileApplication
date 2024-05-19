package com.example.ashmobileapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MyBluetoothManager {
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public interface ConnectionCallback {
        void onConnectionSuccess();
        void onConnectionFailed();
    }

    public MyBluetoothManager(Context context) {
        this.context = context;
        BluetoothManager systemBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = systemBluetoothManager.getAdapter();
    }

    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public boolean isDevicePaired(String macAddress) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices != null && !pairedDevices.isEmpty()) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getAddress().equals(macAddress)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void connectToDevice(final String macAddress, final ConnectionCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAddress);
                boolean connectionSuccessful = false;

                try {
                    socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    bluetoothAdapter.cancelDiscovery();
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                    connectionSuccessful = true;
                } catch (IOException e) {
                    Log.e("MyBluetoothManager", "Connection failed", e);
                    connectionSuccessful = false;
                    try {
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException closeException) {
                        Log.e("MyBluetoothManager", "Could not close the client socket", closeException);
                    }
                }

                if (connectionSuccessful) {
                    callback.onConnectionSuccess();
                } else {
                    callback.onConnectionFailed();
                }
            }
        }).start();
    }

    public void sendMessage(String message) {
        if (outputStream != null) {
            try {
                outputStream.write((message + "\n").getBytes());
            } catch (IOException e) {
                Log.e("MyBluetoothManager", "Failed to send message", e);
            }
        }
    }

    public void listenForMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024];
                int bytes;

                while (true) {
                    try {
                        bytes = inputStream.read(buffer);
                        String message = new String(buffer, 0, bytes);
                        Log.d("MyBluetoothManager", "Received: " + message);
                    } catch (IOException e) {
                        Log.e("MyBluetoothManager", "Error reading from input stream", e);
                        break;
                    }
                }
            }
        }).start();
    }
}
