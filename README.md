# CourseAdapter
适配平台是什么？简单来说，它是一组接口以及封装好的一些组件，使用它可以快速解析各个高校的课程，提供的功能有以下几点：

- 根据学校名称或教务类型获取教务url、课程解析函数（Js）等相关信息
- 获取页面源码
- 通过Js解析出课程集合
- 申请适配（上传源码）

### 注意事项

**在使用本平台前必须获得开发者的授权,违者必究!**

### 如何获得授权

**免费方式**

以下方式任选其一即可：

- 在本平台上适配30所学校，教务类型不得低于5种（教务类型相同的学校解析策略基本一致，难度较低）
- 在本平台上适配10所学校，教务类型不得低于2种，推广[怪兽课表](https://www.coolapk.com/apk/com.zhuangfei.hputimetable)新安装用户1000人
- 推广[怪兽课表](https://www.coolapk.com/apk/com.zhuangfei.hputimetable)新安装用户3000人，涉及学校不得低于3所

**付费方式**

以下方式任选其一即可：

- 人民币100
- 人民币80，推广[怪兽课表](https://www.coolapk.com/apk/com.zhuangfei.hputimetable)新安装用户600人
- 人民币70，推广[怪兽课表](https://www.coolapk.com/apk/com.zhuangfei.hputimetable)新安装用户1200人
- 人民币50，推广[怪兽课表](https://www.coolapk.com/apk/com.zhuangfei.hputimetable)新安装用户1800人
- 人民币30，推广[怪兽课表](https://www.coolapk.com/apk/com.zhuangfei.hputimetable)新安装用户2400人

授权的门槛其实设的有点高，其实我不建议使用付费的方式，推荐使用适配的方式来获取授权，不要担心不会适配，届时我会出一个文档说明适配用到的技术细节。

### 申请授权流程

由于目前学校适配的后台尚不完善，所以暂不支持授权，完善后会开放授权申请通道以及说明申请流程。

预计开放授权申请通道于2018/11/1

## 如何接入

### 1.复制相关类

- 复制`asset/parse.html`到你的项目的`asset`目录下
- 复制`com.zhuangfei.hputimetable.adapter_apis`包下的类到你的项目中

### 2.学校搜索

```java
    //基地址
    public final static String URL_BASE_SCHOOLS="http://www.liuzhuangfei.com/apis/area/";

    //获取已适配学校列表
    public final static String URL_GET_ADAPTER_SCHOOLS="index.php?c=Adapter&a=getAdapterList";
```

返回结果中data是一个数组，parsejs表示解析使用的Js，如下：

```json
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


以Retrofit为例：Model类我就不列出了，可以参考


```java
public interface SchoolService {

    @POST(UrlContacts.URL_GET_ADAPTER_SCHOOLS)
    @FormUrlEncoded
    Call<ListResult<School>> getAdapterSchools(@Field("key") String key);

    //ignore other service
 }
```

```java
public class ApiUtils {
    public static Gson getGson() {
        Gson gson = new GsonBuilder()
                //配置你的Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        return gson;
    }

    public static Retrofit getRetrofitForSchool(Context context) {
        OkHttpClient builder = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder)
                .baseUrl(UrlContacts.URL_BASE_SCHOOLS)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();
        return retrofit;
    }
}
```

```java
/**
     * 获取适配学校列表
     * @param context
     * @param callback
     */
    public static void getAdapterSchools(Context context,String key,Callback<ListResult<School>> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<ListResult<School>> call=schoolService.getAdapterSchools(key);
        call.enqueue(callback);
    }
```


### 3.课程解析
在第一步中复制了一些类进来，先了解一下各个类的作用：

- AssetTools : 资源工具,读取`asset`目录下的文件
- IArea : 接口锲约类,内部包含两个接口：`Callback`、`WebViewCallback`,其中`Callback`用来回调解析的状态，`WebViewCallback`用来监听`WebView`的加载进度
- JsSupport : Js工具类，负责Android与Js之间的交互并提供`WebView`默认配置
- ParseResult : 表示一个课程，解析返回的结果为`List<ParseResult>`
- SpecialArea : `WebView`绑定的对象，Js操作的具体对象即为SpecialArea


**第一步：获取当前页面源码**

```java
// 解析课程相关
    JsSupport jsSupport;
    SpecialArea specialArea;
    String html = "";

    StringBuffer sb = new StringBuffer();
    String url, school, js, type;
```

```java
/**
     * 核心方法:设置WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView() {
        jsSupport = new JsSupport(webView);
        specialArea = new SpecialArea(this, new MyCallback());
        jsSupport.applyConfig(this, new MyWebViewCallback());
        webView.addJavascriptInterface(specialArea, "sa");

        webView.loadUrl(url);
    }
```

```java
    /**
    按钮点击事件
    */
    public void onBtnClicked() {
        jsSupport.getPageHtml("sa");
    }
```


**Callback**

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

那怎么用呢？
有几个函数是专门作为提示用的，只需要`Toast`即可,重点看以下几个函数：

- `onFindTags(final String[] tags)` : 当发现标签时回调该函数，在该函数中你应该弹出一个对话框，然后执行执行的Js函数，参数是由用户的选择决定的，该函数的目的是为了支持多种类型的课程解析
- `onFindResult(List<ParseResult> result)` : 当解析完成后回调该函数，课程被封装为了一个集合，拿到课程后你可以做任意操作
- `String getHtml()` : 该方法暴露给Js调用,Js以此方法的返回值作为输入，进而解析
- `showHtml(String content)` : 获取页面源码是通过注入Js的方式获取的，Js获取到源码后将html传递给该函数，在该函数中你应该将其赋给全局变量，拿到html后，你应该调用`jsSupport.parseHtml(context(),js)`方法来加载库文件(parse.html)，进入解析流程

解析流程：

- 通过按钮触发`jsSupport.getPageHtml("sa")`获取源码
- JsSupport内部通过注入Js的方式获取源码，源码获取完成后会将其传递给与`sa`关联的`SpecialArea`对象的`showHtml(...)`方法
- `showHtml(...)`方法会回调`IArea.Callback`接口的`showHtml(String html)`方法
- 在`IArea.Callback`接口的实现类中的`showHtml(String html)`方法中，首先将html赋给全局变量html，然后调用`jsSupport.parseHtml(context(),js)`开始加载库文件
- 读取`asset`目录下的`parse.html`，然后将js替换进去，开始加载`parse.html`中的html代码，标志位赋为true，开始监听进度的改变
- 进度大于60%时，开始执行解析的入口函数`getTagList()`，标志位复位false





```java
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
```




