package com.demo.facereconstruction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequest {
    private static HttpRequest instance = null;
    private static boolean isRequesting = false;

    HttpCallback mCallback = null;
    HttpCallbackWithToken mCallbackWithToken = null;
    JSONObject mToken = null;
    String mTokenString = null;
    String mUrlString = null;

    public static synchronized HttpRequest shared() {
        if (instance == null) {
            instance = new HttpRequest();
        }
        return instance;
    }

    public boolean downloadModel(String url, String tokenString, HttpCallbackWithToken callback) {
        if (url.length() == 0 || isRequesting == true) {
            return false;
        }

        mCallbackWithToken = callback;
        mTokenString = tokenString;
        mUrlString = url;

        Log.d("#### Request", "DOWNLOAD: " + url);
        OkHttpClient client = new OkHttpClient();
        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .build();
        Log.d("#### Request", request.toString());

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("#### Request", "onFailure:" + e.toString());

                if (mCallbackWithToken != null) {
                    mCallbackWithToken.onFailure(e.toString(), mTokenString);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                Log.d("#### Request", "onResponse-StatusCode: " + response.code());
                if (response.isSuccessful()) {
                    if (mCallbackWithToken != null) {

                        // Get the value of the "Content-Type" header
                        String contentType = response.header("Content-Type");
                        if (contentType != null) {
                            Log.d("#### Request", "Content-Type: " + contentType);
                        }

                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            // Convert the response body to a string
                            InputStream inputStream = responseBody.byteStream();
                            boolean result = Tool.saveModel(inputStream, mTokenString);
                            if(result) {
                                mCallbackWithToken.onSuccess(mTokenString);
                                return;
                            }
                        }
                    }
                }

                if (mCallbackWithToken != null) {
                    mCallbackWithToken.onFailure("", mTokenString);
                }
            }
        });

        return true;
    }

}
