/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.hellotoast;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Displays two Buttons and a TextView.
 * - Pressing the TOAST button shows a Toast.
 * - Pressing the COUNT button increases the displayed mCount.
 *
 * Note: Fixing behavior when device is rotated is a challenge exercise for the student.
 */

public class MainActivity extends AppCompatActivity {

    private int count = 0;
    private TextView countTV;
    private Button zeroBtn;
    private Button countBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countTV = (TextView) findViewById(R.id.show_count);
        countTV.setMaxLines(1);
        countTV.setEllipsize(TextUtils.TruncateAt.END);

        zeroBtn = (Button) findViewById(R.id.button_zero);
        countBtn = (Button) findViewById(R.id.button_count);
    }

    /*
    * Shows a Toast when the TOAST button is clicked.
    *
    * @param view The view that triggered this onClick handler.
    *             Since a toast always shows on the top,
    *             the passed in view is not used.
    */
    public void showToast(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_message,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    /*
    * Increments the number in the TextView when the COUNT button is clicked.
    *
    * @param view The view that triggered this onClick handler.
    *             Since the count always appears in the TextView,
    *             the passed in view is not used.
    */
    public void countUp(View view) {
        count++;
        if (countTV != null)
            countTV.setText(Integer.toString(count));

        int color = ContextCompat.getColor(this, R.color.colorAccent);
        zeroBtn.setBackgroundColor(color);
        zeroBtn.setEnabled(true);

//color int to hex string
//        int color = ContextCompat.getColor(this, colorRes);
//        String hexColor = String.format("#%06X", (0xFFFFFF & color));

        //Button countButton = (Button) view;
        Log.d("####", "count%2: " + count % 2);

        if (count % 2 == 0) {
            color = ContextCompat.getColor(this, R.color.count_button_bg);
        } else {
            color = Color.parseColor("#FF0000");
        }
        countBtn.setBackgroundColor(color);
    }

    public void makeZero(View view) {
        count = 0;
        if (countTV != null)
            countTV.setText(Integer.toString(count));

        int color = ContextCompat.getColor(this, R.color.disable_button_color);
        zeroBtn.setBackgroundColor(color);
        zeroBtn.setEnabled(false);

        color = ContextCompat.getColor(this, R.color.count_button_bg);
        countBtn.setBackgroundColor(color);
    }
}
