package com.zhuangfei.adapterlib.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuangfei.adapterlib.ParseManager;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.StatManager;
import com.zhuangfei.adapterlib.utils.ViewUtils;

import java.util.HashMap;
import java.util.Map;

public class AdapterSameTypeActivity extends AppCompatActivity {

    public static final String STRING_EXTRA_NAME = "extra_name";
    public static final String INT_EXTRA_ID = "extra_id";

    EditText nameEdit;
    TextView titleTextView;
    ImageView backImageView;

    String js=null;
    int id = -1;

    public static final String EXTRA_JS="js";
    public static final String EXTRA_TYPE="type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_same_type);
        ViewUtils.setStatusTextGrayColor(this);
        inits();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ParseManager.isSuccess()){
            finish();
        }
    }

    private void inits() {
        nameEdit=findViewById(R.id.id_school_edittext);
        titleTextView=findViewById(R.id.id_title);
        findViewById(R.id.tv_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        js = getIntent().getStringExtra(EXTRA_JS);
        String type = getIntent().getStringExtra(EXTRA_TYPE);

        if(TextUtils.isEmpty(js)||TextUtils.isEmpty(type)){
            Toast.makeText(this,"js或者教务类型未知，结果不可预期！",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            titleTextView.setText(type);
        }
        backImageView=findViewById(R.id.id_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatManager.sendKVEvent(AdapterSameTypeActivity.this,"pf_jwdr_wzdhq_fh",null);
                finish();
            }
        });

    }

    public void save() {
        final String url = nameEdit.getText().toString();
        if (TextUtils.isEmpty(url)||(!url.startsWith("http")&&!url.startsWith("https"))) {
            Toast.makeText(this, "请输入有效的网址", Toast.LENGTH_SHORT).show();
        } else {
            StatManager.sendKVEvent(this,"pf_jwdr_wzdhq_goto",null);
            toAdapterActivity("自定义网址",url,js);
        }
    }

    public void toAdapterActivity(String school,String url,String js){
        Intent intent=new Intent(this,AdapterSchoolActivity.class);
        intent.putExtra(AdapterSchoolActivity.EXTRA_SCHOOL,school);
        intent.putExtra(AdapterSchoolActivity.EXTRA_URL,url);
        intent.putExtra(AdapterSchoolActivity.EXTRA_PARSEJS,js);
        intent.putExtra(AdapterSchoolActivity.EXTRA_TYPE,"同 "+school);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StatManager.sendKVEvent(this,"pf_jwdr_wzdhq_fh",null);
        finish();
    }
}
