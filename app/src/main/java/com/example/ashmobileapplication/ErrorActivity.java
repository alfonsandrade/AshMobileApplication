package com.example.ashmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ErrorActivity extends BaseActivity {
    private Handler handler = new Handler();
    private Runnable redirectRunnable = this::redirectToConnectionActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error);

        TextView errorTextView = findViewById(R.id.asherrortext);
        String error = getIntent().getStringExtra("robot_error");
        String errorMessage = getErrorMessage(error);
        errorTextView.setText(errorMessage);

        adjustTextSize(errorTextView, errorMessage);

        ImageButton reconnectButton = findViewById(R.id.reconnect_button);
        reconnectButton.setOnClickListener(v -> attemptReconnect());

        handler.postDelayed(redirectRunnable, 30000);
    }

    private String getErrorMessage(String error) {
        switch (error) {
            case "base_not_found":
                return "Base not found.";
            case "robot_stuck":
                return "Robot is stuck. Please check the robot.";
            case "ball_stuck":
                return "A ball is stuck in the robot tube.";
            case "Connection lost with the robot. Please reconnect.":
                return "Connection lost with the robot. Please reconnect.";
            default:
                return "An unknown error occurred. Please check the robot.";
        }
    }

    private void adjustTextSize(TextView textView, String text) {
        if (text.length() > 50) {
            textView.setTextSize(20);
        } else if (text.length() > 40) {
            textView.setTextSize(22);
        } else {
            textView.setTextSize(25);
        }
    }

    private void attemptReconnect() {
        if (bluetoothManager != null) {
            bluetoothManager.disconnect();
            redirectToConnectionActivity();
        } else {
            showToast("BluetoothManager is not initialized");
        }
    }

    private void redirectToConnectionActivity() {
        Intent intent = new Intent(ErrorActivity.this, ConnectionActivity.class);
        startActivity(intent);
        finish();
    }
}
