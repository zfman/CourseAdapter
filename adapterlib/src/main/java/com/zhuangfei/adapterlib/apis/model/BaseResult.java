package com.zhuangfei.adapterlib.apis.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Liu ZhuangFei on 2018/2/21.
 */

public class BaseResult {

    @SerializedName("code")
    private int code;

    @SerializedName("msg")
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
