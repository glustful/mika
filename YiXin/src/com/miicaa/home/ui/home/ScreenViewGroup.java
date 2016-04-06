package com.miicaa.home.ui.home;



import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hacker.e 14-12-25
 */
public class ScreenViewGroup extends ViewGroup {
	
	static String TAG = "ScreenViewGroup";
	
    Context mContext;
    public ScreenViewGroup(Context context){
        super(context);
        mContext = context;
    }
    public ScreenViewGroup(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
               mContext = context;
    }
    
    

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();

        int maxWidth = r - l;

        int x = 0;

        int y = 0;

        int row = 0;
        
        for (int i = 0; i < childCount; i++) {

            final View child = this.getChildAt(i);

            if (child.getVisibility() != View.GONE) {

                int width = child.getMeasuredWidth();

                int height = child.getMeasuredHeight()+10;
                
                /*
                 * child之间有空间
                 */
                if(i == 0){
                x += width;
                y = height;
                }else{
                	x += width+15;
                }


                if (x > maxWidth && i > 0) {

                    x = width;

                    row++;

                    y +=  height;
                    
                }
                
                

                /*
                 * child view 的布局
                 */
                child.layout(x - width, y - height+10, x, y);
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

//        int column = 0;

        for (int index = 0; index < childCount; index++) {

            final View child = getChildAt(index);
            if (child.getVisibility() != View.GONE) {

            	/**
            	 * 设置子view的布局想要多大就要多大
            	 */
//                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST);
            	measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            	++column;
                // 此处增加onlayout中的换行判断，用于计算所需的高度

                int width = child.getMeasuredWidth();

                int height = child.getMeasuredHeight()+10;
                
                Log.d(TAG, "onMeasure child width:"+width
                		+"child height:"+height);
                if(index == 0){
                x += width;
                y =  height;
                }else{
                	x += width +15;
                }

                //如果一行上的列大于一列并且超过group的最大值，那么就转向下一行。
                if (x > maxWidth && index > 0) {

                    x = width;
                    
                    row++;

                    y += height ;
                  //始终保持和index一致
                	
                }

            }


        }

        // 设置容器所需的宽度和高度
        setMeasuredDimension(maxWidth, y);
    }

}

