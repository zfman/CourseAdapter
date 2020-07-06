package com.zhuangfei.adapterlib.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Liu ZhuangFei on 2018/3/8.
 */

public class TimetableUtils {

    /**
     * 获取开学时间
     * @param curWeek
     * @return
     */
    public static String getStartSchoolTime(int curWeek){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long end=getThisWeekMonday(new Date()).getTime();
        int day=(curWeek-1)*7;
        long seconds=day*24*3600;
        long start=end-seconds*1000;
        return sdf.format(new Date(start))+" 00:00:00";
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    /**
     * 获取今天周几
     * @return
     */
    public static int getThisWeek() {
        //获取周几，1->7
        Calendar now = Calendar.getInstance();
        //一周第一天是否为星期天
        boolean isFirstSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
        int weekDay = now.get(Calendar.DAY_OF_WEEK);
        //若一周第一天为星期天，则-1
        if (isFirstSunday) {
            weekDay = weekDay - 1;
            if (weekDay == 0) {
                weekDay = 7;
            }
        }
        return weekDay;
    }

    public static List<Integer> getWeekList(String weeksString) {
        List<Integer> weekList = new ArrayList<>();

        if (weeksString == null || weeksString.length() == 0) return weekList;
//兼容 全周有课这种情况
        if(weeksString.startsWith("全周")){
            for(int i=1;i<=25;i++){
                weekList.add(i);
            }
            return weekList;
        }
        try{
            if (weeksString.indexOf(",") != -1) {
                String[] arr = weeksString.split(",");
                for (int i = 0; i < arr.length; i++) {
                    weekList.addAll(getWeekList2(arr[i],null));
                }
            } else {
                weekList.addAll(getWeekList2(weeksString,null));
            }
            return weekList;
        }catch (Exception e){
            e.printStackTrace();
            return weekList;
        }
    }

    public static List<Integer> getWeekList(String weeksString,String dsz) {
        List<Integer> weekList = new ArrayList<>();

        if (weeksString == null || weeksString.length() == 0) return weekList;
//兼容 全周有课这种情况
        if(weeksString.startsWith("全周")){
            for(int i=1;i<=25;i++){
                weekList.add(i);
            }
            return weekList;
        }
        try{
            if (weeksString.indexOf(",") != -1) {
                String[] arr = weeksString.split(",");
                for (int i = 0; i < arr.length; i++) {
                    weekList.addAll(getWeekList2(arr[i],dsz));
                }
            } else {
                weekList.addAll(getWeekList2(weeksString,dsz));
            }
            return weekList;
        }catch (Exception e){
            e.printStackTrace();
            return weekList;
        }
    }

    public static List<Integer> getWeekList2(String weeksString,String dsz) {
        String weeksString2 = weeksString.replaceAll("[^\\d\\-\\,]", "");
        List<Integer> weekList = new ArrayList<>();
        int first = -1, end = -1, index = -1;
        if ((index = weeksString2.indexOf("-")) != -1) {
            first = Integer.parseInt(weeksString2.substring(0, index));
            end = Integer.parseInt(weeksString2.substring(index + 1));
        } else {
            first = Integer.parseInt(weeksString2);
            end = first;
        }
        int type=1;
        if(weeksString.indexOf("单")!=-1){
            type=1;
        }else if(weeksString.indexOf("双")!=-1){
            type=2;
        }else{
            type=3;
        }

        if(dsz!=null){
            if(dsz.equals("0")){
                type=3;
            }else if(dsz.equals("1")){
                type=1;
            }else{
                type=2;
            }
        }
        for (int i = first; i <= end; i++){
            if(type==1&&i%2==1){
                weekList.add(i);
            }else if(type==2&&i%2==0){
                weekList.add(i);
            }else if(type==3){
                weekList.add(i);
            }
        }
        return weekList;
    }

}
