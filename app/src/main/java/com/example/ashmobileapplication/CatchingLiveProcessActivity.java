package com.example.ashmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class CatchingLiveProcessActivity extends BaseActivity {
    private boolean isPaused = false;
    private CommandScheduler commandScheduler;
    private UltrasonicSensorHandler ultrasonicSensorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catching_live_process);

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
        fovButton.setOnClickListener(v -> startActivity(new Intent(CatchingLiveProcessActivity.this, FovActivity.class)));

        ImageButton liveButton = findViewById(R.id.ash_live_button);
        liveButton.setOnClickListener(v -> startActivity(new Intent(CatchingLiveProcessActivity.this, LiveActivity.class)));

        ImageButton returnButton = findViewById(R.id.ash_base_return_button);
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

        batteryIcon.setVisibility(View.INVISIBLE);

        if (bluetoothManager.isConnected()) {
            bluetoothManager.startListening(robotStatusHandler);
        } else {
            handleBluetoothConnection();
        }
    }

    public void updateSensorData(String direction, double distance) {
        ultrasonicSensorHandler.updateSensorData(direction, distance);
    }
}
