package com.zhuangfei.adapterlib.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.utils.ViewUtils;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.CheckModel;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterTipActivity extends AppCompatActivity {

    public EditText schoolEdit;
    public EditText urlEdit;
    TextView nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_tip);
        ViewUtils.setStatusTextGrayColor(this);
        inits();

    }

    private void inits() {
        schoolEdit=findViewById(R.id.id_school_edittext);
        urlEdit=findViewById(R.id.id_url_edittext);
        nameText=findViewById(R.id.tv_name);

        findViewById(R.id.tv_to_adapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdapterBtnClicked();
            }
        });
        findViewById(R.id.tv_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNameTextClicked();
            }
        });
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        schoolEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence!=null&&charSequence.length()>=2){
                    check(charSequence.toString());
                }else{
                    nameText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onAdapterBtnClicked() {
        final String school = schoolEdit.getText().toString();
        final String url = urlEdit.getText().toString();
        if (TextUtils.isEmpty(school) || TextUtils.isEmpty(url)) {
            Toast.makeText(this, "不允许为空，请填充完整!",Toast.LENGTH_SHORT).show();
        } else if(!url.startsWith("http://")&&!url.startsWith("https://")){
            Toast.makeText(this, "请填写正确的url，以http://或https://开头",Toast.LENGTH_SHORT).show();
            return;
        }else{
            if (!school.endsWith("学校")&&!school.endsWith("学院")&&!school.endsWith("大学")) {
                AlertDialog.Builder builder=new AlertDialog.Builder(this)
                        .setTitle("校名不太对哟")
                        .setMessage("你的校名好像不太对哟，务必填写全称,若校名不全，本次申请将被忽略!")
                        .setPositiveButton("确定是对的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                toUploadHtmlActivity(url,school);
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.create().show();
            } else{
                toUploadHtmlActivity(url,school);
            }
        }
    }

    public void toUploadHtmlActivity(String url,String school){
        Intent intent=new Intent(this,UploadHtmlActivity.class);
        intent.putExtra(UploadHtmlActivity.EXTRA_URL,url);
        intent.putExtra(UploadHtmlActivity.EXTRA_SCHOOL,school);
        startActivity(intent);
        finish();
    }

    public void check(String school) {
        TimetableRequest.checkSchool(this, school, new Callback<ObjResult<CheckModel>>() {
            @Override
            public void onResponse(Call<ObjResult<CheckModel>> call, Response<ObjResult<CheckModel>> response) {
                ObjResult<CheckModel> result=response.body();
                if(result==null){
                    Toast.makeText(AdapterTipActivity.this,"result is null",Toast.LENGTH_SHORT).show();
                }else if(result.getCode()!=200){
                    Toast.makeText(AdapterTipActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                }else{
                    CheckModel model=result.getData();
                    if(model!=null){
                        if(model.getHave()==1&&!TextUtils.isEmpty(model.getUrl())&&!TextUtils.isEmpty(model.getName())){
                            urlEdit.setText(model.getUrl()==null?"":model.getUrl());
                            nameText.setVisibility(View.VISIBLE);
                            nameText.setText("推荐:"+model.getName());
                        }else{
                            nameText.setVisibility(View.INVISIBLE);
                            urlEdit.setText("");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ObjResult<CheckModel>> call, Throwable t) {
            }
        });
    }

    public void onNameTextClicked(){
        String val=nameText.getText().toString();
        if(val!=null&&val.length()>3){
            val=val.substring(3);
        }
        if(!TextUtils.isEmpty(val)) schoolEdit.setText(val);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
