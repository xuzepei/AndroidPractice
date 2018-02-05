package com.apps.radio;

import android.view.View;

/**
 * Created by xuzepei on 2018/2/5.
 */

public class Tool {

    private static final Tool instance = new Tool();

    private Tool() {} //一定要有私有构造

    public static Tool getInstance() {
        return instance;
    }

//    public static void hideSystemBottomNavigationBar(View view) {
//
//        if(view) {
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                /*| View.SYSTEM_UI_FLAG_FULLSCREEN*/;
//            view.setSystemUiVisibility(uiOptions);
//        }
//    }

}
