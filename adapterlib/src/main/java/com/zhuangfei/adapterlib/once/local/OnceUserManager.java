package com.zhuangfei.adapterlib.once.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu ZhuangFei on 2019/4/30.
 */
public class OnceUserManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static final String SP_ONCE_SPACE="share_once_space";
    static final String KEY_ONCE_USERS="key_once_users";

    public OnceUserManager(Context context){
        sharedPreferences=context.getSharedPreferences(SP_ONCE_SPACE,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public boolean saveUser(OnceUser user){
        List<OnceUser> userList=listUsers();
        OnceUser findUser=findUser(userList,user.getNumber());
        if(findUser!=null){
            return false;
        }
        userList.add(user);
        updateUsers(userList);
        return true;
    }

    public void deleteUser(String number){
        List<OnceUser> userList=listUsers();
        OnceUser findUser=findUser(userList,number);
        if(findUser!=null){
            userList.remove(findUser);
            updateUsers(userList);
        }
    }

    public void applyUser(String number){
        List<OnceUser> userList=listUsers();
        OnceUser findUser=findUser(userList,number);
        if(findUser!=null){
            userList.remove(findUser);
            userList.add(0,findUser);
            updateUsers(userList);
        }
    }

    public OnceUser findUser(List<OnceUser> userList,String number){
        for(OnceUser u:userList){
            if(u!=null&&u.getNumber().equals(number)){
                return u;
            }
        }
        return null;
    }

    public List<OnceUser> listUsers(){
        List<OnceUser> list=new ArrayList<>();
        String json=sharedPreferences.getString(KEY_ONCE_USERS,null);
        if(!TextUtils.isEmpty(json)){
            TypeToken<List<OnceUser>> token=new TypeToken<List<OnceUser>>(){};
            list=new Gson().fromJson(json,token.getType());
            if(list==null){
                list=new ArrayList<>();
            }
        }
        return list;
    }

    public OnceUser listFirstUser(){
        List<OnceUser> list=listUsers();
        if(list==null||list.size()<1) return null;
        return list.get(0);
    }

    public void updateUsers(List<OnceUser> userList){
        if(userList!=null){
            String json=new Gson().toJson(userList);
            editor.putString(KEY_ONCE_USERS,json);
            editor.commit();
        }
    }

    public boolean hasLocalUser(){
        List<OnceUser> list=listUsers();
        if(list==null||list.size()==0){
            return false;
        }
        return true;
    }
}
