package com.zhuangfei.adapterlib.apis;

import com.zhuangfei.adapterlib.apis.contants.UrlContants;
import com.zhuangfei.adapterlib.apis.model.AdapterInfo;
import com.zhuangfei.adapterlib.apis.model.AdapterResultV2;
import com.zhuangfei.adapterlib.apis.model.BaseResult;
import com.zhuangfei.adapterlib.apis.model.CheckModel;
import com.zhuangfei.adapterlib.apis.model.HtmlDetail;
import com.zhuangfei.adapterlib.apis.model.HtmlSummary;
import com.zhuangfei.adapterlib.apis.model.ListResult;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.apis.model.StationModel;
import com.zhuangfei.adapterlib.apis.model.UserDebugModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Liu ZhuangFei on 2018/2/23.
 */

public interface SchoolService {

    @POST(UrlContants.URL_GET_ADAPTER_SCHOOLS_V2)
    @FormUrlEncoded
    Call<ObjResult<AdapterResultV2>> getAdapterSchoolsV2(@Field("key") String key);

    @POST(UrlContants.URL_PUT_HTML)
    @FormUrlEncoded
    Call<BaseResult> putHtml(@Field("school") String school,
                             @Field("url") String url,
                             @Field("html") String html);

    @POST(UrlContants.URL_CHECK_SCHOOL)
    @FormUrlEncoded
    Call<ObjResult<CheckModel>> checkSchool(@Field("school") String school);

    @POST(UrlContants.URL_GET_USER_INFO)
    @FormUrlEncoded
    Call<ObjResult<UserDebugModel>> getUserInfo(@Field("name") String name, @Field("id") String id);

    @POST(UrlContants.URL_FIND_HTML_SUMMARY)
    @FormUrlEncoded
    Call<ListResult<HtmlSummary>> findHtmlummary(@Field("school") String schoolName);

    @POST(UrlContants.URL_FIND_HTML_DETAIL)
    @FormUrlEncoded
    Call<ObjResult<HtmlDetail>> findHtmlDetail(@Field("filename") String schoolName);

    @POST(UrlContants.URL_GET_ADAPTER_INFO)
    @FormUrlEncoded
    Call<ObjResult<AdapterInfo>> getAdapterInfo(@Field("key") String uid,
                                                @Field("aid") String aid);

    @POST(UrlContants.URL_GET_STATIONS)
    @FormUrlEncoded
    Call<ListResult<StationModel>> getStations(@Field("key") String key);

    @POST(UrlContants.URL_GET_STATION_BY_ID)
    @FormUrlEncoded
    Call<ListResult<StationModel>> getStationById(@Field("id") int id);
 }
