package com.miicaa.home.ui.picture;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Created by Administrator on 13-12-10.
 */
public class PhotoViewPager extends ViewPager
{
    public PhotoViewPager(Context context)
    {
        super(context);
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
            e.printStackTrace();
            return false;
        }
    }
}
