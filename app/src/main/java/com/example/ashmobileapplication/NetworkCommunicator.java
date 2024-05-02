package com.example.ashmobileapplication;

public interface NetworkCommunicator {
    void sendMessage(String url, String payload, NetworkCallback callback);

    interface NetworkCallback {
        void onSuccess(String response);
        void onFailure(String error);
    }
}
