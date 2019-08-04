package com.zhuangfei.adapterlib.station.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Liu ZhuangFei on 2019/7/28.
 */
public class TinyConfig implements Serializable{

    /**
     * version : 1
     * pages : [{"tab":"indexTab","select":0},{"route":"second.html"}]
     * tabs : [{"name":"indexTab","type":"simple","values":["tab1.html","tab2.html","tab3.html"]}]
     * theme : {"primaryColor":"#000000","actionColor":"#000000","actionTextColor":"#FFFFFF"}
     */

    private int version;
    private int support;
    private String name;
    private ThemeBean theme;
    private List<PagesBean> pages;
    private List<TabsBean> tabs;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public int getSupport() {
        return support;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ThemeBean getTheme() {
        return theme;
    }

    public void setTheme(ThemeBean theme) {
        this.theme = theme;
    }

    public List<PagesBean> getPages() {
        return pages;
    }

    public void setPages(List<PagesBean> pages) {
        this.pages = pages;
    }

    public List<TabsBean> getTabs() {
        return tabs;
    }

    public void setTabs(List<TabsBean> tabs) {
        this.tabs = tabs;
    }

    public static class ThemeBean implements Serializable{
        /**
         * primaryColor : #000000
         * actionColor : #000000
         * actionTextColor : #FFFFFF
         */

        private String primaryColor="#1E90FF";
        private String actionColor="#1E90FF";
        private String actionTextColor="#FFFFFF";
        private boolean actionBarVisiable=true;
        private boolean statusIconGrayColor=false;

        public void setStatusIconGrayColor(boolean statusIconGrayColor) {
            this.statusIconGrayColor = statusIconGrayColor;
        }

        public boolean isStatusIconGrayColor() {
            return statusIconGrayColor;
        }

        public void setActionBarVisiable(boolean actionBarVisiable) {
            this.actionBarVisiable = actionBarVisiable;
        }

        public boolean isActionBarVisiable() {
            return actionBarVisiable;
        }

        public String getPrimaryColor() {
            return primaryColor;
        }

        public void setPrimaryColor(String primaryColor) {
            this.primaryColor = primaryColor;
        }

        public String getActionColor() {
            return actionColor;
        }

        public void setActionColor(String actionColor) {
            this.actionColor = actionColor;
        }

        public String getActionTextColor() {
            return actionTextColor;
        }

        public void setActionTextColor(String actionTextColor) {
            this.actionTextColor = actionTextColor;
        }
    }

    public static class PagesBean implements Serializable{
        /**
         * tab : indexTab
         * select : 0
         * route : second.html
         */

        private String tab;
        private int select;
        private String route;

        public String getTab() {
            return tab;
        }

        public void setTab(String tab) {
            this.tab = tab;
        }

        public int getSelect() {
            return select;
        }

        public void setSelect(int select) {
            this.select = select;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }
    }

    public static class TabsBean implements Serializable{
        /**
         * name : indexTab
         * type : simple
         * values : ["tab1.html","tab2.html","tab3.html"]
         */

        private String name;
        private String type;
        private List<String> values;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }
    }
}
