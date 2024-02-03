package com.example.appdemo.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.appdemo.LoginActivity;
import com.example.appdemo.Tool;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    static String key_user_info = "user_info";

    String access_token = "";
    String refresh_token = "";
    String expires_in = "";

    public String userId = "";
    public String username = "";

    private static User instance = null;
    public static synchronized User shared() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

    public String authorization() {
        if(!Tool.isNullOrEmpty(access_token)) {
            return "Bearer " + access_token;
        }

        return "";
    }

    public boolean update(JSONObject jsonObject) throws JSONException {

        if(jsonObject == null) {
            return false;
        }

        boolean b = jsonObject.getBoolean("success");
        if(b == true) {
            JSONObject data =  jsonObject.getJSONObject("data");
            if(data != null) {
                JSONObject content =  data.getJSONObject("content");
                if(content != null) {
                    //1.
                    String access_token = content.getString("AccessToken");
                    if(Tool.isNullOrEmpty(access_token) == true) {
                        return false;
                    }

                    this.access_token = access_token;
                    JSONObject saveDict = new JSONObject();
                    saveDict.put("access_token", access_token);

                    //2.
                    String refresh_token = content.getString("RefreshToken");
                    if(refresh_token != null) {
                        this.refresh_token = refresh_token;
                        saveDict.put("refresh_token", refresh_token);
                    }

                    //3.
                    String expires_in = content.getString("ExpiresIn");
                    if(expires_in != null) {
                        this.expires_in = expires_in;
                        saveDict.put("expires_in", expires_in);
                    }

                    String jsonString = saveDict.toString();
                    return save(jsonString);
                }
            }

        }


        return false;
    }

    public boolean save(String jsonString) {
        if(jsonString == null) {
            return false;
        }

        return Tool.setStringForKey(key_user_info, jsonString);
    }

    public boolean isLogined() {
        String jsonString = Tool.getStringForKey(key_user_info);
        if(!Tool.isNullOrEmpty(jsonString)) {
            try {
                // Convert JSON string to JSONObject
                JSONObject jsonObject = new JSONObject(jsonString);
                String access_token = jsonObject.getString("access_token");
                if(!Tool.isNullOrEmpty(access_token)) {
                    return true;
                } else {
                    return false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
    void logout() {
        Tool.removeValueForKey(key_user_info);
    }

}
