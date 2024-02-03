package com.example.appdemo;

import android.content.Context;
import android.util.Log;

import com.example.appdemo.common.HttpCallback;
import com.example.appdemo.common.HttpRequest;
import com.example.appdemo.data.User;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Tool {

    private static Tool instance = null;
    public static synchronized Tool shared() {
        if(instance == null) {
            instance = new Tool();
        }
        return instance;
    }

    public Tool() {
    }


    public static String INTENT_PARAMS = "intent_parameters";

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean setStringForKey(String key, String value) {
        if(value != null && !Tool.isNullOrEmpty(key)) {
            MMKV kv = MMKV.defaultMMKV();
            kv.encode(key, value);
            return true;
        }
        return false;
    }

    public static String getStringForKey(String key) {
        if(!Tool.isNullOrEmpty(key)) {
            MMKV kv = MMKV.defaultMMKV();
            return kv.decodeString(key);
        }
        return null;
    }

    public static void removeValueForKey(String key) {
        if(!Tool.isNullOrEmpty(key)) {
            MMKV kv = MMKV.defaultMMKV();
            kv.removeValueForKey(key);
        }
    }

    public static void clearAll() {
        MMKV kv = MMKV.defaultMMKV();
        kv.clearAll();
    }

    public void updateUserInfo() {

        String urlString = "https://bsp.freqtek.com/api/user";

        try {
            HttpRequest request = new HttpRequest();
            request.get(urlString, new HttpCallback() {
                @Override
                public void onFailure(String error) {
                    Log.d("####onFailure", error);
                }

                @Override
                public void onSuccess(Object data) {

                    if(data != null && (data instanceof String)) {
                        // someObject is of type String
                        try {
                            // Convert JSON string to JSONObject
                            JSONObject jsonObject = new JSONObject(data.toString());
                            Log.d("####onSuccess", jsonObject.toString());
                            boolean b = jsonObject.getBoolean("success");
                            if(b == true) {
                                JSONObject dataDict =  jsonObject.getJSONObject("data");
                                if(dataDict != null) {
                                    String userId =  dataDict.getString("userId");
                                    User.shared().userId = userId;
                                    String username =  dataDict.getString("username");
                                    User.shared().username = username;
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    public static void showToast() {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                final Toast toast = Toast.makeText(context, "GAME OVER!\nScore: " + score, duration);
//                toast.show();
//            }
//        });
//    }


}
