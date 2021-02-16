package com.james.common;

import android.app.Application;

import java.io.File;

public class BaseApplication extends Application {

    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public synchronized static BaseApplication getInstance() {
        return sInstance;
    }

    public void loginOutByToken(){

    }
}
