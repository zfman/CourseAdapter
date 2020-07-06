package com.zhuangfei.adapterlib.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtils {
    private static ProgressDialog progressDialog;

    public static void show(Context context){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(context);
            progressDialog.setTitle("加载中...");
        }
        progressDialog.show();
    }

    public static void dismiss(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
