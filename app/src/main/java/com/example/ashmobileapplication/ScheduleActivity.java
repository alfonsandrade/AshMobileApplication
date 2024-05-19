package com.example.ashmobileapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class ScheduleActivity extends AppCompatActivity {
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        startTimePicker = findViewById(R.id.start_time_picker);
        endTimePicker = findViewById(R.id.end_time_picker);

        // Set custom background for the entire TimePicker programmatically
        RelativeLayout startTimePickerLayout = (RelativeLayout) startTimePicker.getParent();
        RelativeLayout endTimePickerLayout = (RelativeLayout) endTimePicker.getParent();

        startTimePickerLayout.setBackgroundResource(R.drawable.rounded_corner_background);
        endTimePickerLayout.setBackgroundResource(R.drawable.rounded_corner_background);

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
                sendScheduledTime(startTime, endTime);
            }
        });
    }

    private void sendScheduledTime(String startTime, String endTime) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.68.200/schedule";
        String payload = "{\"robot_command\":\"schedule\",\"schedule\":{\"start_time\":\"" + startTime + "\",\"end_time\":\"" + endTime + "\"}}";
        RequestBody body = RequestBody.create(payload, okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScheduleActivity.this, "Failed to send schedule", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScheduleActivity.this, "Schedule sent successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
