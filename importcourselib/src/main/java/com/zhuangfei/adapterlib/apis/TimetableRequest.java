package com.zhuangfei.adapterlib.apis;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.zhuangfei.adapterlib.AdapterLibManager;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.apis.model.GreenFruitCourse;
import com.zhuangfei.adapterlib.apis.model.GreenFruitProfile;
import com.zhuangfei.adapterlib.apis.model.GreenFruitTerm;
import com.zhuangfei.adapterlib.apis.model.ParseJsModel;
import com.zhuangfei.adapterlib.apis.model.School;
import com.zhuangfei.adapterlib.apis.model.StationModel;
import com.zhuangfei.adapterlib.apis.model.AdapterInfo;
import com.zhuangfei.adapterlib.apis.model.AdapterResultV2;
import com.zhuangfei.adapterlib.apis.model.BaseResult;
import com.zhuangfei.adapterlib.apis.model.CheckModel;
import com.zhuangfei.adapterlib.apis.model.HtmlDetail;
import com.zhuangfei.adapterlib.apis.model.HtmlSummary;
import com.zhuangfei.adapterlib.apis.model.ListResult;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.apis.model.StationSpaceModel;
import com.zhuangfei.adapterlib.apis.model.TemplateJsV2;
import com.zhuangfei.adapterlib.apis.model.UserDebugModel;
import com.zhuangfei.adapterlib.apis.model.ValuePair;
import com.zhuangfei.adapterlib.apis.model.WxPayResult;
import com.zhuangfei.adapterlib.qingguo.ParamsManager;
import com.zhuangfei.adapterlib.qingguo.utils.GreenFruitParams;
import com.zhuangfei.adapterlib.station.TinyUserManager;
import com.zhuangfei.adapterlib.station.model.TinyConfig;
import com.zhuangfei.adapterlib.station.model.TinyUserInfo;
import com.zhuangfei.adapterlib.utils.DeviceTools;
import com.zhuangfei.adapterlib.utils.Md5Security;
import com.zhuangfei.adapterlib.utils.PackageUtils;
import com.zhuangfei.toolkit.tools.ShareTools;

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

    public static void getStationConfig(Context context, String stationName,Callback<TinyConfig> callback) {
        TinyService tinyService = ApiUtils.getRetrofitForStation(context)
                .create(TinyService.class);
        Call<TinyConfig> call=tinyService.getStationConfig(stationName);
        call.enqueue(callback);
    }

    public static void registerUser(Context context, String name,String password,Callback<BaseResult> callback) {
        SchoolService service = ApiUtils.getRetrofitForSchool(context)
                .create(SchoolService.class);
        Call<BaseResult> call=service.registerUser(name,password);
        call.enqueue(callback);
    }

    public static void loginUser(Context context, String name,String password,String type,String openid,
                                 String gender,String province,String city,String year,
                                 String figureUrl,Callback<ObjResult<TinyUserInfo>> callback) {
        String time=""+System.currentTimeMillis();
        String sign=sign(context,time);
        String packageName= PackageUtils.getPackageMd5(context);
        String appkey= AdapterLibManager.getAppKey();
        SchoolService service = ApiUtils.getRetrofitForSchool(context)
                .create(SchoolService.class);
        if(openid==null) openid="";
        if(type==null) type="1";
        if(name==null) name="";
        if(password==null) password="";
        Call<ObjResult<TinyUserInfo>> call=service.loginUser(name,password,type,openid,
                time,sign,packageName,appkey,AdapterLibManager.appVersionNumber,
                gender,province,city,year,figureUrl);
        call.enqueue(callback);
    }

    public static String sign(Context context,String timestamp){
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

    public static void updateToken(Context context, String token,String time,String sign,String packageName,String appkey,Callback<ObjResult<TinyUserInfo>> callback) {
        SchoolService service = ApiUtils.getRetrofitForSchool(context)
                .create(SchoolService.class);
        Call<ObjResult<TinyUserInfo>> call=service.updateToken(token,time,sign,packageName,appkey,AdapterLibManager.appVersionNumber);
        call.enqueue(callback);
    }

    public static void setStationSpace(Context context, int stationId,String moduleName,String token,String value,Callback<BaseResult> callback) {
        SchoolService service = ApiUtils.getRetrofitForSchool(context)
                .create(SchoolService.class);
        Call<BaseResult> call=service.setStationSpace(stationId,moduleName,token,value);
        call.enqueue(callback);
    }

    public static void getStationSpace(Context context, int stationId,String moduleName,String token,Callback<ObjResult<StationSpaceModel>> callback) {
        SchoolService service = ApiUtils.getRetrofitForSchool(context)
                .create(SchoolService.class);
        Call<ObjResult<StationSpaceModel>> call=service.getStationSpace(stationId,moduleName,token);
        call.enqueue(callback);
    }

    public static void recordUserEvent(Context context,String operator,String type,String data,String params,Callback<BaseResult> callback) {
        String token= TinyUserManager.get(context).getToken();
        if(TextUtils.isEmpty(token)){
            String t=ShareTools.getString(context,"tmp_token",null);
            if(t==null){
                String tmpToken= "tmp://"+ Md5Security.encrypBy("recordUserEvent.class"+System.currentTimeMillis()+"zhuangfei_guaishou");
                ShareTools.putString(context,"tmp_token",tmpToken);
                token=tmpToken;
            }else{
                token=t;
            }
        }
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<BaseResult> call=schoolService.recordUserEvent(token,operator,type,data,params);
        call.enqueue(callback);
    }

    public static void getWxPayOrder(Context context, String goodName,Callback<ObjResult<WxPayResult>> callback) {
        SchoolService service = ApiUtils.getRetrofitForSchool(context)
                .create(SchoolService.class);
        String token= TinyUserManager.get(context).getToken();
        Call<ObjResult<WxPayResult>> call=service.getWxPayOrder(token,goodName);
        call.enqueue(callback);
    }

    public static void getTemplateJs(Context context,Callback<ObjResult<TemplateJsV2>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ObjResult<TemplateJsV2>> call=schoolService.getTemplateJs("");
        call.enqueue(callback);
    }

    public static void getAdapterParseJs(Context context,int aid,Callback<ObjResult<ParseJsModel>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ObjResult<ParseJsModel>> call=schoolService.getAdapterParsejs(""+aid);
        call.enqueue(callback);
    }

    public static void getSearchFixTopConifg(Context context,Callback<ObjResult<TemplateJsV2>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ObjResult<TemplateJsV2>> call=schoolService.getTemplateJs("");
        call.enqueue(callback);
    }

    public static void loginGreenFruit(Context context,String schoolId,String loginId,String password,Callback<GreenFruitProfile> callback) {
        TimetableService service=ApiUtils.getRetrofitForGreenFruit(context).create(TimetableService.class);
        GreenFruitParams params= ParamsManager.get(context).getLoginParams(schoolId,loginId,password);
        Call<GreenFruitProfile> call=service.loginGreenFruit(params.getParam(),params.getParam2(),params.getToken(),params.getAppinfo());
        call.enqueue(callback);
    }

    public static void getGreenFruitCourse(Context context,String loginId,String userType,String termId,String week,String token,Callback<GreenFruitCourse> callback) {
        TimetableService service=ApiUtils.getRetrofitForGreenFruit(context).create(TimetableService.class);
        GreenFruitParams params= ParamsManager.get(context).getCourseParams(loginId,userType,termId,week,token);
        Call<GreenFruitCourse> call=service.getGreenFruitCourse(params.getParam(),params.getParam2(),params.getToken(),params.getAppinfo());
        call.enqueue(callback);
    }

    public static void getGreenFruitTerm(Context context,String userId,String userType,String token,Callback<GreenFruitTerm> callback) {
        TimetableService service=ApiUtils.getRetrofitForGreenFruit(context).create(TimetableService.class);
        GreenFruitParams params= ParamsManager.get(context).getTermParams(userId,userType,token);
        Call<GreenFruitTerm> call=service.getGreenFruitTerm(params.getParam(),params.getParam2(),params.getToken(),params.getAppinfo());
        call.enqueue(callback);
    }
}

