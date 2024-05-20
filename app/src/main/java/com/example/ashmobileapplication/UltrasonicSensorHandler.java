package com.example.ashmobileapplication;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class UltrasonicSensorHandler {
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
                return; // If direction is unknown, do nothing
        }

        sensorSignal.setVisibility(View.VISIBLE);
        sensorData.setText(String.valueOf(distance));
        sensorFrame.setVisibility(View.VISIBLE);

        handler.postDelayed(() -> {
            sensorSignal.setVisibility(View.INVISIBLE);
            sensorData.setVisibility(View.INVISIBLE);
            sensorFrame.setVisibility(View.INVISIBLE);
        }, 10000);
    }
}
