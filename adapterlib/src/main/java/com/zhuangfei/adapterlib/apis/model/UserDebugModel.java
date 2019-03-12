package com.zhuangfei.adapterlib.apis.model;

import java.util.List;

/**
 * Created by Liu ZhuangFei on 2018/10/30.
 */
public class UserDebugModel {

    String name;

    List<AdapterDebugModel> myAdapter;

    public void setName(String name) {
        this.name = name;
    }

    public void setMyAdapter(List<AdapterDebugModel> myAdapter) {
        this.myAdapter = myAdapter;
    }

    public List<AdapterDebugModel> getMyAdapter() {
        return myAdapter;
    }
}
