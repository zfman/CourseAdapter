# CourseAdapter
适配平台是什么？简单来说，它是一组接口以及封装好的一些组件，使用它可以快速解析各个高校的课程，提供的功能有以下几点：

- 根据学校名称或教务类型获取教务url、课程解析函数（Js）等相关信息
- 获取页面源码
- 通过Js解析出课程集合
- 申请适配（上传源码）

## 接入文档

> 限时免费（截止到2019/5/1）

- 如果需要免费使用本功能，需要加入本平台（课程适配联盟）
- 请发送你的软件信息到邮箱`119360556@qq.com`以加入本平台，内容包括：项目名称、下载地址、姓名
- 加入本平台后，你可以永久免费使用本平台的服务，但是必须支持本平台格式的课程导入
- 不想免费接入的同学可以联系`119360556@qq.com`

### 搜索页面

搜索页面是课程适配的入口，只要前往搜索页，然后在本页面接收返回的数据即可

```java
    public static final int REQUEST_CODE=1;
```

```java
    Intent intent=new Intent(this, SearchSchoolActivity.class);
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


## 支持的列表

[支持的软件列表](https://github.com/zfman/CourseAdapter/wiki/%E6%94%AF%E6%8C%81%E7%9A%84%E5%88%97%E8%A1%A8)

任何问题可以联系开发者邮箱`1193600556@qq.com`
