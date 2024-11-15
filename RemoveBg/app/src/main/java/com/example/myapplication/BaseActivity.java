package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class BaseActivity extends AppCompatActivity {

    public LinearLayout adContainer = null;

    ScrollView scrollView = null;

    public float scrollX;
    public float scrollY;

    static public String request_code = "request_code";
    static public int REQUEST_PHOTO_FROM_CAMERA = 0;
    static public int REQUEST_PHOTO_FROM_ALBUM = 1;
    static public int REQUEST_IMAGE_CROPPER = 2;
    static public int REQUEST_DSD_DETAIL = 3;
    static public int REQUEST_LANGUAGE = 4;

    String cameraPermission[] = new String[]{Manifest.permission.CAMERA}; ;
    String storagePermission[]= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String callPhonePermission[]= new String[]{Manifest.permission.CALL_PHONE};
    public final int CAMERA_REQUEST = 100;
    public final int STORAGE_REQUEST = 200;
    public final int CALL_PHONE_REQUEST = 300;
    protected boolean isRequesting = false;
    public boolean isLoadingMore = false;
    public boolean isBlocking = false;
    public String errorMsg;

    public RequestPermissionCallback requestPermissionCallback = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setup();
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        requestPermissionCallback = null;
    }

    private void setup() {
        //initNavigationBar();


    }

    public void onClickLeftButton(View view) {
        Log.d("####", "onClickLeftButton");
    }

    public void onClickRightButton(View view) {
        Log.d("####", "onClickRightButton");
    }

    public void onBackPressed() {
        // 不调用 super，防止默认的行为（即返回前一个Activity）
        super.onBackPressed();
        // 可以在这里添加你的逻辑，比如弹出对话框询问用户是否真的想返回
    }


    public void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    public void dismissKeyboard() {
        // 获取输入法管理器
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏键盘
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    //Permission
    //Storage Permission
    public boolean checkStoragePermission() {
        int permission = ContextCompat.checkSelfPermission(this, storagePermission[0]);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkCameraPermission() {
        int permission = ContextCompat.checkSelfPermission(this, cameraPermission[0]);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkCallPhonePermission() {
        int permission = ContextCompat.checkSelfPermission(this, callPhonePermission[0]);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    // Requesting gallery permission
    public boolean requestStoragePermission() {
        if(checkStoragePermission())
            return true;
        requestPermissions(storagePermission, STORAGE_REQUEST);
        return false;
    }

    // Requesting camera permission
    public boolean requestCameraPermission() {
        if(checkCameraPermission())
            return true;
        requestPermissions(cameraPermission, CAMERA_REQUEST);
        return false;
    }

    public boolean requestCallPhonePermission() {
        if(checkCallPhonePermission())
            return true;
        requestPermissions(callPhonePermission, CALL_PHONE_REQUEST);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_permission_granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(requestPermissionCallback != null) {
                        requestPermissionCallback.onResult(camera_permission_granted, CAMERA_REQUEST, this);
                    }
                }
                break;
            }
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storage_permission_granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(requestPermissionCallback != null) {
                        requestPermissionCallback.onResult(storage_permission_granted, STORAGE_REQUEST, this);
                    }
                }
                break;
            }
            case CALL_PHONE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storage_permission_granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(requestPermissionCallback != null) {
                        requestPermissionCallback.onResult(storage_permission_granted, CALL_PHONE_REQUEST, this);
                    }
                }
                break;
            }
            default: {

            }
        }
    }

}
