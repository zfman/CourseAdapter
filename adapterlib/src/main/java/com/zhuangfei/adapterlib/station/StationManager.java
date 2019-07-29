package com.zhuangfei.adapterlib.station;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.apis.model.StationModel;
import com.zhuangfei.adapterlib.activity.StationWebViewActivity;
import com.zhuangfei.adapterlib.station.model.TinyConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liu ZhuangFei on 2019/2/8.
 */
public class StationManager {
    public static void openStationWithout(Activity context, TinyConfig config,StationModel stationModel){
        if(context==null||stationModel==null) return;
        Intent intent=new Intent(context, StationWebViewActivity.class);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_MODEL,stationModel);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_CONFIG,config);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.anim_station_open_activity,R.anim.anim_station_static);//动画
    }

    public static void openStationOtherPage(Activity context, TinyConfig config,StationModel stationModel){
        if(context==null||stationModel==null) return;
        Intent intent=new Intent(context, StationWebViewActivity.class);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_MODEL,stationModel);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_CONFIG,config);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_IS_JUMP,true);
        context.startActivity(intent);
    }

    public static String getRealUrl(String url){
        if(TextUtils.isEmpty(url)) return null;
        int index=url.indexOf("#station_config#");//16 char
        if(index==-1){
            return url;
        }
        return url.substring(0,index);
    }

    public static Map<String,String> getStationConfig(String url){
        if(TextUtils.isEmpty(url)) return null;
        int index=url.indexOf("#station_config#");//16 char
        if(index==-1) return null;
        int nextIndex=index+"#station_config#".length();
        String config=url.substring(nextIndex);
        Map<String,String> map=new HashMap<>();
        if(!TextUtils.isEmpty(config)){
            String[] array=config.split("&");
            for(int i=0;i<array.length;i++){
                map.put(array[i].split("=")[0],array[i].split("=")[1]);
            }
            return map;
        }else {
            return null;
        }
    }

    public static String getStationName(String url){
        if(url==null) return null;
        int lastIndex=url.lastIndexOf("/");
        int lastIndex2=url.substring(0,lastIndex).lastIndexOf("/");
        if(lastIndex==-1||lastIndex2==-1){
            return null;
        }
        return url.substring(lastIndex2+1,lastIndex);
    }
}
