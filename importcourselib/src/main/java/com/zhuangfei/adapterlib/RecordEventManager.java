package com.zhuangfei.adapterlib;

import android.content.Context;

import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.BaseResult;
import com.zhuangfei.adapterlib.utils.GsonUtils;
import com.zhuangfei.toolkit.tools.ToastTools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Liu ZhuangFei on 2019/9/27.
 */
public class RecordEventManager {
    public static final String TYPE_ENTER_APP="event_enter_app";//启动app
    public static final String TYPE_CHANGE_CONFIG="event_change_config";//修改设置
    public static final String TYPE_USE_FUNCTION="event_use_function";//使用功能
    public static final String TYPE_IMPORT="event_import";//导入课程
    public static final String TYPE_SEARCH="event_search";
    public static final String TYPE_EXCEPTION="event_exception";

    public static final String OP_GOTO_SEARCH="点击搜索框";
    public static final String OP_SEARCH_KEY="";
    public static final String OP_SUPER_CLASS="";

    public static void recordUserEvent(Context context, String type,String operator, String data, String params, Callback<BaseResult> callback) {
        TimetableRequest.recordUserEvent(context,operator,type,data,params,callback);
    }

    public static void recordUserEvent(Context context, String type) {
        TimetableRequest.recordUserEvent(context, type, type, "", "", new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {

            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {

            }
        });
    }

    public static void recordUserEvent(Context context, String type,String operator) {
        TimetableRequest.recordUserEvent(context, operator, type, "", "", new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {

            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {

            }
        });
    }

    public static void recordUserEvent(Context context, String type,String operator,Object event) {
        String data="";
        if(event!=null){
            data= GsonUtils.getGson().toJson(event);
        }
//        TimetableRequest.recordUserEvent(context, operator, type, "", "", new Callback<BaseResult>() {
//            @Override
//            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<BaseResult> call, Throwable t) {
//
//            }
//        });
    }

    public static void recordUserEvent(final Context context, String type, Object event) {
        String data="";
        if(event!=null){
            data= GsonUtils.getGson().toJson(event);
        }
//        TimetableRequest.recordUserEvent(context, type, type, data, "", new Callback<BaseResult>() {
//            @Override
//            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<BaseResult> call, Throwable t) {
//            }
//        });
    }
}
