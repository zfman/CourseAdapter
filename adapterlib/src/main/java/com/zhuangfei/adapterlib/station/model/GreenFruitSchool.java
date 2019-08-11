package com.zhuangfei.adapterlib.station.model;

import java.io.Serializable;

/**
 * Created by Liu ZhuangFei on 2019/6/12.
 */
public class GreenFruitSchool implements Serializable{

    /**
     * xxdm : 333456
     * xxmc : 安徽滁州技师学院
     * pinyin : anhuichuzhoujishixueyuan
     * serviceUrl : http://www.xiqueer.com:8080/manager/
     */

    private String xxdm;
    private String xxmc;
    private String pinyin;
    private String serviceUrl;

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    public String getXxmc() {
        return xxmc;
    }

    public void setXxmc(String xxmc) {
        this.xxmc = xxmc;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
