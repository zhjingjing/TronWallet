package com.zj.tronwallet;

import android.app.Application;

/**
 * create by zj on 2019/1/7
 */
public class MyApplication extends Application{
    public  static  MyApplication  app;

    public  static  MyApplication  getInstance(){
        return  app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

    }
}
