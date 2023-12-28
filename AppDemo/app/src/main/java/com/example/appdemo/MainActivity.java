package com.example.appdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private int count = 0;
    private TextView countTV;
    private Button loginBtn;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        //隐藏actionbar
        //getSupportActionBar().hide();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(myToolbar != null) {
            setSupportActionBar(myToolbar);
            //myToolbar.setVisibility(View.GONE);
        }


        //异步方法
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 在此处执行需要延迟执行的代码
                Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
                if(myToolbar != null) {
                    myToolbar.getMenu().removeItem(R.id.action_settings);
                }

            }
        }, 100);


//        View myActionBar = getSupportActionBar().getCustomView();
//        TextView name = myActionBar.findViewById(R.id.toolbar_title);
//        name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "You have clicked tittle", Toast.LENGTH_LONG).show();
//            }
//        });




//        countTV = (TextView)findViewById(R.id.show_count);
//        loginBtn = (Button)findViewById(R.id.button_login);
//        loginBtn.setOnClickListener(view -> {
//            Log.d("MainActivity", "clicked login button.");
//
//            startNewActivity();
//        });


        Log.d("MainActivity","Hello, world");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection Simplifaction_nameiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "You have clicked settings button", Toast.LENGTH_LONG).show();


            return true;
        } else if (id == R.id.action_favorite) {
            Toast.makeText(MainActivity.this, "You have clicked favorite button", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    public void showToast(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void countUp(View view) {
        count++;
        if (countTV != null) {
            countTV.setText(Integer.toString(count));
        }
    }

    public void startNewActivity(View view) {
        Intent intent = new Intent(this, NewActivity.class);
        intent.putExtra(Tool.INTENT_PARAMS, "from main activity.");
        startActivity(intent);
    }


}