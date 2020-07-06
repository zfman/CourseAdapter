package com.zhuangfei.adapterlib.apis.model;

import java.util.List;

/**
 * Created by Liu ZhuangFei on 2019/6/12.
 */
public class GreenFruitCourse extends GreenFruitModel{

    /**
     * xn : 2018
     * bz : []
     * zc : 16
     * maxjc : 10
     * jssj : 2019-06-16
     * week1 : [{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"3-4","xq":"新校区","jcdm":"03,04","skbj":"100018-008","txxx":"","sftk":"0","skbh":"","skdd":"教北0107","rkjs":"徐京櫆","jsdm":"374","rs":"48","kcmc":"演讲与口才","xf":"2.0"},{"skbjmc":"商英1807 商英1808","dsz":"0","skzs":"1-16周","jcxx":"5-6","xq":"新校区","jcdm":"05,06","skbj":"100012-012","txxx":"","sftk":"0","skbh":"","skdd":"教南0212","rkjs":"言珍","jsdm":"782","rs":"95","kcmc":"毛泽东思想概论","xf":"2.0"},{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"7-8","xq":"新校区","jcdm":"07,08","skbj":"020008-007","txxx":"","sftk":"0","skbh":"","skdd":"教南0110","rkjs":"关步云","jsdm":"241","rs":"48","kcmc":"英语口语（2-2）","xf":"2.0"}]
     * week3 : [{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"1-2","xq":"新校区","jcdm":"01,02","skbj":"370122-016","txxx":"","sftk":"0","skbh":"","skdd":"实0315","rkjs":"郭长明","jsdm":"740","rs":"48","kcmc":"创新思维与方法","xf":"1.0"},{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"3-4","xq":"新校区","jcdm":"03,04","skbj":"110004-016","txxx":"","sftk":"0","skbh":"","skdd":"实0108","rkjs":"张斌","jsdm":"108","rs":"48","kcmc":"计算机应用基础(2-2)","xf":"2.0"},{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"5-6","xq":"新校区","jcdm":"05,06","skbj":"020010-007","txxx":"","sftk":"0","skbh":"","skdd":"实0218","rkjs":"彭永爱","jsdm":"212","rs":"48","kcmc":"英语听力（2-2）","xf":"2.0"},{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"7-8","xq":"新校区","jcdm":"07,08","skbj":"020017-007","txxx":"","sftk":"0","skbh":"","skdd":"教南0109","rkjs":"陈睿","jsdm":"612","rs":"48","kcmc":"英语阅读（2-2）","xf":"4.0"}]
     * xq : 1
     * week2 : [{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"3-4","xq":"新校区","jcdm":"03,04","skbj":"010047-009","txxx":"","sftk":"0","skbh":"","skdd":"教北0112","rkjs":"龙晓辉","jsdm":"181","rs":"48","kcmc":"国际贸易实务","xf":"4.0"}]
     * qssj : 2019-06-10
     * maxzc :
     * week4 : [{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"1-2","xq":"新校区","jcdm":"01,02","skbj":"020023-007","txxx":"","sftk":"0","skbh":"","skdd":"教南0109","rkjs":"吕晶晶","jsdm":"657","rs":"48","kcmc":"大学英语（2-2）","xf":"4.0"},{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"3-4","xq":"新校区","jcdm":"03,04","skbj":"010047-009","txxx":"","sftk":"0","skbh":"","skdd":"教北0112","rkjs":"龙晓辉","jsdm":"181","rs":"48","kcmc":"国际贸易实务","xf":"4.0"},{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"5-6","xq":"新校区","jcdm":"05,06","skbj":"020263-007","txxx":"","sftk":"0","skbh":"","skdd":"实0204","rkjs":"何佩蓉","jsdm":"226","rs":"48","kcmc":"英语语法","xf":"2.0"},{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"7-8","xq":"新校区","jcdm":"07,08","skbj":"020017-007","txxx":"","sftk":"0","skbh":"","skdd":"教南0110","rkjs":"陈睿","jsdm":"612","rs":"48","kcmc":"英语阅读（2-2）","xf":"4.0"}]
     * week5 : [{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"3-4","xq":"新校区","jcdm":"03,04","skbj":"020023-007","txxx":"","sftk":"0","skbh":"","skdd":"教南0108","rkjs":"吕晶晶","jsdm":"657","rs":"48","kcmc":"大学英语（2-2）","xf":"4.0"},{"skbjmc":"商英1807 商英1808","dsz":"0","skzs":"1-16周","jcxx":"5-6","xq":"新校区","jcdm":"05,06","skbj":"100012-012","txxx":"","sftk":"0","skbh":"","skdd":"教南0212","rkjs":"言珍","jsdm":"782","rs":"95","kcmc":"毛泽东思想概论","xf":"2.0"},{"skbjmc":"商英1807","dsz":"0","skzs":"1-18周","jcxx":"7-8","xq":"","jcdm":"07,08","skbj":"100002-016","txxx":"","sftk":"0","skbh":"","skdd":"","rkjs":"蔡邕湘","jsdm":"355","rs":"48","kcmc":"公共体育课(2-2)","xf":"1.0"}]
     * sjhjinfo : []
     */

    private String xn;
    private String zc;
    private String maxjc;
    private String jssj;
    private String xq;
    private String qssj;
    private String maxzc;
    private List<?> bz;
    private List<WeekBean> week1;
    private List<WeekBean> week3;
    private List<WeekBean> week2;
    private List<WeekBean> week4;
    private List<WeekBean> week5;
    private List<WeekBean> week6;
    private List<WeekBean> week7;

    public void setWeek6(List<WeekBean> week6) {
        this.week6 = week6;
    }

    public void setWeek7(List<WeekBean> week7) {
        this.week7 = week7;
    }

    public List<WeekBean> getWeek6() {
        return week6;
    }

    public List<WeekBean> getWeek7() {
        return week7;
    }

    public String getXn() {
        return xn;
    }

    public void setXn(String xn) {
        this.xn = xn;
    }

    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }

    public String getMaxjc() {
        return maxjc;
    }

    public void setMaxjc(String maxjc) {
        this.maxjc = maxjc;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public String getQssj() {
        return qssj;
    }

    public void setQssj(String qssj) {
        this.qssj = qssj;
    }

    public String getMaxzc() {
        return maxzc;
    }

    public void setMaxzc(String maxzc) {
        this.maxzc = maxzc;
    }

    public List<?> getBz() {
        return bz;
    }

    public void setBz(List<?> bz) {
        this.bz = bz;
    }

    public List<WeekBean> getWeek1() {
        return week1;
    }

    public void setWeek1(List<WeekBean> week1) {
        this.week1 = week1;
    }

    public List<WeekBean> getWeek3() {
        return week3;
    }

    public void setWeek3(List<WeekBean> week3) {
        this.week3 = week3;
    }

    public List<WeekBean> getWeek2() {
        return week2;
    }

    public void setWeek2(List<WeekBean> week2) {
        this.week2 = week2;
    }

    public List<WeekBean> getWeek4() {
        return week4;
    }

    public void setWeek4(List<WeekBean> week4) {
        this.week4 = week4;
    }

    public List<WeekBean> getWeek5() {
        return week5;
    }

    public void setWeek5(List<WeekBean> week5) {
        this.week5 = week5;
    }

    public static class WeekBean {
        /**
         * skbjmc : 商英1807
         * dsz : 0
         * skzs : 1-18周
         * jcxx : 3-4
         * xq : 新校区
         * jcdm : 03,04
         * skbj : 100018-008
         * txxx :
         * sftk : 0
         * skbh :
         * skdd : 教北0107
         * rkjs : 徐京櫆
         * jsdm : 374
         * rs : 48
         * kcmc : 演讲与口才
         * xf : 2.0
         */

        private String skbjmc;
        private String dsz;
        private String skzs;
        private String jcxx;
        private String xq;
        private String jcdm;
        private String skbj;
        private String txxx;
        private String sftk;
        private String skbh;
        private String skdd;
        private String rkjs;
        private String jsdm;
        private String rs;
        private String kcmc;
        private String xf;

        public String getSkbjmc() {
            return skbjmc;
        }

        public void setSkbjmc(String skbjmc) {
            this.skbjmc = skbjmc;
        }

        public String getDsz() {
            return dsz;
        }

        public void setDsz(String dsz) {
            this.dsz = dsz;
        }

        public String getSkzs() {
            return skzs;
        }

        public void setSkzs(String skzs) {
            this.skzs = skzs;
        }

        public String getJcxx() {
            return jcxx;
        }

        public void setJcxx(String jcxx) {
            this.jcxx = jcxx;
        }

        public String getXq() {
            return xq;
        }

        public void setXq(String xq) {
            this.xq = xq;
        }

        public String getJcdm() {
            return jcdm;
        }

        public void setJcdm(String jcdm) {
            this.jcdm = jcdm;
        }

        public String getSkbj() {
            return skbj;
        }

        public void setSkbj(String skbj) {
            this.skbj = skbj;
        }

        public String getTxxx() {
            return txxx;
        }

        public void setTxxx(String txxx) {
            this.txxx = txxx;
        }

        public String getSftk() {
            return sftk;
        }

        public void setSftk(String sftk) {
            this.sftk = sftk;
        }

        public String getSkbh() {
            return skbh;
        }

        public void setSkbh(String skbh) {
            this.skbh = skbh;
        }

        public String getSkdd() {
            return skdd;
        }

        public void setSkdd(String skdd) {
            this.skdd = skdd;
        }

        public String getRkjs() {
            return rkjs;
        }

        public void setRkjs(String rkjs) {
            this.rkjs = rkjs;
        }

        public String getJsdm() {
            return jsdm;
        }

        public void setJsdm(String jsdm) {
            this.jsdm = jsdm;
        }

        public String getRs() {
            return rs;
        }

        public void setRs(String rs) {
            this.rs = rs;
        }

        public String getKcmc() {
            return kcmc;
        }

        public void setKcmc(String kcmc) {
            this.kcmc = kcmc;
        }

        public String getXf() {
            return xf;
        }

        public void setXf(String xf) {
            this.xf = xf;
        }
    }
}
