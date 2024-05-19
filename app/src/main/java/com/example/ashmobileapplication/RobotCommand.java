package com.example.ashmobileapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class RobotCommand {
    private String robotCommand;
    private Schedule schedule;

    public RobotCommand(String robotCommand, Schedule schedule) {
        this.robotCommand = robotCommand;
        this.schedule = schedule;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("robot_command", robotCommand);
            if (schedule != null) {
                jsonObject.put("schedule", schedule.toJson());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static class Schedule {
        private String startTime;
        private String endTime;

        public Schedule(String startTime, String endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public JSONObject toJson() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("start_time", startTime);
                jsonObject.put("end_time", endTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }
}
