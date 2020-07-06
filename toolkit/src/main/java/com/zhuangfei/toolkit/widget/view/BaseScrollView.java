package com.zhuangfei.toolkit.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.zhuangfei.toolkit.widget.listener.OnBaseScrollViewListener;

public class BaseScrollView extends ScrollView {
	
	private OnBaseScrollViewListener onBaseScrollViewListener;
	float mDownPosX,mDownPosY;
	
	public void setScrollViewListener(OnBaseScrollViewListener scrollViewListener) {
		this.onBaseScrollViewListener = scrollViewListener;
	}

	public BaseScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BaseScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BaseScrollView(Context context, AttributeSet attrs, int defStyle) {
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
	    final float x = ev.getX();
	    final float y = ev.getY();

	    final int action = ev.getAction();
	    switch (action) {
	        case MotionEvent.ACTION_DOWN:
	            mDownPosX = x;
	            mDownPosY = y;

	            break;
	        case MotionEvent.ACTION_MOVE:
	            final float deltaX = Math.abs(x - mDownPosX);
	            final float deltaY = Math.abs(y - mDownPosY);
	            if (deltaX > deltaY) {
	                return false;
	            }
	    }

	    return super.onInterceptTouchEvent(ev);
	}

}
