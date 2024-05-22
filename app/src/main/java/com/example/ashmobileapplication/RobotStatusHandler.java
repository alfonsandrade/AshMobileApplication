package com.example.ashmobileapplication;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class RobotStatusHandler {
    private static final String TAG = "RobotStatusHandler";
    private final BaseActivity activity;

    public RobotStatusHandler(BaseActivity activity) {
        this.activity = activity;
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
                if (activity.batteryIcon != null) {
                    activity.updateBatteryIcon(batteryLevel);
                    activity.batteryIcon.setVisibility(View.VISIBLE);
                }

                if (activity instanceof CatchingMainProcessActivity) {
                    CatchingMainProcessActivity mainActivity = (CatchingMainProcessActivity) activity;
                    if (sensDistLeft > 0) mainActivity.updateSensorData("left", sensDistLeft);
                    if (sensDistFront > 0) mainActivity.updateSensorData("front", sensDistFront);
                    if (sensDistRight > 0) mainActivity.updateSensorData("right", sensDistRight);
                    if (sensDistBack > 0) mainActivity.updateSensorData("back", sensDistBack);
                }

                if (activity.ballsCollectedView != null) {
                    activity.ballsCollectedView.setText(String.valueOf(ballsCollected));
                    fadeInView(activity.ballsCollectedView);
                    if (activity.ballsCollectedFrame != null) fadeInView(activity.ballsCollectedFrame);
                    blinkTextView(activity.ballsCollectedView, 3000);
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse robot status JSON", e);
        }
    }

    private void fadeInView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(1000);
            fadeIn.setFillAfter(true);
            view.startAnimation(fadeIn);
            Log.d(TAG, "Fade in view: " + view.getId());
        }
    }

    private void blinkTextView(TextView textView, int duration) {
        if (textView != null) {
            AlphaAnimation blink = new AlphaAnimation(0.0f, 1.0f);
            blink.setDuration(500);
            blink.setRepeatMode(AlphaAnimation.REVERSE);
            blink.setRepeatCount(duration / 500);
            textView.startAnimation(blink);
            Log.d(TAG, "Blink text view: " + textView.getId());
        }
    }
}
