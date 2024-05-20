package com.example.ashmobileapplication;

import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class RobotStatusHandler {
    private static final String TAG = "RobotStatusHandler";
    private final BaseActivity activity;
    private final TextView ballsCollectedView;

    public RobotStatusHandler(BaseActivity activity, TextView ballsCollectedView) {
        this.activity = activity;
        this.ballsCollectedView = ballsCollectedView;
    }

    public void handleStatusUpdate(String jsonStatus) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStatus);
            double batteryLevel = jsonObject.getDouble("battery_level");
            double sensDistLeft = jsonObject.getDouble("sens_dist_left");
            double sensDistFront = jsonObject.getDouble("sens_dist_front");
            double sensDistRight = jsonObject.getDouble("sens_dist_right");
            double sensDistBack = jsonObject.getDouble("sens_dist_back");
            int ballsCollected = jsonObject.getInt("balls_collected");

            Log.d(TAG, "Battery level received: " + batteryLevel);
            activity.runOnUiThread(() -> {
                activity.updateBatteryIcon(batteryLevel);
                if (activity instanceof CatchingMainProcessActivity) {
                    ((CatchingMainProcessActivity) activity).updateSensorData("left", sensDistLeft);
                    ((CatchingMainProcessActivity) activity).updateSensorData("front", sensDistFront);
                    ((CatchingMainProcessActivity) activity).updateSensorData("right", sensDistRight);
                    ((CatchingMainProcessActivity) activity).updateSensorData("back", sensDistBack);
                }
                ballsCollectedView.setText(String.valueOf(ballsCollected));
            });

        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse robot status JSON", e);
        }
    }
}
