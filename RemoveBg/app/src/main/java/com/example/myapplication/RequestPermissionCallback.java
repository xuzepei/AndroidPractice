package com.example.myapplication;

import android.content.Context;

public interface RequestPermissionCallback {
    void onResult(boolean granted, int requestCode, Context context);
}
