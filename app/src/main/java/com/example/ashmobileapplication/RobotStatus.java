package com.example.ashmobileapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RobotStatus {
    private double sensDistLeft;
    private double sensDistFront;
    private double sensDistRight;
    private double sensDistBack;
    private double batteryLevel;
    private int ballsCollected;
    private List<Coordinate> ballsCoordinates;
    private String robotStatus;
    private String robotError;

    public RobotStatus(double sensDistLeft, double sensDistFront, double sensDistRight, double sensDistBack,
                       double batteryLevel, int ballsCollected, List<Coordinate> ballsCoordinates,
                       String robotStatus, String robotError) {
        this.sensDistLeft = sensDistLeft;
        this.sensDistFront = sensDistFront;
        this.sensDistRight = sensDistRight;
        this.sensDistBack = sensDistBack;
        this.batteryLevel = batteryLevel;
        this.ballsCollected = ballsCollected;
        this.ballsCoordinates = ballsCoordinates;
        this.robotStatus = robotStatus;
        this.robotError = robotError;
    }

    public static RobotStatus fromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        double sensDistLeft = jsonObject.optDouble("sens_dist_left", 0.0);
        double sensDistFront = jsonObject.optDouble("sens_dist_front", 0.0);
        double sensDistRight = jsonObject.optDouble("sens_dist_right", 0.0);
        double sensDistBack = jsonObject.optDouble("sens_dist_back", 0.0);
        double batteryLevel = jsonObject.optDouble("battery_level", 0.0);
        int ballsCollected = jsonObject.optInt("balls_collected", 0);

        JSONArray ballsCoordinatesArray = jsonObject.optJSONArray("balls_coordinates");
        List<Coordinate> ballsCoordinates = new ArrayList<>();
        if (ballsCoordinatesArray != null) {
            for (int i = 0; i < ballsCoordinatesArray.length(); i++) {
                JSONObject coordinateObject = ballsCoordinatesArray.optJSONObject(i);
                if (coordinateObject != null) {
                    double x = coordinateObject.optDouble("X", 0.0);
                    double y = coordinateObject.optDouble("Y", 0.0);
                    ballsCoordinates.add(new Coordinate(x, y));
                }
            }
        }

        String robotStatus = jsonObject.optString("robot_status", "");
        String robotError = jsonObject.optString("robot_error", "");

        return new RobotStatus(sensDistLeft, sensDistFront, sensDistRight, sensDistBack, batteryLevel,
                ballsCollected, ballsCoordinates, robotStatus, robotError);
    }

    public double getSensDistLeft() {
        return sensDistLeft;
    }

    public double getSensDistFront() {
        return sensDistFront;
    }

    public double getSensDistRight() {
        return sensDistRight;
    }

    public double getSensDistBack() {
        return sensDistBack;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public int getBallsCollected() {
        return ballsCollected;
    }

    public List<Coordinate> getBallsCoordinates() {
        return ballsCoordinates;
    }

    public String getRobotStatus() {
        return robotStatus;
    }

    public String getRobotError() {
        return robotError;
    }

    public static class Coordinate {
        private double x;
        private double y;

        public Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}
