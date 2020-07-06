package com.zhuangfei.adapterlib.apis.model;

import java.util.List;

/**
 * Created by Liu ZhuangFei on 2019/2/15.
 */
public class TemplateJsV2 {
    private List<TemplateModel> template;
    private String base;
    private int templateVersion = 0;

    public List<TemplateModel> getTemplate() {
        return template;
    }

    public void setTemplate(List<TemplateModel> template) {
        this.template = template;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public int getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(int templateVersion) {
        this.templateVersion = templateVersion;
    }
}
