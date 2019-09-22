package com.zhuangfei.adapterlib.station;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.zhuangfei.adapterlib.station.model.TinyUserInfo;

/**
 * @deprecated 请使用
 * @see TinyUserManager
 * Created by Liu ZhuangFei on 2019/8/10.
 */
public class UserManager {
    private static volatile UserManager instance;
    private Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private UserManager(){}
    private UserManager(Context c){
        this.context=c;
        preferences=context.getSharedPreferences("tiny_usermanager",Context.MODE_PRIVATE);
        editor=preferences.edit();
    }
    public static UserManager get(Context context) {
        if(instance==null){
            synchronized (UserManager.class){
                if(instance==null){
                    instance=new UserManager(context);
                }
            }
        }
        return instance;
    }

    public boolean isLogin(){
        return getUserInfo()==null?false:true;
    }

    public void saveUserInfo(TinyUserInfo info){
        if(info==null) return;
        editor.putString("userInfo",new Gson().toJson(info));
        editor.commit();
    }

    public TinyUserInfo getUserInfo(){
        String json=preferences.getString("userInfo",null);
        if(json!=null){
            TinyUserInfo info=new Gson().fromJson(json,TinyUserInfo.class);
            if(info!=null){
                return info;
            }
        }
        return null;
    }
}
