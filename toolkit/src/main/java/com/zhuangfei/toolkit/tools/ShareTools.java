package com.zhuangfei.toolkit.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 对sharePreference的封装
 * @author Administrator
 *
 */
public class ShareTools {
	static SharedPreferences preferences;
	static SharedPreferences.Editor editor;

	static String DEAFULT_STORE="toolkie_store";
	
	@SuppressLint("CommitPrefEdits")
	@SuppressWarnings("static-access")
	private static void getSharePreference(Context context){
		if(context!=null)
		preferences = context.getSharedPreferences(DEAFULT_STORE, context.MODE_PRIVATE);
		editor = preferences.edit();
	}
	
	@SuppressLint("CommitPrefEdits")
	public static Object get(Context context,String key,Object defaultValue){
		getSharePreference(context);
		if(defaultValue instanceof Integer) return preferences.getInt(key,(int)defaultValue);
		if(defaultValue instanceof String) return preferences.getString(key,(String)defaultValue);
		if(defaultValue instanceof Boolean) return preferences.getBoolean(key,(Boolean)defaultValue);
		if(defaultValue instanceof Float) return preferences.getFloat(key,(Float)defaultValue);
		return defaultValue;
	}
	
	public static void put(Context context,String key,Object value){
		getSharePreference(context);
		if(value instanceof Integer) editor.putInt(key,(int) value);
		if(value instanceof String) editor.putString(key,(String) value);
		if(value instanceof Boolean) editor.putBoolean(key,(Boolean) value);
		if(value instanceof Float) editor.putFloat(key,(Float) value);
		editor.commit();
	}

	public static void putString(Context context,String key,String value){
		getSharePreference(context);
		editor.putString(key,value);
		editor.commit();
	}

	public static void putInt(Context context,String key,int value){
		getSharePreference(context);
		editor.putInt(key,value);
		editor.commit();
	}

	public static void putFloat(Context context,String key,float value){
		getSharePreference(context);
		editor.putFloat(key,value);
		editor.commit();
	}

	public static String getString(Context context,String key,String defaultValue){
		getSharePreference(context);
		return preferences.getString(key,defaultValue);
	}

	public static float getFloat(Context context,String key,float defaultValue){
		getSharePreference(context);
		return preferences.getFloat(key,defaultValue);
	}

	public static int getInt(Context context,String key,int defaultValue){
		getSharePreference(context);
		return preferences.getInt(key,defaultValue);
	}

	public static void clear(Context context){
		getSharePreference(context);
		editor.clear();
		editor.commit();
	}
}
