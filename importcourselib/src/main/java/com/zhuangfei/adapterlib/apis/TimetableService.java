package com.zhuangfei.adapterlib.apis;

import com.zhuangfei.adapterlib.apis.contants.UrlContants;
import com.zhuangfei.adapterlib.apis.model.GreenFruitCourse;
import com.zhuangfei.adapterlib.apis.model.GreenFruitProfile;
import com.zhuangfei.adapterlib.apis.model.GreenFruitTerm;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.apis.model.ValuePair;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Liu ZhuangFei on 2018/2/23.
 */

public interface TimetableService {

    @POST(UrlContants.URL_PUT_VALUE)
    @FormUrlEncoded
    Call<ObjResult<ValuePair>> putValue(@Field("value") String value);

    @POST(UrlContants.URL_GET_VALUE)
    @FormUrlEncoded
    Call<ObjResult<ValuePair>> getValue(@Field("id") String id);

    @POST(UrlContants.URL_QINGGUO)
    @FormUrlEncoded
    @Headers({"Content-Type:application/x-www-form-urlencoded;"})
    Call<GreenFruitProfile> loginGreenFruit(@Field("param") String param,
                                            @Field("param2") String param2,
                                            @Field("token") String token,
                                            @Field("appinfo") String appinfo);

    @POST(UrlContants.URL_QINGGUO)
    @FormUrlEncoded
    @Headers({"Content-Type:application/x-www-form-urlencoded;"})
    Call<GreenFruitCourse> getGreenFruitCourse(@Field("param") String param,
                                               @Field("param2") String param2,
                                               @Field("token") String token,
                                               @Field("appinfo") String appinfo);

    @POST(UrlContants.URL_QINGGUO)
    @FormUrlEncoded
    @Headers({"Content-Type:application/x-www-form-urlencoded;"})
    Call<GreenFruitTerm> getGreenFruitTerm(@Field("param") String param,
                                           @Field("param2") String param2,
                                           @Field("token") String token,
                                           @Field("appinfo") String appinfo);
 }
