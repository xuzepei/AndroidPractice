package com.example.appdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity {

    TextView messageFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }


        //去掉actionbar上的标题
        getSupportActionBar().setTitle("");

        // calling the action bar
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null) {
//            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            actionBar.setCustomView(R.layout.actionbar_layout);
//
//            // showing the back button in action bar
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            //actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
//
//            //隐藏actionbar
//            //actionBar.hide();
//        }

        Button imageButton = (Button) findViewById(R.id.action_button);
        imageButton.setOnClickListener(view -> {
            onClickActionButton(view);
        });


        Intent intent = getIntent();
        String message = intent.getStringExtra(Tool.INTENT_PARAMS);
        messageFromIntent = findViewById(R.id.textView2);
        messageFromIntent.setText(message);

    }

    public void onClickActionButton(View view) {
        Log.d(NewActivity.this.getClass().getSimpleName(), "clicked action button.");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_new_activity, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection Simplifaction_nameiableIfStatement
        if (id == R.id.action_favorite) {
            Toast.makeText(NewActivity.this, "You have clicked settings button", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}