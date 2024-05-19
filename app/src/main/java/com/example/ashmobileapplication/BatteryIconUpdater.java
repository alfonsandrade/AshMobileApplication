package com.example.ashmobileapplication;

import android.widget.ImageView;

public class BatteryIconUpdater {
    public static void updateBatteryIcon(ImageView batteryIcon, double batteryLevel) {
        if (batteryLevel >= 75) {
            batteryIcon.setImageResource(R.drawable.battery_100);
        } else if (batteryLevel >= 50) {
            batteryIcon.setImageResource(R.drawable.battery_75);
        } else if (batteryLevel >= 25) {
            batteryIcon.setImageResource(R.drawable.battery_50);
        } else {
            batteryIcon.setImageResource(R.drawable.battery_25);
        }
    }
}
