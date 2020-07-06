package com.zhuangfei.adapterlib.station.model;

import java.io.Serializable;

/**
 * Created by Liu ZhuangFei on 2019/8/10.
 */
public class TinyUserInfo implements Serializable{
    private String name;
    private String token;
    private String imgUrl;


    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
