package com.apps.radio;

/**
 * Created by xuzepei on 2018/2/14.
 */

public class DatabaseManager {

    private static final DatabaseManager instance = new DatabaseManager();
    private DatabaseManager() {} //一定要有私有构造
    public static DatabaseManager getInstance() {
        return instance;
    }


}
