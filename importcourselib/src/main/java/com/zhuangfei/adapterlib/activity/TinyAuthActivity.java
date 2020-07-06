package com.zhuangfei.adapterlib.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.AuthAgent;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.RecordEventManager;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.BaseResult;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.station.TinyUserManager;
import com.zhuangfei.adapterlib.station.UserManager;
import com.zhuangfei.adapterlib.station.model.TinyUserInfo;
import com.zhuangfei.adapterlib.thirdlogin.ThirdLoginContants;
import com.zhuangfei.adapterlib.uikit.NetworkUtil;
import com.zhuangfei.adapterlib.utils.GsonUtils;
import com.zhuangfei.adapterlib.utils.ViewUtils;
import com.zhuangfei.toolkit.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录页面
 */
public class TinyAuthActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
    Button registerButton;

    EditText userName;
    EditText userPassword;
    LinearLayout loadingLayout;
    LinearLayout passwordLayout2;
    EditText userPassword2;
    LinearLayout qqLoginLayout;
    LinearLayout wechatLoginLayout;
    LinearLayout gsLoginLayout;
    LinearLayout gsLoginContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.setTransparent(this);
        setContentView(R.layout.activity_tiny_login);
        initView();
        initEvent();
    }

    private static String getcode (String str) {
        String[] encodelist ={"GB2312","ISO-8859-1","UTF-8","GBK","Big5","UTF-16LE","Shift_JIS","EUC-JP"};
        for(int i =0;i<encodelist.length;i++){
            try {
                if (str.equals(new String(str.getBytes(encodelist[i]),encodelist[i]))) {
                    return encodelist[i];
                }
            } catch (Exception e) {

            } finally{

            }
        } return "";
    }

    public void setLoadingViewVisible(boolean viewVisible){
        if(!viewVisible){
            loadingLayout.setVisibility(View.GONE);
        }else{
            loadingLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initEvent() {
        loginButton.setOnClickListener(this);
    }

    private void initView() {
        loginButton = (Button) findViewById(R.id.login);
        userName = (EditText) findViewById(R.id.user_name);
        userPassword = (EditText) findViewById(R.id.user_password);
        loadingLayout=findViewById(R.id.id_loadlayout);
        registerButton=findViewById(R.id.register);
        passwordLayout2=findViewById(R.id.ll_password2);
        userPassword2=findViewById(R.id.user_password2);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        qqLoginLayout=findViewById(R.id.ll_qq_login);
        wechatLoginLayout=findViewById(R.id.ll_wechat_login);
        gsLoginLayout=findViewById(R.id.ll_gs_login);
        gsLoginContainer=findViewById(R.id.ll_gs_container);
        qqLoginLayout.setOnClickListener(this);
        wechatLoginLayout.setOnClickListener(this);
        gsLoginLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        if(arg0.getId()==R.id.register){
            if(loginButton.getVisibility()==View.VISIBLE){
                loginButton.setVisibility(View.GONE);
                passwordLayout2.setVisibility(View.VISIBLE);
                registerButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_style));
            }else{
                login(false);
            }
        }
        if(arg0.getId()==R.id.ll_gs_login){
            gsLoginContainer.setVisibility(View.VISIBLE);
        }
    }

    public Context getContext(){
        return this;
    }

    /**
     * 登录请求服务器
     */
    private void login(boolean isLogin) {
        try{
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }catch (Exception e){}

        final String name = userName.getText().toString();
        final String pw = userPassword.getText().toString();
        final String pw2 = userPassword2.getText().toString();
        if (name.isEmpty() || pw.isEmpty()) {
            if(isLogin){
                Toast.makeText(this, "请输入账号或密码以登录",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "请输入账号或密码以注册",Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if(isLogin){
            loadingLayout.setVisibility(View.VISIBLE);
            login(name,pw,"1");
        }
        else{
            if(TextUtils.isEmpty(pw2)||!pw2.equals(pw)){
                Toast.makeText(this, "两次输入的密码不相同！",Toast.LENGTH_SHORT).show();
                return;
            }
            if(name.trim().length()<5||pw.trim().length()<6){
                Toast.makeText(this, "账号不得少于5个字符，密码不得少于6个字符",Toast.LENGTH_SHORT).show();
            }else{
                loadingLayout.setVisibility(View.VISIBLE);
                register(name,pw);
            }
        }
    }

    public void login(String name,String pwd,String type){
        login(name,pwd,type,null,null,null,null,null,null);
    }
    public void login(String name,String pwd,String type,String openid){
        login(name,pwd,type,openid,null,null,null,null,null);
    }

    public void login(String name,String pw,String type,String openid,String gender,String province,String city,String year,String figureUrl){
        TimetableRequest.loginUser(getContext(), name, pw, type,openid,gender,province,city,year,figureUrl,new Callback<ObjResult<TinyUserInfo>>() {
            @Override
            public void onResponse(Call<ObjResult<TinyUserInfo>> call, Response<ObjResult<TinyUserInfo>> response) {
                loadingLayout.setVisibility(View.GONE);
                ObjResult<TinyUserInfo> result=response.body();
                if(result!=null){
                    if(result.getCode()==200){
                        Toast.makeText(getContext(),"登录成功",Toast.LENGTH_SHORT).show();
                        TinyUserManager.get(getContext()).saveUserInfo(result.getData());
                        finish();
                    }else if(result.getCode()==338){

                    }else{
                        Toast.makeText(getContext(),result.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Error:result is null",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObjResult<TinyUserInfo>> call, Throwable t) {
                loadingLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Error:"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void sendCode(Context context) {
//        RegisterPage page = new RegisterPage();
//        //如果使用我们的ui，没有申请模板编号的情况下需传null
//        page.setTempCode(null);
//        page.setRegisterCallback(new EventHandler() {
//            public void afterEvent(int event, int result, Object data) {
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    // 处理成功的结果
//                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                    // 国家代码，如“86”
//                    String country = (String) phoneMap.get("country");
//                    // 手机号码，如“13800138000”
//                    String phone = (String) phoneMap.get("phone");
//                    // TODO 利用国家代码和手机号码进行后续的操作
//                    ToastTools.show(getContext(), GsonUtils.getGson().toJson(phoneMap));
//                } else{
//                    // TODO 处理错误的结果
//                }
//            }
//        });
//        page.show(context);
//    }

    public void register(final String name, final String pw){
        TimetableRequest.registerUser(getContext(), name, pw, new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                loadingLayout.setVisibility(View.GONE);
                BaseResult result=response.body();
                if(result!=null){
                    if(result.getCode()==200){
                        Toast.makeText(getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                        login(name,pw,"1");
                    }else{
                        Toast.makeText(getContext(),result.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Error:result is null",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                loadingLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Error:"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void submitPrivacyGrantResult(boolean granted) {
//        MobSDK.submitPolicyGrantResult(granted, new OperationCallback<Void>() {
//            @Override
//            public void onComplete(Void data) {
//                sendCode(getContext());
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ToastTools.show(getContext(),"error:"+t.getMessage());
//            }
//        });
//    }
}