package com.zhuangfei.adapterlib.core;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * js 工具类
 * Created by Liu ZhuangFei on 2018/10/27.
 */
public class JsSupport {
    public WebView webView;
    boolean isParse = false;

    public JsSupport(@NonNull WebView webView){
        this.webView=webView;
    }

    public void startParse(){
        isParse=true;
    }

    public void stopParse(){
        isParse=false;
    }

    /**
     * 对WebView简单配置
     * @param context
     * @param callback 进度回调,可以为空
     */
    public void applyConfig(final Context context, final IArea.WebViewCallback callback){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
//        settings.setDefaultTextEncodingName("utf-8");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearFormData();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, final int newProgress) {
                super.onProgressChanged(view, newProgress);

                if(callback!=null){
                    callback.onProgressChanged(newProgress);
                }

                //调用解析函数
                if (newProgress > 60 && isParse) {
                    callJs("getTagList()");
                    stopParse();
                }
            }
        });
    }

    /**
     * 加载库文件 parse.html
     * @param context
     * @param js 解析用的js
     */
    public void parseHtml(Context context,String js) {
        if(context==null||js==null) return;
        startParse();
        String parseHtml = AssetTools.readAssetFile(context, "parse.html");
        parseHtml = parseHtml.replace("${jscontent}", js);
        webView.loadData(parseHtml, "text/html; charset=UTF-8", null);//这种写法可以正确解码
    }

    /**
     * 获取页面源码
     * @param objName webView addJavaScriptInterface()绑定的对象
     */
    public void getPageHtml(String objName){
        if(webView!=null){
            webView.loadUrl("javascript:var ifrs=document.getElementsByTagName(\"iframe\");" +
                    "var iframeContent=\"\";" +
                    "for(var i=0;i<ifrs.length;i++){" +
                    "iframeContent=iframeContent+ifrs[i].contentDocument.body.parentElement.outerHTML;" +
                    "}\n" +
                    "var frs=document.getElementsByTagName(\"frame\");" +
                    "var frameContent=\"\";" +
                    "for(var i=0;i<frs.length;i++){" +
                    "frameContent=frameContent+frs[i].contentDocument.body.parentElement.outerHTML;" +
                    "}" +
                    "window."+objName+".showHtml(document.getElementsByTagName('html')[0].innerHTML + iframeContent+frameContent);");
        }
    }

    /**
     * 获取页面源码
     * @param objName webView addJavaScriptInterface()绑定的对象
     */
    public void getPageHtmlForAdjust(String objName){
        if(webView!=null){
            webView.loadUrl("javascript:var ifrs=document.getElementsByTagName(\"iframe\");" +
                    "var iframeContent=\"\";" +
                    "for(var i=0;i<ifrs.length;i++){" +
                    "iframeContent=iframeContent+ifrs[i].contentDocument.body.parentElement.outerHTML;" +
                    "}\n" +
                    "var frs=document.getElementsByTagName(\"frame\");" +
                    "var frameContent=\"\";" +
                    "for(var i=0;i<frs.length;i++){" +
                    "frameContent=frameContent+frs[i].contentDocument.body.parentElement.outerHTML;" +
                    "}" +
                    "window."+objName+".showHtmlForAdjust(document.getElementsByTagName('html')[0].innerHTML + iframeContent+frameContent);");
        }
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
