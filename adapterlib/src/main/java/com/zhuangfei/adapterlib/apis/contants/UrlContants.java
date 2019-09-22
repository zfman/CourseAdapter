package com.zhuangfei.adapterlib.apis.contants;

/**
 * Created by Liu ZhuangFei on 2018/2/11.
 */

public class UrlContants {

    public final static String URL_BASE="http://www.liuzhuangfei.com/timetable/";

    public final static String URL_TINY_BASE="http://www.liuzhuangfei.com/apis/area/station/";

    //保存数据
    public final static String URL_PUT_VALUE="index.php?c=Timetable&a=putValue";

    //获取数据
    public final static String URL_GET_VALUE="index.php?c=Timetable&a=getValue";

    //根据专业获取课程
    public final static String URL_GET_BY_MAJOR="index.php?c=Timetable&a=getByMajor";

    //根据专业名称模糊搜索
    public final static String URL_FIND_MAJOR="index.php?c=Timetable&a=findMajor";

    //通过课程名搜索
    public final static String URL_GET_BY_NAME="index.php?c=Timetable&a=getByName";

    public final static String URL_ISSUES="https://github.com/zfman/HpuTimetableClient/issues";

    //获取已适配学校列表
    public final static String URL_GET_ADAPTER_SCHOOLS="index.php?c=Adapter&a=getAdapterList";

    public final static String URL_GET_ADAPTER_SCHOOLS_V2="index.php?c=Adapter&a=getAdapterListV2";

    public final static String URL_BASE_SCHOOLS="http://www.liuzhuangfei.com/apis/area/";

    //上次html
    public final static String URL_PUT_HTML="index.php?c=Adapter&a=putSchoolHtml";

    public final static String URL_CHECK_SCHOOL="index.php?c=Adapter&a=checkSchool";

    public final static String URL_GET_USER_INFO="index.php?c=Adapter&a=getUserInfo";

    public final static String URL_FIND_HTML_SUMMARY="index.php?c=Adapter&a=findHtmlSummaryByKey";

    public final static String URL_FIND_HTML_DETAIL="index.php?c=Adapter&a=findHtmlDetail";

    public final static String URL_GET_ADAPTER_INFO="index.php?c=Adapter&a=getAdapterInfo";

    public final static String URL_GET_STATIONS="index.php?c=Adapter&a=getStations";

    public final static String URL_GET_STATION_BY_ID="index.php?c=Adapter&a=getStationById";

    public final static String URL_GET_STATION_CONFIG="{stationName}/config.json";

    public final static String URL_REGISTER_USER="index.php?c=Adapter&a=registerUser";

    public final static String URL_LOGIN_USER="index.php?c=Adapter&a=loginUser";

    public final static String URL_UPDATE_TOKEN="index.php?c=Adapter&a=updateToken";

    public final static String URL_SET_STATION_SPACE="index.php?c=Adapter&a=setStationSpace";

    public final static String URL_GET_STATION_SPACE="index.php?c=Adapter&a=getStationSpace";
}
