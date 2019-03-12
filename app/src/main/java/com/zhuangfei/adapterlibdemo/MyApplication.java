package com.zhuangfei.adapterlibdemo;

import android.app.Application;

import com.zhuangfei.adapterlib.AdapterLibManager;

/**
 * Created by Liu ZhuangFei on 2019/3/12.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AdapterLibManager.init("com.zhuangfei.adapterlibdemo","");
    }
}
