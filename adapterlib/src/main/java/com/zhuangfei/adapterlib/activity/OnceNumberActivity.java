package com.zhuangfei.adapterlib.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.once.OnceManager;
import com.zhuangfei.adapterlib.once.local.OnceUser;
import com.zhuangfei.adapterlib.once.local.OnceUserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnceNumberActivity extends AppCompatActivity {

    ListView listView;
    List<Map<String,String>> items;
    SimpleAdapter simpleAdapter;

    OnceUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_once_number);

        listView=findViewById(R.id.id_listview);
        items=new ArrayList<>();
        simpleAdapter=new SimpleAdapter(this,items,R.layout.item_once_users,
                new String[]{"number","text"},
                new int[]{R.id.item_number,R.id.item_text});
        listView.setAdapter(simpleAdapter);

        findViewById(R.id.id_add_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddUser();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String number=items.get(i).get("number");
                String[] items={"设置为当前账号","删除该账号"};
                AlertDialog.Builder builder=new AlertDialog.Builder(OnceNumberActivity.this)
                        .setTitle("操作")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:
                                        userManager.applyUser(number);
                                        Toast.makeText(OnceNumberActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                                        showUsers();
                                        break;
                                    case 1:
                                        userManager.deleteUser(number);
                                        Toast.makeText(OnceNumberActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                                        showUsers();
                                        break;
                                }
                            }
                        });
                builder.create().show();
            }
        });
        userManager=new OnceUserManager(this);
        showUsers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showUsers();
    }

    private void toAddUser() {
        Intent intent=new Intent(this,AddUserActivity.class);
        startActivity(intent);
    }

    public void showUsers(){
        items.clear();
        List<OnceUser> userList=userManager.listUsers();
        if(userList!=null){
            int i=0;
            for(OnceUser u:userList){
                Map<String,String> map=new HashMap<>();
                if(u!=null){
                    if(i==0){
                        map.put("number",u.getNumber());
                        map.put("text","当前账号");
                    }else{
                        map.put("number",u.getNumber());
                        map.put("text","");
                    }
                    items.add(map);
                }
                i++;
            }
        }
        simpleAdapter.notifyDataSetChanged();
    }
}
