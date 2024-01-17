package com.example.appdemo;

public class Tool {
    public static String INTENT_PARAMS = "intent_parameters";

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
