package com.zhuangfei.adapterlib;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class StatManager {

    private static IStatSendCallback statSendCallback;

    public static void register(IStatSendCallback callback){
        StatManager.statSendCallback=callback;
    }

    public static void sendKVEvent(Context context, String eventId, Map<String,String> params){
        try{
            String stat=AdapterLibManager.getStat();
            if(TextUtils.isEmpty(stat)){
                stat="default";
            }
            if(statSendCallback!=null){
                if(params==null){
                    Map<String,String> newParams=new HashMap<>();
                    newParams.put("from",stat);
                    statSendCallback.sendKVEvent(context,eventId,newParams);
                }else{
                    params.put("from",stat);
                    statSendCallback.sendKVEvent(context,eventId,params);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void reportMultiAccount(Context context, StatManager.AccountType type,String account){
        try{
            if(statSendCallback!=null){
                statSendCallback.reportMultiAccount(context,type,account);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public enum AccountType {
        UNDEFINED,
        OPEN_WEIXIN,
        OPEN_QQ,
        GUEST_MODE,
        CUSTOM;
    }
}
