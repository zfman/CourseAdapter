package com.zhuangfei.adapterlib.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Security {

    //首先初始化一个MessageDigest对象，该对象通过update方法获取原始数据，
    //并调用digest方法完成哈希计算，然后把字节数组逐位转换为十六进制数，最后拼装加密字符串
    public static String encrypBy(String raw) {
        String md5Str = raw;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(raw.getBytes());
            byte[] encryContext = md.digest();

            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < encryContext.length; offset++) {
                i = encryContext[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            md5Str = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5Str;
    }
}