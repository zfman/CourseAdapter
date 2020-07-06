package com.zhuangfei.toolkit.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 自定义ListView
 * 解决ScrollView嵌套ListView的冲突
 * @author Administrator
 *
 */
public class InnerListView extends ListView {

	public InnerListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public InnerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InnerListView(Context context, AttributeSet attrs,
						 int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	};
}
