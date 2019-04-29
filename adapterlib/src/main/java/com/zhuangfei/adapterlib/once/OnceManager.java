package com.zhuangfei.adapterlib.once;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zhuangfei.adapterlib.AdapterLibManager;
import com.zhuangfei.adapterlib.activity.UploadHtmlActivity;
import com.zhuangfei.adapterlib.core.IArea;
import com.zhuangfei.adapterlib.core.JsSupport;
import com.zhuangfei.adapterlib.utils.ClipUtils;
import com.zhuangfei.adapterlib.utils.PackageUtils;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 一键功能控制类
 * Created by Liu ZhuangFei on 2019/4/25.
 */
public class OnceManager {
    public static final String OBJ_SHOW_SOURCE="source";
    private WebView webView;
    private Context context;
    private Queue<OnceRoute> queue=new LinkedBlockingQueue<>();
    private JsSupport jsSupport;
    private OnOnceResultCallback callback;
    boolean readyInits=false;
    boolean pauseParse=false;
    OnceRoute shouldNextRoute=null;
    String verifyCode=null;

    public WebView getWebView() {
        return webView;
    }

    public Queue<OnceRoute> getQueue() {
        return queue;
    }

    public void getSchedules(Context context, List<OnceRoute> routes, final OnOnceResultCallback callback){
        if(context==null||routes==null) return;
        this.context=context;
        this.callback=callback;
        this.queue.addAll(routes);
        if(!readyInits){
            readyInits(context);
        }
        if(callback!=null){
            callback.onInitFinished();
        }
        executeNextRoute();
    }

    private void executeNextRoute(){
        if (!queue.isEmpty()) {
            OnceRoute route = queue.peek();
            jsSupport.loadUrlDelay(route.getUrl(),200);
        }
    }

    private void updateVerifyCode(){
        if (!queue.isEmpty()) {
            OnceRoute route = queue.peek();
            String codeJs=route.getVerifyCodeJs();
            Toast.makeText(webView.getContext(),"code:"+verifyCode,Toast.LENGTH_LONG).show();
            if(!TextUtils.isEmpty(verifyCode)){
                codeJs.replace("{code}",verifyCode);
                verifyCode=null;
                pauseParse=false;
                queue.poll();
                jsSupport.executeJsDelay(codeJs,100);
                executeNextRoute();
            }
        }else{
            Toast.makeText(webView.getContext(),"code:empty",Toast.LENGTH_LONG).show();
        }
    }

    public void readyInits(final Context ctx){
        //初始化
        webView=new WebView(ctx);
        jsSupport=new JsSupport(webView);
        jsSupport.applyConfig(ctx,new MyWebViewCallback());
        jsSupport.injectObject(new ShowSourceJs(), OBJ_SHOW_SOURCE);

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

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                OnceRoute nowRoute=queue.peek();
                if (!queue.isEmpty()&&url.matches(nowRoute.getRegex())) {
                    String js=nowRoute.getJs();
                    jsSupport.executeJsDelay(js,100);
                    if(!nowRoute.isNeedVerifyCode()||!TextUtils.isEmpty(verifyCode)){
                        queue.poll();
                        String codeJs=nowRoute.getVerifyCodeJs();
                        if(!TextUtils.isEmpty(verifyCode)){
                            codeJs.replace("{code}",verifyCode);
                            verifyCode=null;
                            jsSupport.executeJsDelay(codeJs,100);
                        }
                        pauseParse=false;
                    }else{
                        pauseParse=true;
                    }

                    if(!pauseParse){
                        OnceRoute route = queue.peek();
                        if (route != null) {
                            jsSupport.loadUrlDelay(route.getUrl(),300);
                        }
                    }
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if(!message.startsWith("hide://")){
                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                }
                result.cancel();
                return true;
            }
        });
        readyInits=true;
    }

    public void inputVerifyCode(String code){
        pauseParse=false;
        verifyCode=code;
        updateVerifyCode();
    }

    class MyWebViewCallback implements IArea.WebViewCallback {

        @Override
        public void onProgressChanged(int newProgress) {
//            if (newProgress == 100) {
//                if(!queue.isEmpty()){
//                    OnceRoute route=queue.poll();
//                    jsSupport.executeJs(route.getJs());
//                }
//            }
        }
    }

    public class ShowSourceJs {
        @JavascriptInterface
        public void showHtml(final String content) {
            if (TextUtils.isEmpty(content)) return;
            if(callback!=null&&webView!=null){
                callback.callback(content);
            }
        }

        @JavascriptInterface
        public void onGetImageSrc(final String src){
            if(callback!=null){
                callback.needInputIdentifyCode(src);
            }
            webView.post(new Runnable() {
                @Override
                public void run() {
                    CookieManager manager=CookieManager.getInstance();
                    String cookie=manager.getCookie(webView.getUrl());
                    Toast.makeText(context,src,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface OnOnceResultCallback{
        void urlLoading(String url);
        void onProgressChanged(int newProgress);
        void callback(String html);
        void needInputIdentifyCode(String source);
        void onInitFinished();
    }
}
