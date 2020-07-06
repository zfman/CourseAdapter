package com.zhuangfei.adapterlib.activity.qingguo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.core.AssetTools;
import com.zhuangfei.adapterlib.station.model.GreenFruitSchool;
import java.util.ArrayList;
import java.util.List;

public class ChooseSchoolActivity extends AppCompatActivity {
    ListView listView;
    List<String> list;
    List<GreenFruitSchool> schoolList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_school);

        list=new ArrayList<>();
        schoolList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView=findViewById(R.id.id_choose_school_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GreenFruitSchool school=findSchool(list.get(i));
                if(school!=null){

                }
            }
        });

        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        EditText editText=findViewById(R.id.id_search_edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s=charSequence.toString();
                if(s==null||s.length()==0){
                    if(schoolList!=null){
                        list.clear();
                        for(GreenFruitSchool school:schoolList){
                            list.add(school.getXxmc());
                        }
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    if(schoolList!=null){
                        list.clear();
                        for(GreenFruitSchool school:schoolList){
                            if(school.getXxmc().indexOf(s)!=-1){
                                list.add(school.getXxmc());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loadSchools();
    }

    public GreenFruitSchool findSchool(String xxmc){
        if(schoolList==null) return null;
        for(GreenFruitSchool school:schoolList){
            if(school.getXxmc().equals(xxmc)){
                return school;
            }
        }
        return null;
    }

    public Context getContext(){
        return this;
    }

    private void loadSchools() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String schoolStr= AssetTools.readAssetFile(getContext(),"schools.txt");
                TypeToken<List<GreenFruitSchool>> typeToken=new TypeToken<List<GreenFruitSchool>>(){};
                List<GreenFruitSchool> school=new Gson().fromJson(schoolStr,typeToken.getType());
                Message message=new Message();
                message.obj=school;
                message.what=0x123;
                handler.sendMessage(message);
            }
        }).start();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj!=null){
                schoolList= (List<GreenFruitSchool>) msg.obj;
                if(schoolList!=null){
                    list.clear();
                    for(GreenFruitSchool school:schoolList){
                        list.add(school.getXxmc());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void goBack(){
        finish();
    }
}
