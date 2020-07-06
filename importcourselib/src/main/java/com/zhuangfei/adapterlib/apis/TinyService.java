package com.zhuangfei.adapterlib.apis;

import com.zhuangfei.adapterlib.apis.contants.UrlContants;
import com.zhuangfei.adapterlib.apis.model.ListResult;
import com.zhuangfei.adapterlib.apis.model.StationModel;
import com.zhuangfei.adapterlib.station.model.TinyConfig;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Liu ZhuangFei on 2019/7/28.
 */
public interface TinyService {

    @GET(UrlContants.URL_GET_STATION_CONFIG)
    Call<TinyConfig> getStationConfig(@Path("stationName") String stationName);
}
