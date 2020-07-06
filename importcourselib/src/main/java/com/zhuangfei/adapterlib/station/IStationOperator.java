package com.zhuangfei.adapterlib.station;

import com.zhuangfei.adapterlib.apis.model.StationModel;

import java.io.Serializable;

/**
 * Created by Liu ZhuangFei on 2019/8/3.
 */
public interface IStationOperator extends Serializable{
    void saveOrRemoveStation(StationModel stationModel);
    boolean isCanSaveStaion();
    void postUpdateStationEvent();
    void updateLocalStation(StationModel stationModel);
    void initStation(StationModel thisModel);
    boolean haveLocal();
}
