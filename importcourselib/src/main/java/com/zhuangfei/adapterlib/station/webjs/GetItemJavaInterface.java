package com.zhuangfei.adapterlib.station.webjs;

import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.Set;

public class GetItemJavaInterface extends BaseJavaInterface {
    @Override
    void onActionEvent(JSONObject paramObj) {
        String key=paramObj.optString("key");
        SharedPreferences sp=stationView.getPrivateSharedPreferences();
        String value=sp.getString(key,null);

    }
}
