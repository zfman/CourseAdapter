package com.zhuangfei.adapterlib.qingguo;

import android.content.Context;
import android.os.Build;

import com.zhuangfei.adapterlib.qingguo.newversion.Api;
import com.zhuangfei.adapterlib.qingguo.utils.EncryptMethod;
import com.zhuangfei.adapterlib.qingguo.utils.GreenFruitParams;
import com.zhuangfei.adapterlib.qingguo.utils.PhoneMessageTools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liu ZhuangFei on 2019/6/11.
 */
public class ParamsManager {
    private static ParamsManager instance;
    private ParamsManager(){}
    private Context context;
    public static ParamsManager get(Context context){
        if(instance==null){
            synchronized (EncryptMethod.class){
                if(instance==null){
                    instance=new ParamsManager();
                }
            }
        }
        instance.context=context;
        return instance;
    }
    public String map2String(Map<String,String> map){
        if(map==null||map.isEmpty()) return "";
        String s="";
        for(Map.Entry<String,String> entry:map.entrySet()){
            s+="&"+entry.getKey()+"="+entry.getValue();
        }
        if(s.indexOf("&")==0){
            s=s.substring(1);
        }
        return s;
    }

    public GreenFruitParams getLoginParams(String schoolId, String loginId, String password){
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("loginId",loginId);
        hashMap.put("xxdm", schoolId);
        hashMap.put("pwd", password);
        hashMap.put("action", "getLoginInfoNew");
        hashMap.put("isky", "1");
        hashMap.put("sjbz", PhoneMessageTools.b(context));
        hashMap.put("sswl", PhoneMessageTools.a(context));
        hashMap.put("sjxh", "" + Build.MODEL);
        hashMap.put("os", "android");
        hashMap.put("xtbb", Build.VERSION.RELEASE);
        hashMap.put("loginmode", "");
        hashMap.put("appver", "2.5.101");
        Map<String,String> resultMap= Api.encodeing(hashMap,false,"","","");
        return new GreenFruitParams(resultMap);
    }

    /**
     * 获取学期的参数
     * @return
     */
    public GreenFruitParams getTermParams(String userId, String userType, String token){
        Map hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("usertype", userType);
        hashMap.put("action", "getXtgn");
        hashMap.put("step", "xnxq");
        Map<String,String> resultMap= Api.encodeing(hashMap,false,"","","");
        return new GreenFruitParams(resultMap);
    }

    //WeekCourseFragment#1047L
    public GreenFruitParams getCourseParams(String userId, String userType, String termId, String week, String token){
        Map hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("usertype", userType);
        hashMap.put("action", "getKb");
        hashMap.put("step", "kbdetail_bz");
        hashMap.put("bjdm", "");
        hashMap.put("jsdm", "");
        hashMap.put("xnxq", termId);
        hashMap.put("week",week);
        hashMap.put("channel", "jrkb");
        Map<String,String> resultMap= Api.encodeing(hashMap,false,"","","");
        return new GreenFruitParams(resultMap);
    }
}
