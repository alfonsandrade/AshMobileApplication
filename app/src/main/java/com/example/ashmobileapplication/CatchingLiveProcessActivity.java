package com.example.ashmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CatchingLiveProcessActivity extends BaseActivity {
    private boolean isPaused = false;
    private CommandScheduler commandScheduler;
    private RobotStatusHandler robotStatusHandler;
    private TextView ballsCollectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catching_live_process);

        commandScheduler = new CommandScheduler(MyBluetoothManager.getInstance());
        batteryIcon = findViewById(R.id.battery_icon); // Ensure battery icon is set

        ballsCollectedView = findViewById(R.id.ash_ball_counter_data);
        robotStatusHandler = new RobotStatusHandler(this, ballsCollectedView);

        ImageButton fovButton = findViewById(R.id.ash_fov_button);
        ImageButton liveButton = findViewById(R.id.ash_live_button);
        fovButton.setOnClickListener(v -> {
            Intent intent = new Intent(CatchingLiveProcessActivity.this, FovActivity.class);
            startActivity(intent);
        });

        liveButton.setOnClickListener(v -> {
            Intent intent = new Intent(CatchingLiveProcessActivity.this, LiveActivity.class);
            startActivity(intent);
        });

        ImageButton returnButton = findViewById(R.id.ash_base_return_button);
        returnButton.setOnClickListener(v -> commandScheduler.sendReturnToBaseCommand());

        ImageButton pauseButton = findViewById(R.id.ash_pause_button);
        pauseButton.setOnClickListener(v -> {
            if (isPaused) {
                commandScheduler.sendResumeCommand();
                pauseButton.setImageResource(R.drawable.resume_button);
            } else {
                commandScheduler.sendPauseCommand();
                pauseButton.setImageResource(R.drawable.pause_button);
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
