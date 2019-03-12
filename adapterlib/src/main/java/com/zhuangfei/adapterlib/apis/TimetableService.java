package com.zhuangfei.adapterlib.apis;

import com.zhuangfei.adapterlib.apis.contants.UrlContants;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.apis.model.ValuePair;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
 }
