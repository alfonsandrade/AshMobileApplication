package com.example.ashmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class CatchingMainProcessActivity extends BaseActivity {
    private boolean isPaused = false;
    private CommandScheduler commandScheduler;
    private RobotStatusHandler robotStatusHandler;
    private UltrasonicSensorHandler ultrasonicSensorHandler;
    private TextView ballsCollectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catching_main_process);

        commandScheduler = new CommandScheduler(MyBluetoothManager.getInstance());
        batteryIcon = findViewById(R.id.battery_icon);

        ballsCollectedView = findViewById(R.id.ash_ball_counter_data);
        robotStatusHandler = new RobotStatusHandler(this, ballsCollectedView);

        // Initialize sensor signal and data views
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
