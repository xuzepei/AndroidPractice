package com.example.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;


public class MainApplication extends Application {

    public static MainApplication instance;
    public static Context context;
    private boolean isAppInBackground = false;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private Activity currentActivity = null;

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        context = this;
        instance = this;

        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }

    public boolean isAppInBackground() {
        return isAppInBackground;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }


    private class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityResumed(Activity activity) {
            currentActivity = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {

            currentActivity = activity;

            if (++activityReferences == 1 && !isActivityChangingConfigurations) {
                // App enters foreground
                if(isAppInBackground) {
                    Log.d("####", "App 从后台回到前台");
                    isAppInBackground = false;

                } else {
                    //首次启动

                }

            }
        }

        @Override
        public void onActivityStopped(Activity activity) {

            if (currentActivity == activity) {
                currentActivity = null;
            }

            isActivityChangingConfigurations = activity.isChangingConfigurations();
            if (--activityReferences == 0 && !isActivityChangingConfigurations) {
                // App enters background
                Log.d("####", "App 进入后台");
                isAppInBackground = true;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (currentActivity == activity) {
                currentActivity = null;
            }
        }

        public boolean isAnyActivityRunning() {
            return activityReferences > 0;
        }
    }

}
