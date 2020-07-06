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
import com.zhuangfei.adapterlib.apis.model.ParseJsModel;
import com.zhuangfei.adapterlib.apis.model.StationModel;
import com.zhuangfei.adapterlib.apis.model.StationSpaceModel;
import com.zhuangfei.adapterlib.apis.model.TemplateJsV2;
import com.zhuangfei.adapterlib.apis.model.UserDebugModel;
import com.zhuangfei.adapterlib.apis.model.WxPayResult;
import com.zhuangfei.adapterlib.station.model.TinyConfig;
import com.zhuangfei.adapterlib.station.model.TinyUserInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Liu ZhuangFei on 2018/2/23.
 */

public interface SchoolService {

    @POST(UrlContants.URL_GET_ADAPTER_SCHOOLS_V2)
    @FormUrlEncoded
    Call<ObjResult<AdapterResultV2>> getAdapterSchoolsV2(@Field("key") String key,
                                                         @Field("package") String packageName,
                                                         @Field("appkey") String appkey,
                                                         @Field("time") String time,
                                                         @Field("sign") String sign);

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

    @POST(UrlContants.URL_REGISTER_USER)
    @FormUrlEncoded
    Call<BaseResult> registerUser(@Field("name") String name,
                                  @Field("password") String password);

    @POST(UrlContants.URL_LOGIN_USER_V2)
    @FormUrlEncoded
    Call<ObjResult<TinyUserInfo>> loginUser(@Field("name") String name,
                                            @Field("password") String password,
                                            @Field("type") String type,
                                            @Field("openid") String openId,
                                            @Field("time") String time,
                                            @Field("sign") String sign,
                                            @Field("package") String packageName,
                                            @Field("appkey") String appkey,
                                            @Field("version")int version,
                                            @Field("gender") String gender,
                                            @Field("province") String province,
                                            @Field("city") String city,
                                            @Field("year") String year,
                                            @Field("figureUrl") String figureUrl);

    @POST(UrlContants.URL_UPDATE_TOKEN)
    @FormUrlEncoded
    Call<ObjResult<TinyUserInfo>> updateToken(@Field("token") String token,
                                              @Field("time") String time,
                                              @Field("sign") String sign,
                                              @Field("package") String packageName,
                                              @Field("appkey") String appkey,
                                              @Field("version") int version);

    @POST(UrlContants.URL_SET_STATION_SPACE)
    @FormUrlEncoded
    Call<BaseResult> setStationSpace(@Field("stationId") int stationId,
                               @Field("moduleName") String moduleName,
                                     @Field("token") String token,
                                     @Field("value") String value);

    @POST(UrlContants.URL_GET_STATION_SPACE)
    @FormUrlEncoded
    Call<ObjResult<StationSpaceModel>> getStationSpace(@Field("stationId") int stationId,
                                                        @Field("moduleName") String moduleName,
                                                        @Field("token") String token);

    @POST(UrlContants.URL_RECORD_USER_EVENT)
    @FormUrlEncoded
    Call<BaseResult> recordUserEvent(@Field("token") String token,
                                     @Field("operator") String operator,
                                     @Field("type") String type,
                                     @Field("data") String data,
                                     @Field("params") String params);

    @POST(UrlContants.URL_GET_PAY_ORDER)
    @FormUrlEncoded
    Call<ObjResult<WxPayResult>> getWxPayOrder(@Field("token") String token,
                                               @Field("goodName") String goodName);

    @POST(UrlContants.URL_GET_TEMPLATE_JS)
    @FormUrlEncoded
    Call<ObjResult<TemplateJsV2>> getTemplateJs(@Field("token") String token);

    @POST(UrlContants.URL_GET_ADAPTER_PARSE_JS)
    @FormUrlEncoded
    Call<ObjResult<ParseJsModel>> getAdapterParsejs(@Field("aid") String aid);

    @GET(UrlContants.URL_GET_SEARCH_FIX_TOP_CONFIG)
    Call<TinyConfig> getSearchFixTopConfig(@Field("v") String v);
 }
