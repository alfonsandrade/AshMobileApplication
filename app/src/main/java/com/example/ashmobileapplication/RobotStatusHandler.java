package com.example.ashmobileapplication;

import org.json.JSONException;
import android.util.Log;

public class RobotStatusHandler {
    private static final String TAG = "RobotStatusHandler";
    private BaseActivity activity;

    public RobotStatusHandler(BaseActivity activity) {
        this.activity = activity;
    }

    public void handleStatusUpdate(String jsonStatus) {
        try {
            RobotStatus robotStatus = RobotStatus.fromJson(jsonStatus);
            double batteryLevel = robotStatus.getBatteryLevel();
            Log.d(TAG, "Battery level received: " + batteryLevel);
            activity.runOnUiThread(() -> activity.updateBatteryIcon(batteryLevel));
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse robot status JSON", e);
        }
    }
}
