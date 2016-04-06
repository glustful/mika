package com.miicaa.home.ui.picture;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyPhotoViewPager extends ViewPager{

	public MyPhotoViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyPhotoViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
	    try
	    {
	            return super.onInterceptTouchEvent(ev);
	    }
	    catch (IllegalArgumentException e)
	    {
	    }
	    catch (ArrayIndexOutOfBoundsException e)
	    {

	    }
	    return false;
	}

}
