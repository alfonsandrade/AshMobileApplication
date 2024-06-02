package com.example.ashmobileapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RobotStatusHandler {
    private static final String TAG = "RobotStatusHandler";
    private final BaseActivity activity;
    private final Context context;

    public RobotStatusHandler(BaseActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public void handleStatusUpdate(String jsonStatus) {
        try {
            Log.d(TAG, "Received JSON: " + jsonStatus); // Log the received JSON
            RobotStatus robotStatus = RobotStatus.fromJson(jsonStatus);
            double batteryLevel = robotStatus.getBatteryLevel();
            int ballsCollected = robotStatus.getBallsCollected();
            List<RobotStatus.Coordinate> ballsCoordinates = robotStatus.getBallsCoordinates();
            double sensDistFront = robotStatus.getSensDistFront();
            double sensDistRight = robotStatus.getSensDistRight();
            double sensDistBack = robotStatus.getSensDistBack();
            double sensDistLeft = robotStatus.getSensDistLeft();
            String robotStatusText = robotStatus.getRobotStatus();
            String robotError = robotStatus.getRobotError();

            activity.runOnUiThread(() -> {
                if (activity.batteryIcon != null) {
                    activity.updateBatteryIcon(batteryLevel);
                    activity.batteryIcon.setVisibility(View.VISIBLE);
                }

                if (activity instanceof FovActivity) {
                    FovActivity fovActivity = (FovActivity) activity;
                    fovActivity.updateBallCoordinates(ballsCoordinates);
                }

                if (activity.ballsCollectedView != null) {
                    activity.ballsCollectedView.setText(String.valueOf(ballsCollected));
                    fadeInView(activity.ballsCollectedView);
                    if (activity.ballsCollectedFrame != null) fadeInView(activity.ballsCollectedFrame);
                    blinkTextView(activity.ballsCollectedView, 3000);
                }

                if (activity.sensDistFrontView != null) {
                    activity.sensDistFrontView.setText(String.valueOf(sensDistFront));
                    activity.updateSensorData("front", sensDistFront);
                }
                if (activity.sensDistRightView != null) {
                    activity.sensDistRightView.setText(String.valueOf(sensDistRight));
                    activity.updateSensorData("right", sensDistRight);
                }
                if (activity.sensDistBackView != null) {
                    activity.sensDistBackView.setText(String.valueOf(sensDistBack));
                    activity.updateSensorData("back", sensDistBack);
                }
                if (activity.sensDistLeftView != null) {
                    activity.sensDistLeftView.setText(String.valueOf(sensDistLeft));
                    activity.updateSensorData("left", sensDistLeft);
                }

                activity.updateRobotStatus(robotStatusText);
                if (robotError != null && !robotError.isEmpty()) {
                    handleRobotError(robotError);
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse robot status JSON", e);
        }
    }

    private void handleRobotError(String robotError) {
        if (activity.bluetoothManager != null) {
            activity.bluetoothManager.disconnect();
        }
        Intent intent = new Intent(context, ErrorActivity.class);
        intent.putExtra("robot_error", robotError);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (activity != null) {
            activity.finish();
        }
    }

    private void fadeInView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(1000);
            fadeIn.setFillAfter(true);
            view.startAnimation(fadeIn);
        }
    }

    private void blinkTextView(TextView textView, int duration) {
        if (textView != null) {
            AlphaAnimation blink = new AlphaAnimation(0.0f, 1.0f);
            blink.setDuration(500);
            blink.setRepeatMode(AlphaAnimation.REVERSE);
            blink.setRepeatCount(duration / 500);
            textView.startAnimation(blink);
        }
    }
}
