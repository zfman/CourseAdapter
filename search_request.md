# 学校查找示例

> 你可以是使用该功能查找学校或者教务类型，使用返回的解析函数对课程页面进行解析即可，以下使用`Retrofit`演示


## API


```
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


## Code

以Retrofit为例：Model类我就不列出了，参见[SchoolService](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/api/service/SchoolService.java)、[TimetableRequest](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/api/TimetableRequest.java)、[ApiUtils](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/api/ApiUtils.java)

```java
    //基地址
    public final static String URL_BASE_SCHOOLS="http://www.liuzhuangfei.com/apis/area/";

    //获取已适配学校列表
    public final static String URL_GET_ADAPTER_SCHOOLS="index.php?c=Adapter&a=getAdapterList";
```


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