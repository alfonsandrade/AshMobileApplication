package com.example.ashmobileapplication;

import android.util.Log;

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
    public void sendMessage(String url, String payload, NetworkCallback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), payload);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HttpCommunicator", "Failed to send message: " + e.getMessage());
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Log.d("HttpCommunicator", "Response received: " + responseBody);
                    callback.onSuccess(responseBody);
                } else {
                    Log.d("HttpCommunicator", "Error response received: " + response.code());
                    callback.onFailure("Server responded with error: " + response.code());
                }
            }
        });
    }
}
