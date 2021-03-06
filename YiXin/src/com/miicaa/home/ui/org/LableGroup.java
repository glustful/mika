package com.miicaa.home.ui.org;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LM on 14-6-17.
 */
public class LableGroup extends ViewGroup {
   
	Context mContext;
    public LableGroup(Context context){
        super(context);
        mContext = context;
    }
    public LableGroup(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
               mContext = context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int width = r;
//        int height = b;
//
//        int paddLeft = getPaddingLeft();
//        int paddRight = getPaddingRight();
//        int padTop = getPaddingTop();
//        int padBottom = getPaddingBottom();
//        //int childrenWidth = (width-mMargin*5- paddLeft - paddRight);
//        //offWidth
//        int childWidth = (width-20*3- paddLeft - paddRight)/4;
//        int childHeight = childWidth;
//
//        int nowX = paddLeft;
//        int nowY = padTop;
//        int childCount = getChildCount();
//        for(int i = 0; i < childCount; i++)
//        {
//            View child = getChildAt(i);
//            if(child.getVisibility() == View.GONE)
//                continue;
//
//            if(nowX + childWidth > width - paddRight )
//            {
//                nowX = paddLeft;
//                nowY = nowY +childHeight + 20;
//
//                child.layout(nowX, nowY, nowX + childWidth,nowY + childHeight);
//                nowX = nowX + 20 + childWidth;
//            }
//            else
//            {
//                child.layout(nowX, nowY, nowX + childWidth,nowY + childHeight);
//                nowX = nowX + 20 + childWidth;
//            }
//        }
        final int childCount = getChildCount();

        int maxWidth = r - l;

        int x = 0;

        int y = 0;

        int row = 0;

        for (int i = 0; i < childCount; i++) {

            final View child = this.getChildAt(i);

            if (child.getVisibility() != View.GONE) {

                int width = child.getMeasuredWidth();

                int height = child.getMeasuredHeight();

                x += width;

                y = row * height + height+10;

                if (x > maxWidth) {

                    x = width;

                    row++;

                    y = row * height + height+10;

                }

                child.layout(x - width, y - height, x, y);
            }
        }
    }
    private int measureHeight(int measureSpec)
    {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        result = getChildrenHeight();
        return result;
    }

    private int measureWidth(int measureSpec)
    {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = specSize;
        if (specMode == MeasureSpec.AT_MOST)
        {
            result = specSize;
        }

        else if (specMode == MeasureSpec.EXACTLY)
        {
            result = specSize;
        }

        return result;
    }

    public int getChildrenHeight()
    {

        int width = getMeasuredWidth();
        int height = 0;
        int paddLeft = getPaddingLeft();
        int paddRight = getPaddingRight();
        int padTop = getPaddingTop();
        int padBottom = getPaddingBottom();

        int childWidth = (width-20*3 - paddLeft - paddRight)/4;
        int childHeight = childWidth;


        int childCount = getChildCount();
        int rowCount = childCount/4;
        if(childCount%4 > 0)
        {
            rowCount++;
        }

        if(rowCount > 0)
        {
            childHeight = rowCount*childHeight + (rowCount -1)*20 + padTop + padBottom;
        }
        else
        {
            childHeight =  padTop + padBottom;
        }

        return childHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);

        int childCount = getChildCount();

        int x = 0;

        int y = 0;

        int row = 0;



        for (int index = 0; index < childCount; index++) {

            final View child = getChildAt(index);

            if (child.getVisibility() != View.GONE) {

                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

                // 此处增加onlayout中的换行判断，用于计算所需的高度

                int width = child.getMeasuredWidth();

                int height = child.getMeasuredHeight();

                x += width;

                //增加高度
                y = row * height+height+20;

                if (x > maxWidth) {

                    x = width;

                    row++;

                    y = row * height + height+20;

                }

            }

        }

        // 设置容器所需的宽度和高度

        setMeasuredDimension(maxWidth, y);
//        int measuredHeight = heightMeasureSpec;
//        int measuredWidth = widthMeasureSpec;
//        setMeasuredDimension(measuredWidth, measuredHeight);
//        for(int i = 0;i < getChildCount();i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, heightMeasureSpec);
//            setMeasuredDimension(measuredWidth, measuredHeight);
//        }
//        int measuredHeight = measureHeight(heightMeasureSpec);
//        int measuredWidth = measureWidth(widthMeasureSpec);
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//        for(int i = 0;i < getChildCount();i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, heightMeasureSpec);
//            setMeasuredDimension(measuredWidth, measuredHeight);
//        }
    }

}
