package com.zhuangfei.adapterlib.apis.model;

/**
 * Created by Liu ZhuangFei on 2019/6/12.
 */
public class GreenFruitModel {

    /**
     * message : 口令失败
     * errcode : -1
     */

    private String message;
    private String errcode="0";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }
}
