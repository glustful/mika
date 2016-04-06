package com.miicaa.home.ui.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class NoScrollGridView extends GridView{

	public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoScrollGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	 @Override

	 public boolean dispatchTouchEvent(MotionEvent ev) {

	          if (ev.getAction() == MotionEvent.ACTION_MOVE) {

	               return true;  //禁止GridView滑动

	          }


	          return super.dispatchTouchEvent(ev);

	 }	
}
