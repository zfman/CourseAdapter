package com.zhuangfei.importcourse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.stat.StatMultiAccount;
import com.tencent.stat.StatService;
import com.zhuangfei.adapterlib.AdapterLibManager;
import com.zhuangfei.adapterlib.IStatSendCallback;
import com.zhuangfei.adapterlib.ParseManager;
import com.zhuangfei.adapterlib.StatManager;
import com.zhuangfei.adapterlib.activity.SearchSchoolActivity;
import com.zhuangfei.adapterlib.core.ParseResult;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        Button btn = (Button) findViewById(R.id.btn_goto_searchschool);

        Intent intent=getIntent();
        String appkey=intent.getStringExtra("appkey");
        if(!TextUtils.isEmpty(appkey)){
            Toast.makeText(this,"appkey="+appkey,Toast.LENGTH_SHORT).show();
        }
        AdapterLibManager.register("1a0dd2a43b83597e93961e42ab0e57b2","gstest");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SearchSchoolActivity.class);
                intent.putExtra(SearchSchoolActivity.EXTRA_SEARCH_KEY,"河南理工大学");
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==SearchSchoolActivity.RESULT_CODE){
            if(ParseManager.isSuccess()&&ParseManager.getData()!=null){
                List<ParseResult> result=ParseManager.getData();
                String str="";
                for(ParseResult item:result){
                    str+=item.getName()+"\n";
                }
                Toast.makeText(MainActivity.this,str, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
