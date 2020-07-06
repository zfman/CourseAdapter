package com.zhuangfei.adapterlib.station;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuangfei.adapterlib.AdapterLibManager;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.station.model.TinyUserInfo;
import com.zhuangfei.adapterlib.utils.Md5Security;
import com.zhuangfei.adapterlib.utils.PackageUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Liu ZhuangFei on 2019/8/10.
 */
public class TinyUserManager {
    private static volatile TinyUserManager instance;
    private Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private TinyUserManager(){}
    private TinyUserManager(Context c){
        this.context=c;
        preferences=context.getSharedPreferences("tiny_usermanager",Context.MODE_PRIVATE);
        editor=preferences.edit();
    }
    public static TinyUserManager get(Context context) {
        if(instance==null){
            synchronized (TinyUserManager.class){
                if(instance==null){
                    instance=new TinyUserManager(context);
                }
            }
        }
        return instance;
    }

    public boolean isLogin(){
        return getUserInfo()==null?false:true;
    }

    public void saveUserInfo(TinyUserInfo info){
        TinyUserInfo old=getUserInfo();
        if(info==null){
            editor.putString("userInfo",null);
            editor.commit();
            return;
        }
        if(old!=null&&info.getImgUrl()==null&&old.getImgUrl()!=null){
            info.setImgUrl(old.getImgUrl());
        }
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

    public String getToken(){
        TinyUserInfo userInfo=getUserInfo();
        if(userInfo==null) return null;
        return userInfo.getToken();
    }

    public void updateToken(){
        TinyUserInfo userInfo=getUserInfo();
        String time=""+System.currentTimeMillis();
        String sign=sign(time);
        String packageName=PackageUtils.getPackageMd5(context);
        String appkey= AdapterLibManager.getAppKey();
        if(userInfo==null||userInfo.getToken()==null||sign==null||userInfo.getName()==null){
            saveUserInfo(null);
            return;
        }
        TimetableRequest.updateToken(context,userInfo.getToken(),time,sign,
                packageName,appkey,new Callback<ObjResult<TinyUserInfo>>(){

            @Override
            public void onResponse(Call<ObjResult<TinyUserInfo>> call, Response<ObjResult<TinyUserInfo>> response) {
                ObjResult<TinyUserInfo> result=response.body();
                if(result!=null){
                    if(result.getCode()==200){
                        TinyUserManager.get(context).saveUserInfo(result.getData());
                    }else if(result.getCode()==332){
                        saveUserInfo(null);
                    }
                }
            }
            @Override
            public void onFailure(Call<ObjResult<TinyUserInfo>> call, Throwable t) {
            }
        });
    }

    public String sign(String timestamp){
        String packageMd5= PackageUtils.getPackageMd5(context);
        String appkey= AdapterLibManager.getAppKey();
        StringBuffer sb=new StringBuffer();
        sb.append("time="+timestamp);
        String sign= Md5Security.encrypBy(sb.toString()+context.getResources().getString(R.string.md5_sign_key));
        if(TextUtils.isEmpty(packageMd5)||TextUtils.isEmpty(appkey)){
            Toast.makeText(context,"未初始化",Toast.LENGTH_SHORT).show();
            return null;
        }
        return sign;
    }
}
