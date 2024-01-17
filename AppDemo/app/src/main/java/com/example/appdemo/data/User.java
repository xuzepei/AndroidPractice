package com.example.appdemo.data;

public class User {

    private static User instance = null;
    public static synchronized User shared() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }
}
