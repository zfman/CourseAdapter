package com.zhuangfei.toolkit.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Liu ZhuangFei on 2018/2/7.
 */

public class ToastTools {

    public static  void show(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
