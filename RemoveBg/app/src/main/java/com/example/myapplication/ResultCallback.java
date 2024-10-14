package com.example.myapplication;

public interface ResultCallback<T> {
    void OnResult(boolean b, T data);
}
