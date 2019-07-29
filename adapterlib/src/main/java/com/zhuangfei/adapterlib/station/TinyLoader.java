package com.zhuangfei.adapterlib.station;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhuangfei.adapterlib.station.model.TinyConfig;

/**
 * 解析服务站配置文件
 * Created by Liu ZhuangFei on 2019/7/28.
 */
public class TinyLoader {

    public void load(String configContent){
        if(TextUtils.isEmpty(configContent)) return;
        TinyConfig tinyConfig=new Gson().fromJson(configContent, TinyConfig.class);

    }
}
