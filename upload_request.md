# 源码上传示例

> 你可以是使用该功能查找学校或者教务类型，使用返回的解析函数对课程页面进行解析即可，以下使用`Retrofit`演示


## API


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


## Code

以Retrofit为例：Model类我就不列出了，参见[SchoolService](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/api/service/SchoolService.java)、[TimetableRequest](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/api/TimetableRequest.java)、[ApiUtils](https://github.com/zfman/hputimetable/blob/master/app/src/main/java/com/zhuangfei/hputimetable/api/ApiUtils.java)

```java
    //基地址
    public final static String URL_BASE_SCHOOLS="http://www.liuzhuangfei.com/apis/area/";
    public final static String URL_PUT_HTML="index.php?c=Adapter&a=putSchoolHtml";
```


```java
public interface SchoolService {

    @POST(UrlContacts.URL_GET_ADAPTER_SCHOOLS)
    @FormUrlEncoded
    Call<ListResult<School>> getAdapterSchools(@Field("key") String key);

    @POST(UrlContacts.URL_PUT_HTML)
    @FormUrlEncoded
    Call<BaseResult> putHtml(@Field("school") String school,
                             @Field("url") String url,
                             @Field("html") String html);
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
public static void putHtml(Context context,String school,String url,String html,Callback<BaseResult> callback) {
        SchoolService schoolService=ApiUtils.getRetrofitForSchool(context).create(SchoolService.class);
        Call<BaseResult> call=schoolService.putHtml(school,url,html);
        call.enqueue(callback);
    }
```