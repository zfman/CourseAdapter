package com.zhuangfei.adapterlib.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.once.local.OnceUser;
import com.zhuangfei.adapterlib.once.local.OnceUserManager;

public class AddUserActivity extends AppCompatActivity {

    EditText numberEdit1;
    EditText passwordEdit1;
    EditText numberEdit2;
    EditText passwordEdit2;
    OnceUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        numberEdit1=findViewById(R.id.id_number1);
        passwordEdit1=findViewById(R.id.id_password1);
        numberEdit2=findViewById(R.id.id_number2);
        passwordEdit2=findViewById(R.id.id_password2);

        findViewById(R.id.id_save_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        userManager=new OnceUserManager(this);
    }

    public void save(){
        String number1=numberEdit1.getText().toString();
        String password1=passwordEdit1.getText().toString();
        String number2=numberEdit2.getText().toString();
        String password2=passwordEdit2.getText().toString();
        if(TextUtils.isEmpty(number1)||TextUtils.isEmpty(password1)){
            Toast.makeText(this,"学号1和密码1不允许为空",Toast.LENGTH_LONG).show();
            return;
        }

        OnceUser user=new OnceUser();
        user.setNumber(number1);
        user.setPassword(password1);
        user.setNumber2(number2);
        user.setPassword2(password2);
        boolean save=userManager.saveUser(user);
        if(save){
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this,"保存失败，账号重复",Toast.LENGTH_SHORT).show();
        }
    }
}
