package com.zhuangfei.adapterlibdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zhuangfei.adapterlib.ParseManager;
import com.zhuangfei.adapterlib.core.ParseResult;
import com.zhuangfei.adapterlib.activity.SearchSchoolActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.id_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SearchSchoolActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE&&resultCode==SearchSchoolActivity.RESULT_CODE){
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
