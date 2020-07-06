package com.zhuangfei.adapterlib.station.webjs;

import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.Set;

/**
 * 本地存储
 */
public class PutItemJavaInterface extends BaseJavaInterface {
    @Override
    void onActionEvent(JSONObject paramObj) {
        String key=paramObj.optString("key");
        Object value=paramObj.opt("value");
        SharedPreferences sp=stationView.getPrivateSharedPreferences();
        SharedPreferences.Editor editor=sp.edit();
        if(value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }else if(value instanceof Float){
            editor.putFloat(key, (Float) value);
        }else if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }else if(value instanceof Long){
            editor.putLong(key, (Long) value);
        }else if(value instanceof String){
            editor.putString(key, (String) value);
        }else if(value instanceof Set){
            editor.putStringSet(key, (Set<String>) value);
        }
        editor.apply();
    }
}
