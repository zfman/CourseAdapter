package com.zhuangfei.adapterlib.station;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.WebView;

import com.zhuangfei.adapterlib.core.JsSupport;

/**
 * Created by Liu ZhuangFei on 2019/8/3.
 */
public interface IStationView {

    int dp2px(int dp);
    String getClipContent();
    void registerClipBoard(String regex);
    void unregisterClipBoard();
    boolean isRegisterClipBoard();
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
    void notifyLoadingStart();
    void finish();
    void goback();
    SharedPreferences getSharedPreferences(String space);
    void postThread(IMainRunner runner);
    void getFromServer(String moduleName,String tag);
    void putToServer(String moduleName,String value,String tag);

    SharedPreferences getPrivateSharedPreferences();
    SharedPreferences getCommonSharedPreferences();

    interface IMainRunner{
        void done();
    }
}


