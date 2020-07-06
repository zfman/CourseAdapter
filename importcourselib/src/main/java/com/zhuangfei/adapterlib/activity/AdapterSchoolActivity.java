package com.zhuangfei.adapterlib.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuangfei.adapterlib.ParseManager;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.ShareManager;
import com.zhuangfei.adapterlib.StatManager;
import com.zhuangfei.adapterlib.activity.scan.ScanImportActivity;
import com.zhuangfei.adapterlib.apis.model.ValuePair;
import com.zhuangfei.adapterlib.callback.IAdapterOperator;
import com.zhuangfei.adapterlib.callback.OnValueCallback;
import com.zhuangfei.adapterlib.utils.ViewUtils;
import com.zhuangfei.adapterlib.core.IArea;
import com.zhuangfei.adapterlib.core.JsSupport;
import com.zhuangfei.adapterlib.core.ParseResult;
import com.zhuangfei.adapterlib.core.SpecialArea;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 适配学校页面
 */
public class AdapterSchoolActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";
    // wenview与加载条
    WebView webView;

    // 标题
    TextView titleTextView;
    TextView displayTextView;

    //右上角图标
    ImageView popmenuImageView;

    //加载进度
    ContentLoadingProgressBar loadingProgressBar;
    LinearLayout noMatchesLayout;
    LinearLayout btnGroupLayout;

    // 解析课程相关
    JsSupport jsSupport;
    SpecialArea specialArea;
    String html = "";
    String url, school, js, type;

    //标记按钮是否已经被点击过
    //解析按钮如果点击一次，就不需要再去获取html了，直接解析
    boolean isButtonClicked=false;

    public static final String EXTRA_URL="url";
    public static final String EXTRA_SCHOOL="school";
    public static final String EXTRA_PARSEJS="parsejs";
    public static final String EXTRA_TYPE="type";

    public int nowIndex=0;
    TextView tv;

    public static String htmlCode=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_adapter_school);
        ViewUtils.setStatusTextGrayColor(this);
        //init area
        initView();
        initUrl();
        loadWebView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(htmlCode!=null){
            ShareManager.getValue(this, htmlCode, new OnValueCallback() {
                @Override
                public void onSuccess(ValuePair pair) {
                    htmlCode=null;
                    html=pair.getValue();
                    jsSupport.parseHtml(context(),js);
                }
            });
        }
    }

    private void initView() {
        webView=findViewById(R.id.id_webview);
        titleTextView=findViewById(R.id.id_web_title);
        popmenuImageView=findViewById(R.id.id_webview_help);
        loadingProgressBar=findViewById(R.id.id_loadingbar);
        displayTextView=findViewById(R.id.tv_display);
        noMatchesLayout=findViewById(R.id.ll_no_matches);
        btnGroupLayout=findViewById(R.id.ll_btn_group);

        findViewById(R.id.id_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv= findViewById(R.id.tv_webview_parse);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnClicked();
            }
        });
        findViewById(R.id.id_webview_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopMenu();
            }
        });
    }

    /**
     * 获取参数
     */
    private void initUrl() {
        url = getIntent().getStringExtra(EXTRA_URL);
        school = getIntent().getStringExtra(EXTRA_SCHOOL);
        js = getIntent().getStringExtra(EXTRA_PARSEJS);
        type = getIntent().getStringExtra(EXTRA_TYPE);

        if(TextUtils.isEmpty(url)){
            url="http://www.liuzhuangfei.com";
        }
        if(TextUtils.isEmpty(school)){
            school="WebView";
        }
        if(TextUtils.isEmpty(js)){
            Toast.makeText(this,"js is null,结果不可预期",Toast.LENGTH_SHORT).show();
            finish();
        }

        titleTextView.setText(school);
        if(school.indexOf("河南理工")!=-1){
            setUA(true);
        }else {
            setUA(false);
        }


        if(school.equals("河南理工大学")){
            AlertDialog.Builder builder=new AlertDialog.Builder(this)
                    .setTitle("Html导入")
                    .setCancelable(false)
                    .setMessage("由于河南理工大学新教务系统需要使用有内核的浏览器才可以登陆，所以需要配合电脑导入，使用电脑打开以下网址：\n http://liuzhuangfei.com/import.html\n然后点击扫码按钮进行导入")
                    .setPositiveButton("扫码", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkReadPermission();
                        }
                    }).setNegativeButton("取消",null);
            builder.create().show();
        }
    }

    private void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this,
                Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent=new Intent(getContext(), ScanImportActivity.class);
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.VIBRATE},
                    10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请允许相机权限后再试", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent=new Intent(getContext(), ScanImportActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 核心方法:设置WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView() {
        jsSupport = new JsSupport(webView);
        specialArea = new SpecialArea(this, new MyCallback());
        jsSupport.applyConfig(this, new MyWebViewCallback());
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setTextZoom(100);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        String ua=webView.getSettings().getUserAgentString();
        ua=ua.replace("Mobile","eliboM");
        ua=ua.replace("Android","ndroidA");

        webView.getSettings().setUserAgentString(ua);
//        if(isNanjingArtSchool){
//            webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
//        }
        webView.addJavascriptInterface(specialArea, "sa");
        webView.loadUrl(url);

        Map<String,String> params=new HashMap<>();
        params.put("url",url);
        StatManager.sendKVEvent(getContext(),"pf_jwdr_jzwy",params);
    }

    public Context getContext(){
        return this;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else finish();
    }

    class MyWebViewCallback implements IArea.WebViewCallback {

        @Override
        public void onProgressChanged(final int newProgress) {
            //进度更新
            loadingProgressBar.setProgress(newProgress);
            if(newProgress>0&&newProgress!=100){
                displayTextView.setText("页面加载中 "+newProgress+"%...");
            }
            if (newProgress == 100) {
                jsSupport.getPageHtmlForAdjust("sa");
                loadingProgressBar.hide();
            }
            else loadingProgressBar.show();
        }
    }

    class MyCallback implements IArea.Callback {

        @Override
        public void onNotFindTag() {
            recordFailImport();
            onError("Tag标签未设置");
            finish();
        }

        @Override
        public void onFindTags(final String[] tags) {

            displayTextView.setText("预测:选择解析标签");
            AlertDialog.Builder builder = new AlertDialog.Builder(context());
            builder.setTitle("请选择解析标签");
            builder.setCancelable(false);
            builder.setItems(tags, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    jsSupport.callJs("parse('" + tags[i] + "')");
                    displayTextView.setText("预测:解析 "+tags[i]);
                }
            });
            builder.create().show();
        }

        @Override
        public void onNotFindResult() {
            onError("未发现匹配");
            finish();
        }

        @Override
        public void onFindResult(List<ParseResult> result) {
            saveSchedule(result);
        }

        @Override
        public void onError(String msg) {
            Toast.makeText(context(), msg,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onInfo(String msg) {
            Toast.makeText(context(), msg,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWarning(String msg) {
            Toast.makeText(context(), msg,Toast.LENGTH_SHORT).show();
        }

        @Override
        public String getHtml() {
            return html;
        }

        @Override
        public void showHtml(String content) {
            if (TextUtils.isEmpty(content)) {
                onError("showHtml:is Null");
            }
            html = content;
            jsSupport.parseHtml(context(),js);
        }

        @Override
        public void showHtmlForAdjust(final String html) {
            if(TextUtils.isEmpty(html)){
                displayTextView.setText("实时分析失败");
                return;
            }
            displayTextView.setText("实时分析中...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message=new Message();
                    message.what=nowIndex;
                    if(html.indexOf("湖南青果软件有限公司")!=-1){
                        message.obj="预测:湖南青果教务";
                        handler.sendMessage(message);
                        return;
                    }
                    if(html.indexOf("正方软件股份有限公司")!=-1&&html.indexOf("杭州西湖区")!=-1){
                        message.obj="预测:新正方教务";
                        handler.sendMessage(message);
                        return;
                    }
                    if(html.indexOf("正方软件股份有限公司")!=-1){
                        message.obj="预测:正方教务";
                        handler.sendMessage(message);
                        return;
                    }
                    if(html.indexOf("displayTag")!=-1||html.indexOf("URP")!=-1){
                        message.obj="预测:URP教务";
                        handler.sendMessage(message);
                        return;
                    }
                    if(html.indexOf("金智")!=-1){
                        message.obj="预测:金智教务";
                        handler.sendMessage(message);
                        return;
                    }
                    if(html.indexOf("金睿")!=-1){
                        message.obj="预测:金睿教务";
                        handler.sendMessage(message);
                        return;
                    }
                    if(html.indexOf("优慕课")!=-1){
                        message.obj="预测:优慕课";
                        handler.sendMessage(message);
                        return;
                    }
                    if(html.indexOf("强智")!=-1){
                        message.obj="预测:强智教务";
                        handler.sendMessage(message);
                        return;
                    }
                    if(html.indexOf("星期一")!=-1&&html.indexOf("星期二")!=-1){
                        message.obj="预测:教务类型未知";
                        handler.sendMessage(message);
                        return;
                    }
                    if(html.indexOf("周一")!=-1&&html.indexOf("周二")!=-1){
                        message.obj="预测:教务类型未知";
                        handler.sendMessage(message);
                        return;
                    }
                    message.obj="预测:未到达课表页面";
                    handler.sendMessage(message);

                }
            }).start();
        }
    }

    private StringBuilder ycStringBuilder=new StringBuilder();

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what=msg.what;
            String content=msg.obj.toString();
            if(what>=nowIndex&&!TextUtils.isEmpty(content)){
                ycStringBuilder.append(content);
                displayTextView.setText(content);
                nowIndex=what;
            }
        }
    };

    public Context context() {
        return AdapterSchoolActivity.this;
    }

    public void saveSchedule(List<ParseResult> data) {
        if (data == null) {
            recordFailImport();
            finish();
            return;
        }
        Map<String,String> params=new HashMap<>();
        params.put("size",""+data.size());
        params.put("school",school);
        params.put("success","1");
        StatManager.sendKVEvent(getContext(),"pf_jwdr_success",params);

        //todo save
        ParseManager.setSuccess(true);
        ParseManager.setTimestamp(System.currentTimeMillis());
        ParseManager.setData(data);
        finish();
    }

    private void recordFailImport(){
        String yc=ycStringBuilder.toString();
        Map<String,String> params=new HashMap<>();
        params.put("size","0");
        params.put("school",school);
        params.put("success","0");
        params.put("yc",yc);
        StatManager.sendKVEvent(getContext(),"pf_jwdr_success",params);
    }

    public void onBtnClicked() {
        StatManager.sendKVEvent(getContext(),"pf_jwdr_jxkc",null);
        if(!isButtonClicked){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("重要内容!")
                    .setMessage("1.请在你看到课表后再点击此按钮\n\n2.URP教务登陆后可能会出现点击无反应的问题，在右上角选择URP-兼容模式\n\n3.解析失败请加qq群反馈:684993074")
                    .setPositiveButton("解析课表", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isButtonClicked=true;
                            jsSupport.getPageHtml("sa");
                        }
                    })
                    .setNegativeButton("稍后解析", null);
            builder.create().show();
        }else{

            jsSupport.parseHtml(context(),js);
        }
    }

    public void showPopMenu() {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(this, popmenuImageView);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.adapter_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.id_menu1){
                    StatManager.sendKVEvent(getContext(),"pf_jwdr_wzdhq",null);
                    Intent intent=new Intent(AdapterSchoolActivity.this,AdapterSameTypeActivity.class);
                    intent.putExtra(AdapterSameTypeActivity.EXTRA_TYPE,type);
                    intent.putExtra(AdapterSameTypeActivity.EXTRA_JS,js);
                    startActivity(intent);
                    finish();
                }
                if(item.getItemId()==R.id.id_menu3){
                    setUA(false);
                    webView.reload();
                }

                if(item.getItemId()==R.id.id_menu4){
                    setUA(true);
                    webView.reload();
                }
                if(item.getItemId()==R.id.id_menu6){
                    StatManager.sendKVEvent(getContext(),"pf_jwdr_fk",null);
                }
                return false;
            }
        });
        popup.show();
    }

    public void setUA(boolean mobile){
        if(mobile){
            StatManager.sendKVEvent(getContext(),"pf_jwdr_mobileua",null);
            webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 7.1.1; Mi Note 3 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36");
        }else{
            StatManager.sendKVEvent(getContext(),"pf_jwdr_pcua",null);
            webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webView!=null){
            webView.destroy();
        }
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
