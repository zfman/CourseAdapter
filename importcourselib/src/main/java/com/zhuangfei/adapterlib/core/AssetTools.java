package com.zhuangfei.adapterlib.core;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * 加载Asset下的文件工具
 * Created by Liu ZhuangFei on 2018/10/6.
 */
public class AssetTools {
    public static String readAssetFile(Context context,String fileName) {
        InputStream is = null;
        String msg = null;
        try {
            is = context.getResources().getAssets().open(fileName);
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            msg = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

}
