package com.zhuangfei.adapterlib.callback;

import com.zhuangfei.adapterlib.apis.model.StationModel;

import java.io.Serializable;

/**
 * Created by Liu ZhuangFei on 2019/8/3.
 */
public interface IAdapterOperator extends Serializable{
    boolean isVip();
    void gotoVip();
}
