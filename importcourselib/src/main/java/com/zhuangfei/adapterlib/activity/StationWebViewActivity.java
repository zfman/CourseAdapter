package com.zhuangfei.adapterlib.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuangfei.adapterlib.activity.custom.CustomPopWindow;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.activity.view.MyWebView;
import com.zhuangfei.adapterlib.apis.model.BaseResult;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.apis.model.StationSpaceModel;
import com.zhuangfei.adapterlib.core.JsSupport;
import com.zhuangfei.adapterlib.station.DefaultStationOperator;
import com.zhuangfei.adapterlib.station.IStationOperator;
import com.zhuangfei.adapterlib.station.IStationView;
import com.zhuangfei.adapterlib.station.StationContants;
import com.zhuangfei.adapterlib.station.TinyUserManager;
import com.zhuangfei.adapterlib.station.UserManager;
import com.zhuangfei.adapterlib.station.model.ClipBoardModel;
import com.zhuangfei.adapterlib.station.model.TinyConfig;
import com.zhuangfei.adapterlib.station.model.TinyUserInfo;
import com.zhuangfei.adapterlib.utils.GsonUtils;
import com.zhuangfei.adapterlib.utils.ScreenUtils;
import com.zhuangfei.adapterlib.station.StationManager;
import com.zhuangfei.adapterlib.utils.ViewUtils;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.ListResult;
import com.zhuangfei.adapterlib.apis.model.StationModel;
import com.zhuangfei.adapterlib.station.StationSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 服务站加载引擎
 */
public class StationWebViewActivity extends AppCompatActivity implements IStationView {

    private static final String TAG = "StationWebViewActivity";

    // wenview与加载条
    MyWebView webView;

    // 标题
    TextView titleTextView;
    String url, title;
    TextView titleTextView2;

    // 声明PopupWindow
    private CustomPopWindow popupWindow;

    StationModel stationModel;
    TinyConfig tinyConfig;
    public static final String EXTRAS_STATION_MODEL = "station_model_extras";
    public static final String EXTRAS_STATION_CONFIG = "station_config_extras";
    public static final String EXTRAS_STATION_IS_JUMP = "station_is_jump";
    public static final String EXTRAS_STATION_SDK="station_sdk";

    LinearLayout rootLayout;
    int deleteId = -1;

    LinearLayout actionbarLayout;
    ImageView backImageView;
    ImageView backImageView2;

    ImageView moreImageView;
    ImageView closeImageView;
    LinearLayout buttonGroupLayout;
    View diverView;//分隔竖线

    int needUpdate = 1;

    View loadingTipView1;
    View loadingTipView2;
    View loadingTipView3;
    LinearLayout loadingViewLayout;

    private int currentLoading = 1;
    boolean isJump=false;
    boolean loadFinish=false;
    StationSdk stationSdk;

    Timer timer;
    View statusBar;
    LinearLayout floatActionBar;
    IStationOperator stationOperator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(R.layout.activity_station_web_view);
        ViewUtils.setTransparent(this);
        initUrl();
        initView();
        loadWebView();
        getStationById();

    }

    public void jumpPage(String page) {
        StationModel newStationModel = stationModel.copyModel();
        String newUrl = StationManager.getBaseUrl()+tinyConfig.getName()+"/" + page;
        newStationModel.setUrl(newUrl);
        StationManager.openStationOtherPage(this, tinyConfig, newStationModel,stationOperator);
    }

    private void initUrl() {
        url = StationManager.getRealUrl(stationModel.getUrl());
        title = stationModel.getName();
    }

    private void initView() {
        webView = findViewById(R.id.id_webview);
        moreImageView = findViewById(R.id.iv_station_more);
        diverView = findViewById(R.id.id_station_diver);
        buttonGroupLayout = findViewById(R.id.id_station_buttongroup);
        closeImageView = findViewById(R.id.iv_station_close);
        actionbarLayout = findViewById(R.id.id_station_action_bg);
        rootLayout = findViewById(R.id.id_station_root);
        titleTextView = findViewById(R.id.id_web_title);
        backImageView = findViewById(R.id.id_back);
        backImageView2 = findViewById(R.id.id_back2);
        titleTextView2 = findViewById(R.id.id_web_title2);
        statusBar=findViewById(R.id.id_statusbar);
        floatActionBar=findViewById(R.id.id_station_float_actionbar);

        loadingTipView1 = findViewById(R.id.id_loading_tip1);
        loadingTipView2 = findViewById(R.id.id_loading_tip2);
        loadingTipView3 = findViewById(R.id.id_loading_tip3);
        loadingViewLayout=findViewById(R.id.id_loadingview_layout);

        stationOperator= (IStationOperator) getIntent().getSerializableExtra(SearchSchoolActivity.EXTRA_STATION_OPERATOR);
        if(stationOperator==null){
            stationOperator=new DefaultStationOperator();
        }
        stationOperator.initStation(stationModel);

        try{
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewUtils.getStatusHeight(this));
            statusBar.setLayoutParams(lp);
            TinyConfig.ThemeBean themeBean = tinyConfig.getTheme();
            statusBar.setBackgroundColor(Color.parseColor(themeBean.getPrimaryColor()));
        }catch (Exception e){
            Log.e(TAG, "initView: ",e );
        }

        stationSdk= (StationSdk) getIntent().getSerializableExtra(EXTRAS_STATION_SDK);
        if(stationSdk==null){
            stationSdk=new StationSdk();
        }
        stationSdk.init(this, getStationSpace());
        startLoading();


        isJump = getIntent().getBooleanExtra(EXTRAS_STATION_IS_JUMP, false);
        if (isJump) {
            backImageView.setVisibility(View.VISIBLE);
            backImageView.setColorFilter(Color.parseColor(tinyConfig.getTheme().getActionTextColor()));
            backImageView2.setVisibility(View.VISIBLE);
            backImageView2.setColorFilter(Color.parseColor(tinyConfig.getTheme().getActionTextColor()));
        }

        titleTextView.setText(title);
        titleTextView2.setText(title);
        try {
            actionbarLayout.setBackgroundColor(Color.parseColor(tinyConfig.getTheme().getActionColor()));
            floatActionBar.setBackgroundColor(Color.parseColor(tinyConfig.getTheme().getActionColor()));
        } catch (Exception e) {
        }

        try {
            int textcolor = Color.parseColor(tinyConfig.getTheme().getActionTextColor());
            titleTextView.setTextColor(textcolor);
            titleTextView2.setTextColor(textcolor);
            moreImageView.setColorFilter(textcolor);
            closeImageView.setColorFilter(textcolor);
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(ScreenUtils.dip2px(this, 25));
            gd.setStroke(2, textcolor);
            diverView.setBackgroundColor(textcolor);
            buttonGroupLayout.setBackgroundDrawable(gd);
        } catch (Exception e) {
        }


        if(!tinyConfig.getTheme().isActionBarVisiable()){
            actionbarLayout.setVisibility(View.GONE);
        }

        findViewById(R.id.id_station_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.id_station_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMorePopWindow();
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        webView.setScrollChangeCallback(new MyWebView.onScrollChangeCallback() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                stationSdk.getJsSupport().checkAndcallJs("onScrollChanged("+l+","+t+","+oldl+","+oldt+")");
            }
        });
    }

    private void startLoading() {
        loadFinish=false;
        loadingViewLayout.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        currentLoading = 1;
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCurrentLoadingView();
                    }
                });
            }
        }, 0,300);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!loadFinish){
                            notifyLoadingFinish();
                        }
                    }
                });
            }
        },2000);
    }

    public void updateTinyConfig(){
        String stationName=StationManager.getStationName(stationModel.getUrl());
        if(TextUtils.isEmpty(stationName)) return;
        TimetableRequest.getStationConfig(getContext(), stationName, new Callback<TinyConfig>() {
            @Override
            public void onResponse(Call<TinyConfig> call, Response<TinyConfig> response) {
                if(response!=null){
                    TinyConfig config=response.body();
                    if(config!=null&&config.getVersion()>tinyConfig.getVersion()){
                        SharedPreferences sp=getSharedPreferences("station_space_all", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("config_"+stationModel.getStationId(), GsonUtils.getGson().toJson(config));
                        editor.commit();

                        tinyConfig=config;
                        startLoading();
                        showMessage("配置文件更新，请重新进入");
                        finish();
                    }
                }else{
                    Toast.makeText(getContext(),"Error:response is null",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TinyConfig> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getNextLoadingIndex() {
        if (currentLoading < 3) return currentLoading + 1;
        return 1;
    }

    private int getLastLoadingIndex(){
        if(currentLoading>1) return currentLoading-1;
        return 3;
    }

    private void showCurrentLoadingView() {
        loadingTipView1.setBackgroundDrawable(getResources().getDrawable(R.drawable.loadingview_tip1));
        loadingTipView2.setBackgroundDrawable(getResources().getDrawable(R.drawable.loadingview_tip1));
        loadingTipView3.setBackgroundDrawable(getResources().getDrawable(R.drawable.loadingview_tip1));
        switch (currentLoading) {
            case 1:
                loadingTipView1.setBackgroundDrawable(getResources().getDrawable(R.drawable.loadingview_tip3));
                break;
            case 2:
                loadingTipView2.setBackgroundDrawable(getResources().getDrawable(R.drawable.loadingview_tip3));
                break;
            case 3:
                loadingTipView3.setBackgroundDrawable(getResources().getDrawable(R.drawable.loadingview_tip3));
                break;
            default:
                break;
        }
        switch (getLastLoadingIndex()) {
            case 1:
                loadingTipView1.setBackgroundDrawable(getResources().getDrawable(R.drawable.loadingview_tip2));
                break;
            case 2:
                loadingTipView2.setBackgroundDrawable(getResources().getDrawable(R.drawable.loadingview_tip2));
                break;
            case 3:
                loadingTipView3.setBackgroundDrawable(getResources().getDrawable(R.drawable.loadingview_tip2));
                break;
            default:
                break;
        }
        currentLoading=getNextLoadingIndex();
    }

    private void beforeSetContentView() {
        stationModel = (StationModel) getIntent().getSerializableExtra(EXTRAS_STATION_MODEL);
        tinyConfig = (TinyConfig) getIntent().getSerializableExtra(EXTRAS_STATION_CONFIG);
        if (stationModel == null || tinyConfig == null) {
            Toast.makeText(this, "传参异常", Toast.LENGTH_SHORT).show();
            finish();
        }
        updateTinyConfig();
    }

    public void getStationById() {
        if (needUpdate == 0) return;
        TimetableRequest.getStationById(this, stationModel.getStationId(), new Callback<ListResult<StationModel>>() {
            @Override
            public void onResponse(Call<ListResult<StationModel>> call, Response<ListResult<StationModel>> response) {
                ListResult<StationModel> result = response.body();
                if (result != null) {
                    if (result.getCode() == 200) {
                        showStationResult(result.getData());
                    } else {
                        Toast.makeText(StationWebViewActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StationWebViewActivity.this, "station response is null!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListResult<StationModel>> call, Throwable t) {

            }
        });
    }

    private void showStationResult(List<StationModel> result) {
        if (result == null || result.size() == 0) return;
        final StationModel model = result.get(0);
        if (model != null) {
            if(this.stationModel.isDisplayAfterRequest()){
                this.stationModel=model;
                title = stationModel.getName();
                titleTextView.setText(title);
                titleTextView2.setText(title);
            }

            boolean update = false;
            if (model.getName() != null && !model.getName().equals(stationModel.getName())) {
                update = true;
            }
            if (model.getUrl() != null && !model.getUrl().equals(stationModel.getUrl())) {
                update = true;
            }
            if (model.getImg() != null && !model.getImg().equals(stationModel.getImg())) {
                update = true;
            }

            if (update) {
                stationOperator.updateLocalStation(model);
                AlertDialog.Builder builder=new AlertDialog.Builder(this)
                        .setTitle("服务站更新")
                        .setMessage("本地保存的服务站已过期，需要重新加载")
                        .setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //todo post reload event
                                stationOperator.postUpdateStationEvent();
                                finish();
                            }
                        });
                builder.create().show();
            }
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            if (v.getId() == R.id.pop_add_home) {
                if (stationOperator.haveLocal()) {
                    stationOperator.saveOrRemoveStation(stationModel);
                    stationOperator.postUpdateStationEvent();
                    Toast.makeText(StationWebViewActivity.this, "已从主页删除", Toast.LENGTH_SHORT).show();
                } else {
                    if (!stationOperator.isCanSaveStaion()) {
                        Toast.makeText(StationWebViewActivity.this, "已达到最大数量限制15，请先删除其他服务站后尝试", Toast.LENGTH_SHORT).show();
                    } else {
                        stationOperator.saveOrRemoveStation(stationModel);
                        stationOperator.postUpdateStationEvent();
                        Toast.makeText(StationWebViewActivity.this, "已添加到首页", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (v.getId() == R.id.pop_about) {
                if (stationModel != null && stationModel.getOwner() != null) {
                    Toast.makeText(StationWebViewActivity.this, stationModel.getOwner(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StationWebViewActivity.this, "所有者未知!", Toast.LENGTH_SHORT).show();
                }
            }

            if (v.getId() == R.id.pop_to_home) {
                webView.clearHistory();
                startLoading();
                webView.loadUrl(stationModel.getUrl());
            }
            popupWindow.dismiss();
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView() {
        webView.loadUrl(url);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("gb2312");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        String ua=webView.getSettings().getUserAgentString();
        if(ua!=null){
            ua=ua+" stationSdk/"+StationSdk.SDK_VERSION+" token/"+ TinyUserManager.get(this).getToken();
        }
        webView.getSettings().setUserAgentString(ua);
        webView.addJavascriptInterface(stationSdk, "sdk");
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + url);
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >70&&!loadFinish){
                    notifyLoadingFinish();
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                titleTextView.setText(title);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        stationSdk.getJsSupport().checkAndcallJs("onLifecycle('onDestroy')");
        super.onDestroy();
    }

    @Override
    public void notifyLoadingFinish(){
        loadFinish=true;
        loadingViewLayout.setVisibility(View.GONE);
        if(webView!=null){
            webView.setVisibility(View.VISIBLE);
        }
        timer.cancel();
    }

    @Override
    public void notifyLoadingStart() {
        startLoading();
    }

    @Override
    public void finish() {
        super.finish();
        if(!isJump){
            this.overridePendingTransition(R.anim.anim_station_static, R.anim.anim_station_close_activity);
        }
    }

    /**
     * 弹出popupWindow
     */
    public void showMorePopWindow() {
        popupWindow = new CustomPopWindow(StationWebViewActivity.this, stationOperator.haveLocal(), itemsOnClick);
        popupWindow.showAtLocation(rootLayout,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.backgroundAlpha(StationWebViewActivity.this, 1f);
            }
        });
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public WebView getWebView() {
        return webView;
    }

    public String getStationSpace() {
        return "station_space_" + stationModel.getStationId();
    }

    @Override
    public void setTitle(String title) {
        titleTextView.setText(title);
        titleTextView2.setText(title);
    }

    @Override
    public void goback(){
        finish();
    }

    @Override
    public SharedPreferences getSharedPreferences(String space) {
        return getSharedPreferences(space,MODE_PRIVATE);
    }

    @Override
    public void postThread(final IMainRunner runner) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runner.done();
            }
        });
    }

    @Override
    public void getFromServer(String moduleName, final String tag) {
        TinyUserInfo userInfo=UserManager.get(getContext()).getUserInfo();
        if(userInfo==null||TextUtils.isEmpty(userInfo.getToken())){
            stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_NEED_LOGIN,"请先登录",null);
            Intent intent=new Intent(getContext(), TinyAuthActivity.class);
            startActivity(intent);
            return;
        }
        TimetableRequest.getStationSpace(getContext(), stationModel.getStationId(), moduleName, userInfo.getToken(), new Callback<ObjResult<StationSpaceModel>>() {
            @Override
            public void onResponse(Call<ObjResult<StationSpaceModel>> call, Response<ObjResult<StationSpaceModel>> response) {
                ObjResult<StationSpaceModel> result=response.body();
                if(result==null){
                    stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_ERROR,"result is null",null);
                }else{
                    if(result.getCode()==200){
                        String data=GsonUtils.getGson().toJson(result);
                        try {
                            JSONObject obj=new JSONObject(data);
                            JSONObject dataObj=obj.getJSONObject("data");
                            String value=dataObj.getString("value");
                            stationSdk.getJsSupport().checkAndcallJs(tag, StationContants.CODE_SUCCESS,"success",value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_ERROR,e.getMessage(),null);
                        }
                    }else if(result.getCode()==332){
                        stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_ERROR,result.getMsg(),null);
                        Intent intent=new Intent(getContext(), TinyAuthActivity.class);
                        startActivity(intent);
                    }
                    else{
                        stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_ERROR,result.getMsg(),null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ObjResult<StationSpaceModel>> call, Throwable t) {
                stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_ERROR,t.getMessage(),null);
            }
        });
    }

    @Override
    public void putToServer(String moduleName, String value, final String tag) {
        TinyUserInfo userInfo=UserManager.get(getContext()).getUserInfo();
        if(userInfo==null||TextUtils.isEmpty(userInfo.getToken())){
            stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_NEED_LOGIN,"请先登录",null);
            Intent intent=new Intent(getContext(), TinyAuthActivity.class);
            startActivity(intent);
            return;
        }
        TimetableRequest.setStationSpace(getContext(), stationModel.getStationId(), moduleName, userInfo.getToken(),value, new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                BaseResult result=response.body();
                if(result==null){
                    stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_ERROR,"result is null",null);
                }else{
                    if(result.getCode()==200){
                        stationSdk.getJsSupport().checkAndcallJs(tag, StationContants.CODE_SUCCESS,"success",null);
                    }else if(result.getCode()==332){
                        stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_ERROR,result.getMsg(),null);
                        Intent intent=new Intent(getContext(), TinyAuthActivity.class);
                        startActivity(intent);
                    }else{
                        stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_ERROR,result.getMsg(),null);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                stationSdk.getJsSupport().checkAndcallJs(tag,StationContants.CODE_ERROR,t.getMessage(),null);
            }
        });
    }

    @Override
    public SharedPreferences getPrivateSharedPreferences() {
        return getSharedPreferences(getStationSpace());
    }

    @Override
    public SharedPreferences getCommonSharedPreferences() {
        return getSharedPreferences("station_common_space");
    }

    @Override
    protected void onResume() {
        super.onResume();
        stationSdk.getJsSupport().checkAndcallJs("onLifecycle('onResume')");
    }

    @Override
    protected void onStart() {
        super.onStart();
        stationSdk.getJsSupport().checkAndcallJs("onLifecycle('onStart')");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stationSdk.getJsSupport().checkAndcallJs("onLifecycle('onRestart')");
    }

    @Override
    protected void onPause() {
        super.onPause();
        stationSdk.getJsSupport().checkAndcallJs("onLifecycle('onPause')");
    }

    @Override
    protected void onStop() {
        super.onStop();
        stationSdk.getJsSupport().checkAndcallJs("onLifecycle('onStop')");
    }

    @Override
    public void setActionBarVisiable(boolean b) {
        actionbarLayout.setVisibility(b?View.VISIBLE:View.GONE);
    }

    @Override
    public void setFloatActionBarVisiable(boolean b) {
        floatActionBar.setVisibility(b?View.VISIBLE:View.GONE);
    }

    @Override
    public void setStatusBarColor(String color){
        statusBar.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public int dp2px(int dp) {
        return ScreenUtils.dip2px(this,dp);
    }

    @Override
    public String getClipContent() {
        String s=StationManager.getClipContent(this);
        StationManager.clearClip(this);
        return s;
    }

    @Override
    public void registerClipBoard(String regex) {
        SharedPreferences sp=getSharedPreferences("station_common_space");
        SharedPreferences.Editor editor=sp.edit();
        String clipString=sp.getString("clip",null);
        List<ClipBoardModel> localClips=new ArrayList<>();
        if(clipString!=null){
            TypeToken<List<ClipBoardModel>> typeToken=new TypeToken<List<ClipBoardModel>>(){};
            List<ClipBoardModel> tmp=new Gson().fromJson(clipString,typeToken.getType());
            localClips.addAll(tmp);
        }
        ClipBoardModel findModel=null;
        for(ClipBoardModel model:localClips){
            if(model!=null&&model.getStationModel()!=null){
                if(model.getStationModel().getStationId()==stationModel.getStationId()){
                    findModel=model;
                    break;
                }
            }
        }
        if(findModel!=null){
            findModel.setRegex(regex);
        }else {
            ClipBoardModel newClip=new ClipBoardModel();
            newClip.setRegex(regex);
            newClip.setStationModel(stationModel);
            localClips.add(newClip);
        }
        editor.putString("clip",new Gson().toJson(localClips));
        editor.commit();
    }

    @Override
    public void unregisterClipBoard() {
        SharedPreferences sp=getSharedPreferences("station_common_space");
        SharedPreferences.Editor editor=sp.edit();
        String clipString=sp.getString("clip",null);
        List<ClipBoardModel> localClips=new ArrayList<>();
        if(clipString!=null){
            TypeToken<List<ClipBoardModel>> typeToken=new TypeToken<List<ClipBoardModel>>(){};
            List<ClipBoardModel> tmp=new Gson().fromJson(clipString,typeToken.getType());
            localClips.addAll(tmp);
        }
        List<ClipBoardModel> removeClip=new ArrayList<>();
        for(ClipBoardModel model:localClips){
            if(model!=null&&model.getStationModel()!=null){
                if(model.getStationModel().getStationId()==stationModel.getStationId()){
                    removeClip.add(model);
                }
            }
        }
        if(removeClip!=null){
            localClips.removeAll(removeClip);
        }
        editor.putString("clip",new Gson().toJson(localClips));
        editor.commit();
    }

    @Override
    public boolean isRegisterClipBoard() {
        SharedPreferences sp=getSharedPreferences("station_common_space");
        String clipString=sp.getString("clip",null);
        List<ClipBoardModel> localClips=new ArrayList<>();
        if(clipString!=null){
            TypeToken<List<ClipBoardModel>> typeToken=new TypeToken<List<ClipBoardModel>>(){};
            List<ClipBoardModel> tmp=new Gson().fromJson(clipString,typeToken.getType());
            localClips.addAll(tmp);
        }
        ClipBoardModel findModel=null;
        for(ClipBoardModel model:localClips){
            if(model!=null&&model.getStationModel()!=null){
                if(model.getStationModel().getStationId()==stationModel.getStationId()){
                    findModel=model;
                    break;
                }
            }
        }
        if(findModel!=null) return true;
        return false;
    }


    @Override
    public void setActionBarAlpha(float alpha){
        floatActionBar.setAlpha(alpha);
    }

    @Override
    public void setActionBarColor(String color){
        actionbarLayout.setBackgroundColor(Color.parseColor(color));
        floatActionBar.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public void setActionTextColor(String color) {
        titleTextView.setTextColor(Color.parseColor(color));
        titleTextView2.setTextColor(Color.parseColor(color));
    }
}
