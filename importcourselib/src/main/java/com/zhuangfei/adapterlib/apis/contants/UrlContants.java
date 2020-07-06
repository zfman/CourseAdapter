package com.zhuangfei.adapterlib.apis.contants;

/**
 * Created by Liu ZhuangFei on 2018/2/11.
 */

public class UrlContants {

    public final static String URL_BASE="http://www.liuzhuangfei.com/timetable/";

    public final static String URL_TINY_BASE="http://www.liuzhuangfei.com/apis/area/station/";

    //青果URL
    public final static String URL_QINGGUO="wap/wapController.jsp";
    public final static String URL_BASE_QINGGUO="http://www.xiqueer.com:8080/manager/";

    //保存数据
    public final static String URL_PUT_VALUE="index.php?c=Timetable&a=putValue";

    //获取数据
    public final static String URL_GET_VALUE="index.php?c=Timetable&a=getValue";

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

    public final static String URL_LOGIN_USER_V2="index.php?c=Adapter&a=loginUserV2";

    public final static String URL_UPDATE_TOKEN="index.php?c=Adapter&a=updateToken";

    public final static String URL_SET_STATION_SPACE="index.php?c=Adapter&a=setStationSpace";

    public final static String URL_GET_STATION_SPACE="index.php?c=Adapter&a=getStationSpace";

    public final static String URL_RECORD_USER_EVENT="index.php?c=Adapter&a=recordUserEvent";

    public final static String URL_GET_PAY_ORDER="index.php?c=Adapter&a=getPayOrder";

    public final static String URL_GET_SEARCH_FIX_TOP_CONFIG="searchFixTop.json";


    //新的接口
    public final static String URL_GET_ADAPTER_SCHOOLS_V2="index.php?c=AdapterV2&a=getAdapterListV2";
    public final static String URL_GET_TEMPLATE_JS="index.php?c=AdapterV2&a=getTemplateJs";
    public final static String URL_GET_ADAPTER_PARSE_JS="index.php?c=AdapterV2&a=getAdapterParseJs";
    public final static String URL_GET_ADAPTER_APPS_INFO="index.php?c=AdapterV2&a=getAdapterAppsInfo";
    public final static String URL_NOTIFY_ADAPTER_RESULT="index.php?c=AdapterV2&a=notifyAdapterResult";

}
