package com.zhuangfei.adapterlib.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.zhuangfei.adapterlib.R;

/**
 * Created by Liu ZhuangFei on 2019/3/12.
 */
public class PackageUtils {
    public static String getPackageName(Context context){
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPackageMd5(Context context){
        if(context==null) return null;
        String packageName=getPackageName(context);
        if(packageName==null) return null;
        return Md5Security.encrypBy(packageName+context.getResources().getString(R.string.md5_key));
    }

    public static boolean isInstallApk(Context context,String packageName){
        Intent intent=context.getPackageManager().getLaunchIntentForPackage(packageName);
        if(intent==null){
            return false;
        }
        return true;
    }
}
