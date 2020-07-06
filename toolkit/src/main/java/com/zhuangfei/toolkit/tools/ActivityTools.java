package com.zhuangfei.toolkit.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.WindowManager;

import com.zhuangfei.toolkit.R;
import com.zhuangfei.toolkit.model.BundleModel;

import java.io.Serializable;

public class ActivityTools {
	/**
	 * 设置透明状态栏
	 */
	@SuppressLint("InlinedApi")
	public static void setTransparent(Activity activity) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT&&activity!=null) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}

	/**
	 * 清除透明状态栏
	 * @param activity
	 */
	public static void clearTransparent(Activity activity){
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT&&activity!=null) {
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}

	/**
	 * 返回Home
	 * @param context
	 */
	public static void toHome(Context context){
		if(context==null) return;
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		home.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(home);
	}

	/**
	 * 使用默认的入场动画效果
	 * 从context跳转到target页面
	 * @param context
	 * @param target
	 */
	public static void toActivity(Activity context,Class<?> target){
		Intent intent=new Intent(context,target);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide2_in, R.anim.slide2_out);//动画
	//	context.finish();
	}

	public static void toActivity(Activity context,Class<?> target,BundleModel model){
		Intent intent=new Intent(context,target);
		Bundle bundle=new Bundle();
		bundle.putSerializable("model",model);
		intent.putExtras(bundle);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide2_in, R.anim.slide2_out);//动画
	//	context.finish();
	}

	public static void toActivityWithout(Activity context,Class<?> target,BundleModel model){
		Intent intent=new Intent(context,target);
		Bundle bundle=new Bundle();
		bundle.putSerializable("model",model);
		intent.putExtras(bundle);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide2_in, R.anim.slide2_out);//动画
	}

	public static void toActivityWithout(Activity context,Class<?> target){
		Intent intent=new Intent(context,target);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide2_in, R.anim.slide2_out);//动画
	}

	public static void toBackActivityAnim(Activity context,Class<?> target){
//		Intent intent=new Intent(context,target);
//		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);//动画
		context.finish();
	}

	public static void toBackActivityAnim(Activity context,Class<?> target,BundleModel model){
//		Intent intent=new Intent(context,target);
//		Bundle bundle=new Bundle();
//		bundle.putSerializable("model",model);
//		intent.putExtras(bundle);
//		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);//动画
		context.finish();
	}
}
