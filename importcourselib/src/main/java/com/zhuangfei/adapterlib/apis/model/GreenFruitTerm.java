package com.zhuangfei.adapterlib.apis.model;

import java.util.List;

/**
 * Created by Liu ZhuangFei on 2019/6/12.
 */
public class GreenFruitTerm extends GreenFruitModel{

    private List<XnxqBean> xnxq;

    public List<XnxqBean> getXnxq() {
        return xnxq;
    }

    public void setXnxq(List<XnxqBean> xnxq) {
        this.xnxq = xnxq;
    }

    public static class XnxqBean {
        /**
         * dm : 20191
         * mc : 2019-2020学年第二学期
         * dqxq : 0
         */

        private String dm;
        private String mc;
        private String dqxq;

        public String getDm() {
            return dm;
        }

        public void setDm(String dm) {
            this.dm = dm;
        }

        public String getMc() {
            return mc;
        }

        public void setMc(String mc) {
            this.mc = mc;
        }

        public String getDqxq() {
            return dqxq;
        }

        public void setDqxq(String dqxq) {
            this.dqxq = dqxq;
        }
    }
}
