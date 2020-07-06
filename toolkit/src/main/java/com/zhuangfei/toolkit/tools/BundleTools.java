package com.zhuangfei.toolkit.tools;

import android.app.Activity;
import android.content.Context;

import com.zhuangfei.toolkit.model.BundleModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Liu ZhuangFei on 2018/2/9.
 */

public class BundleTools {

    public static Serializable getBundleModel(Activity context){
        return context.getIntent().getSerializableExtra("model");
    }

    public static BundleModel getModel(Activity context){
        Serializable serializable=getBundleModel(context);
        if(serializable==null) return null;
        return (BundleModel) serializable;
    }

    public static Class getFromClass(Activity context,Class defaultClass){
        BundleModel model=getModel(context);
        if(model==null) return defaultClass;
        Class returnClass=model.getFromClass();
        return returnClass==null?defaultClass:returnClass;
    }

    public static int getToItem(Activity context,int defaultItem){
        BundleModel model=getModel(context);
        if(model==null) return defaultItem;
        int item=model.getToItem();
        return item==0?defaultItem:item;
    }

    public static Object getObject(Activity context,String key,Object obj){
        BundleModel model=getModel(context);
        if(model==null) return obj;
        Object returnObj=model.get(key);
        return returnObj==null?obj:returnObj;
    }

    public static String getString(Activity context,String key,String obj){
        BundleModel model=getModel(context);
        if(model==null) return obj;
        try{
            String returnObj= (String) model.get(key);
            return returnObj==null?obj:returnObj;
        }catch (Exception e){
            return obj;
        }
    }

    public static Object getInt(Activity context,String key,int obj){
        BundleModel model=getModel(context);
        if(model==null) return obj;
        try{
            int returnObj= (int) model.get(key);
            return returnObj==0?obj:returnObj;
        }catch (Exception e){
            return obj;
        }
    }

    public static List getList(Activity context, String key, List list){
        BundleModel model=getModel(context);
        if(model==null) return list;
        try{
            List returnObj= (List) model.get(key);
            return returnObj==null?list:returnObj;
        }catch (Exception e){
            return list;
        }
    }
}
