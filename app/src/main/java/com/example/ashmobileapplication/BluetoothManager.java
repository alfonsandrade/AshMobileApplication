package com.example.ashmobileapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothManager {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private Context context;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // Standard SerialPortService ID

    public BluetoothManager(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public void requestEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(context, "Please enable Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    public void connectToDevice(String deviceAddress) {
        try {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            Toast.makeText(context, "Connected to device", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Connection failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, "Invalid Bluetooth address: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void sendCommand(String command) {
        if (outputStream != null) {
            try {
                outputStream.write((command + "\n").getBytes());  // Append newline character
                Toast.makeText(context, "Command sent", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(context, "Error sending command: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Not connected to any device", Toast.LENGTH_SHORT).show();
        }
    }

    public void disconnect() {
        if (bluetoothSocket != null) {
            try {
                outputStream.close();
                bluetoothSocket.close();
                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}

