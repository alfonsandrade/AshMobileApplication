package com.example.ashmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CatchingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catching_live_process);

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
                Intent intent = new Intent( CatchingActivity.this, LiveActivity.class);
                startActivity(intent);
            }
        });
    }
}
