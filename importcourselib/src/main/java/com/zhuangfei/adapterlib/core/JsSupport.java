package com.zhuangfei.adapterlib.core;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zhuangfei.toolkit.tools.ToastTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
//        settings.setBuiltInZoomControls(true);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }catch (Exception e){
                    ToastTools.show(context,e.getMessage());
                }
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
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

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
     * 获取页面源码用于判断教务类型
     * 该方法在每次页面加载完毕都会回调一次
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

    public void executeJs(String javascript) {
        if(!TextUtils.isEmpty(javascript)){
            webView.loadUrl("javascript:" + javascript);
        }
    }

    public void executeLocalJsFile(String filename) {
        if(!TextUtils.isEmpty(filename)){
            String fff=assetFile2Str(webView.getContext(),filename);
            AlertDialog.Builder builder=new AlertDialog.Builder(webView.getContext())
                    .setTitle("executeLocalJsFile")
                    .setMessage(fff)
                    .setPositiveButton("确定",null);
            builder.create().show();
            webView.loadUrl("javascript:" + fff);
        }
    }

    public void executeJsDelay(final String javascript, long delay) {
        if(!TextUtils.isEmpty(javascript)){
            webView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:" + javascript);
                }
            },delay);
        }
    }

    public void loadUrl(String url) {
        if(!TextUtils.isEmpty(url)){
            webView.loadUrl(url);
        }
    }

    public void loadUrlDelay(final String url, long delay) {
        if(!TextUtils.isEmpty(url)){
            webView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(url);
                }
            },delay);
        }
    }


    @SuppressLint("JavascriptInterface")
    public void injectObject(Object object, String objName){
        webView.addJavascriptInterface(object,objName);
    }

    /**
     * 注入script标签
     * @param scriptUrl
     */
    public void injectScript(String scriptUrl){
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"" + scriptUrl + "\";";
        js += "document.scripts[0].parentNode.insertBefore(newscript,document.scripts[0]);";
        webView.loadUrl("javascript:" + js);
    }

    public void injectScript(String scriptUrl,String onloadMethod){
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"" + scriptUrl + "\";";
        js+="newscript.onload=function(){"+onloadMethod+";}";
        js += "document.scripts[0].parentNode.insertBefore(newscript,document.scripts[0]);";
        webView.loadUrl("javascript:" + js);
    }

    public void clearCookies(){
        CookieManager manager=CookieManager.getInstance();
        CookieSyncManager cookieSyncManager=CookieSyncManager.createInstance(webView.getContext());
        manager.setAcceptCookie(true);
        manager.removeSessionCookie();
        manager.removeAllCookie();
        cookieSyncManager.sync();
    }

    /**
     * 解析assets文件夹里面的代码,去除注释,取可执行的代码
     * @param c context
     * @param urlStr 路径
     * @return 可执行代码
     */
    public static String assetFile2Str(Context c, String urlStr){
        InputStream in = null;
        try{
            in = c.getAssets().open(urlStr);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuilder sb = new StringBuilder();
            do {
                line = bufferedReader.readLine();
                if (line != null) {
                    sb.append(line);
                }
            } while (line != null);

            bufferedReader.close();
            in.close();

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
