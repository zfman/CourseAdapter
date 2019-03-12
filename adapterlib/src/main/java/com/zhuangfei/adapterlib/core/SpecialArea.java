package com.zhuangfei.adapterlib.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * js调用的对象 ：列出了js可操作的所有方法
 * Created by Liu ZhuangFei on 2018/10/27.
 */
public class SpecialArea {

    Activity activity;
    IArea.Callback callback;
    
    public SpecialArea(@NonNull Activity activity,@NonNull IArea.Callback callback){
        this.callback=callback;
        this.activity=activity;
    }
    
    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void forTagResult(final String[] tags) {
        final String[] finalTags = tags;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (finalTags == null || finalTags.length == 0) {
                    callback.onNotFindTag();
                } else {
                    callback.onFindTags(tags);
                }
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void forResult(final String result) {
        final String finalResult = result;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (finalResult == null) {
                    callback.onNotFindResult();
                } else {
                    callback.onFindResult(parse(result));
                }
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void error(final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onError(msg);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void info(final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onInfo(msg);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void warning(final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onWarning(msg);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public String getHtml() {
        return callback.getHtml();
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void showHtml(final String content) {
        if (TextUtils.isEmpty(content)) return;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.showHtml(content);
            }
        });
    }

    @JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    public void showHtmlForAdjust(final String content) {
        if (TextUtils.isEmpty(content)) return;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.showHtmlForAdjust(content);
            }
        });
    }

    /**
     * 课程解析
     * @param data
     * @return
     */
    public List<ParseResult> parse(String data) {
        List<ParseResult> result=new ArrayList<>();
        if(data==null) return result;

        String[] items = data.trim().split("#");

        for (String item : items) {
            if (!TextUtils.isEmpty(item)) {
                String[] perItem = item.split("\\$");
                if (perItem == null || perItem.length < 7) continue;
                String name = perItem[0];
                String teacher = perItem[1];
                String weeks = perItem[2];
                String day = perItem[3];
                String start = perItem[4];
                String step = perItem[5];
                String room = perItem[6];
//
                int dayInt=0;
                int startInt=0;
                int stepInt=0;
                boolean exception=false;
                try{
                    dayInt = Integer.parseInt(day);
                }catch (Exception e){
                    dayInt=7;
                    exception=true;
                }

                try{
                    startInt = Integer.parseInt(start);
                }catch (Exception e){
                    startInt=1;
                    exception=true;
                }

                try{
                    stepInt = Integer.parseInt(step);
                }catch (Exception e){
                    startInt=4;
                    exception=true;
                }

                String[] weeksArray = weeks.split(" ");
                List<Integer> weeksList = new ArrayList<>();
                for (String val : weeksArray) {
                    if (!TextUtils.isEmpty(val)) weeksList.add(Integer.parseInt(val));
                }

                if(exception){
                    if(name!=null){
                        name="[出现异常,本课程数据不正确]"+name;
                    }else {
                        name="[出现异常,本课程数据不正确]";
                    }
                }

                ParseResult model = new ParseResult();
                model.setWeekList(weeksList);
                model.setTeacher(teacher);
                model.setStep(stepInt);
                model.setStart(startInt);
                model.setRoom(room);
                model.setName(name);
                model.setDay(dayInt);
                result.add(model);
            }
        }
        return result;
    }
}
