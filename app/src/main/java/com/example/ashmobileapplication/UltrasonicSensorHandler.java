package com.example.ashmobileapplication;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class UltrasonicSensorHandler {
    private static final String TAG = "UltrasonicSensorHandler";
    private final View sensorSignalLeft, sensorSignalFront, sensorSignalRight, sensorSignalBack;
    private final TextView sensorDataLeft, sensorDataFront, sensorDataRight, sensorDataBack;
    private final View sensorFrameLeft, sensorFrameFront, sensorFrameRight, sensorFrameBack;
    private final Handler handler;

    public UltrasonicSensorHandler(View sensorSignalLeft, View sensorSignalFront, View sensorSignalRight, View sensorSignalBack,
                                   TextView sensorDataLeft, TextView sensorDataFront, TextView sensorDataRight, TextView sensorDataBack,
                                   View sensorFrameLeft, View sensorFrameFront, View sensorFrameRight, View sensorFrameBack) {
        this.sensorSignalLeft = sensorSignalLeft;
        this.sensorSignalFront = sensorSignalFront;
        this.sensorSignalRight = sensorSignalRight;
        this.sensorSignalBack = sensorSignalBack;
        this.sensorDataLeft = sensorDataLeft;
        this.sensorDataFront = sensorDataFront;
        this.sensorDataRight = sensorDataRight;
        this.sensorDataBack = sensorDataBack;
        this.sensorFrameLeft = sensorFrameLeft;
        this.sensorFrameFront = sensorFrameFront;
        this.sensorFrameRight = sensorFrameRight;
        this.sensorFrameBack = sensorFrameBack;
        this.handler = new Handler();
    }

    public void updateSensorData(String direction, double distance) {
        final View sensorSignal;
        final TextView sensorData;
        final View sensorFrame;

        switch (direction) {
            case "left":
                sensorSignal = sensorSignalLeft;
                sensorData = sensorDataLeft;
                sensorFrame = sensorFrameLeft;
                break;
            case "front":
                sensorSignal = sensorSignalFront;
                sensorData = sensorDataFront;
                sensorFrame = sensorFrameFront;
                break;
            case "right":
                sensorSignal = sensorSignalRight;
                sensorData = sensorDataRight;
                sensorFrame = sensorFrameRight;
                break;
            case "back":
                sensorSignal = sensorSignalBack;
                sensorData = sensorDataBack;
                sensorFrame = sensorFrameBack;
                break;
            default:
                return;
        }

        Log.d(TAG, "Updating sensor data for direction: " + direction + " with distance: " + distance);
        if (distance > 0) {
            sensorData.setText(String.valueOf(distance));
            fadeInView(sensorSignal);
            fadeInView(sensorData);
            fadeInView(sensorFrame);

            handler.postDelayed(() -> {
                fadeOutView(sensorSignal);
                fadeOutView(sensorData);
                fadeOutView(sensorFrame);
            }, 5000);
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

    private void fadeOutView(View view) {
        if (view != null) {
            AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
            fadeOut.setDuration(1000);
            fadeOut.setFillAfter(true);
            view.startAnimation(fadeOut);

            handler.postDelayed(() -> {
                view.setVisibility(View.INVISIBLE);
                view.clearAnimation();
                Log.d(TAG, "View set to invisible and animation cleared: " + view.getId());
            }, 1000);
        }
    }
}
