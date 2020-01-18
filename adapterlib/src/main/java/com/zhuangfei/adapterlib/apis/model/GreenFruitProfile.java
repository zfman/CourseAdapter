package com.zhuangfei.adapterlib.apis.model;

import java.util.List;

/**
 * Created by Liu ZhuangFei on 2019/6/12.
 */
public class GreenFruitProfile {

    /**
     * xqzhstate : 0
     * jw : 1.13
     * xqdlzh :
     * xqzh : b2241547544272865076a
     * xkljms : 1
     * xm :
     * userid : 12597_
     * pcurl : http://www.xiqueer.com:80/pc/
     * usertype : STU
     * imurl : 47.104.81.149:21000
     * xz : 0
     * msg : 通过身份验证！
     * flag : 0
     * token : 689586
     * ispay : FALSE
     * serviceurl : http://www.xiqueer.com:8080/manager/
     * uuid :
     * rzfs : 教务
     * grantmodule : 教务;
     * imfs : jiguang
     * kbbzrel : 1
     * xxdm : 12597
     * xxmc : 湖南外贸职业学院
     * moduleVerInfo : [{"moduleversion":"02","modulecode":"jw","ver":"1.13"},{"moduleversion":"","modulecode":"mh","ver":"0"},{"moduleversion":"","modulecode":"rl","ver":""},{"moduleversion":"","modulecode":"oa","ver":"0"},{"moduleversion":"","modulecode":"sx","ver":""},{"moduleversion":"","modulecode":"su","ver":""}]
     */

    private String xqzhstate;
    private String jw;
    private String xqdlzh;
    private String xqzh;
    private String xkljms;
    private String xm;
    private String userid;
    private String pcurl;
    private String usertype;
    private String imurl;
    private String xz;
    private String msg;
    private String flag;
    private String token;
    private String ispay;
    private String serviceurl;
    private String uuid;
    private String rzfs;
    private String grantmodule;
    private String imfs;
    private String kbbzrel;
    private String xxdm;
    private String xxmc;
    private List<ModuleVerInfoBean> moduleVerInfo;

    public String getXqzhstate() {
        return xqzhstate;
    }

    public void setXqzhstate(String xqzhstate) {
        this.xqzhstate = xqzhstate;
    }

    public String getJw() {
        return jw;
    }

    public void setJw(String jw) {
        this.jw = jw;
    }

    public String getXqdlzh() {
        return xqdlzh;
    }

    public void setXqdlzh(String xqdlzh) {
        this.xqdlzh = xqdlzh;
    }

    public String getXqzh() {
        return xqzh;
    }

    public void setXqzh(String xqzh) {
        this.xqzh = xqzh;
    }

    public String getXkljms() {
        return xkljms;
    }

    public void setXkljms(String xkljms) {
        this.xkljms = xkljms;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPcurl() {
        return pcurl;
    }

    public void setPcurl(String pcurl) {
        this.pcurl = pcurl;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getImurl() {
        return imurl;
    }

    public void setImurl(String imurl) {
        this.imurl = imurl;
    }

    public String getXz() {
        return xz;
    }

    public void setXz(String xz) {
        this.xz = xz;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIspay() {
        return ispay;
    }

    public void setIspay(String ispay) {
        this.ispay = ispay;
    }

    public String getServiceurl() {
        return serviceurl;
    }

    public void setServiceurl(String serviceurl) {
        this.serviceurl = serviceurl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRzfs() {
        return rzfs;
    }

    public void setRzfs(String rzfs) {
        this.rzfs = rzfs;
    }

    public String getGrantmodule() {
        return grantmodule;
    }

    public void setGrantmodule(String grantmodule) {
        this.grantmodule = grantmodule;
    }

    public String getImfs() {
        return imfs;
    }

    public void setImfs(String imfs) {
        this.imfs = imfs;
    }

    public String getKbbzrel() {
        return kbbzrel;
    }

    public void setKbbzrel(String kbbzrel) {
        this.kbbzrel = kbbzrel;
    }

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

    public List<ModuleVerInfoBean> getModuleVerInfo() {
        return moduleVerInfo;
    }

    public void setModuleVerInfo(List<ModuleVerInfoBean> moduleVerInfo) {
        this.moduleVerInfo = moduleVerInfo;
    }

    public static class ModuleVerInfoBean {
        /**
         * moduleversion : 02
         * modulecode : jw
         * ver : 1.13
         */

        private String moduleversion;
        private String modulecode;
        private String ver;

        public String getModuleversion() {
            return moduleversion;
        }

        public void setModuleversion(String moduleversion) {
            this.moduleversion = moduleversion;
        }

        public String getModulecode() {
            return modulecode;
        }

        public void setModulecode(String modulecode) {
            this.modulecode = modulecode;
        }

        public String getVer() {
            return ver;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }
    }
}
