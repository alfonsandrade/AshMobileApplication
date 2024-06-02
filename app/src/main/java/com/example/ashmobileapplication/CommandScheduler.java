package com.example.ashmobileapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.json.JSONObject;

public class CommandScheduler {
    private MyBluetoothManager bluetoothManager;
    private Context context;
    private static final String TAG = "CommandScheduler";

    public CommandScheduler(MyBluetoothManager bluetoothManager, Context context) {
        this.bluetoothManager = bluetoothManager;
        this.context = context;
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
                if (errorCode == ErrorCode.SERVICE_UNAVAILABLE) {
                    Intent intent = new Intent(context, ErrorActivity.class);
                    intent.putExtra("robot_error", "connection_lost");
                    context.startActivity(intent);
                }
            }
        });
    }
}
