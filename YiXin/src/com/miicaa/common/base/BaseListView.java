package com.miicaa.common.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by apple on 13-11-27.
 */
public class BaseListView extends ListView {
    private int section;
    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public BaseListView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
    }

    public BaseListView(Context context)
    {
        super(context);
    }

    public BaseListView(Context context, AttributeSet attrs, int defStyle)
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
