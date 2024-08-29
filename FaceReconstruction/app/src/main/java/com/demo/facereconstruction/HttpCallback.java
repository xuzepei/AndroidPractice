package com.demo.facereconstruction;
public interface HttpCallback<T> {
    void onFailure(String error);
    void onSuccess(T data);
}
