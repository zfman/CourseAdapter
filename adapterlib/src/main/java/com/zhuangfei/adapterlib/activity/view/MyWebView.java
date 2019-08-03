package com.zhuangfei.adapterlib.activity.view;


import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
public class MyWebView extends WebView {
    private onScrollChangeCallback callback;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (callback != null){
            callback.onScrollChanged(l,t,oldl,oldt);
        }
    }

    public onScrollChangeCallback getOnScrollChangeCallback(){
        return callback;
    }

    public void setScrollChangeCallback(onScrollChangeCallback callback){
        this.callback = callback;
    }
    public static interface onScrollChangeCallback{
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}

