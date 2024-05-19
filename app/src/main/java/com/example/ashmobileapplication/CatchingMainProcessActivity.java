package com.example.ashmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CatchingMainProcessActivity extends BaseActivity {
    private boolean isPaused = false;
    private CommandScheduler commandScheduler;
    private RobotStatusHandler robotStatusHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catching_main_process);

        commandScheduler = new CommandScheduler(MyBluetoothManager.getInstance());
        robotStatusHandler = new RobotStatusHandler(this);
        batteryIcon = findViewById(R.id.battery_icon);
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
}
