package com.miicaa.home.ui.menu;

import org.androidannotations.annotations.EViewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@EViewGroup
public class ApprScreenView extends ViewGroup{
	final int mMargin = 20;
	Context context;
	public ApprScreenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public ApprScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public ApprScreenView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		 int width = r;
	        int height = b;

	        int paddLeft = getPaddingLeft();
	        int paddRight = getPaddingRight();
	        int padTop = getPaddingTop();
	        int padBottom = getPaddingBottom();

	        //int childrenWidth = (width-mMargin*5- paddLeft - paddRight);
	        //offWidth
	        int childWidth = (width-mMargin*3- paddLeft - paddRight)/4;
	        int childHeight = childWidth;

	        int nowX = paddLeft;
	        int nowY = padTop;
	        int childCount = getChildCount();
	        for(int i = 0; i < childCount; i++)
	        {
	            View child = getChildAt(i);
	            if(child.getVisibility() == View.GONE)
	                continue;

	            if(nowX + childWidth > width - paddRight )
	            {
	                nowX = paddLeft;
	                nowY = nowY +childHeight + mMargin;

	                child.layout(nowX, nowY, nowX + childWidth,nowY + childHeight);
	                nowX = nowX + mMargin + childWidth;
	            }
	            else
	            {
	                child.layout(nowX, nowY, nowX + childWidth,nowY + childHeight);
	                nowX = nowX + mMargin + childWidth;
	            }
	        }
	}
	
	 @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	    {
	        int measuredHeight = measureHeight(heightMeasureSpec);
	        int measuredWidth = measureWidth(widthMeasureSpec);
	        setMeasuredDimension(measuredWidth, measuredHeight);
	        for(int i = 0;i < getChildCount();i++) {
	            View child = getChildAt(i);
	            child.measure(widthMeasureSpec, heightMeasureSpec);
	            setMeasuredDimension(measuredWidth, measuredHeight);
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

	        int childWidth = (width-mMargin*3 - paddLeft - paddRight)/4;
	        int childHeight = childWidth;


	        int childCount = getChildCount();
	        int rowCount = childCount/4;
	        if(childCount%4 > 0)
	        {
	            rowCount++;
	        }

	        if(rowCount > 0)
	        {
	            childHeight = rowCount*childHeight + (rowCount -1)*mMargin + padTop + padBottom;
	        }
	        else
	        {
	            childHeight =  padTop + padBottom;
	        }

	        return childHeight;
	    }
	   
	   void add(){
		   TextView tv = new TextView(context);
		   tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		   tv.setText("sadhaksdhuasdhasdasd");
		   this.addView(tv);
	   }

}
