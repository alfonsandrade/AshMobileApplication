package com.example.ashmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CatchingLiveProcessActivity extends BaseActivity {
    private boolean isPaused = false;
    private CommandScheduler commandScheduler;

<<<<<<< Updated upstream:app/src/main/java/com/example/ashmobileapplication/CatchingActivity.java
public class CatchingActivity extends AppCompatActivity {
=======
>>>>>>> Stashed changes:app/src/main/java/com/example/ashmobileapplication/CatchingLiveProcessActivity.java
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
                Intent intent = new Intent(CatchingActivity.this, FovActivity.class);
                startActivity(intent);
            }
        });

        liveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< Updated upstream:app/src/main/java/com/example/ashmobileapplication/CatchingActivity.java
                Intent intent = new Intent( CatchingActivity.this, LiveActivity.class);
=======
                Intent intent = new Intent(CatchingLiveProcessActivity.this, LiveActivity.class);
>>>>>>> Stashed changes:app/src/main/java/com/example/ashmobileapplication/CatchingLiveProcessActivity.java
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
