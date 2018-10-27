#Callback

`IArea.Callback`是课程解析的回调接口，当解析状态发生改变时会回调该接口

这里先把接口的定义列出来，各个接口的回调时机都在注释中写明了

```java
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
    }
```

