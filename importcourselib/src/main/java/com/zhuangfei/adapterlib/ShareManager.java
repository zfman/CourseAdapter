package com.zhuangfei.adapterlib;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.apis.model.ValuePair;
import com.zhuangfei.adapterlib.callback.OnValueCallback;
import com.zhuangfei.adapterlib.core.ParseResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Liu ZhuangFei on 2019/3/12.
 */
public class ShareManager {
    private static final String TAG = "ShareManager";

    public static void putValue(final Context context, String value, final OnValueCallback callback){
        TimetableRequest.putValue(context, value, new Callback<ObjResult<ValuePair>>() {
            @Override
            public void onResponse(Call<ObjResult<ValuePair>> call, Response<ObjResult<ValuePair>> response) {
                if(context==null) return;
                ObjResult<ValuePair> result=response.body();
                if(result!=null){
                    if(result.getCode()==200){
                        ValuePair pair=result.getData();
                        if(pair!=null){
                            if(callback!=null){
                                callback.onSuccess(pair);
                            }
                        }else{
                            ShareManager.showMsg(context,"PutValue:data is null");
                        }
                    }else{
                        ShareManager.showMsg(context,"PutValue:"+result.getMsg());
                    }
                }else{
                    ShareManager.showMsg(context,"PutValue:result is null");
                }
            }

            @Override
            public void onFailure(Call<ObjResult<ValuePair>> call, Throwable t) {
                ShareManager.showMsg(context,"Error:"+t.getMessage());
            }
        });
    }

    public static void showMsg(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static String getShareToken(ValuePair pair){
        if(pair!=null){
            String content = "Hi，你收到了来自适配联盟的课程分享！\n在此查看联盟支持的软件列表 http://t.cn/EM9fsHs\n复制这条消息，打开列表中任意软件即可导入#"+pair.getId()+"";
            return content;
        }
        return "";
    }

    public static void shareTable(Context context, ValuePair pair) {
        if(pair!=null){
            String content =getShareToken(pair);

            //获取剪贴板管理器：
            try{
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", content);
                cm.setPrimaryClip(mClipData);
                showMsg(context,"已复制到剪切板,快复制给你的朋友吧!");

                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                share_intent.setType("text/plain");//设置分享内容的类型
                share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享课程");
                share_intent.putExtra(Intent.EXTRA_TEXT, content);//添加分享内容
                share_intent = Intent.createChooser(share_intent, "分享课程");
                context.startActivity(share_intent);
            }catch (Exception e){
                showMsg(context,"Exception:"+e.getMessage());
            }
        }
    }

    public static void getFromClip(Context context,OnValueCallback callback) {
        try{
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = cm.getPrimaryClip();
            if (data != null) {
                if (data.getItemCount() > 0) {
                    ClipData.Item item = data.getItemAt(0);
                    if (item.getText() != null) {
                        String content = item.getText().toString();
                        if (!TextUtils.isEmpty(content)) {
                            int index = content.indexOf("#");
                            if (index != -1 && content.indexOf("Hi，你收到了来自适配联盟的课程分享！")!=-1) {
                                if (content.length() > index + 1) {
                                    String id = content.substring(index + 1);
                                    showDialogOnImport(context,id,callback);
                                    clearClip(context);
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.e(TAG, "getFromClip: ",e );
        }

    }

    private static void showDialogOnImport(final Context context, final String id, final OnValueCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("有人给你分享了课表，是否导入?")
                .setTitle("导入分享")
                .setPositiveButton("导入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getValue(context,id,callback);
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                    }
                })
                .setNegativeButton("取消", null);
        builder.create().show();
    }

    public static void clearClip(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", "");
        cm.setPrimaryClip(mClipData);
    }

    public static void getValue(final Context context, String id, final OnValueCallback callback) {
        TimetableRequest.getValue(context, id, new Callback<ObjResult<ValuePair>>() {
            @Override
            public void onResponse(Call<ObjResult<ValuePair>> call, Response<ObjResult<ValuePair>> response) {
                if(context==null) return;
                ObjResult<ValuePair> result = response.body();
                if (result != null) {
                    if (result.getCode() == 200) {
                        ValuePair pair = result.getData();
                        if (pair != null) {
                            if(callback!=null){
                                callback.onSuccess(pair);
                            }
                        } else {
                            showMsg(context,"GetValue:data is null");
                        }
                    } else {
                        showMsg(context,"GetValue:" + result.getMsg());
                    }
                } else {
                    showMsg(context,"GetValue:result is null");
                }
            }

            @Override
            public void onFailure(Call<ObjResult<ValuePair>> call, Throwable t) {
            }
        });
    }

    public static List<ParseResult> getShareData(String jsonValue){
        TypeToken<List<ParseResult>> token=new TypeToken<List<ParseResult>>(){};
        return new Gson().fromJson(jsonValue,token.getType());
    }

    public static String getShareJson(List<ParseResult> data){
        TypeToken<List<ParseResult>> token=new TypeToken<List<ParseResult>>(){};
        return new Gson().toJson(data,token.getType());
    }
}
