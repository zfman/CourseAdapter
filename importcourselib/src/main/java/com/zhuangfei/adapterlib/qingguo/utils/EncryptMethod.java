package com.zhuangfei.adapterlib.qingguo.utils;

import android.app.AlertDialog;
import android.content.Context;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liu ZhuangFei on 2019/6/11.
 */
public class EncryptMethod {
    private static EncryptMethod instance;
    private Map<Integer, Character> a;
    private EncryptMethod(){}
    private Context context;
    public static EncryptMethod get(Context context){
        if(instance==null){
            synchronized (EncryptMethod.class){
                if(instance==null){
                    instance=new EncryptMethod();
                }
            }
        }
        instance.context=context;
        return instance;
    }
    public String encrypt1(String str, String str2) {
        if (str == null || "".equals(str) || str2 == null || "".equals(str2)) {
            return str;
        }
        String str3 = "";
        int length = str2.length();
        int length2 = str.length();
        int ceil = (int) Math.ceil((((double) length2) * 1.0d) / ((double) length));
        int ceil2 = (((int) Math.ceil((((((double) length2) * 3.0d) * 6.0d) / 9.0d) / 6.0d)) * 6) % length;
        String str4 = "";
        int i = 0;
        while (i < ceil) {
            String str5 = str4;
            for (int i2 = 1; i2 <= length; i2++) {
                int i3 = (i * length) + i2;
                String str6 = "000" + String.valueOf((Integer.valueOf(str.charAt(i3-1)).intValue() + Integer.valueOf(str2.charAt(i2 - 1)).intValue()) + ceil2);
                str5 = str5 + str6.substring(str6.length() - 3, str6.length());
                if (i3 == length2) {
                    break;
                }
            }
            i++;
            str4 = str5;
        }
        int i4 = 0;
        while (i4 < str4.length()) {
            i = i4 + 9;
            if (i >= str4.length()) {
                i = str4.length();
            }
            String substring = str4.substring(i4, i);
            i4 += 9;
            substring = "000000" + a(Long.valueOf(substring).longValue());
            str3 = str3 + substring.substring(substring.length() - 6, substring.length());
        }
        return str3;
    }

    public String a(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        char[] toCharArray = str.toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            if (i != toCharArray.length - 1) {
                stringBuffer.append(toCharArray[i]).append(",");
            } else {
                stringBuffer.append(toCharArray[i]);
            }
        }
        return stringBuffer.toString();
    }

    private String a(long j) {
        a();
        String str = "";
        if (j < 0) {
            return "-" + a(Math.abs(j));
        }
        do {
            String str2 = str;
            str = ((Character) a.get(Integer.valueOf((int) (j % 36)))).toString();
            if (!"".equals(str2)) {
                str = str + str2;
            }
            j /= 36;
        } while (j > 0);
        return str;
    }

    private void a() {
        int i = 0;
        if (a == null) {
            a = new HashMap();
        }
        for (int i2 = 0; i2 < 10; i2++) {
            a.put(Integer.valueOf(i2), Character.valueOf((char) (i2 + 48)));
        }
        while (i < 26) {
            a.put(Integer.valueOf(i + 10), Character.valueOf((char) (i + 97)));
            i++;
        }
    }


    public String encrypt2(String str) throws Exception {
        String[] split = b(str).split("");
        int length = split.length;
        String str2 = "";
        int i = 0;
        while (i < length) {
            if (!(i == 3 || i == 10 || i == 17 || i == 25)) {
                str2 = str2 + split[i];
            }
            i++;
        }
        return b(str2);
    }

    public String b(String str) throws Exception {
        return Md5.a(new Md5(str).a());
    }

    public Map<String, String> encrypt(Map<String, String> map) {
        return encrypt(map,null);
    }

    public Map<String, String> encrypt(Map<String, String> map,String token) {
        if(token==null){
            token="00000";
        }
        boolean z=false;
        String c = "";
        String str = "";
        for (Map.Entry entry : map.entrySet()) {
            String trim = ((String) entry.getKey()).trim();
            String str2 = entry.getValue() == null ? "" : (String) entry.getValue();
            str = str + "&" + trim + "=" + str2;
        }
        if (str.indexOf("&") == 0) {
            str = str.substring(1);
        }
        Map<String, String> hashMap = new HashMap();
        try {
            hashMap.put("param", encrypt1(str, c));
            hashMap.put("param2", encrypt2(str));
            hashMap.put("token", token);
            hashMap.put("appinfo","android2.4.302");
        } catch (Exception e) {
            showMessage(context,getStackTrace(e));
            hashMap.put("param", "error");
            hashMap.put("param2", "error");
            if (z) {
                hashMap.put("token", "error");
                hashMap.put("appinfo", "android2.4.302");
            }
        }
        return hashMap;
    }

    public String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        try {
            throwable.printStackTrace(printWriter);
            return stringWriter.toString();
        } finally {
            printWriter.close();
        }
    }

    public void showMessage(Context context,String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setTitle("Param")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定",null);
        builder.create().show();
    }
    private static String a(byte[] bArr) {
        char[] toCharArray = "0123456789abcdef".toCharArray();
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            stringBuilder.append(toCharArray[(bArr[i] >> 4) & 15]);
            stringBuilder.append(toCharArray[bArr[i] & 15]);
        }
        return stringBuilder.toString();
    }


}
