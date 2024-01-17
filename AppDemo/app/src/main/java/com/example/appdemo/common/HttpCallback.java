package com.example.appdemo.common;
public interface HttpCallback<T> {
    void onFailure(String error);
    void onSuccess(T data);
}
