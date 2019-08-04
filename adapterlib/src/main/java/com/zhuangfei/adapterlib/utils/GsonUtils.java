package com.zhuangfei.adapterlib.utils;

import com.google.gson.Gson;

/**
 * Created by Liu ZhuangFei on 2019/8/4.
 */
public class GsonUtils {
    private static Gson gson;
    private GsonUtils(){}
    public static Gson getGson(){
        if(gson==null){
            synchronized (GsonUtils.class){
                if(gson==null){
                    gson=new Gson();
                }
            }
        }
        return gson;
    }
}
