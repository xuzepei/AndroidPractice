package com.example.hellotoast2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int count = 0;
    private TextView countTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countTV = (TextView) findViewById(R.id.show_count);
    }

    public void showToast(View view) {
        Log.d("MainActivity", "Clicked show toast button");
        Toast toast = Toast.makeText(this, R.string.toast_message,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void countUp(View view) {
        count++;
        if(countTV != null) {
            countTV.setText(Integer.toString(count));
        }
    }
}