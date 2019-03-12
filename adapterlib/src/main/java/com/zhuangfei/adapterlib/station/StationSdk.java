package com.zhuangfei.adapterlib.station;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;
import com.zhuangfei.adapterlib.activity.StationWebViewActivity;

/**
 * Created by Liu ZhuangFei on 2019/2/6.
 */
public class StationSdk {
    StationWebViewActivity stationView;
    StationJsSupport jsSupport;
    private int minSupport=1;
    private int sdkVersion=1;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public StationSdk(StationWebViewActivity stationWebViewActivity,String space){
        stationView=stationWebViewActivity;
        jsSupport=new StationJsSupport(stationView.getWebView());
        preferences=stationWebViewActivity.getSharedPreferences(space, Context.MODE_PRIVATE);
        editor=preferences.edit();
        setSdkVersion(1);
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void setMinSupport(int minSupport) {
        this.minSupport = minSupport;
        if(sdkVersion<minSupport){
            stationView.showMessage("版本太低，不支持本服务站，请升级新版本!");
            stationView.finish();
        }
    }

    private void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public int getSdkVersion() {
        return sdkVersion;
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public String getBindSchool() {
        return null;
    }

    public void relaseMemory(){
        stationView=null;
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    /**
     * @params firstUrl 重定向的地址
     */
    public void addButton(final String btnText, final String[] textArray, final String[] linkArray){
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stationView.setButtonSettings(btnText,textArray,linkArray);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void saveSchedules(String name,String json){

    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void toast(String msg){
        stationView.showMessage(msg);
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void messageDialog(final String tag, final String title,
                              final String content, final String confirmText){
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder=new AlertDialog.Builder(stationView.getStationContext())
                        .setTitle(title)
                        .setMessage(content)
                        .setPositiveButton(confirmText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(dialogInterface!=null){
                                    dialogInterface.dismiss();
                                }
                                jsSupport.callJs("onMessageDialogCallback('$0')",new String[]{tag});
                            }
                        });
                builder.create().show();
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void setTitle(final String title){
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stationView.setTitle(title);
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
        final String[] result = {null};
        stationView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result[0] =preferences.getString(key,defVal);
            }
        });
        return result[0];
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
    public int getCurWeek(){
        return 0;
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void getCurrentSchedule(){

    }
}
