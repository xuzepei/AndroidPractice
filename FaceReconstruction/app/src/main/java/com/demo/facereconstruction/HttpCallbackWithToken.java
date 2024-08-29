package com.demo.facereconstruction;

import org.json.JSONObject;

public interface HttpCallbackWithToken<T> {
    void onFailure(String error, String token);
    void onSuccess(T data, JSONObject token);
    void onSuccess(String token);
}
