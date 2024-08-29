package com.demo.facereconstruction;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class MainApplication extends Application {
    public static Context context;

    private boolean isAppInBackground = false;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;

    public void onCreate() {
        super.onCreate();
        context = this;

        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }

    public boolean isAppInBackground() {
        return isAppInBackground;
    }

    private class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {

            if (++activityReferences == 1 && !isActivityChangingConfigurations) {
                // App enters foreground
                if(isAppInBackground) {
                    Log.d("####", "App 从后台回到前台");
                    isAppInBackground = false;
                } else {
                }

            }
        }

        @Override
        public void onActivityStopped(Activity activity) {

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
        }

        public boolean isAnyActivityRunning() {
            return activityReferences > 0;
        }
    }
}
