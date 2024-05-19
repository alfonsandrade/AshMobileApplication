package com.example.ashmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomePageActivity extends BaseActivity {
    private MyBluetoothManager bluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        bluetoothManager = MyBluetoothManager.getInstance();

        ImageButton catchButton = findViewById(R.id.catch_button);
        CommandScheduler commandScheduler = new CommandScheduler(bluetoothManager);
        catchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< Updated upstream
                Intent intent = new Intent(HomePageActivity.this, CatchingActivity.class);
=======
                SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                boolean webServerConnected = preferences.getBoolean("web_server_connected", false);
                Intent intent;
                if (webServerConnected) {
                    intent = new Intent(HomePageActivity.this, CatchingLiveProcessActivity.class);
                } else {
                    intent = new Intent(HomePageActivity.this, CatchingMainProcessActivity.class);
                }
                commandScheduler.sendStartCommand();
                startActivity(intent);
            }
        });

        ImageButton scheduleButton = findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, ScheduleActivity.class);
>>>>>>> Stashed changes
                startActivity(intent);
            }
        });
    }
}
