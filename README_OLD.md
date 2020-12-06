# ImportCourse
仓库已迁移至 [https://github.com/zfman/ImportCourse](https://github.com/zfman/ImportCoursehttps://github.com/zfman/ImportCourse)

适配平台是什么？简单来说，它是一组接口以及封装好的一些组件，使用它可以快速解析各个高校的课程，提供的功能有以下几点：

- 根据学校名称或教务类型获取教务url、课程解析函数（Js）等相关信息
- 获取页面源码
- 通过Js解析出课程集合
- 申请适配（上传源码）

### 申请appkey

请将你的应用程序包名发送到邮箱`1193600556@qq.com`，我会在一天内将appkey回复给你，接入完毕后请务必告知我，我会将你加入联盟支持的列表中。

### 引入依赖库 [![]([![](https://jitpack.io/v/zfman/ImportCourse.svg)](https://jitpack.io/#zfman/ImportCourse))

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency

```gradle
	dependencies {
	        implementation 'com.github.zfman.ImportCourse:importcourselib:2.1.2'
	}
```

### 鉴权

在MyApplication中进行初始化：
```java
	public class MyApplication extends Application {
		@Override
		public void onCreate() {
			super.onCreate();
			AdapterLibManager.init("申请的appkey","埋点前缀，可省略");
		}
	}
```

设置MyApplication：
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.zhuangfei.adapterlibdemo">

    <application
        android:name=".MyApplication">
        <!--省略其他内容-->
    </application>

</manifest>
```

### 教务导入页面

```java
    Intent intent=new Intent(MainActivity.this, AutoImportActivity.class);
    startActivityForResult(intent,1);
```

**接收解析的结果**

- `ParseManager`是解析管理类，可以判断是否解析成功以及取出解析结果
- `ParseResult`是本平台提供的课程实体类

接收结果示例如下：

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==AutoImportActivity.RESULT_CODE){
            if(ParseManager.isSuccess()&&ParseManager.getData()!=null){
                List<ParseResult> result=ParseManager.getData();
                String str="";
                for(ParseResult item:result){
                    str+=item.getName()+"\n";
                }
                Toast.makeText(MainActivity.this,str, Toast.LENGTH_SHORT).show();
            }
        }
    }
```

有任何问题可以联系开发者邮箱`1193600556@qq.com`


=============================================






以下是旧文档
# CourseAdapter
适配平台是什么？简单来说，它是一组接口以及封装好的一些组件，使用它可以快速解析各个高校的课程，提供的功能有以下几点：

- 根据学校名称或教务类型获取教务url、课程解析函数（Js）等相关信息
- 获取页面源码
- 通过Js解析出课程集合
- 申请适配（上传源码）

## 资源

- [成为开发者](https://github.com/zfman/CourseAdapter/wiki)
- [版本变更](https://github.com/zfman/CourseAdapter/wiki/%E7%89%88%E6%9C%AC%E5%8F%98%E6%9B%B4)
- [Demo下载](https://www.coolapk.com/apk/com.zhuangfei.adapterlibdemo)
## 接入文档

- 如果需要免费使用本功能，需要加入本平台（课程适配联盟）
- 请发送你的软件信息到邮箱`1193600556@qq.com`以加入本平台，内容包括：项目名称、下载地址、姓名
- 加入本平台后，你可以永久免费使用本平台的服务，但是必须支持本平台格式的课程导入
- 不想免费接入的同学可以联系`1193600556@qq.com`

### 申请appkey

请将你的应用程序包名发送到邮箱`1193600556@qq.com`，我会在一天内将appkey回复给你，接入完毕后请务必告知我，我会将你加入联盟支持的列表中。

### 引入依赖库 [![](https://jitpack.io/v/zfman/CourseAdapter.svg)](https://jitpack.io/#zfman/CourseAdapter)

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency

```gradle
	dependencies {
	        implementation 'com.github.zfman:CourseAdapter:1.0.1'
	}
```

### 鉴权

在MyApplication中进行初始化：
```java
	public class MyApplication extends Application {
		@Override
		public void onCreate() {
			super.onCreate();
			AdapterLibManager.init("申请的appkey");
		}
	}
```

设置MyApplication：
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.zhuangfei.adapterlibdemo">

    <application
        android:name=".MyApplication">
        <!--省略其他内容-->
    </application>

</manifest>
```

### 搜索页面

搜索页面是课程适配的入口，只要前往搜索页，然后在本页面接收返回的数据即可

```java
    public static final int REQUEST_CODE=1;
```

```java
    Intent intent=new Intent(this, SearchSchoolActivity.class);
    startActivityForResult(intent,REQUEST_CODE);
```


默认情况下进入搜索页面是空的，当然也可以设置一个默认的关键字，进入搜索页面后立即请求，示例如下:

```java
	Intent intent=new Intent(MainActivity.this, SearchSchoolActivity.class);
	intent.putExtra(SearchSchoolActivity.EXTRA_SEARCH_KEY,"河南理工大学");
	startActivityForResult(intent,REQUEST_CODE);
```

**接收解析的结果**

- `ParseManager`是解析管理类，可以判断是否解析成功以及取出解析结果
- `ParseResult`是本平台提供的课程实体类

接收结果示例如下：

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE&&resultCode==SearchSchoolActivity.RESULT_CODE){
            if(ParseManager.isSuccess()&&ParseManager.getData()!=null){
                List<ParseResult> result=ParseManager.getData();
                String str="";
                for(ParseResult item:result){
                    str+=item.getName()+"\n";
                }
                Toast.makeText(MainActivity.this,str, Toast.LENGTH_SHORT).show();
            }
        }
    }
```

### 生成口令

> 口令是一段具有一定格式的文本，通过口令可以解析出id，根据id可以从服务端获取课程

```java
    //List<ParseResult> result=ParseManager.getData();
	String shareJson=ShareManager.getShareJson(result);
	ShareManager.putValue(context, shareJson, new OnValueCallback() {
		@Override
		public void onSuccess(ValuePair pair) {
			ShareManager.showMsg(context,ShareManager.getShareToken(pair));
			ShareManager.shareTable(context,pair);
		}
	});
```

- `ShareManager.putValue(context,json,callback)`以json字符串生成32位密钥
- `ShareManager.getShareJson(result)`将`List<ParseResult>`转化为Json字符串
- `ShareManager.showMsg(context,msg)`显示一个Toast
- `ShareManager.getShareToken(pair)`可以获取到生成的口令
- `ShareManager.shareTable(context,pair)`调起分享界面

生成的口令如下:

```text
Hi，你收到了来自适配联盟的课程分享！
在此查看联盟支持的软件列表 http://t.cn/EM9fsHs
复制这条消息，打开列表中任意软件即可导入#27ae3f8393035f70151f238c8e152ac6
```

### 导入口令

>在`onResume`中监听剪切板，如果发现有粘贴的口令，则导入课程

```java
	@Override
    protected void onResume() {
        super.onResume();
        //导入
        ShareManager.getFromClip(this, new OnValueCallback() {
            @Override
            public void onSuccess(ValuePair pair) {
                List<ParseResult> result=ShareManager.getShareData(pair.getValue());
                showParseResult(result);
            }
        });
    }
```

- `ShareManager.getFromClip(context,callback)`监听剪切板
- `ShareManager.getShareData(pair.getValue())`将json字符串转化为`List<ParseResult>`

### 取值

```java
	ShareManager.getValue(context, id, new OnValueCallback() {
		@Override
		public void onSuccess(ValuePair pair) {

		}
	});
```

### 混淆

本库没有混淆，但是引入了两个第三方库,请自行查阅资料添加混淆规则
```gradle
    api 'com.squareup.retrofit2:retrofit:2.0.2'
    api 'com.squareup.retrofit2:converter-gson:2.0.2'
```

## 支持的列表

[支持的软件列表](https://github.com/zfman/CourseAdapter/wiki/%E6%94%AF%E6%8C%81%E7%9A%84%E5%88%97%E8%A1%A8)

任何问题可以联系开发者邮箱`1193600556@qq.com`
