package com.example.ashmobileapplication;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.TextView;

public class ScheduleActivity extends BaseActivity {
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private CommandScheduler commandScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        commandScheduler = new CommandScheduler(MyBluetoothManager.getInstance(), this);

        startTimePicker = findViewById(R.id.start_time_picker);
        endTimePicker = findViewById(R.id.end_time_picker);
        batteryIcon = findViewById(R.id.battery_icon);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        ImageButton sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(v -> {
            int startHour = startTimePicker.getHour();
            int startMinute = startTimePicker.getMinute();
            int endHour = endTimePicker.getHour();
            int endMinute = endTimePicker.getMinute();
            String startTime = String.format("%02d:%02d", startHour, startMinute);
            String endTime = String.format("%02d:%02d", endHour, endMinute);
            commandScheduler.scheduleCommand(startTime, endTime);
        });

        robotStatusHandler = new RobotStatusHandler(this);

        if (bluetoothManager.isConnected()) {
            bluetoothManager.startListening(robotStatusHandler);
        } else {
            handleBluetoothConnection();
        }
    }
}
