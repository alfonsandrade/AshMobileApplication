package com.example.ashmobileapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;

public class HttpCommunicator implements NetworkCommunicator {
    private OkHttpClient client;

    public HttpCommunicator() {
        this.client = new OkHttpClient();
    }
    @Override
    public void sendMessage(String url, String payload, ResponseHandler.ResponseCallback callback) {
        RequestBody body = RequestBody.create(payload, okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(ErrorCode.SERVICE_UNAVAILABLE, "Failed to send message: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    ResponseHandler.handleResponse(responseBody, callback);
                } else {
                    callback.onFailure(response.code(), "Server responded with error: " + response.code());
                }
            }
        });
    }

}
