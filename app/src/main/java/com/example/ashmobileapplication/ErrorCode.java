package com.example.ashmobileapplication;

public class ErrorCode {
    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;
    public static final int SERVICE_UNAVAILABLE = 503;

    public static String getMessage(int errorCode) {
        switch (errorCode) {
            case OK:
                return "Success";
            case BAD_REQUEST:
                return "Bad Request - Malformed request or missing fields";
            case NOT_FOUND:
                return "Not Found - Target not found (e.g., base not found)";
            case CONFLICT:
                return "Conflict - Robot stuck or ball stuck";
            case SERVICE_UNAVAILABLE:
                return "Service Unavailable - Robot unavailable due to critical operations in progress";
            default:
                return "Unknown Error";
        }
    }
}
