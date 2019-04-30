package com.zhuangfei.adapterlib.apis.model;

/**
 * Created by Liu ZhuangFei on 2018/9/28.
 */
public class School {
    private String schoolName;
    private String url;
    private String type;
    private String menujs;
    private String eventjs;
    private String parsejs;
    private boolean support_once;
    private String once;

    public void setOnce(String once) {
        this.once = once;
    }

    public void setSupport_once(boolean support_once) {
        this.support_once = support_once;
    }

    public boolean isSupport_once() {
        return support_once;
    }

    public String getOnce() {
        return once;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMenujs() {
        return menujs;
    }

    public void setMenujs(String menujs) {
        this.menujs = menujs;
    }

    public String getEventjs() {
        return eventjs;
    }

    public void setEventjs(String eventjs) {
        this.eventjs = eventjs;
    }

    public String getParsejs() {
        return parsejs;
    }

    public void setParsejs(String parsejs) {
        this.parsejs = parsejs;
    }
}
