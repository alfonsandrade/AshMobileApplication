package com.example.ashmobileapplication;

import org.json.JSONObject;
import android.util.Log;

public class CommandScheduler {
    private MyBluetoothManager bluetoothManager;
    private static final String TAG = "CommandScheduler";

    public CommandScheduler(MyBluetoothManager bluetoothManager) {
        this.bluetoothManager = bluetoothManager;
    }

    public void sendStartCommand() {
        RobotCommand command = new RobotCommand("start", null);
        sendCommand(command);
    }

    public void sendReturnToBaseCommand() {
        RobotCommand command = new RobotCommand("return_to_base", null);
        sendCommand(command);
    }

    public void sendPauseCommand() {
        RobotCommand command = new RobotCommand("pause", null);
        sendCommand(command);
    }

    public void sendResumeCommand() {
        RobotCommand command = new RobotCommand("resume", null);
        sendCommand(command);
    }

    public void scheduleCommand(String startTime, String endTime) {
        RobotCommand.Schedule schedule = new RobotCommand.Schedule(startTime, endTime);
        RobotCommand command = new RobotCommand("schedule", schedule);
        sendCommand(command);
    }

    private void sendCommand(RobotCommand command) {
        String commandJson = command.toJson();
        Log.d(TAG, "Sending command: " + commandJson);
        bluetoothManager.sendMessage(commandJson, new ResponseHandler.ResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Command sent successfully");
            }

            @Override
            public void onFailure(int errorCode, String errorMessage) {
                Log.e(TAG, "Failed to send command: " + errorMessage);
            }
        });
    }
}
