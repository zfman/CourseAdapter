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

**在接入前必须获得开发者的授权,违者追究法律责任!**

> 以下接入方式针对Android平台，IOS的接入请参考流程自行处理，本平台的API是各个平台通用的

### 1.复制相关类

- 复制[asset/parse.html](https://github.com/zfman/hputimetable/tree/master/app/src/main/assets)到你的项目的`asset`目录下
- 复制[adapter_apis](https://github.com/zfman/hputimetable/tree/master/app/src/main/java/com/zhuangfei/hputimetable/adapter_apis)包下的类到你的项目中

这一步需要复制文件和几个类，其作用如下：

- [parse.html](https://github.com/zfman/hputimetable/blob/master/app/src/main/assets/parse.html) : 一个简单html文件，内部预置了两个
- [AssetTools](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/adapter_apis/AssetTools.java) : 资源工具,读取`asset`目录下的文件
- [IArea](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/adapter_apis/IArea.java) : 接口锲约类,内部包含两个接口：`Callback`、`WebViewCallback`,其中`Callback`用来回调解析的状态，`WebViewCallback`用来监听`WebView`的加载进度
- [JsSupport](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/adapter_apis/JsSupport.java) : Js工具类，负责Android与Js之间的交互并提供`WebView`默认配置
- [ParseResult](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/adapter_apis/ParseResult.java) : 表示一个课程，解析返回的结果为`List<ParseResult>`
- [SpecialArea](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/adapter_apis/SpecialArea.java) : `WebView`绑定的对象，Js操作的具体对象即为SpecialArea

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

### 6.获取适配公告

```java
//请求地址：
http://www.liuzhuangfei.com/timetable/index.php?c=Timetable&a=getValue

//请求方式:
POST

//请求参数：传入固定值
id:1f088b55140a49e101e79c420b19bce6

//返回结果：
{
    "code":200,
    "msg":"成功",
    "data":{
        "id":"1f088b55140a49e101e79c420b19bce6",
        "value":"嗨!适配平台的后台管理页面没有开发完毕，并且v1.0.9的源码收集模块也存在Bug，所以适配进度会慢一点，预计一周后开始大范围适配，感谢你们的支持，另外注意：已经适配过的学校就不需要再申请适配了！
上传源码或解析过程中出现问题请联系1193600556@qq.com"
    }
}
```

Retrofit简单演示如下：

```java
  public interface TimetableService {

    @POST(UrlContacts.URL_GET_VALUE)
    @FormUrlEncoded
    Call<ObjResult<ValuePair>> getValue(@Field("id") String id);
 }
```

然后调用即可
```java
    public static void getValue(Context context, String id,Callback<ObjResult<ValuePair>> callback) {
        TimetableService timetableService = ApiUtils.getRetrofit(context)
                .create(TimetableService.class);
        Call<ObjResult<ValuePair>> call=timetableService.getValue(id);
        call.enqueue(callback);
    }
```

## 申请成为适配者

> 随着用户提交的源码增多，以我一人之力肯定不能适配这么多的学校，所以邀请开发者参与适配。适配用到的语言是Js，但是逻辑都很简单，就是正则匹配到结果后返回，不会的话也没问题，我相信你可以通过我的文档以及各种各样的的案例学会它

那么如何适配呢？

- 发送邮件至开发者邮箱`1193600556@qq.com`
- 邮件主题:申请课程适配账户-用户姓名,如：`申请课程适配账户-刘壮飞`
- 邮件内容:可写可不写，写的信息会作为备注

这就Ok了？没错!

只要你的名字不是瞎编的，肯定会通过的，作者会在两日之内将userKey(32位串)发送到你的邮箱,然后登录[全国大学生课程适配平台-官网](http://www.liuzhuangfei.com/)，使用你的姓名和userKey即可登录

## 适配流程

1.登录[全国大学生课程适配平台-官网](http://www.liuzhuangfei.com/)

![Alt](img/adapter_img1.png)

2.个人中心

登录成功后会跳转到个人中心页面，个人中心页面分为以下几个部分：

- 我适配的学校 ： 用户与学校建立绑定关系后会显示在这个区域，可以暂停发布、解除绑定关系、前往编码控制台
- 无人认领的学校 ： 经管理员筛选后且未与用户建立绑定关系的学校会显示在这个区域，点击去认领即建立绑定关系
- 所有适配列表 ： 所有与用户建立绑定关系的学校会显示在这个区域

![Alt](img/adapter_img2.png)

3.编码控制台

顶栏是按钮区，依次为模式选项按钮组、运行选项按钮组、本地记录按钮组

模式选项按钮组：

- 编码模式：两列，左侧为源码区，右侧是编码区，比例4：6
- 标准模式：三列，左侧是源码区，中间是编码区，右侧是调试区，比例3：4：3
- 调试模式：两列，左侧是编码区，右侧是调试区，比例6：4

![Alt](img/adapter_img3.png)

## 适配API

### 基本方法

在编码控制台编写解析函数时，你可以使用Javascript、Jquery-2.0.0

开始适配时，编码控制台会自动创建三个函数，`parse(tag)`、`getTagList()`这两个函数的定义不能修改,因为它们是入口函数

```
/*
注意事项:
1.不得使用//来注释
2.授权作者发布
3.保留你的署名权
4.出现问题请联系作者QQ:1193600556
*/

/*
解析的入口，必须为该方法名和参数列表
tag:标签,用来标记解析的类型.
比如可以设置两种解析方法：个人课表和专业课表,那么需要在此处根据标签值进行解析方法的选择
获取输入源码:window.sa.getHtml()
*/
function parse(tag){
   if(tag=="个人课表") parsePersonal(window.sa.getHtml());
}

/*
获取标签入口,必须为该方法名和参数列表
*/
function getTagList(){
   var array=new Array();
   array.push("个人课表");
   window.sa.forTagResult(array);
}

/*
解析个人课表.
返回结果使用:window.sa.forResult(_getResult(totalArray))
未发现匹配:window.sa.forResult(null)
创建一个课程项:var itemArray=_build(name,teacher,weeks,day,start,step,room);
参数均为String,weeks:以空格分隔每个周次,如1 3 5 7
*/
function parsePersonal(html){
   /*二维数组,保存结果*/
   var totalArray=new Array();

   /*完善此处代码*/

   window.sa.forResult(_getResult(totalArray));
}
```

在解析时，可以使用`Javascript`、`Jquery-2.0.0`以及以下函数：

- `window.sa.forTagResult(array)`:将标签数组返回给Android
- `window.sa.forResult(_getResult(totalArray))`：将结果totalArray返回给Android
- `window.sa.info('msg')`:显示一个Info级别的Toast
- `window.sa.warning('msg')`:显示一个Warn级别的Toast
- `window.sa.error('msg')`:显示一个Error级别的Toast
- `_getResult(totalArray)`:将totalArray转化为字符串
- `_build(name,teacher,weeks,day,start,step,room)`：构建一个课程项，该函数返回一个数组

### 经典案例

源码参见[13e431ca504e406f09a304229b32b96f.txt](http://www.liuzhuangfei.com/apis/area/public/htmlsource.html?filename=13e431ca504e406f09a304229b32b96f.txt)

```java
/*
解析的入口，必须为该方法名和参数列表
tag:标签，用来标记解析的类型，比如可以设置两种解析方法：个人课表和专业课表，
那么需要在此处根据标签值进行解析方法的选择
*/
function parse(tag){
	if(tag=="个人课表") parsePersonal(window.sa.getHtml());
}

/*
获取标签入口，必须为该方法名和参数列表
多个标签使用空格分隔
*/
function getTagList(){
	var array=new Array();
	array.push("个人课表");
	window.sa.forTagResult(array);
}

function parsePersonal(html){

	/*将包含课表内容的HTML截取出来*/
	var contentReg=/<table.*?class=\"displayTag\".*?>[\s\S]*?<tbody>([\s\S]*?)<\/tbody>/g;
	/*有两个匹配项，需要的内容在第2个里，所以忽略第1个匹配到的内容*/
	contentReg.exec(html);
	var result=contentReg.exec(html);

	if(result==null) {
		window.sa.forResult(null);
		return;
	}

	/*行的正则*/
	var trReg=/<tr.*?>([\s\S]*?)<\/tr>/g;
	var r=null;/*每行的匹配结果*/

	var tdReg=/<td.*?>([\s\S]*?)<\/td>/g;/*每个td的正则*/
	var tdRes=null;/*匹配到的每个td*/
	var preArray=null;/*上次匹配到的一行（包含课程名的行）*/
	var totalArray=new Array();/*二维数组，保存结果*/

	/*循环匹配*/
	while((r=trReg.exec(result[1]))!=null){
		var tr=r[1];/*每行*/
		var array=new Array();/*保存每个td的值*/
		while((tdRes=tdReg.exec(tr))!=null){
			var val=tdRes[1].replace(/\s+/g,"");
			val=val.replace(/&nbsp;/g,"");
			val=val.replace(/\(.*?\)/g,"");
			array.push(val);
		}
		if(array.length>8){
			var a=_build(array[2],array[7],getWeekStr(array[11]),array[12],getStart(array[13]),array[14],array[16]+array[17]);
			totalArray.push(a);
			preArray=a;
		}else{
			var a=_build(preArray[0],preArray[1],getWeekStr(array[0]),array[1],getStart(array[2]),array[3],array[5]+array[6]);
			totalArray.push(a);
		}
	}

	window.sa.forResult(_getResult(totalArray));
}

function getStart(startStr){
	if(startStr=="第一大节") return "1";
	if(startStr=="第二大节") return "3";
	if(startStr=="第三大节") return "5";
	if(startStr=="第四大节") return "7";
	if(startStr=="第五大节") return "9";
	if(startStr=="第六大节") return "11";
	return "";
}

/*
将weeks解析为可理解的周次，如1,3-5周上转化为:1 3 4 5
*/
function getWeekStr(weeks){
/*去中文*/
	var weeksReg=new RegExp("[^\\d-\\,]","g");
	var newWeeks=weeks.replace(weeksReg,"");
	var newWeeksStr="";/*结果*/

	/*如果存在逗号*/
	if(newWeeks.indexOf(",")!=-1){
		var splitArray=newWeeks.split(",");
		for(var i=0;i<splitArray.length;i++){
			newWeeksStr+=splitWeeks(splitArray[i]);
		}
	}else newWeeksStr=splitWeeks(newWeeks);
	return newWeeksStr;
}

/*
对周次去掉逗号后的处理：去掉横杠
*/
function splitWeeks(str){
	var res="";
	if(str.indexOf("-")!=-1){
		var splitArray3=str.split("-");
		for(var start=parseInt(splitArray3[0]);start<=parseInt(splitArray3[1]);start++){
			res=res+start+" ";
		}
	}else{
		res=res+str+" ";
	}
	return res;
}
```

**运行结果**

```
forTagResult:  [个人课表]
Start parsing tag with 个人课表...
=============
forResult:
共22门课(实际运行中课程信息不全的会被忽略):
1
name	热工基础与应用
teacher	盛伟*
weeks	11 12 13 14 15 16
day	3
start	3
step	2
room	机械综合楼305

2
name	热工基础与应用
teacher	盛伟*
weeks	10 11 12 13 14 15 16
day	4
start	9
step	2
room	机械综合楼305


3
name	电路史诗
teacher	韩素敏*
weeks
day
start
step
room


4
name	人机工程学
teacher	任卫红*
weeks	10 11 12 13 14 15 16
day	4
start	1
step	2
room	机械综合楼305


5
name	人机工程学
teacher	任卫红*
weeks	10 11 12 13 14 15 16
day	1
start	9
step	2
room	理化综合楼303


6
name	液压与气压传动
teacher	刘俊利*
weeks	10 11 12 13 14 15 16
day	1
start	1
step	2
room	3号教学楼3501


7
name	液压与气压传动
teacher	刘俊利*
weeks	10 11 12 13 14 15 16
day	5
start	1
step	2
room	理化综合楼305


8
name	机械设计课程设计
teacher	张会端*
weeks
day
start
step
room


9
name	电工电子技术训练b
teacher	史祥翠*
weeks
day
start
step
room


10
name	可编程控制器原理
teacher	陈春朝*
weeks	4 5 6 7 8 9 10
day	3
start	3
step	2
room	2号教学楼2310


11
name	可编程控制器原理
teacher	陈春朝*
weeks	4 5 6 7 8 9
day	5
start	3
step	2
room	2号教学楼2210


12
name	机械工程控制基础
teacher	高国富*
weeks	6 7 10 11 12 13 14 15 16
day	2
start	9
step	2
room	机械综合楼102


13
name	机械工程控制基础
teacher	高国富*
weeks	6 7 10 11 12 13 14 15 16
day	4
start	5
step	2
room	机械综合楼102


14
name	形势与政策-4
teacher	王威*
weeks	10
day	6
start	5
step	4
room	理化综合楼205


15
name	机械设计a
teacher	张会端*
weeks	1 2 3 4 5 6 7 10 11 12 13 14 15
day	1
start	3
step	2
room	3号教学楼3505


16
name	机械设计a
teacher	张会端*
weeks	13 14 15
day	2
start	1
step	2
room	理化综合楼303


17
name	机械设计a
teacher	张会端*
weeks	1 2 3 4 5 6 7 10 11 12 13 14 15
day	3
start	1
step	2
room	理化综合楼303


18
name	毛泽东思想和中国特色社会主义理论体系概论
teacher	成小明*
weeks	1 2 3 4 5 6 7 10 11
day	1
start	5
step	2
room	机械综合楼305


19
name	毛泽东思想和中国特色社会主义理论体系概论
teacher	成小明*
weeks	1 2 3 4 5 6 7 10 11
day	3
start	5
step	2
room	机械综合楼305


20
name	毛泽东思想和中国特色社会主义理论体系概论
teacher	成小明*
weeks	1 2 3 4 5 6 7 10 11 12
day	5
start	5
step	2
room	机械综合楼305


21
name	极限配合与测量技术基础
teacher	赵明利*
weeks	6 7 10 11 12 13 14 15 16
day	2
start	3
step	2
room	2号教学楼2405


22
name	极限配合与测量技术基础
teacher	赵明利*
weeks	6 7 10 11 12 13 14 15 16
day	4
start	3
step	2
room	2号教学楼2405
```