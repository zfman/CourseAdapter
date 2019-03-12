package com.zhuangfei.adapterlib.apis.model;

/**
 * Created by Liu ZhuangFei on 2018/2/23.
 */

public class ObjResult<T> extends BaseResult{
    T data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
