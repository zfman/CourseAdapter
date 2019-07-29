package com.zhuangfei.adapterlib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.once.OnceManager;
import com.zhuangfei.adapterlib.once.OnceRoute;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class OnceActivity extends AppCompatActivity {

    private static final String TAG = "StationWebViewActivity";
    LinearLayout layout;
    OnceManager manager = new OnceManager();

    LinearLayout importSchedulesLayout;
    EditText codeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ViewUtils.setStatusBarColor(this, Color.BLUE);
        setContentView(R.layout.activity_once);
        layout = findViewById(R.id.layout);
        importSchedulesLayout = findViewById(R.id.id_once_schedules);
        codeEdit = findViewById(R.id.id_code_edit);
        importSchedulesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOnceOperator();
            }
        });
        findViewById(R.id.id_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = codeEdit.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(getContext(), "请填入验证码", Toast.LENGTH_SHORT).show();
                } else {
                    manager.inputVerifyCode(code);
                }
            }
        });
        findViewById(R.id.id_once_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNumberManager();
            }
        });
        manager.readyInits(this);

    }

    private void toNumberManager() {
        Intent intent = new Intent(this, OnceNumberActivity.class);
        startActivity(intent);
    }

    public Context getContext() {
        return this;
    }

    public void onOnceOperator() {
        manager.clearAllCookie();
        OnceRoute route1 = new OnceRoute();
        route1.setUrl("https://vpn.hpu.edu.cn/por/login_psw.csp?rnd=0.037596503214589294#https%3A%2F%2Fvpn.hpu.edu.cn%2F");
        String javascript1 = "document.getElementById(\"user\").value=\"{number1}\";" +
                "document.getElementById(\"pwd\").value=\"{password1}\";" +
                "document.getElementById(\"login_form\").submit();";
        route1.setJs(javascript1);
        route1.setNeedVerifyCode(false);
        route1.setRegex("https://vpn.hpu.edu.cn/por/login_psw.csp\\?rnd=0\\.\\d{18}#https%3A%2F%2Fvpn.hpu.edu.cn%2F");

        OnceRoute route2 = new OnceRoute();
        route2.setUrl("https://vpn.hpu.edu.cn/web/1/http/0/218.196.240.97:80/");
        String javascript2 = "var vchart=document.getElementById('vchart');\n" +
                "window.source.onGetImageSrc(vchart.src);\n" +
                "var oinput=document.getElementsByTagName('input');\n" +
                "oinput[8].value=\"{password2}\";\n" +
                "oinput[7].value=\"{number2}\";\n" +
                "alert(\"hide://\");\n";
        route2.setJs(javascript2);
        route2.setNeedVerifyCode(true);
        route2.setRegex("https://vpn.hpu.edu.cn/web/1/http/0/218.196.240.97:80/");
        String codeJs = "var oinput=document.getElementsByTagName('input');\n" +
                "oinput[9].value=\"{code}\";\n" +
                "var loginForm=document.getElementsByName('loginForm')[0];" +
                "alert(\"form:\"+loginForm);" +
                "loginForm.submit();" +
                "alert(\"hide://\");";
        route2.setVerifyCodeJs(codeJs);

        OnceRoute route3 = new OnceRoute();
        route3.setUrl("https://vpn.hpu.edu.cn/web/1/http/2/218.196.240.97/xkAction.do?actionType=6");
        route3.setRegex("https://vpn.hpu.edu.cn/web/1/http/2/218.196.240.97/xkAction.do?actionType=6");

        List<OnceRoute> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        manager.getSchedules(this, routes, null, new OnceManager.OnOnceResultCallback() {
            @Override
            public void urlLoading(String url) {

            }

            @Override
            public void onProgressChanged(int newProgress) {

            }

            @Override
            public void callback(String html) {

            }

            @Override
            public void needInputIdentifyCode(String source) {
//                Toast.makeText(getContext(),"source:"+source,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInitFinished() {
                layout.removeAllViews();
                layout.addView(manager.getWebView());
            }
        });
    }
}
