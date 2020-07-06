package com.zhuangfei.adapterlib.qingguo.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liu ZhuangFei on 2019/6/12.
 */
public class GreenFruitParams {
    private String param;
    private String appinfo;
    private String param2;
    private String token;
    Map<String,String> map=new HashMap<>();

    public GreenFruitParams(Map<String,String> map) {
        if(map!=null){
            setParam(map.get("param"));
            setParam2(map.get("param2"));
            setToken(map.get("token"));
            setAppinfo(map.get("appinfo"));
            this.map=map;
        }
    }

    public Map<String, String> getMap() {
        return map;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        if(param==null) param="";
        this.param = param;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        if(param2==null) param2="";
        this.param2 = param2;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if(token==null) token="";
        this.token = token;
    }

    public String getAppinfo() {
        return appinfo;
    }

    public void setAppinfo(String appinfo) {
        if(appinfo==null) appinfo="";
        this.appinfo = appinfo;
    }
}
