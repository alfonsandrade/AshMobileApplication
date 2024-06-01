package com.example.ashmobileapplication;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

public class FovActivity extends BaseActivity {
    private FrameLayout ballContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fov_map);

        batteryIcon = findViewById(R.id.battery_icon);
        ballContainer = findViewById(R.id.ball_container);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        robotStatusHandler = new RobotStatusHandler(this);

        if (bluetoothManager.isConnected()) {
            bluetoothManager.startListening(robotStatusHandler);
        } else {
            handleBluetoothConnection();
        }
    }

    public void updateBallCoordinates(List<RobotStatus.Coordinate> coordinates) {
        ballContainer.removeAllViews();

        for (RobotStatus.Coordinate coordinate : coordinates) {
            ImageView ballIcon = new ImageView(this);
            ballIcon.setImageResource(R.drawable.ash_fov_ball_icon);
            ballIcon.setLayoutParams(new FrameLayout.LayoutParams(50, 50));

            float xPosition = (float) coordinate.getX() * ballContainer.getWidth();
            float yPosition = (float) coordinate.getY() * ballContainer.getHeight();
            ballIcon.setX(xPosition);
            ballIcon.setY(yPosition);

            ballContainer.addView(ballIcon);

            AlphaAnimation blink = new AlphaAnimation(0.0f, 1.0f);
            blink.setDuration(500);
            blink.setRepeatMode(AlphaAnimation.REVERSE);
            blink.setRepeatCount(20);
            ballIcon.startAnimation(blink);

            ballIcon.postDelayed(() -> ballContainer.removeView(ballIcon), 10000);
        }
    }
}
