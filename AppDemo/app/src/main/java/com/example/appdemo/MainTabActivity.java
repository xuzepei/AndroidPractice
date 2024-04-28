package com.example.appdemo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appdemo.home.HomeFragment;
import com.example.appdemo.notification.NotificationFragment;
import com.example.appdemo.workspace.WorkspaceFragment;
import com.tencent.mmkv.MMKV;

public class MainTabActivity extends AppCompatActivity {

    int tintColor = 0;
    HomeFragment homeFragment=new HomeFragment();
    WorkspaceFragment workspaceFragment=new WorkspaceFragment();
    NotificationFragment notificationFragment=new NotificationFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String rootDir = MMKV.initialize(this);
        Log.d("####", "MMKV root: " + rootDir);

        setContentView(R.layout.activity_main_tab);
        tintColor = ContextCompat.getColor(this, R.color.colorPrimary);

        //选中tab home
        onTabItemClick(findViewById(R.id.text_home));

        //先弹出登录框
        //showLoginActivity();
    }

    public void onTabItemClick(View v) {

        resetTabItemState();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        int viewId = v.getId();
        TextView textView = findViewById(viewId);;
        Drawable originalDrawable = null;

        Log.d("####", "viewId:" + viewId);
        if(viewId == R.id.text_home) {
            ft.replace(R.id.content, homeFragment);
            // Get the Drawable from resources
            originalDrawable = ContextCompat.getDrawable(this, R.drawable.ic_home_black_24dp);
        } else if (viewId == R.id.text_workspace) {
            ft.replace(R.id.content, workspaceFragment);
            originalDrawable = ContextCompat.getDrawable(this, R.drawable.ic_dashboard_black_24dp);
        } else if (viewId == R.id.text_notifications) {
            ft.replace(R.id.content, notificationFragment);
            originalDrawable = ContextCompat.getDrawable(this, R.drawable.ic_notifications_black_24dp);
        }

        ft.commit();

        setTabItemState(textView, tintDrawable(originalDrawable, tintColor), tintColor);
    }

    private void setTabItemState(TextView textView, Drawable drawable, int color) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);//Call requires API level 17
        textView.setTextColor(color);
    }

    private Drawable tintDrawable(Drawable drawable, int color) {
        // Tint the Drawable with the specified color
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable.mutate(), color); // Use .mutate() to avoid sharing state with other drawables
        return wrappedDrawable;
    }

    private void resetTabItemState() {
        TextView tv0 = findViewById(R.id.text_home);;
        TextView tv1 = findViewById(R.id.text_workspace);;
        TextView tv2 = findViewById(R.id.text_notifications);;

        Drawable originalDrawable0 = ContextCompat.getDrawable(this, R.drawable.ic_home_black_24dp);
        Drawable originalDrawable1 = ContextCompat.getDrawable(this, R.drawable.ic_dashboard_black_24dp);
        Drawable originalDrawable2 = ContextCompat.getDrawable(this, R.drawable.ic_notifications_black_24dp);

        int default_color = ContextCompat.getColor(this, R.color.black);;
        setTabItemState(tv0, tintDrawable(originalDrawable0, default_color), default_color);
        setTabItemState(tv1, tintDrawable(originalDrawable1, default_color), default_color);
        setTabItemState(tv2, tintDrawable(originalDrawable2, default_color), default_color);
    }

    void showLoginActivity() {
        Intent intent = new Intent(MainTabActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}