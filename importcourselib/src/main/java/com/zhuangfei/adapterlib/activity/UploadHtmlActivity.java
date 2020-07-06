package com.zhuangfei.adapterlib.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.AdapterLibManager;
import com.zhuangfei.adapterlib.utils.PackageUtils;
import com.zhuangfei.adapterlib.utils.ViewUtils;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.BaseResult;
import com.zhuangfei.adapterlib.core.IArea;
import com.zhuangfei.adapterlib.core.JsSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 源码上传页面
 * 内部增加了对河南理工大学的兼容，不需要的话可以忽略
 */
public class UploadHtmlActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";
    // wenview与加载条
    WebView webView;

    // 关闭
    private LinearLayout closeLayout;

    // 标题
    TextView titleTextView;
    String url, school;
    ImageView helpView;

    boolean isNeedLoad = false;

    //传入的参数
    public static final String EXTRA_URL="url";
    public static final String EXTRA_SCHOOL="school";

    JsSupport jsSupport;
    TextView displayTextView;
    public int nowIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_html);
        ViewUtils.setStatusTextGrayColor(this);
        initView();
        initUrl();
        loadWebView();
    }

    private void initView() {
        titleTextView=findViewById(R.id.id_web_title);
        helpView=findViewById(R.id.id_webview_help);
        webView=findViewById(R.id.id_webview);
        displayTextView=findViewById(R.id.tv_display);

        findViewById(R.id.id_webview_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopmenu();
            }
        });
        findViewById(R.id.tv_webview_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnClicked();
            }
        });
        findViewById(R.id.id_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initUrl() {
        url = getIntent().getStringExtra(EXTRA_URL);
        school = getIntent().getStringExtra(EXTRA_SCHOOL);
        if(TextUtils.isEmpty(url)){
            url="http://www.liuzhuangfei.com";
        }
        if(TextUtils.isEmpty(school)){
            school="WebView";
        }
        titleTextView.setText("适配-"+school);
    }

    /**
     * 显示弹出菜单
     */
    public void showPopmenu() {
        PopupMenu popup = new PopupMenu(this, helpView);
        popup.getMenuInflater().inflate(R.menu.menu_webview, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.top1){
                    String now=webView.getUrl();
                    if(now.indexOf("/")!=-1){
                        int index=now.lastIndexOf("/");
                        webView.loadUrl(now.substring(0,index)+"/xkAction.do?actionType=6");
                    }else{
                        webView.loadUrl(now+"/xkAction.do?actionType=6");
                    }
                }
                return true;
            }
        });

        popup.show();
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView() {
        jsSupport=new JsSupport(webView);
        jsSupport.applyConfig(this,new MyWebViewCallback());
        webView.addJavascriptInterface(new ShowSourceJs(), "source");
        String ua=webView.getSettings().getUserAgentString();
        ua=ua.replace("Mobile","eliboM");
        ua=ua.replace("Android","ndroidA");

        webView.getSettings().setUserAgentString(ua);
        webView.loadUrl(url);
    }

    class MyWebViewCallback implements IArea.WebViewCallback {

        @Override
        public void onProgressChanged(int newProgress) {
            if(newProgress>0&&newProgress!=100){
                displayTextView.setText("页面加载中 "+newProgress+"%...");
            }
            if (newProgress == 100) {
                jsSupport.getPageHtmlForAdjust("source");
            }
        }
    }

    public class ShowSourceJs {
        @JavascriptInterface
        public void showHtml(final String content) {
            if (TextUtils.isEmpty(content)) return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String finalContent="";
                    finalContent+="LibVersionName:"+ AdapterLibManager.getLibVersionName()+"<br/>";
                    finalContent+="LibVersionNumber:"+ AdapterLibManager.getLibVersionNumber()+"<br/>";
                    finalContent+="Package:"+ PackageUtils.getPackageName(UploadHtmlActivity.this)+"<br/>";
                    finalContent+="url:"+ webView.getUrl()+"<br/>";
                    finalContent+=content;
                    putHtml(finalContent);
                }
            });
        }

        @JavascriptInterface
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
                    if(html.indexOf("星期一")!=-1&&html.indexOf("星期二")!=-1&&html.indexOf("星期三")!=-1){
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

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what=msg.what;
            String content=msg.obj.toString();
            if(what>=nowIndex&&!TextUtils.isEmpty(content)){
                displayTextView.setText(content);
                nowIndex=what;
            }
        }
    };

    private void putHtml(String html) {
        TimetableRequest.putHtml(this, school, url, html, new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                BaseResult result=response.body();
                if(result!=null){
                    if(result.getCode()==200){
                        Toast.makeText(UploadHtmlActivity.this,"上传源码成功，请等待开发者适配!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UploadHtmlActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UploadHtmlActivity.this,"result is null!",Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                Toast.makeText(UploadHtmlActivity.this, t.getMessage(),Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void onBtnClicked() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this)
                .setTitle("重要内容!")
                .setMessage("1.请在你看到课表后再点击此按钮\n\n2.URP教务登陆后可能会出现点击无反应的问题，在右上角选择URP-兼容模式")
                .setPositiveButton("上传课表", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isNeedLoad = true;
                        jsSupport.getPageHtml("source");
                    }
                })
                .setNegativeButton("稍后上传", null);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()&&!isNeedLoad)
            webView.goBack();
        else finish();
    }
}
