package com.apps.radio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xuzepei on 2018/2/9.
 */

public class ItemListActivity extends BaseActivity {

    private String cateID = null;
    private String title = null;
    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        this.cateID = getIntent().getStringExtra("cateID");
        this.title = getIntent().getStringExtra("title");

        initActionBar();
        updateTitle(this.title);

        //加载数据
        updateContent();
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

    public void updateContent() {

        int isOpenAll = 1;
        if(Tool.isOpenAll() == false)
            isOpenAll = 0;

        String urlString = String.format("%s?cate_id=%s&page=%d&isopenall=%d",Constants.BASE_URL,this.cateID,this.page,isOpenAll);

        Log.i("HttpRequest: ", urlString);

        HttpRequestManager manager = HttpRequestManager.getInstance();
        manager.request(urlString, null, new HttpRequestCallback() {
            @Override
            public void onFinish(final String resultString, Object token) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("onFinish: ", resultString);
                    }
                });
            }
        });


//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().get().url(urlString).build();
//        Call call = client.newCall(request);
//        //异步调用并设置回调函数
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("HttpRequest: ", "Failed");
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                final String responseStr = response.body().string();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        try {
//                            String result = Tool.decode(responseStr,Constants.DATE);
//                            Log.i("HttpRequest: ", "Succeeded, result: " + result);
//                        } catch (Exception e) {
//                            Log.i("HttpRequest: ", "Decode failed.");
//                        }
//
//
//                    }
//                });
//            }
//        });

    }

}
