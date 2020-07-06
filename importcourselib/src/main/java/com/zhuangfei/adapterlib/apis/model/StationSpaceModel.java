package com.zhuangfei.adapterlib.apis.model;

/**
 * Created by Liu ZhuangFei on 2019/8/10.
 */
public class StationSpaceModel {

    /**
     * id : 1
     * moduleName : test
     * value : TestValue
     */

    private String id;
    private String moduleName;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
