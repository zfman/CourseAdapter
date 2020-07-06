package com.zhuangfei.adapterlib.station;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.activity.SearchSchoolActivity;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.StationModel;
import com.zhuangfei.adapterlib.activity.StationWebViewActivity;
import com.zhuangfei.adapterlib.station.model.ClipBoardModel;
import com.zhuangfei.adapterlib.station.model.TinyConfig;
import com.zhuangfei.adapterlib.utils.GsonUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Liu ZhuangFei on 2019/2/8.
 */
public class StationManager {

    static SharedPreferences sp;
    static SharedPreferences.Editor editor;

    public static String getBaseUrl(){
        return "http://www.liuzhuangfei.com/apis/area/station/";
    }

    public static void openStationWithout(Activity context, TinyConfig config,StationModel stationModel,IStationOperator operator){
        if(context==null||stationModel==null) return;
        Intent intent=new Intent(context, StationWebViewActivity.class);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_MODEL,stationModel);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_CONFIG,config);
        intent.putExtra(SearchSchoolActivity.EXTRA_STATION_OPERATOR, operator);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.anim_station_open_activity,R.anim.anim_station_static);//动画
    }

    public static void openStationWithout(Activity context, TinyConfig config,StationModel stationModel,IStationOperator operator,StationSdk sdk){
        if(context==null||stationModel==null) return;
        Intent intent=new Intent(context, StationWebViewActivity.class);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_MODEL,stationModel);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_CONFIG,config);
        intent.putExtra(SearchSchoolActivity.EXTRA_STATION_OPERATOR, operator);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_SDK, sdk);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.anim_station_open_activity,R.anim.anim_station_static);//动画
    }

    public static void openStationOtherPage(Activity context, TinyConfig config,StationModel stationModel,IStationOperator operator){
        if(context==null||stationModel==null) return;
        Intent intent=new Intent(context, StationWebViewActivity.class);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_MODEL,stationModel);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_CONFIG,config);
        intent.putExtra(StationWebViewActivity.EXTRAS_STATION_IS_JUMP,true);
        intent.putExtra(SearchSchoolActivity.EXTRA_STATION_OPERATOR, operator);
        context.startActivity(intent);
    }

    public static String getRealUrl(String url){
        if(TextUtils.isEmpty(url)) return null;
        int index=url.indexOf("#station_config#");//16 char
        if(index==-1){
            return url;
        }
        return url.substring(0,index);
    }

    public static Map<String,String> getStationConfig(String url){
        if(TextUtils.isEmpty(url)) return null;
        int index=url.indexOf("#station_config#");//16 char
        if(index==-1) return null;
        int nextIndex=index+"#station_config#".length();
        String config=url.substring(nextIndex);
        Map<String,String> map=new HashMap<>();
        if(!TextUtils.isEmpty(config)){
            String[] array=config.split("&");
            for(int i=0;i<array.length;i++){
                map.put(array[i].split("=")[0],array[i].split("=")[1]);
            }
            return map;
        }else {
            return null;
        }
    }

    public static String getStationName(String url){
        if(url==null) return null;
        int lastIndex=url.lastIndexOf("/");
        int lastIndex2=url.substring(0,lastIndex).lastIndexOf("/");
        if(lastIndex==-1||lastIndex2==-1){
            return null;
        }
        return url.substring(lastIndex2+1,lastIndex);
    }

    public static void checkClip(Activity context,IStationOperator operator){
        String content=getClipContent(context);
        if(TextUtils.isEmpty(content)) return;
        SharedPreferences sp=context.getSharedPreferences("station_common_space",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        String clipString=sp.getString("clip",null);
        List<ClipBoardModel> localClips=new ArrayList<>();
        if(clipString!=null){
            TypeToken<List<ClipBoardModel>> typeToken=new TypeToken<List<ClipBoardModel>>(){};
            List<ClipBoardModel> tmp=new Gson().fromJson(clipString,typeToken.getType());
            localClips.addAll(tmp);
        }

        for(ClipBoardModel model:localClips){
            if(model!=null){
                if(content.matches(model.getRegex())){
                    getStationConfig(context,model.getStationModel(),content,operator);
                    break;
                }
            }
        }
        clearClip(context);
    }

    public static void getStationConfig(final Activity context, final StationModel stationModel, final String content, final IStationOperator operator){
        final String stationName=StationManager.getStationName(stationModel.getUrl());
        if(TextUtils.isEmpty(stationName)) return;
        TimetableRequest.getStationConfig(context, stationName, new Callback<TinyConfig>() {
            @Override
            public void onResponse(Call<TinyConfig> call, Response<TinyConfig> response) {
                if(response!=null){
                    TinyConfig config=response.body();
                    if(config!=null){
                        if(config.getSupport()> StationSdk.SDK_VERSION){
                            Toast.makeText(context,"版本太低，不支持本服务站，请升级新版本!",Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                stationModel.setUrl(stationModel.getUrl()+"?clip="+ URLEncoder.encode(content,"utf8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            StationManager.openStationWithout(context,config,stationModel,operator);
                        }
                    }else{
                        Toast.makeText(context,"Error:config is null",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,"Error:response is null",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TinyConfig> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void checkStationSharePreferences(Context context){
        if(sp==null){
            sp=context.getSharedPreferences("station_space_all", Context.MODE_PRIVATE);
            editor=sp.edit();
        }
    }

    public static void openStationWithId(final Activity context, String stationName,int stationId,final IStationOperator operator){
        checkStationSharePreferences(context);
        if(TextUtils.isEmpty(stationName)) return;
        final StationModel stationModel=new StationModel();
        stationModel.setStationId(stationId);
        stationModel.setUrl(getBaseUrl()+stationName);
        stationModel.setDisplayAfterRequest(true);
        String config=sp.getString("config_"+stationModel.getStationId(),null);
        if(!TextUtils.isEmpty(config)){
            handleConfig(context,GsonUtils.getGson().fromJson(config,TinyConfig.class),stationModel,operator);
            return;
        }
        TimetableRequest.getStationConfig(context, stationName, new Callback<TinyConfig>() {
            @Override
            public void onResponse(Call<TinyConfig> call, Response<TinyConfig> response) {
                if(response!=null){
                    TinyConfig config=response.body();
                    handleConfig(context,config,stationModel,operator);
                    if(config!=null){
                        editor.putString("config_"+stationModel.getStationId(), GsonUtils.getGson().toJson(config));
                        editor.commit();
                    }
                }else{
                    Toast.makeText(context,"Error:response is null",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TinyConfig> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void handleConfig(Activity context,TinyConfig config,StationModel stationModel,IStationOperator operator) {
        if(config!=null){
            if(config.getSupport()> StationSdk.SDK_VERSION){
                Toast.makeText(context,"版本太低，不支持本服务站，请升级新版本!",Toast.LENGTH_SHORT).show();
            }else{
                StationManager.openStationWithout(context,config,stationModel,operator,new StationSdk());
            }
        }else{
            Toast.makeText(context,"Error:config is null",Toast.LENGTH_SHORT).show();
        }
    }

    public static void clearClip(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", "");
        cm.setPrimaryClip(mClipData);
    }

    public static String getClipContent(Context ctx){
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        if (data != null) {
            if (data.getItemCount() > 0) {
                ClipData.Item item = data.getItemAt(0);
                if (item.getText() != null) {
                    String content = item.getText().toString();
                    if (!TextUtils.isEmpty(content)) {
                        return content;
                    }
                }
            }
        }
        return null;
    }
}
