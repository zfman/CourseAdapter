package com.zhuangfei.adapterlib.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuangfei.adapterlib.ParseManager;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.station.StationSdk;
import com.zhuangfei.adapterlib.utils.ViewUtils;
import com.zhuangfei.adapterlib.core.IArea;
import com.zhuangfei.adapterlib.core.JsSupport;
import com.zhuangfei.adapterlib.core.ParseResult;
import com.zhuangfei.adapterlib.core.SpecialArea;
import java.util.List;

/**
 * 适配学校页面
 */
public class AdapterSchoolActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";
    // wenview与加载条
    WebView webView;

    // 关闭
    private LinearLayout closeLayout;

    // 标题
    TextView titleTextView;
    TextView displayTextView;

    //右上角图标
    ImageView popmenuImageView;

    //加载进度
    ContentLoadingProgressBar loadingProgressBar;

    // 解析课程相关
    JsSupport jsSupport;
    SpecialArea specialArea;
    String html = "";
    String url, school, js, type;

    //标记按钮是否已经被点击过
    //解析按钮如果点击一次，就不需要再去获取html了，直接解析
    boolean isButtonClicked=false;

    //选课结果
    public static final String URL_COURSE_RESULT="https://vpn.hpu.edu.cn/web/1/http/2/218.196.240.97/xkAction.do?actionType=6";

    public static final String EXTRA_URL="url";
    public static final String EXTRA_SCHOOL="school";
    public static final String EXTRA_PARSEJS="parsejs";
    public static final String EXTRA_TYPE="type";

    public int nowIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_school);
        ViewUtils.setStatusTextGrayColor(this);
        //init area
        initView();
        initUrl();
        loadWebView();
    }

    private void initView() {
        webView=findViewById(R.id.id_webview);
        titleTextView=findViewById(R.id.id_web_title);
        popmenuImageView=findViewById(R.id.id_webview_help);
        loadingProgressBar=findViewById(R.id.id_loadingbar);
        displayTextView=findViewById(R.id.tv_display);

        findViewById(R.id.id_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.tv_webview_parse).setOnClickListener(new View.OnClickListener() {
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
    }

    /**
     * 核心方法:设置WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView() {
        jsSupport = new JsSupport(webView);
        specialArea = new SpecialArea(this, new MyCallback());
        jsSupport.applyConfig(this, new MyWebViewCallback());
        webView.addJavascriptInterface(specialArea, "sa");

        webView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else finish();
    }

    class MyWebViewCallback implements IArea.WebViewCallback {

        @Override
        public void onProgressChanged(int newProgress) {
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

            //河南理工大学教务兼容性处理
            if (webView.getUrl().startsWith("https://vpn.hpu.edu.cn/web/1/http/1/218.196.240.97/loginAction.do")) {
                webView.loadUrl("https://vpn.hpu.edu.cn/web/1/http/2/218.196.240.97/xkAction.do?actionType=6");
            }
        }
    }

    class MyCallback implements IArea.Callback {

        @Override
        public void onNotFindTag() {
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

    public Context context() {
        return AdapterSchoolActivity.this;
    }

    public void saveSchedule(List<ParseResult> data) {
        if (data == null) {
            finish();
            return;
        }

        //todo save
        ParseManager.setSuccess(true);
        ParseManager.setTimestamp(System.currentTimeMillis());
        ParseManager.setData(data);
        finish();
    }

    public void onBtnClicked() {
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
        }else jsSupport.parseHtml(context(),js);
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
                    Intent intent=new Intent(AdapterSchoolActivity.this,AdapterSameTypeActivity.class);
                    intent.putExtra(AdapterSameTypeActivity.EXTRA_TYPE,type);
                    intent.putExtra(AdapterSameTypeActivity.EXTRA_JS,js);
                    startActivity(intent);
                    finish();
                }
                if(item.getItemId()==R.id.id_menu2){
                    String now=webView.getUrl();
                    if(now.indexOf("/")!=-1){
                        int index=now.lastIndexOf("/");
                        webView.loadUrl(now.substring(0,index)+"/xkAction.do?actionType=6");
                    }else{
                        webView.loadUrl(now+"/xkAction.do?actionType=6");
                    }
                }
                if(item.getItemId()==R.id.id_menu3){
                    webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
                    webView.reload();
                }

                if(item.getItemId()==R.id.id_menu4){
                    webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 7.1.1; Mi Note 3 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36");
                    webView.reload();
                }
                return false;
            }
        });
        popup.show();
    }
}
