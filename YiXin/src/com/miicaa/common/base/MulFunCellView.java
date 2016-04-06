package com.miicaa.common.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 13-12-26.
 */
public class MulFunCellView extends RelativeLayout
{
    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;

    private Context mContext = null;
    private Scroller mScroller;
    private int mTouchSlop;
    private int mCurrentScreen = 0;
    private int mScrollX = 0;
    private int mTouchState = TOUCH_STATE_REST;
    //private int mMaxLenghtX = 0;
    private float mLastMotionX;
    private VelocityTracker mVelocityTracker;
    private static final int SNAP_VELOCITY = 1000;
    private Boolean mOpen = false;
    OnMulFunListener mMulFunListener = null;

    public MulFunCellView(Context context)
    {
        super(context);
        mContext = context;
        init();
    }

    public MulFunCellView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        mContext = context;
        init();
    }


    public void resetView(){
//    	mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
    	scrollTo(0, 0);
    }

    private void init()
    {
        mCurrentScreen = 0;
        mScroller = new Scroller(mContext);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setOnMulFunListener(OnMulFunListener onMulFunListener)
    {
        mMulFunListener = onMulFunListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();

        switch (action)
        {
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                boolean xMoved = xDiff > mTouchSlop;
                if (xMoved) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
                        : TOUCH_STATE_SCROLLING;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }

        return mTouchState != TOUCH_STATE_REST;
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4)
    {
        int w = getWidth();
        int childHeight = getChildrenHeight();
        for(int ai = 0; ai < getChildCount(); ai++)
        {
            View view = getChildAt(ai);
            String tag = (String)view.getTag();
            if(tag.equals("m"))
            {
                view.layout(0,0,w,childHeight);
            }
            else
            {
                view.layout(w,0,w + getChildrenWidth((ViewGroup)view),childHeight);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        final int action = event.getAction();
        final float x = event.getX();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                final int deltaX = (int) (mLastMotionX - x);
                mLastMotionX = x;
                if (deltaX < 0) {
                    if (mScrollX > 0) {
                        scrollBy(Math.max(-mScrollX, deltaX), 0);
                    }
                }
                else if (deltaX > 0)
                {
                    int width = getWidth();
                    int availableToScroll = getChildrenWidth() - mScrollX - getWidth();
                    if (availableToScroll > 0)
                    {
                        scrollBy(Math.min(availableToScroll, deltaX), 0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                final int deltamX = (int) (mLastMotionX - x);
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();
                if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0)
                {
                    snapToScreen(mCurrentScreen - 1);
                } else if (velocityX < -SNAP_VELOCITY
                        && mCurrentScreen < getPageCount() -1) {
                    snapToScreen(mCurrentScreen + 1);
                } else {
                    snapToDestination(deltamX > 0);
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                final int deltamvX = (int) (mLastMotionX - x);
                snapToDestination(deltamvX > 0);
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        mScrollX = this.getScrollX();
        return true;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int count = getChildCount();
//        for (int i = 0; i < count; i++) {
//            getChildAt(i).measure(MeasureSpec.AT_MOST, MeasureSpec.AT_MOST);
//            //getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset()) {
            mScrollX = mScroller.getCurrX();
            scrollTo(mScrollX, 0);
            postInvalidate();
        }
        invalidate();
    }

    private void snapToDestination(Boolean show) {
        final int screenWidth = getWidth();
        if(show)
        {
            final int w = getChildrenWidth() - screenWidth;
            final int whichScreen = (mScrollX + (w / 2)) / w;
            snapToScreen(whichScreen);
        }
        else
        {
            final int w = getChildrenWidth() - screenWidth;
            final int whichScreen = (mScrollX - (w / 2)) / w;
            snapToScreen(whichScreen);
        }
    }

    public void snapToScreen(int whichScreen)
    {
        if(whichScreen >= getPageCount())
        {
            return;
        }
        int width = getWidth();
        int newX = 0;
        if(whichScreen == getPageCount() -1)
        {
            newX = getChildrenWidth() - width;
        }
        else
        {
            newX = whichScreen * width;
        }
        mCurrentScreen = whichScreen;
        if(mCurrentScreen == 0)
        {
            mOpen = false;
        }
        else
        {
            mOpen = true;
        }
        if(mMulFunListener != null)
        {
            mMulFunListener.openModeChanged(this,mOpen);
        }

        final int delta = newX - mScrollX;
        mScroller.startScroll(mScrollX, 0, delta, 0, Math.abs(delta) * 2);
        invalidate();
    }
    public void setToScreen(int whichScreen) {
        if(whichScreen >= getPageCount())
        {
            return;
        }
        int width = getWidth();
        int newX = 0;
        if(whichScreen == getPageCount() -1)
        {
            newX = getChildrenWidth() - width;
        }
        else
        {
            newX = whichScreen * width;
        }
        mCurrentScreen = whichScreen;
        if(mCurrentScreen == 0)
        {
            mOpen = false;
        }
        else
        {
            mOpen = true;
        }
        if(mMulFunListener != null)
        {
            mMulFunListener.openModeChanged(this,mOpen);
        }

        final int delta = newX - mScrollX;
        mScroller.startScroll(newX, 0, 0, 0, 10);
        invalidate();
    }

    private int getChildrenHeight()
    {
        int height = 0;
        for(int i =0; i < getChildCount(); i++)
        {
            int temp = getChildAt(i).getMeasuredHeight();
            if(temp > height)
            {
                height = temp;
            }
        }
        return height;
    }

    private int getPageCount()
    {
        int theWidth = getMeasuredWidth();
        int count = getChildrenWidth() /theWidth;
        if(getChildrenWidth() %theWidth > 0)
        {
            count++;
        }
        return count;
    }

    private int getChildrentHeight()
    {
        int height = 0;
        for(int i =0; i < getChildCount(); i++)
        {
            int temp = getChildAt(i).getMeasuredHeight();
            if(temp > height)
            {
                height = temp;
            }
        }
        return height;
    }

    private int getChildrenWidth()
    {
        int width = 0;
        for(int i =0; i < getChildCount(); i++)
        {
            int temp = getChildAt(i).getMeasuredWidth();
            if(temp == 0)
            {
                temp = getChildrenWidth((ViewGroup)getChildAt(i));
            }
            width = width +temp;
        }
        return width;
    }

    private int getChildrenWidth(ViewGroup view)
    {
        int width = 0;
        if(view != null)
        {
            for(int i =0; i < view.getChildCount(); i++)
            {
                int temp = view.getChildAt(i).getMeasuredWidth();
                width = width +temp;
            }
        }
        return width;
    }

    public interface OnMulFunListener
    {
        public void openModeChanged(MulFunCellView v, Boolean open);
    }
}
