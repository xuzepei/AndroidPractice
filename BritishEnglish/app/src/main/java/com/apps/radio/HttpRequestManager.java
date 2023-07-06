package com.apps.radio;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xuzepei on 2018/2/9.
 */

public class HttpRequestManager {

    private static final HttpRequestManager instance = new HttpRequestManager();
    private HttpRequestManager() {} //一定要有私有构造
    public static HttpRequestManager getInstance() {
        return instance;
    }
    private ArrayList<String> requestingArray = new ArrayList<>();
    private HttpRequestCallback _callback = null;

    public boolean request(String urlString, final Object token, final HttpRequestCallback callback) {

        if(isRequesting(urlString) == true)
            return false;

        this.addToArray(urlString);

        _callback = callback;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(urlString).build();
        Call call = client.newCall(request);
        //异步调用并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                String urlString = call.request().url().toString();
                Log.i("onFailure: ", urlString);
                removeFromArray(urlString);


                if(_callback != null)
                    _callback.onFinish("",token);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                try {

                    String urlString = call.request().url().toString();
                    Log.i("onResponse: ", urlString);
                    removeFromArray(urlString);

                    final String responseStr = response.body().string();
                    String result = Tool.decode(responseStr,Constants.DATE);
                    //Log.i("HttpRequest: ", "Succeeded, result: " + result);

                    if(_callback != null)
                        _callback.onFinish(result,token);

                } catch (Exception e) {
                    Log.i("HttpRequest: ", "Decode failed.");

                    if(_callback != null)
                        _callback.onFinish("",token);
                }
            }
        });

        return true;
    }
    
    public boolean addToArray(String urlString) {
        
        if(urlString.length() == 0)
            return false;

        if(isRequesting(urlString) == true)
            return false;

        this.requestingArray.add(urlString);

        return true;
    }

    public void removeFromArray(String urlString) {

        if(urlString.length() == 0)
            return;

        for (String temp : this.requestingArray) {

            if(urlString.equals(temp) == true)
            {
                this.requestingArray.remove(temp);
                return;
            }

        }
    }

    public boolean isRequesting(String urlString) {

        if(urlString.length() == 0)
            return false;

        for (String temp : this.requestingArray) {

            if(urlString.equals(temp) == true)
            {
                return true;
            }

        }

        return false;
    }
}
