package com.zhuangfei.adapterlib.station;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.zhuangfei.adapterlib.activity.StationWebViewActivity;

import org.json.JSONObject;

/**
 * Created by Liu ZhuangFei on 2019/2/6.
 */
public class StationSdk {
    private static final String TAG = "StationSdk";
    StationWebViewActivity stationView;
    StationJsSupport jsSupport;
    public static int SDK_VERSION = 2;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public StationSdk(StationWebViewActivity stationWebViewActivity, String space) {
        stationView = stationWebViewActivity;
        jsSupport = new StationJsSupport(stationView.getWebView());
        preferences = stationWebViewActivity.getSharedPreferences(space, Context.MODE_PRIVATE);
        editor = preferences.edit();
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
    /**
     * @params firstUrl 重定向的地址
     */
    public void addButton(final String btnText, final String[] textArray, final String[] linkArray) {
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stationView.setButtonSettings(btnText, textArray, linkArray);
            }
        });
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
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(stationView.getStationContext())
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
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(stationView.getStationContext())
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
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stationView.setTitle(title);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void putInt(final String key, final int value) {
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editor.putInt(key, value);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void putString(final String key, final String value){
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editor.putString(key,value);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public String getString(final String key, final String defVal){
        return preferences.getString(key,defVal);
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void commit(){
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editor.commit();
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void clear(){
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editor.clear();
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void jumpPage(final String page){
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stationView.jumpPage(page);
            }
        });
    }
}
