package com.example.appdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdemo.common.HttpCallback;
import com.example.appdemo.common.HttpRequest;
import com.example.appdemo.common.IndicatorDialog;
import com.example.appdemo.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Wave;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setup();
    }

    void setup() {

        //1. forgot password
        TextView forgotPasswordTV = (TextView) findViewById(R.id.forget);
        forgotPasswordTV.setPaintFlags(forgotPasswordTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordActivity();
            }
        });

        //2. login button
        Button imageButton = (Button) findViewById(R.id.btn_login);
        imageButton.setOnClickListener(view -> {
            onClickLoginButton(view);
        });

        //1. register
        TextView registerTV = (TextView) findViewById(R.id.register_tv);
        registerTV.setPaintFlags(forgotPasswordTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterActivity();
            }
        });

        //3. set test username
        usernameET = (EditText) findViewById(R.id.username_edit);
        usernameET.setText("TestLabCN");

        passwordET = (EditText) findViewById(R.id.password_edit);
        passwordET.setText("cntestfreqty");

    }

    public void onClickLoginButton(View view) {
        Log.d("####", "clicked login button.");

        hideKeyboard(usernameET);
        hideKeyboard(passwordET);

        String urlString = "https://dl.freqtek.com:18791/dsd/token";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", usernameET.getText());
            jsonObject.put("password", passwordET.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        showProgressDialog();

        try {
            HttpRequest.shared().post(urlString, jsonObject, new HttpCallback() {
                @Override
                public void onFailure(String error) {
                    Log.d("####onFailure", error);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            dismissProgressDialog();
                            Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onSuccess(Object data) {
                    if(data != null && (data instanceof String)) {
                        // someObject is of type String
                        try {
                            // Convert JSON string to JSONObject
                            JSONObject jsonObject = new JSONObject(data.toString());
                            Log.d("####onSuccess", jsonObject.toString());

                            boolean b = User.shared().update(jsonObject);
                            if(b) {
                                Tool.shared().updateUserInfo();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dismissProgressDialog();
                                        if(User.shared().isLogined()){
                                            Toast.makeText(LoginActivity.this, "Login succeeded.", Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dismissProgressDialog();
                                        Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showProgressDialog() {
        View blocker = findViewById(R.id.loading_blocker);
        blocker.setZ(100);
        blocker.setVisibility(View.VISIBLE);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.GONE);

        View blocker = findViewById(R.id.loading_blocker);
        blocker.setZ(0);
        blocker.setVisibility(View.GONE);
    }

    // Function to hide the keyboard
    void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    private void showForgotPasswordActivity() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void showRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}