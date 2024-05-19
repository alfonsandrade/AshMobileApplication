package com.example.ashmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CatchingLiveProcessActivity extends BaseActivity {
    private boolean isPaused = false;
    private CommandScheduler commandScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catching_live_process);

        commandScheduler = new CommandScheduler(MyBluetoothManager.getInstance());

        ImageButton fovButton = findViewById(R.id.ash_fov_button);
        ImageButton liveButton = findViewById(R.id.ash_live_button);
        fovButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatchingLiveProcessActivity.this, FovActivity.class);
                startActivity(intent);
            }
        });

        liveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatchingLiveProcessActivity.this, LiveActivity.class);
                startActivity(intent);
            }
        });

        ImageButton returnButton = findViewById(R.id.ash_base_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commandScheduler.sendReturnToBaseCommand();
            }
        });

        ImageButton pauseButton = findViewById(R.id.ash_pause_button);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    commandScheduler.sendResumeCommand();
                    pauseButton.setImageResource(R.drawable.pause_button);
                } else {
                    commandScheduler.sendPauseCommand();
                    pauseButton.setImageResource(R.drawable.resume_button);
                }
                isPaused = !isPaused;
            }
        });
    }
}
