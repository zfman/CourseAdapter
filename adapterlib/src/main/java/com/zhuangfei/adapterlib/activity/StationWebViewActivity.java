package com.zhuangfei.adapterlib.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuangfei.adapterlib.activity.custom.CustomPopWindow;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.utils.ScreenUtils;
import com.zhuangfei.adapterlib.station.StationManager;
import com.zhuangfei.adapterlib.utils.ViewUtils;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.ListResult;
import com.zhuangfei.adapterlib.apis.model.StationModel;
import com.zhuangfei.adapterlib.station.StationSdk;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 服务站加载引擎
 * 暂时不可用
 */
public class StationWebViewActivity extends AppCompatActivity {

    private static final String TAG = "StationWebViewActivity";

    // wenview与加载条
    WebView webView;

    // 标题
    TextView titleTextView;
    String url,title;

    ContentLoadingProgressBar loadingProgressBar;
    TextView functionButton;

    // 声明PopupWindow
    private CustomPopWindow popupWindow;

    StationModel stationModel;
    public static final String EXTRAS_STATION_MODEL="station_model_extras";

    LinearLayout rootLayout;
    List<StationModel> localStationModels;
    boolean haveLocal=false;
    int deleteId=-1;

    LinearLayout actionbarLayout;
    Map<String,String> configMap;

    ImageView moreImageView;
    ImageView closeImageView;
    LinearLayout buttonGroupLayout;
    View diverView;//分隔竖线

    int needUpdate=0;
    String[] textArray=null,linkArray=null;
    String tipText=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(R.layout.activity_station_web_view);
        initUrl();
        initView();
        loadWebView();
        findStationLocal();
        getStationById();
    }

    private void initUrl() {
        url=StationManager.getRealUrl(stationModel.getUrl());
        title=stationModel.getName();
        needUpdate=0;
    }

    private void initView() {
        webView=findViewById(R.id.id_webview);
        moreImageView=findViewById(R.id.iv_station_more);
        diverView=findViewById(R.id.id_station_diver);
        buttonGroupLayout=findViewById(R.id.id_station_buttongroup);
        closeImageView=findViewById(R.id.iv_station_close);
        actionbarLayout=findViewById(R.id.id_station_action_bg);
        rootLayout=findViewById(R.id.id_station_root);
        titleTextView=findViewById(R.id.id_web_title);
        loadingProgressBar=findViewById(R.id.id_loadingbar);
        functionButton=findViewById(R.id.id_btn_function);



        titleTextView.setText(title);
        if(configMap!=null&&!configMap.isEmpty()){
            try{
                actionbarLayout.setBackgroundColor(Color.parseColor(configMap.get("actionColor")));
            }catch (Exception e){}

            try{
                int textcolor=Color.parseColor(configMap.get("actionTextColor"));
                titleTextView.setTextColor(textcolor);
                moreImageView.setColorFilter(textcolor);
                closeImageView.setColorFilter(textcolor);
                GradientDrawable gd=new GradientDrawable();
                gd.setCornerRadius(ScreenUtils.dip2px(this,25));
                gd.setStroke(2,textcolor);
                diverView.setBackgroundColor(textcolor);
                buttonGroupLayout.setBackgroundDrawable(gd);
            }catch (Exception e){}
        }

        functionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClicked();
            }
        });

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
    }

    private void beforeSetContentView() {
        stationModel= (StationModel) getIntent().getSerializableExtra(EXTRAS_STATION_MODEL);
        if(stationModel==null){
            Toast.makeText(this,"传参异常",Toast.LENGTH_SHORT).show();
            finish();
        }
        configMap= StationManager.getStationConfig(stationModel.getUrl());
        if(configMap!=null&&!configMap.isEmpty()){
            try{
                ViewUtils.setStatusBarColor(this, Color.parseColor(configMap.get("statusColor")));
            }catch (Exception e){}
        }
    }

    public void getStationById(){
        if(needUpdate==0) return;
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
        if (result == null||result.size()==0) return;
        final StationModel model=result.get(0);
        if(model!=null){
            boolean update=false;
            if(model.getName()!=null&&!model.getName().equals(stationModel.getName())){
                update=true;
            }
            if(model.getUrl()!=null&&!model.getUrl().equals(stationModel.getUrl())){
                update=true;
            }
            if(model.getImg()!=null&&!model.getImg().equals(stationModel.getImg())){
                update=true;
            }

            if(update){
//                final StationModel local=DataSupport.find(StationModel.class,stationModel.getId());
//                if(local!=null){
//                    local.setName(model.getName());
//                    local.setUrl(model.getUrl());
//                    local.setImg(model.getImg());
//                    local.update(stationModel.getId());
//                }
//
//                AlertDialog.Builder builder=new AlertDialog.Builder(this)
//                        .setTitle("服务站更新")
//                        .setMessage("本地保存的服务站已过期，需要重新加载")
//                        .setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //todo post reload event
//                                finish();
//                            }
//                        });
//                builder.create().show();
            }
        }
    }

    /**
     * 获取添加到首页的服务站
     */
    public void findStationLocal(){
//        FindMultiExecutor findMultiExecutor=DataSupport.findAllAsync(StationModel.class);
//        findMultiExecutor.listen(new FindMultiCallback() {
//            @Override
//            public <T> void onFinish(List<T> t) {
//                List<StationModel> stationModels= (List<StationModel>) t;
//                if(localStationModels==null){
//                    localStationModels=new ArrayList<>();
//                }
//                localStationModels.clear();
//                localStationModels.addAll(stationModels);
//                haveLocal=searchInList(localStationModels,stationModel.getStationId());
//            }
//        });
    }

    public boolean searchInList(List<StationModel> list,int stationId){
        if(list==null) return false;
        for(StationModel model:list){
            if(model.getStationId()==stationId){
                this.deleteId=model.getId();
                return true;
            }
        }
        return false;
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            if(v.getId()==R.id.pop_add_home){
                if(haveLocal){
                    Toast.makeText(StationWebViewActivity.this,"已从主页删除",Toast.LENGTH_SHORT).show();
                }else {
                    if(localStationModels.size()>=15){
                        Toast.makeText(StationWebViewActivity.this,"已达到最大数量限制15，请先删除其他服务站后尝试",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(StationWebViewActivity.this,"已添加到首页",Toast.LENGTH_SHORT).show();
                    }
                }
                findStationLocal();
            }

            if(v.getId()==R.id.pop_add_home){

            }

            if(v.getId()==R.id.pop_about){
                if(stationModel!=null&&stationModel.getOwner()!=null){
                    Toast.makeText(StationWebViewActivity.this,stationModel.getOwner(),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(StationWebViewActivity.this,"所有者未知!",Toast.LENGTH_SHORT).show();
                }
            }

            if(v.getId()==R.id.pop_to_home){
                webView.clearHistory();
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
        webView.addJavascriptInterface(new StationSdk(this,getStationSpace()), "sdk");

//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(true);

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

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading: "+url);
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
                loadingProgressBar.setProgress(newProgress);
                if(newProgress==100) loadingProgressBar.hide();
                else loadingProgressBar.show();
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
        if (webView!= null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView= null;
        }
        super.onDestroy();
    }

    public void setButtonSettings(String btnText,String[] textArray,String[] linkArray){
        if(TextUtils.isEmpty(btnText)) return;
        functionButton.setText(btnText);
        functionButton.setVisibility(View.VISIBLE);
        this.textArray=textArray;
        this.linkArray=linkArray;
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.anim_station_static,R.anim.anim_station_close_activity);
    }

    /**
     * 弹出popupWindow
     */
    public void showMorePopWindow() {
        popupWindow = new CustomPopWindow(StationWebViewActivity.this,haveLocal, itemsOnClick);
        popupWindow.showAtLocation(rootLayout,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.backgroundAlpha(StationWebViewActivity.this, 1f);
            }
        });
    }

    public void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public Context getStationContext(){
        return this;
    }

    public WebView getWebView(){
        return webView;
    }

    public String getStationSpace(){
        return "station_space_"+stationModel.getStationId();
    }

    public void setTitle(String title){
        titleTextView.setText(title);
    }

    public void onButtonClicked(){
        if(textArray==null||linkArray==null) return;
        if(textArray.length!=linkArray.length) return;
        AlertDialog.Builder builder=new AlertDialog.Builder(this)
                .setTitle("请选择功能")
                .setItems(textArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i<linkArray.length){
                            webView.loadUrl(linkArray[i]);
                        }
                        if(dialogInterface!=null){
                            dialogInterface.dismiss();
                        }
                    }
                });
        builder.create().show();
    }
}
