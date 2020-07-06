package com.zhuangfei.adapterlib.station;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuangfei.adapterlib.station.model.ClipBoardModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu ZhuangFei on 2019/2/6.
 */
public class StationSdk implements Serializable{
    private static final String TAG = "StationSdk";
    protected IStationView stationView;
    protected StationJsSupport jsSupport;
    public static int SDK_VERSION = 4;
    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;

    public void init(IStationView stationView, String space){
        this.stationView = stationView;
        jsSupport = new StationJsSupport(stationView.getWebView());
        preferences = stationView.getSharedPreferences(space);
        editor = preferences.edit();
    }

    public StationJsSupport getJsSupport() {
        return jsSupport;
    }

    @Deprecated
    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void setMinSupport(int minSupport) {
        Log.d(TAG, "setMinSupport: " + SDK_VERSION + ":min:" + minSupport);
        if (SDK_VERSION < minSupport) {
            stationView.showMessage("版本太低，不支持本服务站，请升级新版本!");
            stationView.finish();
        }
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void saveSchedules(String name, String json) {

    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void toast(String msg) {
        stationView.showMessage(msg);
    }

    @Deprecated
    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void messageDialog(final String tag, final String title,
                              final String content, final String confirmText) {
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                AlertDialog.Builder builder = new AlertDialog.Builder(stationView.getContext())
                        .setTitle(title)
                        .setMessage(content)
                        .setPositiveButton(confirmText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (dialogInterface != null) {
                                    dialogInterface.dismiss();
                                }
                                jsSupport.callJs("onMessageDialogCallback('$0')", new String[]{tag});
                            }
                        });
                builder.create().show();
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void simpleDialog(final String title, final String content, final String okBtn, final String cancelBtn, final String callback) {
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                AlertDialog.Builder builder = new AlertDialog.Builder(stationView.getContext())
                        .setTitle(title)
                        .setMessage(content);

                if (cancelBtn != null) {
                    builder.setNegativeButton(cancelBtn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (dialogInterface != null) {
                                dialogInterface.dismiss();
                            }
                            if(callback!=null){
                                jsSupport.callJs(callback+"(false)");
                            }
                        }
                    });
                }

                if (okBtn != null){
                    builder.setPositiveButton(okBtn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (dialogInterface != null) {
                                dialogInterface.dismiss();
                            }
                            if(callback!=null){
                                jsSupport.callJs(callback+"(true)");
                            }
                        }
                    });
                }
                builder.create().show();
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void setTitle(final String title) {
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.setTitle(title);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void putInt(final String key, final int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public int getInt(final String key, final int def) {
        return preferences.getInt(key,def);
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void putString(final String key, final String value){
        editor.putString(key,value);
        editor.commit();
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public String getString(final String key, final String defVal){
        return preferences.getString(key,defVal);
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void commit(){
        editor.commit();
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void clear(){
        editor.clear();
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void jumpPage(final String page){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.jumpPage(page);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void loadingFinish(){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.notifyLoadingFinish();
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void loadingStart(){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.notifyLoadingStart();
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void goback(){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.goback();
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void setStatusBarColor(final String color){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.setStatusBarColor(color);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void setFloatActionBarVisiable(final boolean b){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.setFloatActionBarVisiable(b);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void setActionBarVisiable(final boolean b){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.setActionBarVisiable(b);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void setActionBarAlpha(final float alpha){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.setActionBarAlpha(alpha);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public int dp2px(final int dp){
        return stationView.dp2px(dp);
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void setActionBarColor(final String color){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.setActionBarColor(color);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void setActionTextColor(final String color){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.setActionTextColor(color);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void registerClipBoard(final String regex){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.registerClipBoard(regex);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void unregisterClipBoard(){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.unregisterClipBoard();
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public boolean isRegisterClipBoard(){
        return stationView.isRegisterClipBoard();
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public String getClipContent(){
        return stationView.getClipContent();
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void getFromServer(final String moduleName, final String tag){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.getFromServer(moduleName,tag);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void putToServer(final String moduleName, final String value, final String tag){
        stationView.postThread(new IStationView.IMainRunner() {
            @Override
            public void done() {
                stationView.putToServer(moduleName,value,tag);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void doAction(String action,String params,String tag){

    }
}
