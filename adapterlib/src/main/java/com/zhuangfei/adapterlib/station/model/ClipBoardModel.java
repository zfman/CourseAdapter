package com.zhuangfei.adapterlib.station.model;

import com.zhuangfei.adapterlib.apis.model.StationModel;

import java.io.Serializable;

/**
 * Created by Liu ZhuangFei on 2019/8/3.
 */
public class ClipBoardModel implements Serializable{
    private String regex;
    private StationModel stationModel;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public StationModel getStationModel() {
        return stationModel;
    }

    public void setStationModel(StationModel stationModel) {
        this.stationModel = stationModel;
    }
}
