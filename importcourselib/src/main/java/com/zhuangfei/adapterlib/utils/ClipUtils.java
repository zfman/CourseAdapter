package com.zhuangfei.adapterlib.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Liu ZhuangFei on 2019/4/26.
 */
public class ClipUtils {
    public static void shareTable(Context context, String content) {
        if(content!=null){

            //获取剪贴板管理器：
            try{
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", content);
                cm.setPrimaryClip(mClipData);
                Toast.makeText(context,"已复制到剪切板,快复制给你的朋友吧!",Toast.LENGTH_SHORT).show();

                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                share_intent.setType("text/plain");//设置分享内容的类型
                share_intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                share_intent.putExtra(Intent.EXTRA_TEXT, content);//添加分享内容
                share_intent = Intent.createChooser(share_intent, "Share");
                context.startActivity(share_intent);
            }catch (Exception e){
                Toast.makeText(context,"Exception:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
