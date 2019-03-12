package com.zhuangfei.adapterlib.core;

import java.util.List;

/**
 * 课程适配回调接口
 * Created by Liu ZhuangFei on 2018/10/27.
 */
public interface IArea {

    interface Callback{
        /**
         * 未发现标签时回调
         */
        void onNotFindTag();

        /**
         * 发现标签时回调，你应该在该方法中选择一个标签来解析
         * @param tags
         */
        void onFindTags(String[] tags);

        /**
         * 未发现匹配结果时回调
         */
        void onNotFindResult();

        /**
         * 发现匹配结果时回调
         * @param result 课程集合
         */
        void onFindResult(List<ParseResult> result);

        /**
         * window.sa.error调用时回调
         * @param msg
         */
        void onError(String msg);

        /**
         * window.sa.info调用时回调
         * @param msg
         */
        void onInfo(String msg);

        /**
         * window.sa.warning调用时回调
         * @param msg
         */
        void onWarning(String msg);

        /**
         * 返回源码给js
         * @return
         */
        String getHtml();

        /**
         * 在此保存源码，例如将html赋值给全局变量sourceHtml,
         * 在getHtml()中返回sourceHtml
         *
         * @param html
         */
        void showHtml(String html);

        /*
        每次页面加载完毕后判断教务类型
         */
        void showHtmlForAdjust(String html);
    }

    interface WebViewCallback{
        /**
         * WebView的进度发生改变时回调
         * @param newProgress
         */
        void onProgressChanged(int newProgress);
    }
}
