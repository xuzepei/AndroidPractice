package com.example.appdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appdemo.common.ClickCallback;
import com.example.appdemo.common.NavigationBar;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setup();
    }

    void setup() {

        //1. Navigation bar
        NavigationBar navigationBar = findViewById(R.id.navigation_bar);
        navigationBar.setup(new ClickCallback() {
            @Override
            public void onClickLeftButton(View view) {
                Log.d("####", "onClickLeftButton");
                onClickBackButton(view);
            }

            @Override
            public void onClickRightButton(View view) {
                Log.d("####", "onClickRightButton");
            }
        });

        navigationBar.setLeftButtonDrawable(this,R.drawable.back_arrow, Color.BLACK);
        navigationBar.setRightButtonDrawable(this,R.drawable.ic_tab_alarm, ContextCompat.getColor(this, R.color.red));
        navigationBar.setBackgroundColor(Color.TRANSPARENT);
        navigationBar.hideButtonById(R.id.right_button);

        navigationBar.setTitle("Forgot Password", Color.BLACK);
        //navigationBar.setLeftButtonTintColor(this, R.color.black);
        setSupportActionBar(navigationBar);
        //navigationBar.setVisibility(View.GONE);

        // Hide the toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setTitle("Custom Toolbar");
            //actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.hide();
        }

        //2.
        Button submitButton = (Button) findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(view -> {
            onClickSubmitButton(view);
        });
    }

    void onClickSubmitButton(View view) {
        finish();
    }

    public void onClickBackButton(View view) {
        Log.d("####", "onClickBackButton");
        finish();
    }
}