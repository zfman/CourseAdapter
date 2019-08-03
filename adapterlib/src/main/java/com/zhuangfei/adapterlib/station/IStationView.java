package com.zhuangfei.adapterlib.station;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.WebView;

/**
 * Created by Liu ZhuangFei on 2019/8/3.
 */
public interface IStationView {

    int dp2px(int dp);
    void setActionBarAlpha(float alpha);
    void setActionBarVisiable(boolean b);
    void setFloatActionBarVisiable(boolean b);
    void setStatusBarColor(String color);
    void setActionBarColor(String color);
    void setActionTextColor(String color);
    Context getContext();
    WebView getWebView();
    void showMessage(String msg);
    void setTitle(String title);
    void jumpPage(String page);
    void notifyLoadingFinish();
    void finish();
    void goback();
    SharedPreferences getSharedPreferences(String space);
    void postThread(IMainRunner runner);

    interface IMainRunner{
        void done();
    }
}


