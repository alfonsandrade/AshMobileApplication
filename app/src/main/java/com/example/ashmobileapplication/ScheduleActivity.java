package com.example.ashmobileapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TimePicker;

public class ScheduleActivity extends BaseActivity {
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private CommandScheduler commandScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        commandScheduler = new CommandScheduler(MyBluetoothManager.getInstance());

        startTimePicker = findViewById(R.id.start_time_picker);
        endTimePicker = findViewById(R.id.end_time_picker);
        batteryIcon = findViewById(R.id.battery_icon); // Ensure battery icon is set

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startHour = startTimePicker.getHour();
                int startMinute = startTimePicker.getMinute();
                int endHour = endTimePicker.getHour();
                int endMinute = endTimePicker.getMinute();
                String startTime = String.format("%02d:%02d", startHour, startMinute);
                String endTime = String.format("%02d:%02d", endHour, endMinute);
                commandScheduler.scheduleCommand(startTime, endTime);
            }
        });

        if (bluetoothManager.isConnected()) {
            bluetoothManager.startListening(robotStatusHandler);
        } else {
            handleBluetoothConnection();
        }
    }
}
