package com.zhuangfei.adapterlib.station.webjs;

import android.content.Context;

import com.zhuangfei.adapterlib.core.JsSupport;
import com.zhuangfei.adapterlib.station.IStationView;
import com.zhuangfei.adapterlib.station.StationJsSupport;

import org.json.JSONObject;

import java.util.Map;

public abstract class BaseJavaInterface {
    Context context;
    IStationView stationView;
    Map<String,Object> otherParams;
    StationJsSupport jsSupport;

    void onInitParams(Context context,IStationView stationView, Map<String,Object> otherParams){
        this.context=context;
        this.otherParams=otherParams;
        this.stationView=stationView;
        this.jsSupport=new StationJsSupport(stationView.getWebView());
    }

    abstract void onActionEvent(JSONObject paramObj);

    void callWeb(String tag,String returnValue){
        jsSupport.checkAndcallJs(tag+"?"+returnValue);
    }
}
