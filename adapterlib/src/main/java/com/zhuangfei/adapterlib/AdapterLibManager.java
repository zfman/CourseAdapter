package com.zhuangfei.adapterlib;

import android.text.TextUtils;

/**
 * Created by Liu ZhuangFei on 2018/10/19.
 */
public class AdapterLibManager {
    //核心库版本号
    private static int libVersionNumber=1;
    private static String libVersionName="lib-1.0";

    //包名和appkey
    private static String packageName;
    private static String appKey;

    public static String getLibVersionName() {
        return libVersionName;
    }

    public static String getPackageName() {
        return packageName;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static int getLibVersionNumber() {
        return libVersionNumber;
    }

    public static void init(String packageName,String appkey){
        if(!TextUtils.isEmpty(packageName)){
            AdapterLibManager.packageName=packageName;
        }
        if(!TextUtils.isEmpty(appkey)){
            AdapterLibManager.appKey=appkey;
        }
    }
}
