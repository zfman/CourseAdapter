package com.zhuangfei.adapterlib;
import android.text.TextUtils;

/**
 * Created by Liu ZhuangFei on 2018/10/19.
 */
public class AdapterLibManager {
    private static final String TAG = "AdapterLibManager";

    //核心库版本号
    private static int libVersionNumber=10;
    private static String libVersionName="lib-2.0.0";

    //包名和appkey
    private static String appKey;

    public static String getLibVersionName() {
        return libVersionName;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static int getLibVersionNumber() {
        return libVersionNumber;
    }

    public static void init(String appkey,String secretKey){
        if(!TextUtils.isEmpty(appkey)){
            AdapterLibManager.appKey=appkey;
        }
    }
}
