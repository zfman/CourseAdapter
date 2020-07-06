package com.zhuangfei.adapterlib.station;

import com.zhuangfei.adapterlib.station.model.GreenFruitSchool;

import java.io.Serializable;

/**
 * Created by Liu ZhuangFei on 2019/8/11.
 */
public interface IStationSearchOperator extends Serializable{
    void onXuqerItemClicked(GreenFruitSchool school);
}

