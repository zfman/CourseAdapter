package com.zhuangfei.toolkit.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

import com.zhuangfei.toolkit.widget.listener.OnBaseScrollViewListener;

public class CustomScrollView extends ScrollView {

	private OnBaseScrollViewListener onBaseScrollViewListener;
	ListView innerListView;

	public void setInnerListView(ListView innerListView) {
		this.innerListView = innerListView;
	}

	public ListView getInnerListView() {
		return innerListView;
	}

	public void setScrollViewListener(OnBaseScrollViewListener scrollViewListener) {
		this.onBaseScrollViewListener = scrollViewListener;
	}

	public CustomScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		// TODO Auto-generated method stub
		super.onScrollChanged(x, y, oldx, oldy);
		if(onBaseScrollViewListener!=null){
			onBaseScrollViewListener.onScrollChanged(x, y, oldx, oldy);
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	    return super.onInterceptTouchEvent(ev);
	}

}
