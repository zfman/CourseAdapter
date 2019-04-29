package com.zhuangfei.adapterlib.callback;

/**
 * 核心版本检查回调接口
 * Created by Liu ZhuangFei on 2019/3/12.
 */
public interface OnVersionFindCallback {
    void onNewVersionFind(int newNumber,String newVersionName,String newVersionDesc);
}
