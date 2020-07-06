package com.zhuangfei.importcourse;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.stat.StatMultiAccount;
import com.tencent.stat.StatService;
import com.zhuangfei.adapterlib.IStatSendCallback;
import com.zhuangfei.adapterlib.StatManager;

import java.util.Map;
import java.util.Properties;

public class App extends Application {
    private static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();

        StatService.registerActivityLifecycleCallbacks(this);
        StatManager.register(new IStatSendCallback() {
            @Override
            public void sendKVEvent(Context context, String eventId, Map<String, String> params) {
                Properties properties=new Properties();
                if(params!=null){
                    for(Map.Entry<String,String> entry:params.entrySet()){
                        if(entry!=null){
                            if(entry.getKey()==null||entry.getValue()==null){
                                Log.d(TAG, "sendKVEvent: key="+entry.getKey()+"; v="+entry.getValue()+";id="+eventId);
                            }else{
                                properties.setProperty(entry.getKey(),entry.getValue());
                            }
                        }
                    }
                }
                StatService.trackCustomKVEvent(getApplicationContext(),eventId,properties);
            }

            @Override
            public void reportMultiAccount(Context context, StatManager.AccountType type, String v) {
                StatMultiAccount.AccountType thisType=null;
                if(type==StatManager.AccountType.OPEN_QQ){
                    thisType=StatMultiAccount.AccountType.OPEN_QQ;
                }else if(type==StatManager.AccountType.OPEN_WEIXIN){
                    thisType=StatMultiAccount.AccountType.OPEN_WEIXIN;
                }else if(type==StatManager.AccountType.GUEST_MODE){
                    thisType=StatMultiAccount.AccountType.GUEST_MODE;
                }else if(type==StatManager.AccountType.CUSTOM){
                    thisType=StatMultiAccount.AccountType.CUSTOM;
                }else{
                    thisType=StatMultiAccount.AccountType.GUEST_MODE;
                }
                StatMultiAccount account = new StatMultiAccount(
                        thisType, v);
                long time = System.currentTimeMillis() / 1000;
                // 登陆时间，单秒为秒
                account.setLastTimeSec(time);
                StatService.reportMultiAccount(context, account);
            }
        });
    }
}
