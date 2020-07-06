package com.zhuangfei.adapterlib.qingguo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneMessageTools {
    @SuppressLint("MissingPermission")
    public static String a(Context context) {
        String subscriberId;
        String str = null;
        try {
            subscriberId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        } catch (Exception e) {
            subscriberId = "111111";
        }
        if (subscriberId == null) {
            str = "55555";
            subscriberId = "55555";
        } else if (subscriberId.startsWith("46000") || subscriberId.startsWith("46002")) {
            str = "46000";
        } else if (subscriberId.startsWith("46002")) {
            str = "46002";
        } else if (subscriberId.startsWith("46001")) {
            str = "46001";
        } else if (subscriberId.startsWith("46003")) {
            str = "46003";
        }
        return str;
    }

    @SuppressLint("MissingPermission")
    public static String b(Context context) {
        String str = "999999";
        try {
            str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            return str==null?"":str;
        } catch (Exception e) {
            return "999999";
        }
    }
}