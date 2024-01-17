package com.example.appdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appdemo.common.HttpCallback;
import com.example.appdemo.common.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setup();
    }

    void setup() {
        TextView textView = (TextView) findViewById(R.id.forget);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button imageButton = (Button) findViewById(R.id.btn_login);
        imageButton.setOnClickListener(view -> {
            onClickLoginButton(view);
        });
    }

    public void onClickLoginButton(View view) {
        Log.d("####", "clicked login button.");

        String urlString = "https://dl.freqtek.com:18791/dsd/token";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", "TestLabCN");
            jsonObject.put("password", "cntestfreqty");
            //jsonObject.put("module", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            HttpRequest.shared().post(urlString, jsonObject, new HttpCallback() {
                @Override
                public void onFailure(String error) {

                }

                @Override
                public void onSuccess(Object data) {
                    if(data != null && (data instanceof String)) {
                        // someObject is of type String
                        try {
                            // Convert JSON string to JSONObject
                            JSONObject jsonObject = new JSONObject(data.toString());

                            Log.d("####", jsonObject.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }



        //finish();
    }
}