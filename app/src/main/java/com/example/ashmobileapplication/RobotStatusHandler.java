package com.example.ashmobileapplication;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RobotStatusHandler {
    private static final String TAG = "RobotStatusHandler";
    private final BaseActivity activity;

    public RobotStatusHandler(BaseActivity activity) {
        this.activity = activity;
    }

    public void handleStatusUpdate(String jsonStatus) {
        try {
            RobotStatus robotStatus = RobotStatus.fromJson(jsonStatus);
            double batteryLevel = robotStatus.getBatteryLevel();
            int ballsCollected = robotStatus.getBallsCollected();
            List<RobotStatus.Coordinate> ballsCoordinates = robotStatus.getBallsCoordinates();

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
