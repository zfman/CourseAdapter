package com.zhuangfei.adapterlib.station;

import com.zhuangfei.adapterlib.apis.model.StationModel;

/**
 * Created by Liu ZhuangFei on 2019/8/3.
 */
public class DefaultStationOperator implements IStationOperator {
    @Override
    public void saveOrRemoveStation(StationModel stationModel) {

    }

    @Override
    public boolean isCanSaveStaion() {
        return false;
    }

    @Override
    public void postUpdateStationEvent() {

    }

    @Override
    public void updateLocalStation(StationModel stationModel) {

    }

    @Override
    public void initStation(StationModel thisModel) {

    }

    @Override
    public boolean haveLocal() {
        return false;
    }
}
