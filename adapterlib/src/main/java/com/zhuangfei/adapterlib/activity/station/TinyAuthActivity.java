package com.zhuangfei.adapterlib.activity.station;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.BaseResult;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.station.TinyUserManager;
import com.zhuangfei.adapterlib.station.model.TinyUserInfo;
import com.zhuangfei.adapterlib.utils.ViewUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.setTransparent(this);
        setContentView(R.layout.activity_tiny_login);
        initView();
        initEvent();
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
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        if(arg0.getId()==R.id.login){
            login(true);
        }
        if(arg0.getId()==R.id.register){
            login(false);
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
        if (name.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "请先输入账号或密码",Toast.LENGTH_SHORT).show();
            return;
        }

        if(isLogin){
            loadingLayout.setVisibility(View.VISIBLE);
            login(name,pw);
        }
        else{
            if(name.trim().length()<5||pw.trim().length()<6){
                Toast.makeText(this, "账号不得少于5个字符，密码不得少于6个字符",Toast.LENGTH_SHORT).show();
            }else{
                loadingLayout.setVisibility(View.VISIBLE);
                register(name,pw);
            }
        }
    }

    public void login(String name,String pw){
        TimetableRequest.loginUser(getContext(), name, pw, new Callback<ObjResult<TinyUserInfo>>() {
            @Override
            public void onResponse(Call<ObjResult<TinyUserInfo>> call, Response<ObjResult<TinyUserInfo>> response) {
                loadingLayout.setVisibility(View.GONE);
                ObjResult<TinyUserInfo> result=response.body();
                if(result!=null){
                    if(result.getCode()==200){
                        Toast.makeText(getContext(),"登录成功",Toast.LENGTH_SHORT).show();
                        TinyUserManager.get(getContext()).saveUserInfo(result.getData());
                        finish();
                    }else{
                        Toast.makeText(getContext(),result.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Errot:result is null",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObjResult<TinyUserInfo>> call, Throwable t) {
                loadingLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Errot:"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void register(final String name, final String pw){
        TimetableRequest.registerUser(getContext(), name, pw, new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                loadingLayout.setVisibility(View.GONE);
                BaseResult result=response.body();
                if(result!=null){
                    if(result.getCode()==200){
                        Toast.makeText(getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                        login(name,pw);
                    }else{
                        Toast.makeText(getContext(),result.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Errot:result is null",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                loadingLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Errot:"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}