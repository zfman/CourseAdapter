package com.zhuangfei.adapterlib.apis.model;

import java.io.Serializable;

/**
 * Created by Liu ZhuangFei on 2019/2/15.
 */
public class TemplateModel implements Serializable{
    private String templateName;
    private String templateTag;
    private String templateJs;
    private String stat;

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getStat() {
        return stat;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateTag() {
        return templateTag;
    }

    public void setTemplateTag(String templateTag) {
        this.templateTag = templateTag;
    }

    public String getTemplateJs() {
        return templateJs;
    }

    public void setTemplateJs(String templateJs) {
        this.templateJs = templateJs;
    }
}
