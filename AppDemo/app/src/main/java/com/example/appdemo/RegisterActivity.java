package com.example.appdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.appdemo.common.ClickCallback;
import com.example.appdemo.common.NavigationBar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setup();
    }

    void setup() {
        //1. Navigation bar
        initNavigationBar();

        //2. SegmentedControl
//        RadioGroup radioGroup = findViewById(R.id.radioGroup);
//
//        // Set a listener to handle segment selection
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // Handle the checked RadioButton
////                switch (checkedId) {
////                    case R.id.radio_btn_1:
////                        // Do something for Segment 1
////                        break;
////                    case R.id.radio_btn_2:
////                        // Do something for Segment 2
////                        break;
////                    // Add cases for additional segments
////                }
//            }
//        });

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        //TabLayout.Tab tab = tabLayout.getTabAt(0);
        //tab.getOrCreateBadge().setNumber(666);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("####", "onTabSelected: " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Log.d("####", "onTabUnselected: " + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Log.d("####", "onTabReselected: " + tab.getPosition());
            }
        });

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        RegisterPagerAdapter pagerAdapter = new RegisterPagerAdapter(this);
        pagerAdapter.addFragment(new RegisterByPhoneNumberFragment(), getString(R.string.phone_number));
        pagerAdapter.addFragment(new SecondFragment(), getString(R.string.email));
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(pagerAdapter.getPageTitle(position))
        ).attach();

//        Button submitButton = (Button) findViewById(R.id.btn_submit);
//        submitButton.setOnClickListener(view -> {
//            onClickSubmitButton(view);
//        });
    }

    void initNavigationBar() {

        NavigationBar navigationBar = findViewById(R.id.navigation_bar);
        navigationBar.setup(new ClickCallback() {
            @Override
            public void onClickLeftButton(View view) {
                Log.d("####", "onClickLeftButton");
                finish();
            }

            @Override
            public void onClickRightButton(View view) {
                Log.d("####", "onClickRightButton");
            }
        });

        navigationBar.setLeftButtonDrawable(this,R.drawable.back_arrow, Color.BLACK);
        navigationBar.setBackgroundColor(Color.TRANSPARENT);
        //navigationBar.setTitle("Register", Color.BLACK);

        setSupportActionBar(navigationBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
            //actionBar.hide();
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the home back arrow click event here
                finish();
                return true;

            // Add more cases if you have other menu items

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void onClickSubmitButton(View view) {
        finish();
    }
}