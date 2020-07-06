package com.zhuangfei.adapterlib.station.webjs;

import android.text.TextUtils;

import org.json.JSONObject;

/**
 * 设置标题
 */
public class SetTitleJavaInterface extends BaseJavaInterface {
    @Override
    void onActionEvent(JSONObject paramObj) {
        String value=paramObj.optString("value");
        if(!TextUtils.isEmpty(value)){
            stationView.setTitle(value);
        }
    }
}
