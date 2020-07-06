package com.zhuangfei.adapterlib;

import com.zhuangfei.adapterlib.core.ParseResult;

import java.util.List;

/**
 * 存储解析后的结果
 * Created by Liu ZhuangFei on 2019/3/12.
 */
public class ParseManager {
    private static boolean success;
    private static long timestamp;
    private static List<ParseResult> data;

    public static void setSuccess(boolean success) {
        ParseManager.success = success;
    }

    public static boolean isSuccess() {
        return success;
    }

    public static long getTimestamp() {
        return timestamp;
    }

    public static void setTimestamp(long timestamp) {
        ParseManager.timestamp = timestamp;
    }

    public static List<ParseResult> getData() {
        return data;
    }

    public static void setData(List<ParseResult> data) {
        ParseManager.data = data;
    }

    public static void clearCache(){
        setSuccess(false);
        setTimestamp(0L);
        setData(null);
    }
}
