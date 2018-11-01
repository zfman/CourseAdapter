# CourseAdapter
适配平台是什么？简单来说，它是一组接口以及封装好的一些组件，使用它可以快速解析各个高校的课程，提供的功能有以下几点：

- 根据学校名称或教务类型获取教务url、课程解析函数（Js）等相关信息
- 获取页面源码
- 通过Js解析出课程集合
- 申请适配（上传源码）

## 注意事项

**其他项目接入本平台前必须获得开发者的授权,违者必究!**

**欢迎广大开发者参与学校的适配中**

## 目录

- [申请授权流程](#申请授权流程)
    - [免费方式](#免费方式)
    - [付费方式](#付费方式)
    - [试用方式](#试用方式)

- [申请成为适配者](#申请成为适配者)

- [适配流程](#适配流程)
    - [登录](#适配流程)
    - [个人中心](#个人中心)
    - [编码控制台](#编码控制台)

- [解析API](#解析API)
    - [基本方法](#基本方法)
    - [对信息不全的处理](#对信息不全的处理)
    - [经典案例1-URP教务](#经典案例1-URP教务)
    - [经典案例2-强智教务](#经典案例2-强智教务)
    - [经典案例3-正方教务](#经典案例3-正方教务)

- [授权列表](#授权列表)

## 效果展示

可以直接搜索学校获得支持的学校列表，然后进入相应的URL，登录个人教务账号后点击解析按钮，直接可以解析出课程集合，案例参考以下软件

[怪兽课表](https://www.coolapk.com/apk/com.zhuangfei.hputimetable)

该平台也可以和课表控件相结合，具体的看该控件的使用文档

[课表控件](https://github.com/zfman/TimetableView)

## 申请授权流程

> 如果你想将本功能接入到自己的项目中，需要向开发者提出申请!

以邮件的形式向作者邮箱`119360556@qq.com`提出申请

邮件主题:申请课程适配授权-xxx

邮件内容请注明：

- 项目名称（有链接的话提供链接）
- 申请者姓名
- 选择的授权方式

我会在两天之内回复，如果你选择的是免费方式，那么你需要在一个月内完成适配任务，并且在适配完成后通知作者`119360556@qq.com`并提供你的userKey

如果你的申请通过的话，我会将你的项目加入到授权列表中

Ps:xxx是姓名

以下三种授权方式任选其一即可，如果你不确定这个功能是否好用的话，你可以无条件免费试用一周，但是需要事先向开发者邮件申请，一周后继续使用的话必须获得授权

### 免费方式

在本平台上适配**20所**学校，教务类型不得低于**3种**，限时**一个月**完成适配任务

> 教务类型相同的学校解析策略基本一致，难度较低，只需要把测试用例运行一遍，一般情况下几秒就可以解决了，所以只需要写5种解析策略就可以了，限时一个月

### 付费方式

资费为**120 元/年**，向作者邮件申请后会得到后续流程

### 试用方式

没有任何附加条件，但是必须向作者申请，仅仅作为记录

不管选择哪种方式，都必须向作者报备，申请通过后作者会将接入文档回复给你

## 申请成为适配者

> 随着用户提交的源码增多，以我一人之力肯定不能适配这么多的学校，所以邀请开发者参与适配。适配用到的语言是Js，但是逻辑都很简单，就是正则匹配到结果后返回，不会的话也没问题，我相信你可以通过我的文档以及各种各样的的案例学会它
> 你可以申请参与学校的适配，无门槛申请，但是如果申请后长时间不适配则可能被取消适配资格!

那么如何适配呢？

- 发送邮件至开发者邮箱`1193600556@qq.com`
- 邮件主题:申请课程适配账户-用户姓名,如：`申请课程适配账户-刘壮飞`
- 邮件内容:可写可不写，写的信息会作为备注

这就Ok了？没错!

只要你的名字不是瞎编的，肯定会通过的，作者会在两日之内将userKey(32位串)发送到你的邮箱,然后登录[全国大学生课程适配平台-官网](http://www.liuzhuangfei.com/)，使用你的姓名和userKey即可登录

## 适配流程

### 1.登录[全国大学生课程适配平台-官网](http://www.liuzhuangfei.com/)

![Alt](img/adapter_img1.png)

### 2.个人中心

登录成功后会跳转到个人中心页面，个人中心页面分为以下几个部分：

- 我适配的学校 ： 用户与学校建立绑定关系后会显示在这个区域，可以暂停发布、解除绑定关系、前往编码控制台
- 无人认领的学校 ： 经管理员筛选后且未与用户建立绑定关系的学校会显示在这个区域，点击去认领即建立绑定关系
- 所有适配列表 ： 所有与用户建立绑定关系的学校会显示在这个区域

![Alt](img/adapter_img2.png)

### 3.编码控制台

左侧编码区，右侧调试区，选择一个测试用例，在【源码参见】处可以看到这个测试用例的具体信息，通过对该用例页面的分析编写解析函数，点击调试程序，会模拟显示出程序的执行结果，多个用例调试均无误后，可以在功能菜单中选择发布程序按钮，此时所有用户都可以搜索到该校并使用该Js对课程解析

![Alt](img/adapter_img3.png)

## 解析API

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

- `window.sa.getHtml()`:返回值是页面源码，解析也是以此作为输入
- `window.sa.forTagResult(array)`:将标签数组返回给Android
- `window.sa.forResult(_getResult(totalArray))`：将结果totalArray返回给Android
- `window.sa.info('msg')`:显示一个Info级别的Toast
- `window.sa.warning('msg')`:显示一个Warn级别的Toast
- `window.sa.error('msg')`:显示一个Error级别的Toast
- `_build(name,teacher,weeks,day,start,step,room)`：构建一个课程项，该函数返回一个数组
- `_getResult(totalArray)`:将totalArray转化为字符串

基本流程是这样的：

- 先把包含了课程的那部分提取出来
- 对每行、每列处理，发现数据后将数据提取处理
- 对数据进行清洗，取出标签、括号、空格之类的东西
- 将结果返回

如果解析时某个属性为空，尽量不要设为空，比如有个课程没有教室信息，那么你可以设置为"未知"，否则可能会被App忽略掉

### 对信息不全的处理

如果课程确实某个属性，可以使用以下方式处理：

- 默认值：day、start、step的默认值分别为"7","1","4",weeks的默认值为1-20的所有周次，空格分隔
- 课程名后面加上说明文字`(无时间课程，请自行修改上课时间)`，比如体育课没有时间，那么课程名应该为`体育课(无时间课程，请自行修改上课时间)`

### 经典案例1-URP教务

河南理工大学源码参见[4f948987401e0ab21dbb698e74a1d0d5.txt](http://www.liuzhuangfei.com/apis/area/public/htmlsource.html?filename=4f948987401e0ab21dbb698e74a1d0d5.txt)

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
        	var name=array[2];
        	var teacher=array[7];
           var weeks=getWeekStr(array[11]);
           var change=0;
           if(array[11]==""){
              weeks=getWeekStr("1-20周");
           }
           var day=array[12];
           if(day=="") {
              day="7";
              change=1;
           }

           var start=getStart(array[13]);
           if(start=="") {
              start="1";
              change=1;
           }

           var step=array[14];
           if(step=="") {
              step="4";
              change=1;
           }

           var room=array[16]+array[17];
           if(room=="") {
              room="未知";
              change=1;
           }
           if(change==1){
              name+="(无时间课程，请自行修改上课时间)";
           }

				var a=_build(name,teacher,weeks,day,start,step,room);
				totalArray.push(a);
				preArray=a;
		}else{
        var name=preArray[0];
        	var teacher=preArray[1];
           var weeks=getWeekStr(array[0]);
           var change=0;
           if(weeks=="") weeks=getWeekStr("1-20");;
           var day=array[1];
           if(day=="") {
              day="7";
              change=1;
           }

           var start=getStart(array[2]);
           if(start=="") {
              start="1";
              change=1;
           }

           var step=array[3];
           if(step=="") {
              step="4";
              change=1;
           }

           var room=array[5]+array[6];
           if(room=="") {
              room="未知";
              change=1;
           }
           if(change==1){
              name+="(无时间课程，请自行修改上课时间)";
           }

				var a=_build(name,teacher,weeks,day,start,step,room);
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


[经典案例1-运行结果](经典案例1-运行结果.md)

### 经典案例2-强智教务

山东科技大学源码参见[60e2034e278ccc816234b7566174c553.txt](http://www.liuzhuangfei.com/apis/area/public/htmlsource.html?filename=60e2034e278ccc816234b7566174c553.txt)

```java
/*
注意事项:
1.不得使用//来注释
2.授权作者发布
3.保留你的署名权
4.出现问题请联系作者QQ:1193600556
*/

/*
解析的入口，必须为该方法名和参数列表
tag:标签，用来标记解析的类型，比如可以设置两种解析方法：个人课表和专业课表，
那么需要在此处根据标签值进行解析方法的选择
*/
function parse(tag){
	if(tag=="我的课表->学期理论课表") parsePersonal(window.sa.getHtml());
}

/*
获取标签入口，必须为该方法名和参数列表
*/
function getTagList(){
	var array=new Array();
	array.push("我的课表->学期理论课表");
	window.sa.forTagResult(array);
}

function parsePersonal(html){
	/*将包含课表内容的HTML截取出来*/
	var contentReg=/<table.*?id=\"kbtable\".*?>[\s\S]*?<tbody>([\s\S]*?)<\/tbody>/g;
	var result=contentReg.exec(html);
	if(result==null) {
		window.sa.forResult(null);
		return;
	}
	/*行的正则*/
	var trReg=/<tr.*?>([\s\S]*?)<\/tr>/g;
	var r=null;/*每行的匹配结果*/

	var tdReg=/<td.*?>([\s\S]*?)<\/td>/g;
	var tdRes=null;
	var tdDivReg=/<div.*?class=\"kbcontent\".*?>(.*?)<br><font title=\"老师\">(.*?)<\/font><br><font title=\"周次\(节次\)\">(.*?)<\/font><br>(<font title=\"教室\">(.*?)<\/font>)?[\s\S]*?<\/div>/;
	var tdDivReg2=/<div.*?class=\"kbcontent\".*?>(.*?)<\/div>/;
	var tdDivRes=null;/*匹配到的每个td*/
	var tdDivRes2=null;
	var totalArray=new Array();/*二维数组，保存结果*/
	var tmpRes=trReg.exec(result[1]);
	var tdCount=1;
	if(tmpRes==null){
		window.sa.forResult(null);
		return;
	}
	while((r=trReg.exec(result[1]))!=null){
		if(tdCount>5) break;
		var tr=r[1];/*每行*/
		var day=1;
		while((tdRes=tdReg.exec(tr))!=null){
			if(day>7) break;

			tdDivRes=tdDivReg.exec(tdRes[1]);
			if(tdDivRes!=null){
				var name=tdDivRes[1].replace(/\（[\d ]*?-[\d ]*?\）/g,"");
				var teacher=tdDivRes[2];
				var start=""+(2*tdCount-1);
				var step="2";
				var weeks=getWeekStr(tdDivRes[3]);
				var room=tdDivRes[4];
           if(room==undefined) room='未知';
           if(tdDivRes[4]!=null){
              room=tdDivRes[4].replace(/<font.*?>/,"");
              room=room.replace(/<\/font>/,"");
           }
				totalArray.push(_build(name,teacher,weeks,day,start,step,room));
			}
			day++;
		}
		tdCount++;
	}
	window.sa.forResult(_getResult(totalArray));
}

/*
将weeks解析为可理解的周次
*/
function getWeekStr(weekStr){
	if(weekStr.indexOf("(周)")!=-1){
		weekStr=weekStr.substr(0,weekStr.length-"(周)".length);
		return splitWeeks(weekStr,0);
	}else if(weekStr.indexOf("(双周)")!=-1){
		weekStr=weekStr.substr(0,weekStr.length-"(双周)".length);
		return splitWeeks(weekStr,1);
	}else{
		weekStr=weekStr.substr(0,weekStr.length-"(单周)".length);
		return splitWeeks(weekStr,2);
	}
}

/*
对周次去掉逗号后的处理：去掉横杠
*/
function splitWeeks(str,type){
	var res="";
	if(str.indexOf("-")!=-1){
		var splitArray3=str.split("-");
		for(var start=parseInt(splitArray3[0]);start<=parseInt(splitArray3[1]);start++){
			if(type==0){
				res=res+start+" ";
			}else if(type==1){
				if(start%2==0){
					res=res+start+" ";
				}
			}else{
				if(start%2==1){
					res=res+start+" ";
				}
			}
		}
	}else res=res+str+" ";
	return res.trim();
}
```

[经典案例2-运行结果](经典案例2-运行结果.md)

### 经典案例3-正方教务

青岛理工大学琴岛学院源码参见[d7b636e7245f08c566473f29bf5d2fac.txt](http://www.liuzhuangfei.com/apis/area/public/htmlsource.html?filename=d7b636e7245f08c566473f29bf5d2fac.txt)

由于rowspan的存在，导致解析正方教务时获取星期较难,具体实现原理代码很详细

```java
/*
   由于rowspan的存在，不能根据每行中的td的在该行中的计数来表示上课的星期，
   对每行、每列来说，读到了一个单元格，假设当前的tdCount=1，
   检测一下dayFlagArray[tdCount]的值有没有覆盖到当前行trCount，
   如果覆盖到了当前行，那么说明读取到的这个单元格并不是tdCount列，
   tdCount++，继续检测下一个单元格有没有被覆盖，如果没有被覆盖，则说明
   读取到的单元格内容是tdCount列，此时需要获取该单元格的rowspan属性，更新
   dayFlagArray[tdCount]的值
   */
```

```java
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
   if(tag=="学生课表->表格模式") parsePersonal(window.sa.getHtml());
}

/*
获取标签入口,必须为该方法名和参数列表
*/
function getTagList(){
   var array=new Array();
   window.sa.info("调、停信息暂未解析!");
   array.push("学生课表->表格模式");
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
   /*将包含课表内容的HTML截取出来*/
	var contentReg=/<table.*?id=\"Table6\".*?>[\s\S]*?<tbody>([\s\S]*?)<\/tbody>/g;
	var result=contentReg.exec(html);
	if(result==null) {
		window.sa.forResult(null);
		return;
	}
	/*行的正则*/
	var trReg=/<tr.*?>([\s\S]*?)<\/tr>/g;
	var r=null;/*每行的匹配结果*/


	var tdRes=null;
	var tdDivRes=null;
   var fontRes=null;

   trReg.exec(result[1]);
   trReg.exec(result[1]);
   var trCount=1;

   /*
   一个一维数组，存储的是每列当前覆盖的最大的行
   */
   var dayFlagArray=new Array(9);
   for(var f=0;f<dayFlagArray.length;f++){
      dayFlagArray[f]=0;
   }

   /*
   由于rowspan的存在，不能根据每行中的td的在该行中的计数来表示上课的星期，
   对每行、每列来说，读到了一个单元格，假设当前的tdCount=1，
   检测一下dayFlagArray[tdCount]的值有没有覆盖到当前行trCount，
   如果覆盖到了当前行，那么说明读取到的这个单元格并不是tdCount列，
   tdCount++，继续检测下一个单元格有没有被覆盖，如果没有被覆盖，则说明
   读取到的单元格内容是tdCount列，此时需要获取该单元格的rowspan属性，更新
   dayFlagArray[tdCount]的值
   */
   while((r=trReg.exec(result[1]))!=null){
      var tdContentReg=/(.*?)<br>(.*?)<br>(.*?)<br>(.*?)<br>/;
      var tdContentRes=null;
      var tdReg=/<td.*?>(.*?)<\/td>/g;
      var rowspanReg=/<td.*?rowspan="(.*?)".*?>.*?<\/td>/g;
      var start_end_regex=/(.*?)\((.*?)\)/;
      var tdCount=1;
      var s="";
      for(var f=0;f<dayFlagArray.length;f++){
        s+=dayFlagArray[f]+":";
   		}

      while((tdRes=tdReg.exec(r[1]))!=null){
         if(tdRes!=null){
            var rowspanRes=rowspanReg.exec(tdRes[0]);
            var rowspan=1;
            if(rowspanRes!=null){
               rowspan=rowspanRes[1];
            }
            while(tdCount<=9){
               if(trCount>dayFlagArray[tdCount-1]){
                  dayFlagArray[tdCount-1]=trCount+parseInt(rowspan)-1;
                  break;
                }else tdCount++;
             }

            if(tdRes[1]!="&nbsp;"){
               var tdContentArray=tdRes[1].split("<br><br><br>");
               for(var m=0;tdContentArray!=null&&m<tdContentArray.length;m++){

                  var tdContent=tdContentArray[m];
                  if(m!=tdContentArray.length-1) tdContent+="<br>";
                  if((tdContentRes=tdContentReg.exec(tdContent))!=null){

               		var name=tdContentRes[1];
               		var weeks_start_end=tdContentRes[2];
           	   		 var teacher=tdContentRes[3];
                  var room=tdContentRes[4];
                  var weekStr="";
            	    var start_end_str="";
                  var weeks="";
                  var start_end_res=start_end_regex.exec(weeks_start_end);
                  if(start_end_res!=null){
                     weekStr=start_end_res[1];
                     weeks=splitWeeks2(weekStr);
                     start_end_str=start_end_res[2];
                  }
                  var start="0";
                  var step="0";
                  var start_end_array=start_end_str.split(",");
                  if(start_end_array.length>=2){
                     start=start_end_array[0];
                     step=start_end_array[start_end_array.length-1]-start+1;
                  }

                  name=name.replace(/\(.*?\)/g,"");
                  weeks=weeks.trim();
                  totalArray.push(_build(name,teacher,weeks,tdCount-2,start,step,room));
               }
              }
            }
         }
         tdCount++;
      }
      trCount++;
   }

   /*
   解析无时间的课程
   */
   var noTimeReg=/<table.*?id=\"DataGrid1\".*?>[\s\S]*?<tbody>([\s\S]*?)<\/tbody>/;
	var noTimeRes=noTimeReg.exec(html);
	if(noTimeRes!=null) {
     trReg.exec(noTimeRes[1]);
		while((r=trReg.exec(noTimeRes[1]))!=null){
        	var name="";
          var teacher="";
          var weekStr="";
           var weeks="";
          var room="";

          tdRes=tdReg.exec(r[1]);
        	if(tdRes!=null) name=tdRes[1];

           tdRes=tdReg.exec(r[1]);
        	if(tdRes!=null) teacher=tdRes[1];

           tdReg.exec(r[1]);
           tdRes=tdReg.exec(r[1]);
        	if(tdRes!=null) weekStr=tdRes[1];

           tdRes=tdReg.exec(r[1]);
        	if(tdRes!=null) room=tdRes[1];

           if(name=="&nbsp;") name="(无时间课程，请自行修改上课时间)";
           else name+="(无时间课程，请自行修改上课时间)";

           if(teacher=="&nbsp;") teacher="未知";
           if(room=="&nbsp;") room="未知";
           if(weekStr=="&nbsp;") weeks=splitWeeks2("1-20");
           else weeks=splitWeeks2(weekStr);

           weeks=weeks.trim();
           totalArray.push(_build(name,teacher,weeks,7,1,4,room));
     }
	}

   window.sa.forResult(_getResult(totalArray));
}

/*
去掉单双周
*/
function getWeekStr(weekStr){
   if(weekStr=='') return '';
	if(weekStr.indexOf("单")!=-1){
		weekStr=weekStr.substr(0,weekStr.length-"单".length);
		return splitWeeks(weekStr,2);
	}else if(weekStr.indexOf("双")!=-1){
		weekStr=weekStr.substr(0,weekStr.length-"双".length);
		return splitWeeks(weekStr,1);
	}else{
      weekStr=weekStr.substr(0,weekStr.length);
		return splitWeeks(weekStr,0);
	}
 }

/*
将周次字符串解析为上课周次，处理逗号
*/
function splitWeeks2(newWeeks){
   var newWeekStr='';
	if(newWeeks.indexOf(",")!=-1){
		var splitArray=newWeeks.split(",");
		for(var i=0;i<splitArray.length;i++){
			newWeekStr+=getWeekStr(splitArray[i]);
		}
	}else newWeekStr=getWeekStr(newWeeks);
	return newWeekStr;
}
/*
对周次去掉逗号后的处理：去掉横杠
*/
function splitWeeks(str,type){
	var res="";
	if(str.indexOf("-")!=-1){
		var splitArray3=str.split("-");
		for(var start=parseInt(splitArray3[0]);start<=parseInt(splitArray3[1]);start++){
			if(type==0){
				res=res+start+" ";
			}else if(type==1){
				if(start%2==0){
					res=res+start+" ";
				}
			}else{
				if(start%2==1){
					res=res+start+" ";
				}
			}
		}
	}else res=res+str+" ";
	return res;
}
```

[经典案例3-运行结果](经典案例3-运行结果.md)


## 授权列表

> 在此列出的为作者授权使用“全国大学生适配系统”的开发者列表，未列出的名单禁止借助任何手段使用本平台提供的服务

| 项目名称 | 申请者 | 授权日期 | 完成日期 |授权状态
| ------ | ------ | ------ | ------ |------ |
| [怪兽课表](https://www.coolapk.com/apk/com.zhuangfei.hputimetable) | [刘壮飞](https://github.com/zfman) | 2018/10/18 | --- |已授权|

任何问题可以联系开发者邮箱`1193600556@qq.com`