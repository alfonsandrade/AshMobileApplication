package com.example.ashmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
public class CatchingMainProcessActivity extends BaseActivity {
    private boolean isPaused = false;
    private CommandScheduler commandScheduler;
    private UltrasonicSensorHandler ultrasonicSensorHandler;
    private ImageView ashBlueAppIcon;
    private String robotStatus = "collecting_balls";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catching_main_process);

        commandScheduler = new CommandScheduler(MyBluetoothManager.getInstance());
        batteryIcon = findViewById(R.id.battery_icon);

        ballsCollectedView = findViewById(R.id.ash_ball_counter_data);
        ballsCollectedFrame = findViewById(R.id.ash_ball_counter);

        setBallsCollectedView(ballsCollectedView);
        setBallsCollectedFrame(ballsCollectedFrame);

        robotStatusHandler = new RobotStatusHandler(this);

        View sensorSignalLeft = findViewById(R.id.sensor_signal_left);
        View sensorSignalFront = findViewById(R.id.sensor_signal_front);
        View sensorSignalRight = findViewById(R.id.sensor_signal_right);
        View sensorSignalBack = findViewById(R.id.sensor_signal_back);

        TextView sensorDataLeft = findViewById(R.id.sensor_data_left);
        TextView sensorDataFront = findViewById(R.id.sensor_data_front);
        TextView sensorDataRight = findViewById(R.id.sensor_data_right);
        TextView sensorDataBack = findViewById(R.id.sensor_data_back);

        View sensorFrameLeft = findViewById(R.id.sensor_data_left_frame);
        View sensorFrameFront = findViewById(R.id.sensor_data_front_frame);
        View sensorFrameRight = findViewById(R.id.sensor_data_right_frame);
        View sensorFrameBack = findViewById(R.id.sensor_data_back_frame);

        ultrasonicSensorHandler = new UltrasonicSensorHandler(sensorSignalLeft, sensorSignalFront, sensorSignalRight, sensorSignalBack,
                sensorDataLeft, sensorDataFront, sensorDataRight, sensorDataBack,
                sensorFrameLeft, sensorFrameFront, sensorFrameRight, sensorFrameBack);

        ImageButton fovButton = findViewById(R.id.ash_fov_button);
        fovButton.setOnClickListener(v -> startActivity(new Intent(CatchingMainProcessActivity.this, FovActivity.class)));

        ImageButton returnButton = findViewById(R.id.ash_home_button);
        returnButton.setOnClickListener(v -> commandScheduler.sendReturnToBaseCommand());

        ImageButton pauseButton = findViewById(R.id.ash_pause_button);
        pauseButton.setOnClickListener(v -> {
            if (isPaused) {
                commandScheduler.sendResumeCommand();
                pauseButton.setImageResource(R.drawable.pause_button);
            } else {
                commandScheduler.sendPauseCommand();
                pauseButton.setImageResource(R.drawable.resume_button);
            }
            isPaused = !isPaused;
        });

        ashBlueAppIcon = findViewById(R.id.ashblueappicon);
        ashBlueAppIcon.setOnLongClickListener(v -> {
            showRobotStatusSnackbar(robotStatus);
            return true;
        });

        batteryIcon.setVisibility(View.INVISIBLE);

        if (bluetoothManager.isConnected()) {
            bluetoothManager.startListening(robotStatusHandler);
        } else {
            handleBluetoothConnection();
        }
    }

    private void showRobotStatusSnackbar(String status) {
        String message;
        switch (status) {
            case "collecting_balls":
                message = "Right now I'm collecting balls";
                break;
            case "searching_for_balls":
                message = "Right now I'm searching for balls";
                break;
            case "returning_to_base":
                message = "Right now I'm returning to base";
                break;
            case "paused":
                message = "I am paused";
                break;
            default:
                message = "Status unknown";
        }

        Snackbar snackbar = Snackbar.make(findViewById(R.id.mainFragment), "", Snackbar.LENGTH_LONG);
        snackbar.setDuration(7000);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.snackbar_robot_status, null);
        ImageView snackbarIcon = customView.findViewById(R.id.snackbar_icon);
        snackbarIcon.setImageResource(R.drawable.ash_toast_message_icon);
        TextView snackbarText = customView.findViewById(R.id.snackbar_text);
        snackbarText.setText(message);
        snackbarLayout.removeAllViews();
        snackbarLayout.addView(customView);
        snackbar.show();
    }
    public void updateSensorData(String direction, double distance) {
        ultrasonicSensorHandler.updateSensorData(direction, distance);
    }
}
