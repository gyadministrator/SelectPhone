package com.example.gy.selectphone;

import android.app.Application;

import cn.waps.AppConnect;

/**
 * Created by Administrator on 2017/9/7.
 */

public class MyApplication extends Application {
    private static final String APP_KEY = "650edb5a016d507375b346d6dbd78051";
    private static final String APPPID = "baidu";

    @Override
    public void onCreate() {
        super.onCreate();
        AppConnect.getInstance(APP_KEY, APPPID, this);
        AppConnect.getInstance(this).initPopAd(this);
    }
}
