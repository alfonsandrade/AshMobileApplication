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
        double sensDistLeft = jsonObject.getDouble("sens_dist_left");
        double sensDistFront = jsonObject.getDouble("sens_dist_front");
        double sensDistRight = jsonObject.getDouble("sens_dist_right");
        double sensDistBack = jsonObject.getDouble("sens_dist_back");
        double batteryLevel = jsonObject.getDouble("battery_level");
        int ballsCollected = jsonObject.getInt("balls_collected");

        JSONArray ballsCoordinatesArray = jsonObject.getJSONArray("balls_coordinates");
        List<Coordinate> ballsCoordinates = new ArrayList<>();
        for (int i = 0; i < ballsCoordinatesArray.length(); i++) {
            JSONObject coordinateObject = ballsCoordinatesArray.getJSONObject(i);
            double x = coordinateObject.getDouble("X");
            double y = coordinateObject.getDouble("Y");
            ballsCoordinates.add(new Coordinate(x, y));
        }

        String robotStatus = jsonObject.getString("robot_status");
        String robotError = jsonObject.getString("robot_error");

        return new RobotStatus(sensDistLeft, sensDistFront, sensDistRight, sensDistBack, batteryLevel,
                ballsCollected, ballsCoordinates, robotStatus, robotError);
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
