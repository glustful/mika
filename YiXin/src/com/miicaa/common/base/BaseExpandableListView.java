package com.miicaa.common.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by apple on 13-11-22.
 */
public class BaseExpandableListView extends ExpandableListView
{

    public BaseExpandableListView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
    }

    public BaseExpandableListView(Context context)
    {
        super(context);
    }

    public BaseExpandableListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,expandSpec);
    }
}
