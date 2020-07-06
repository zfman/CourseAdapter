package com.zhuangfei.adapterlib.station.webjs;

import android.content.Context;
import android.text.TextUtils;

import com.zhuangfei.toolkit.tools.ToastTools;
import org.json.JSONObject;

/**
 * toast提示
 */
public class ToastJavaInterface extends BaseJavaInterface {

    @Override
    void onActionEvent(JSONObject paramObj) {
        String value=paramObj.optString("value");
        if(TextUtils.isEmpty(value)){
            ToastTools.show(context,value);
        }
    }
}
