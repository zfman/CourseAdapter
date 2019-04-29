package com.zhuangfei.adapterlib.once;

import android.text.TextUtils;

/**
 * Created by Liu ZhuangFei on 2019/4/27.
 */
public class OnceRoute{
    private String url="";
    private String js="";
    private boolean needVerifyCode=false;
    private String verifyCodeJs="";
    private String regex="";

    public void setVerifyCodeJs(String verifyCodeJs) {
        this.verifyCodeJs = verifyCodeJs;
    }

    public String getVerifyCodeJs() {
        return verifyCodeJs;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public void setNeedVerifyCode(boolean needVerifyCode) {
        this.needVerifyCode = needVerifyCode;
    }

    public boolean isNeedVerifyCode() {
        return needVerifyCode;
    }

    public String getUrl() {
        return url;
    }

    public OnceRoute setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getJs() {
        return js;
    }

    public OnceRoute setJs(String js) {
        if(TextUtils.isEmpty(js)){
            js="";
        }else{
            this.js = js;
        }
        return this;
    }
}
