# CourseAdapter
适配平台是什么？简单来说，它是一组接口以及封装好的一些组件，使用它可以快速解析各个高校的课程，提供的功能有以下几点：

- 根据学校名称或教务类型获取教务url、课程解析函数（Js）等相关信息
- 获取页面源码
- 通过Js解析出课程集合
- 申请适配（上传源码）

### 注意事项

**在使用本平台前必须获得开发者的授权,违者必究!**

### 申请授权流程

由于目前学校适配的后台尚不完善，所以暂不支持授权，完善后会开放授权申请通道以及说明申请流程。

预计开放授权申请通道于2018/11/1

## 如何接入

> 以下接入方式针对Android平台，IOS的接入请参考流程自行处理，本平台的API是各个平台通用的

### 1.复制相关类

- 复制[asset/parse.html](https://github.com/zfman/hputimetable/tree/master/app/src/main/assets)到你的项目的`asset`目录下
- 复制[adapter_apis](https://github.com/zfman/hputimetable/tree/master/app/src/main/java/com/zhuangfei/hputimetable/adapter_apis)包下的类到你的项目中

这一步需要复制文件和几个类，其作用如下：

- parse.html : 一个简单html文件，内部预置了两个
- AssetTools : 资源工具,读取`asset`目录下的文件
- IArea : 接口锲约类,内部包含两个接口：`Callback`、`WebViewCallback`,其中`Callback`用来回调解析的状态，`WebViewCallback`用来监听`WebView`的加载进度
- JsSupport : Js工具类，负责Android与Js之间的交互并提供`WebView`默认配置
- ParseResult : 表示一个课程，解析返回的结果为`List<ParseResult>`
- SpecialArea : `WebView`绑定的对象，Js操作的具体对象即为SpecialArea

### 2.学校搜索

```java
//请求地址：
http://www.liuzhuangfei.com/apis/area/index.php?c=Adapter&a=getAdapterList

//请求方式:
POST

//请求参数：关键字
key

//返回结果：
//parsejs：解析使用的Js代码
//type：教务类型
//url：教务处网址
//schoolName：学校名称
//aid：适配ID
//其他字段暂时用不到
{
    "code":200,
    "msg":"成功",
    "data":[
        {
            "aid":"2",
            "schoolName":"河南理工大学",
            "url":"https://vpn.hpu.edu.cn",
            "type":"urp",
            "menujs":"",
            "eventjs":"",
            "parsejs":""
        }
    ]
}
```

详细使用案例参见[search_request](search_request.md)

这一步中可以获取到学校的Js了，之后的操作就是利用该Js解析出课程...

### 3.课程解析

`SpecialArea`构造函数的第2个参数是监听加载的进度的，如果你不需要可以设置为null

`IArea.Callback`用来回调解析的状态，`IArea.WebViewCallback`用来监听`WebView`的加载进度

- `onFindTags(final String[] tags)` : 当发现标签时回调该函数，在该函数中你应该弹出一个对话框，然后执行执行的Js函数，参数是由用户的选择决定的，该函数的目的是为了支持多种类型的课程解析
- `onFindResult(List<ParseResult> result)` : 当解析完成后回调该函数，课程被封装为了一个集合，拿到课程后你可以做任意操作
- `String getHtml()` : 该方法暴露给Js调用,Js以此方法的返回值作为输入，进而解析
- `showHtml(String content)` : 获取页面源码是通过注入Js的方式获取的，Js获取到源码后将html传递给该函数，在该函数中你应该将其赋给全局变量，拿到html后，你应该调用`jsSupport.parseHtml(context(),js)`方法来加载库文件(parse.html)，进入解析流程

接口的详细情况请参见[docs_callback](docs_callback.md)

详细使用案例参见[AdapterSchoolActivity](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/AdapterSchoolActivity.java)

解析由按钮事件`public void onBtnClicked()`触发，演示中的`Toasty`可以替换为`Toast`,核心用法如下：
```java
    // 解析课程相关
    JsSupport jsSupport;
    SpecialArea specialArea;

    //标记按钮是否已经被点击过
    //解析按钮如果点击一次，就不需要再去获取html了，直接解析
    boolean isButtonClicked=false;

    /**
     * 核心方法:设置WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView() {
        jsSupport = new JsSupport(webView);
        specialArea = new SpecialArea(this, new MyCallback());
        jsSupport.applyConfig(this, new MyWebViewCallback());
        //must be "sa"
        webView.addJavascriptInterface(specialArea, "sa");

        webView.loadUrl(url);
    }

    class MyWebViewCallback implements IArea.WebViewCallback {

        @Override
        public void onProgressChanged(int newProgress) {
            //进度更新
            loadingProgressBar.setProgress(newProgress);
            if (newProgress == 100) loadingProgressBar.hide();
            else loadingProgressBar.show();

            //河南理工大学教务兼容性处理,不需要的话可以省略
            if (webView.getUrl().startsWith("https://vpn.hpu.edu.cn/web/1/http/1/218.196.240.97/loginAction.do")) {
                webView.loadUrl("https://vpn.hpu.edu.cn/web/1/http/2/218.196.240.97/xkAction.do?actionType=6");
            }
        }
    }

    class MyCallback implements IArea.Callback {

        @Override
        public void onNotFindTag() {
            onError("Tag标签未设置");
            goBack();
        }

        @Override
        public void onFindTags(final String[] tags) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context());
            builder.setTitle("请选择解析标签");
            builder.setItems(tags, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    jsSupport.callJs("parse('" + tags[i] + "')");
                }
            });
            builder.create().show();
        }

        @Override
        public void onNotFindResult() {
            onError("未发现匹配");
            goBack();
        }

        @Override
        public void onFindResult(List<ParseResult> result) {
            saveSchedule(result);
        }

        @Override
        public void onError(String msg) {
            Toasty.error(context(), msg).show();
        }

        @Override
        public void onInfo(String msg) {
            Toasty.info(context(), msg).show();
        }

        @Override
        public void onWarning(String msg) {
            Toasty.warning(context(), msg).show();
        }

        @Override
        public String getHtml() {
            return html;
        }

        @Override
        public void showHtml(String content) {
            if (TextUtils.isEmpty(content)) {
                onError("showHtml:is Null");
            }
            html = content;
            jsSupport.parseHtml(context(),js);
        }
    }

    /**
    按钮事件触发获取源码操作
    */
    public void onBtnClicked() {
        if(!isButtonClicked){
            isButtonClicked=true;
            jsSupport.getPageHtml("sa");
        }else {
            jsSupport.parseHtml(context(),js);
        }
    }
```

### 4.获取页面源码

获取源码由按钮事件`public void onBtnClicked()`触发，核心用法如下：

```java

    JsSupport jsSupport;

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView() {
        jsSupport=new JsSupport(webView);
        jsSupport.applyConfig(this,new MyWebViewCallback());
        webView.addJavascriptInterface(new ShowSourceJs(), "source");

        webView.loadUrl(url);
    }

    class MyWebViewCallback implements IArea.WebViewCallback {

        @Override
        public void onProgressChanged(int newProgress) {
            //河南理工大学教务兼容性处理
            if (webView.getUrl()!=null&&webView.getUrl().startsWith("https://vpn.hpu.edu.cn/web/1/http/1/218.196.240.97/loginAction.do")) {
                webView.loadUrl("https://vpn.hpu.edu.cn/web/1/http/2/218.196.240.97/xkAction.do?actionType=6");
            }
        }
    }

    public class ShowSourceJs {
        @JavascriptInterface
        public void showHtml(final String content) {
            if (TextUtils.isEmpty(content)) return;
            putHtml(content);
        }
    }

    /**
    按钮事件触发获取源码操作
    */
    public void onBtnClicked() {
        jsSupport.getPageHtml("source");
    }
```

其中`putHtml(content)`是源码上传操作，下文详细讲解..

### 5.源码上传(申请适配)


```java
//请求地址：
http://www.liuzhuangfei.com/apis/area/index.php?c=Adapter&a=putSchoolHtml

//请求方式:
POST

//请求参数：关键字
//school:学校全称
//url：地址
//html：源码
school
url
html

//返回结果：
{
    "code":200,
    "msg":"成功"
}
```

详细使用案例参见[upload_request](upload_request.md)