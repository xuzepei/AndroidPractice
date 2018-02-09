package com.apps.radio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by xuzepei on 2018/2/9.
 */

public class ItemListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        initActionBar();
        updateTitle(R.string.tab_0);
    }

    public void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){

            //显示返回按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // Set the padding to match the Status Bar height 可以换成任何View组件
        //toolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        //toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void updateTitle(String title) {
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(title);
    }

    public void updateTitle(int resID) {
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(resID);
    }
}
