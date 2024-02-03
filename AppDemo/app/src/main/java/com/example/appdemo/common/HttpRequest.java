package com.example.appdemo.common;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appdemo.Tool;
import com.example.appdemo.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequest {
    private static HttpRequest instance = null;
    private static boolean isRequesting = false;

    HttpCallback mCallback = null;
    public static synchronized HttpRequest shared() {
        if(instance == null) {
            instance = new HttpRequest();
        }
        return instance;
    }

    public boolean post(String url, JSONObject jsonObject, HttpCallback callback) throws IOException {

        if(url.length() == 0 || isRequesting == true) {
            return false;
        }

        mCallback = callback;

        Log.d("Request","POST: " + url);
        OkHttpClient client = new OkHttpClient();
        if(jsonObject != null){
            String jsonString = jsonObject.toString();
            if(Tool.isNullOrEmpty(jsonString) == false) {

                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(jsonString, mediaType);

                // Build the request
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", User.shared().authorization())
                        .build();

                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("Request", "onFailure:", e);

                        if(mCallback != null) {
                            mCallback.onFailure("");
                        }
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        Log.d("Request","onResponse-StatusCode: " + response.code());
                        if(response.isSuccessful()){
                            if(mCallback != null) {
                                ResponseBody responseBody = response.body();
                                if (responseBody != null) {
                                    // Convert the response body to a string
                                    String responseString = responseBody.string();

                                    // Print the response body
                                    Log.d("Request","onResponse-ResponseString: " + responseString);

                                    mCallback.onSuccess(responseString);
                                }

                                return;
                            }
                        }

                        if(mCallback != null) {
                            mCallback.onFailure("");
                        }
                    }
                });
            }
        }

        return true;
    }

    public boolean get(String url, HttpCallback callback) throws IOException {

        if(url.length() == 0 || isRequesting == true) {
            return false;
        }

        mCallback = callback;

        Log.d("Request","GET: " + url);
        OkHttpClient client = new OkHttpClient();
        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Authorization", User.shared().authorization())
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Request", "onFailure:", e);

                if(mCallback != null) {
                    mCallback.onFailure("");
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                Log.d("Request","onResponse-StatusCode: " + response.code());
                if(response.isSuccessful()){
                    if(mCallback != null) {
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            // Convert the response body to a string
                            String responseString = responseBody.string();

                            // Print the response body
                            Log.d("Request","onResponse-ResponseString: " + responseString);

                            mCallback.onSuccess(responseString);
                        }

                        return;
                    }
                }

                if(mCallback != null) {
                    mCallback.onFailure("");
                }
            }
        });

        return true;
    }


}
