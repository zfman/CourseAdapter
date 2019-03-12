package com.zhuangfei.adapterlib.station;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebView;

/**
 * Created by Liu ZhuangFei on 2019/2/11.
 */
public class StationJsSupport {
    WebView webView;
    public StationJsSupport(WebView webView){
        this.webView=webView;
    }

    /**
     * 调用一个函数，只负责调用，不对返回结果处理
     * @param method 方法名
     */
    public void callJs(String method) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            callEvaluateJavascript(method);
        } else { // 当Android SDK < 4.4时
            callMethod(method);
        }
    }

    public void callJs(String method,String[] realdatas) {
        if(realdatas==null) callJs(method);
        else {
            for(int i=0;i<realdatas.length;i++){
                method=method.replaceFirst("$"+i,realdatas[i]);
            }
        }
    }

    /**
     * 4.4之下的调用js方法
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void callMethod(String method) {
        webView.loadUrl("javascript:" + method);
    }

    /**
     * 调用js方法（4.4之上）
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetJavaScriptEnabled")
    private void callEvaluateJavascript(String method) {
        // 调用html页面中的js函数
        webView.evaluateJavascript(method, null);
    }
}
