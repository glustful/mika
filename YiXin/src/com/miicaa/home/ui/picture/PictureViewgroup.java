package com.miicaa.home.ui.picture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miicaa.common.base.numberPicker.Scroller;
import com.miicaa.home.R;

/**
 * Created by LM on 14-4-18.
 */
public class PictureViewgroup extends ViewGroup implements GestureDetector.OnGestureListener {
    private float mLastMotionY;// 最后点击的点
    private GestureDetector detector;
    int move = 0;// 移动距离
    int MAXMOVE = 850;// 最大允许的移动距离
    private Scroller mScroller;
    int up_excess_move = 0;// 往上多移的距离
    int down_excess_move = 0;// 往下多移的距离
    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;
    private int mTouchSlop;
    private int mTouchState = TOUCH_STATE_REST;
    private Handler handler;
    private ArrayList<String> mFidList ;
    private int count =0;
    Context mContext;

    public PictureViewgroup(Context context) {
        super(context);
        mContext = context;
        // TODO Auto-generated constructor stub
        mScroller = new Scroller(context);
        detector = new GestureDetector(this);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获得可以认为是滚动的距离
        mTouchSlop = configuration.getScaledTouchSlop();
        mFidList = new ArrayList<String>();

    }

    public PictureViewgroup(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        mContext = context;
        // TODO Auto-generated constructor stub
        mScroller = new Scroller(context);
        detector = new GestureDetector(this);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获得可以认为是滚动的距离
        mTouchSlop = configuration.getScaledTouchSlop();
        mFidList = new ArrayList<String>();
    }

    public void loadBitMap(Bitmap bitmap,final String fileId){
       ImageView imageView = mHardMapView.get(fileId);
       imageView.setImageBitmap(bitmap);
       imageView.setOnClickListener(new OnClickListener(){
           @Override
           public void onClick(View view){
               if (mFidList != null && mFidList.size() != 0) {
                   Intent intent = new Intent(mContext, PhotoCheck.class);
                   Bundle bundle = new Bundle();
                   bundle.putStringArrayList("fids", mFidList);
                   bundle.putString("fid", fileId);
                   intent.putExtra("bundle", bundle);
                   mContext.startActivity(intent);
               }
           }
       });
//       addView(imageView);
    }
    
    HashMap<String, ImageView> mHardMapView = new HashMap<String, ImageView>();
    public void addView(String fid){
    	 ImageView imageView = new ImageView(mContext);
         imageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.image_selector));
         addView(imageView);
         mHardMapView.put(fid, imageView);
    }

    public void setFileIdGroup(ArrayList<String> fileIds){
        mFidList = fileIds;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // 返回当前滚动X方向的偏移
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
                        : TOUCH_STATE_SCROLLING;
                break;
            case MotionEvent.ACTION_MOVE:
                final int yDiff = (int) Math.abs(y - mLastMotionY);
                boolean yMoved = yDiff > mTouchSlop;
                // 判断是否是移动
                if (yMoved) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        // final int action = ev.getAction();

        final float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                    move = mScroller.getFinalY();
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getPointerCount() == 1) {
                    // 随手指 拖动的代码
                    int deltaY = 0;
                    deltaY = (int) (mLastMotionY - y);
                    mLastMotionY = y;
                    Log.d("move", "" + move);
                    if (deltaY < 0) {
                        // 下移
                        // 判断上移 是否滑过头
                        if (up_excess_move == 0) {
                            if (move > 0) {
                                int move_this = Math.max(-move, deltaY);
                                move = move + move_this;
                                scrollBy(0, move_this);
                            } else if (move == 0) {// 如果已经是最顶端 继续往下拉
                                Log.d("down_excess_move", "" + down_excess_move);
                                down_excess_move = down_excess_move - deltaY / 2;// 记录下多往下拉的值
                                scrollBy(0, deltaY / 2);
                            }
                        } else if (up_excess_move > 0)// 之前有上移过头                      {
                            if (up_excess_move >= (-deltaY)) {
                                up_excess_move = up_excess_move + deltaY;
                                scrollBy(0, deltaY);
                            } else {
                                up_excess_move = 0;
                                scrollBy(0, -up_excess_move);
                            }
                    } else if (deltaY > 0) {
                        // 上移
                        if (down_excess_move == 0) {
                            if (MAXMOVE - move > 0) {
                                int move_this = Math.min(MAXMOVE - move, deltaY);
                                move = move + move_this;
                                scrollBy(0, move_this);
                            } else if (MAXMOVE - move == 0) {
                                if (up_excess_move <= 100) {
                                    up_excess_move = up_excess_move + deltaY / 2;
                                    scrollBy(0, deltaY / 2);
                                }
                            }
                        } else if (down_excess_move > 0) {
                            if (down_excess_move >= deltaY) {
                                down_excess_move = down_excess_move - deltaY;
                                scrollBy(0, deltaY);
                            } else {
                                down_excess_move = 0;
                                scrollBy(0, down_excess_move);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 多滚是负数 记录到move里
                if (up_excess_move > 0) {
                    // 多滚了 要弹回去
                    scrollBy(0, -up_excess_move);
                    invalidate();
                    up_excess_move = 0;
                }
                if (down_excess_move > 0) {
                    // 多滚了 要弹回去
                    scrollBy(0, down_excess_move);
                    invalidate();
                    down_excess_move = 0;
                }
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return this.detector.onTouchEvent(ev);
    }

    int Fling_move = 0;

    @Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        //随手指 快速拨动的代码
        Log.d("onFling", "onFling");
        if (up_excess_move == 0 && down_excess_move == 0) {

            int slow = -(int) velocityY * 3 / 4;
            mScroller.fling(0, move, 0, slow, 0, 0, 0, MAXMOVE);
            move = mScroller.getFinalY();
            computeScroll();
        }
        return false;
    }

    @Override
	public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
	public void onShowPress(MotionEvent e) {
        // // TODO Auto-generated method stub
    }

    @Override
	public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
	public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {

        int width = r;
        int height = b;

        int paddLeft = getPaddingLeft();
        int paddRight = getPaddingRight();
        int padTop = getPaddingTop();
        int padBottom = getPaddingBottom();

        //int childrenWidth = (width-mMargin*5- paddLeft - paddRight);
        //offWidth
        int childWidth = (width-20*3- paddLeft - paddRight)/4;
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
                nowY = nowY +childHeight + 20;

                child.layout(nowX, nowY, nowX + childWidth,nowY + childHeight);
                nowX = nowX + 20 + childWidth;
            }
            else
            {
                child.layout(nowX, nowY, nowX + childWidth,nowY + childHeight);
                nowX = nowX + 20 + childWidth;
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        for(int i = 0;i < getChildCount();i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }
}
