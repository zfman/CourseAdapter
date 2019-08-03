package com.zhuangfei.adapterlib.station;

import com.zhuangfei.adapterlib.apis.model.StationModel;

/**
 * Created by Liu ZhuangFei on 2019/8/3.
 */
public interface IStationOperator {
    void saveOrRemoveStation(StationModel stationModel);
    boolean isCanSaveStaion();
    void postUpdateStationEvent();
    void updateLocalStation(StationModel stationModel);
}
