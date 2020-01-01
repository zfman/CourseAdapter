package com.zhuangfei.adapterlibdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zhuangfei.adapterlib.activity.station.TinyAuthActivity;
import com.zhuangfei.adapterlib.callback.OnValueCallback;
import com.zhuangfei.adapterlib.ParseManager;
import com.zhuangfei.adapterlib.ShareManager;
import com.zhuangfei.adapterlib.apis.model.ValuePair;
import com.zhuangfei.adapterlib.core.ParseResult;
import com.zhuangfei.adapterlib.activity.school.SearchSchoolActivity;
import com.zhuangfei.adapterlib.station.StationManager;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE=1;
    Context context;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        layout=findViewById(R.id.layout);
        findViewById(R.id.id_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SearchSchoolActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        findViewById(R.id.id_search2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SearchSchoolActivity.class);
                intent.putExtra(SearchSchoolActivity.EXTRA_TINY_APP_ENABLE,true);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        findViewById(R.id.id_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse("https://github.com/zfman/CourseAdapter"));
                    context.startActivity(intent);
                }catch (Exception e){}
            }
        });

        findViewById(R.id.id_tiny_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), TinyAuthActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.sendEmptyMessage(0x123);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                StationManager.checkClip(MainActivity.this,null);
            }
        }
    };

    public Context getContext() {
         return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //导入
        ShareManager.getFromClip(this, new OnValueCallback() {
            @Override
            public void onSuccess(ValuePair pair) {
                List<ParseResult> result=ShareManager.getShareData(pair.getValue());
                showParseResult(result);
            }
        });
    }

    //展示数据
    private void showParseResult(List<ParseResult> result) {
        if(result==null) return;
        StringBuffer sb=new StringBuffer();
        for(ParseResult item:result){
            sb.append(item.getName()+"\n");
            sb.append("周"+item.getDay()+" "+item.getStart()+"-"+(item.getStep()+item.getStart()-1)+"节上\n");
            sb.append(item.getRoom()+" "+item.getTeacher()+"\n\n");
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setTitle("导入成功")
                .setCancelable(false)
                .setMessage(sb.toString())
                .setPositiveButton("知道了", null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE&&resultCode==SearchSchoolActivity.RESULT_CODE){
            if(ParseManager.isSuccess()&&ParseManager.getData()!=null){
                List<ParseResult> result=ParseManager.getData();
                String shareJson=ShareManager.getShareJson(result);
                ShareManager.showMsg(context,"导入成功，正在生成课表口令...");
                ShareManager.putValue(context, shareJson, new OnValueCallback() {
                    @Override
                    public void onSuccess(ValuePair pair) {
                        ShareManager.showMsg(context,ShareManager.getShareToken(pair));
                        ShareManager.shareTable(context,pair);
                    }
                });
            }
        }
    }
}
