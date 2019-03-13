package com.zhuangfei.adapterlib.apis;

import android.content.Context;

import com.zhuangfei.adapterlib.apis.model.StationModel;
import com.zhuangfei.adapterlib.apis.model.AdapterInfo;
import com.zhuangfei.adapterlib.apis.model.AdapterResultV2;
import com.zhuangfei.adapterlib.apis.model.BaseResult;
import com.zhuangfei.adapterlib.apis.model.CheckModel;
import com.zhuangfei.adapterlib.apis.model.HtmlDetail;
import com.zhuangfei.adapterlib.apis.model.HtmlSummary;
import com.zhuangfei.adapterlib.apis.model.ListResult;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.apis.model.UserDebugModel;
import com.zhuangfei.adapterlib.apis.model.ValuePair;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Liu ZhuangFei on 2018/3/2.
 */

public class TimetableRequest {

    public static void putHtml(Context context,String school,String url,String html,Callback<BaseResult> callback) {
        SchoolService schoolService= ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<BaseResult> call=schoolService.putHtml(school,url,html);
        call.enqueue(callback);
    }

    public static void checkSchool(Context context,String school,Callback<ObjResult<CheckModel>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ObjResult<CheckModel>> call=schoolService.checkSchool(school);
        call.enqueue(callback);
    }

    public static void getUserInfo(Context context,String name,String uid,Callback<ObjResult<UserDebugModel>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ObjResult<UserDebugModel>> call=schoolService.getUserInfo(name,uid);
        call.enqueue(callback);
    }

    public static void findHtmlSummary(Context context,String schoolName,Callback<ListResult<HtmlSummary>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ListResult<HtmlSummary>> call=schoolService.findHtmlummary(schoolName);
        call.enqueue(callback);
    }

    public static void findHtmlDetail(Context context,String filename,Callback<ObjResult<HtmlDetail>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ObjResult<HtmlDetail>> call=schoolService.findHtmlDetail(filename);
        call.enqueue(callback);
    }

    public static void getAdapterInfo(Context context,String uid,String aid,Callback<ObjResult<AdapterInfo>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ObjResult<AdapterInfo>> call=schoolService.getAdapterInfo(uid,aid);
        call.enqueue(callback);
    }

    public static void getStations(Context context,String key,Callback<ListResult<StationModel>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ListResult<StationModel>> call=schoolService.getStations(key);
        call.enqueue(callback);
    }

    public static void getAdapterSchoolsV2(Context context,String key,String packageName,String appkey,String time,String sign,Callback<ObjResult<AdapterResultV2>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ObjResult<AdapterResultV2>> call=schoolService.getAdapterSchoolsV2(key,packageName,appkey,time,sign);
        call.enqueue(callback);
    }

    public static void getStationById(Context context,int id,Callback<ListResult<StationModel>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ListResult<StationModel>> call=schoolService.getStationById(id);
        call.enqueue(callback);
    }

    public static void putValue(Context context, String val,Callback<ObjResult<ValuePair>> callback) {
        TimetableService timetableService = ApiUtils.getRetrofit(context)
                .create(TimetableService.class);
        Call<ObjResult<ValuePair>> call = timetableService.putValue(val);
        call.enqueue(callback);
    }

    public static void getValue(Context context, String id,Callback<ObjResult<ValuePair>> callback) {
        TimetableService timetableService = ApiUtils.getRetrofit(context)
                .create(TimetableService.class);
        Call<ObjResult<ValuePair>> call=timetableService.getValue(id);
        call.enqueue(callback);
    }

}
