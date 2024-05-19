package com.example.ashmobileapplication;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseHandler {

    public static void handleResponse(String response, ResponseCallback callback) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int errorCode = jsonResponse.optInt("error_code", ErrorCode.OK);

            if (errorCode == ErrorCode.OK) {
                callback.onSuccess(jsonResponse);
            } else {
                String errorMessage = ErrorCode.getMessage(errorCode);
                callback.onFailure(errorCode, errorMessage);
            }
        } catch (JSONException e) {
            Log.e("ResponseHandler", "Failed to parse response", e);
            callback.onFailure(ErrorCode.BAD_REQUEST, "Failed to parse response");
        }
    }

    public interface ResponseCallback {
        void onSuccess(JSONObject response);
        void onFailure(int errorCode, String errorMessage);
    }
}
