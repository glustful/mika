package com.miicaa.common.base;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.miicaa.home.R;

/**
 * Created by Administrator on 13-12-9.
 */
public class WheelView extends View
{
    private static final int SCROLLING_DURATION = 400;
    private static final int MIN_DELTA_FOR_SCROLLING = 1;
    private static final int VALUE_TEXT_COLOR = 0xF0000000;
    private static final int ITEMS_TEXT_COLOR = 0xFF000000;
    private static final int[] SHADOWS_COLORS = new int[] { 0xFF111111,
            0x00AAAAAA, 0x00AAAAAA };
    private static final int ADDITIONAL_ITEM_HEIGHT = 15;
    public int TEXT_SIZE;
    private final int ITEM_OFFSET = TEXT_SIZE / 5;
    private static final int ADDITIONAL_ITEMS_SPACE = 10;
    private static final int LABEL_OFFSET = 8;
    private static final int PADDING = 10;
    private static final int DEF_VISIBLE_ITEMS = 5;

    private final int MESSAGE_SCROLL = 0;
    private final int MESSAGE_JUSTIFY = 1;

    private WheelAdapter mAdapter = null;
    private int mCurrentItem = 0;
    private int mItemsWidth = 0;
    private int mLabelWidth = 0;
    private int mVisibleItems = DEF_VISIBLE_ITEMS;
    private int mItemHeight = 0;

    private TextPaint mItemsPaint;
    private TextPaint mValuePaint;

    private StaticLayout mItemsLayout;
    private StaticLayout mLabelLayout;
    private StaticLayout mValueLayout;

    private String mLabel;
    private Drawable mCenterDrawable;

    private GradientDrawable mTopShadow;
    private GradientDrawable mBottomShadow;

    private boolean mIsScrollingPerformed;
    private int mScrollingOffset;

    private GestureDetector mGestureDetector;
    private Scroller mScroller;
    private int mLastScrollY;

    boolean mIsCyclic = false;
    private float mScal = 0.0f;

    private List<OnWheelChangedListener> mChangingListeners = new LinkedList<OnWheelChangedListener>();
    private List<OnWheelScrollListener> mScrollingListeners = new LinkedList<OnWheelScrollListener>();

    public WheelView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initData(context);
    }
    public WheelView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initData(context);
    }

    public WheelView(Context context)
    {
        super(context);
        initData(context);
    }

    private void initData(Context context)
    {
        mGestureDetector = new GestureDetector(context, gestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
        mScroller = new Scroller(context);
        mScal = context.getResources().getDisplayMetrics().density;
        
    }

    public WheelAdapter getAdapter()
    {
        return mAdapter;
    }

    public void setAdapter(WheelAdapter adapter)
    {
        this.mAdapter = adapter;
        invalidateLayouts();
        invalidate();
    }

    public void setInterpolator(Interpolator interpolator)
    {
        mScroller.forceFinished(true);
        mScroller = new Scroller(getContext(), interpolator);
    }

    public int getVisibleItems()
    {
        return mVisibleItems;
    }

    public void setVisibleItems(int count)
    {
        mVisibleItems = count;
        invalidate();
    }

    public String getLabel()
    {
        return mLabel;
    }

    public void setLabel(String newLabel)
    {
        if (mLabel == null || !mLabel.equals(newLabel))
        {
            mLabel = newLabel;
            mLabelLayout = null;
            invalidate();
        }
    }

    public void addChangingListener(OnWheelChangedListener listener)
    {
        mChangingListeners.add(listener);
    }

    public void removeChangingListener(OnWheelChangedListener listener)
    {
        mChangingListeners.remove(listener);
    }

    protected void notifyChangingListeners(int oldValue, int newValue)
    {
        for (OnWheelChangedListener listener : mChangingListeners)
        {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    public void addScrollingListener(OnWheelScrollListener listener)
    {
        mScrollingListeners.add(listener);
    }

    public void removeScrollingListener(OnWheelScrollListener listener)
    {
        mScrollingListeners.remove(listener);
    }

    protected void notifyScrollingListenersAboutStart()
    {
        for (OnWheelScrollListener listener : mScrollingListeners)
        {
            listener.onScrollingStarted(this);
        }
    }

    protected void notifyScrollingListenersAboutEnd()
    {
        for (OnWheelScrollListener listener : mScrollingListeners)
        {
            listener.onScrollingFinished(this);
        }
    }

    public int getCurrentItem()
    {
        return mCurrentItem;
    }

    public void setCurrentItem(int index, boolean animated)
    {
        if (mAdapter == null || mAdapter.getItemsCount() == 0)
        {
            return;
        }

        if (index < 0 || index >= mAdapter.getItemsCount())
        {
            if (mIsCyclic)
            {
                while (index < 0)
                {
                    index += mAdapter.getItemsCount();
                }
                index %= mAdapter.getItemsCount();
            }
            else
            {
                return;
            }
        }
        System.out.println("indexx="+index+"="+mCurrentItem);
        if (index != mCurrentItem)
        {
            if (animated)
            {
                scroll(index - mCurrentItem, SCROLLING_DURATION);
            }
            else
            {
                invalidateLayouts();
                int old = mCurrentItem;
                mCurrentItem = index;
                notifyChangingListeners(old, mCurrentItem);
                invalidate();
            }
        }
    }

    public void setCurrentItem(int index)
    {
        setCurrentItem(index, false);
    }

    public boolean isCyclic()
    {
        return mIsCyclic;
    }

    public void setCyclic(boolean isCyclic)
    {
        this.mIsCyclic = isCyclic;

        invalidate();
        invalidateLayouts();
    }

    private void invalidateLayouts()
    {
        mItemsLayout = null;
        mValueLayout = null;
        mScrollingOffset = 0;
    }

    private void initResourcesIfNecessary()
    {
    	TEXT_SIZE = dip2px(getContext(), TEXT_SIZE);
        if (mItemsPaint == null)
        {
            mItemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                    | Paint.FAKE_BOLD_TEXT_FLAG);

            mItemsPaint.setTextSize(TEXT_SIZE);
            
        }
        if (mValuePaint == null)
        {
            mValuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG | Paint.DITHER_FLAG);
            mValuePaint.setTextSize(TEXT_SIZE);
            mValuePaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0);
        }

        if (mCenterDrawable == null)
        {
            mCenterDrawable = getContext().getResources().getDrawable(R.drawable.wheel_val);
        }

        if (mTopShadow == null)
        {
            mTopShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, SHADOWS_COLORS);
        }

        if (mBottomShadow == null)
        {
            mBottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,SHADOWS_COLORS);
        }

        setBackgroundResource(R.drawable.wheel_bg);
    }

    private int getDesiredHeight(Layout layout)
    {
        if (layout == null)
        {
            return 0;
        }

        int desired = getItemHeight() * mVisibleItems - ITEM_OFFSET * 2 - ADDITIONAL_ITEM_HEIGHT;

        desired = Math.max(desired, getSuggestedMinimumHeight());
        return desired;
    }

    private String getTextItem(int index)
    {
        if (mAdapter == null || mAdapter.getItemsCount() == 0)
        {
            return null;
        }
        int count = mAdapter.getItemsCount();
        if ((index < 0 || index >= count) && !mIsCyclic)
        {
            return null;
        }
        else
        {
            while (index < 0)
            {
                index = count + index;
            }
        }

        index %= count;
        return mAdapter.getItem(index);
    }

    private String buildText(boolean useCurrentValue)
    {
        StringBuilder itemsText = new StringBuilder();
        int addItems = mVisibleItems / 2 + 1;

        for (int i = mCurrentItem - addItems; i <= mCurrentItem + addItems; i++)
        {
            if (useCurrentValue || i != mCurrentItem)
            {
                String text = getTextItem(i);
                if (text != null)
                {
                    itemsText.append(text);
                }
            }
            if (i < mCurrentItem + addItems)
            {
                itemsText.append("\n");
            }
        }

        return itemsText.toString();
    }

    private int getMaxTextLength()
    {
        WheelAdapter adapter = getAdapter();
        if (adapter == null)
        {
            return 0;
        }

        int adapterLength = adapter.getMaximumLength();
        if (adapterLength > 0)
        {
            return adapterLength;
        }

        String maxText = null;
        int addItems = mVisibleItems / 2;
        for (int i = Math.max(mCurrentItem - addItems, 0); i < Math.min(
                mCurrentItem + mVisibleItems, adapter.getItemsCount()); i++)
        {
            String text = adapter.getItem(i);
            if (text != null
                    && (maxText == null || maxText.length() < text.length())) {
                maxText = text;
            }
        }

        return maxText != null ? maxText.length() : 0;
    }

    private int getItemHeight()
    {
        if (mItemHeight != 0)
        {
            return mItemHeight;
        }
        else if (mItemsLayout != null && mItemsLayout.getLineCount() > 2)
        {
            mItemHeight = mItemsLayout.getLineTop(2) - mItemsLayout.getLineTop(1);
            return mItemHeight;
        }

        return getHeight() / mVisibleItems;
    }

    private int calculateLayoutWidth(int widthSize, int mode)
    {
        initResourcesIfNecessary();

        int width = widthSize;

        int maxLength = getMaxTextLength();
        if (maxLength > 0) {
            float textWidth = FloatMath.ceil(Layout.getDesiredWidth("0",
                    mItemsPaint));
            mItemsWidth = (int) (maxLength * textWidth);
        }
        else
        {
            mItemsWidth = 0;
        }
        mItemsWidth += ADDITIONAL_ITEMS_SPACE; // make it some more

        mLabelWidth = 0;
        if (mLabel != null && mLabel.length() > 0)
        {
            mLabelWidth = (int) FloatMath.ceil(Layout.getDesiredWidth(mLabel,
                    mValuePaint));
        }

        boolean recalculate = false;
        if (mode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
            recalculate = true;
        }
        else
        {
            width = mItemsWidth + mLabelWidth + 2 * PADDING;
            if (mLabelWidth > 0)
            {
                width += LABEL_OFFSET;
            }

            // Check against our minimum width
            width = Math.max(width, getSuggestedMinimumWidth());

            if (mode == MeasureSpec.AT_MOST && widthSize < width)
            {
                width = widthSize;
                recalculate = true;
            }
        }

        if (recalculate)
        {
            // recalculate width
            int pureWidth = width - LABEL_OFFSET - 2 * PADDING;
            if (pureWidth <= 0)
            {
                mItemsWidth = mLabelWidth = 0;
            }
            if (mLabelWidth > 0)
            {
                double newWidthItems = (double) mItemsWidth * pureWidth
                        / (mItemsWidth + mLabelWidth);
                mItemsWidth = (int) newWidthItems;
                mLabelWidth = pureWidth - mItemsWidth;
            }
            else
            {
                mItemsWidth = pureWidth + LABEL_OFFSET; // no label
            }
        }

        if (mItemsWidth > 0)
        {
            createLayouts(mItemsWidth, mLabelWidth);
        }

        return width;
    }

    private void createLayouts(int widthItems, int widthLabel)
    {
        if (mItemsLayout == null || mItemsLayout.getWidth() > widthItems)
        {
            mItemsLayout = new StaticLayout(buildText(mIsScrollingPerformed),
                    mItemsPaint, widthItems,
                    widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE
                            : Layout.Alignment.ALIGN_CENTER, 1,
                    ADDITIONAL_ITEM_HEIGHT, false);
        }
        else
        {
            mItemsLayout.increaseWidthTo(widthItems);
        }

        if (!mIsScrollingPerformed
                && (mValueLayout == null || mValueLayout.getWidth() > widthItems))
        {
            String text = getAdapter() != null ? getAdapter().getItem(
                    mCurrentItem) : null;
            mValueLayout = new StaticLayout(text != null ? text : "",
                    mValuePaint, widthItems,
                    widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE
                            : Layout.Alignment.ALIGN_CENTER, 1,
                    ADDITIONAL_ITEM_HEIGHT, false);
        }
        else if (mIsScrollingPerformed)
        {
            mValueLayout = null;
        }
        else
        {
            mValueLayout.increaseWidthTo(widthItems);
        }

        if (widthLabel > 0)
        {
            if (mLabelLayout == null || mLabelLayout.getWidth() > widthLabel)
            {
                mLabelLayout = new StaticLayout(mLabel, mValuePaint, widthLabel,
                        Layout.Alignment.ALIGN_NORMAL, 1,
                        ADDITIONAL_ITEM_HEIGHT, false);
            }
            else
            {
                mLabelLayout.increaseWidthTo(widthLabel);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = calculateLayoutWidth(widthSize, widthMode);

        int height;
        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        }
        else
        {
            height = getDesiredHeight(mItemsLayout);

            if (heightMode == MeasureSpec.AT_MOST)
            {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (mItemsLayout == null)
        {
            if (mItemsWidth == 0)
            {
                calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
            }
            else
            {
                createLayouts(mItemsWidth, mLabelWidth);
            }
        }

        if (mItemsWidth > 0)
        {
            canvas.save();
            canvas.translate(PADDING, -ITEM_OFFSET);
            drawItems(canvas);
            drawValue(canvas);
            canvas.restore();
        }

        drawCenterRect(canvas);
        drawShadows(canvas);
    }

    private void drawShadows(Canvas canvas)
    {
        mTopShadow.setBounds(0, 0, getWidth(), getHeight() / mVisibleItems);
        mTopShadow.draw(canvas);

        mBottomShadow.setBounds(0, getHeight() - getHeight() / mVisibleItems,
                getWidth(), getHeight());
        mBottomShadow.draw(canvas);
    }

    private void drawValue(Canvas canvas)
    {
        mValuePaint.setColor(VALUE_TEXT_COLOR);
        mValuePaint.drawableState = getDrawableState();

        Rect bounds = new Rect();
        mItemsLayout.getLineBounds(mVisibleItems / 2, bounds);

        // draw label
        if (mLabelLayout != null)
        {
            canvas.save();
            canvas.translate(mItemsLayout.getWidth() + LABEL_OFFSET, bounds.top);
            mLabelLayout.draw(canvas);
            canvas.restore();
        }

        // draw current value
        if (mValueLayout != null)
        {
            canvas.save();
            canvas.translate(0, bounds.top + mScrollingOffset);
            mValueLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawItems(Canvas canvas)
    {
        canvas.save();

        int top = mItemsLayout.getLineTop(1);
        canvas.translate(0, -top + mScrollingOffset);

        mItemsPaint.setColor(ITEMS_TEXT_COLOR);
        mItemsPaint.drawableState = getDrawableState();
        mItemsLayout.draw(canvas);

        canvas.restore();
    }

    private void drawCenterRect(Canvas canvas)
    {
        int center = getHeight() / 2;
        int offset = getItemHeight() / 2;
        mCenterDrawable.setBounds(0, center - offset, getWidth(), center
                + offset);
        mCenterDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        WheelAdapter adapter = getAdapter();
        if (adapter == null) {
            return true;
        }

        if (!mGestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP)
        {
            justify();
        }
        return true;
    }

    private void doScroll(int delta)
    {
        mScrollingOffset += delta;

        int count = mScrollingOffset / getItemHeight();
        int pos = mCurrentItem - count;
        if (mIsCyclic && mAdapter.getItemsCount() > 0)
        {
            // fix position by rotating
            while (pos < 0)
            {
                pos += mAdapter.getItemsCount();
            }
            pos %= mAdapter.getItemsCount();
        } else if (mIsScrollingPerformed)
        {
            //
            if (pos < 0)
            {
                count = mCurrentItem;
                pos = 0;
            }
            else if (pos >= mAdapter.getItemsCount())
            {
                count = mCurrentItem - mAdapter.getItemsCount() + 1;
                pos = mAdapter.getItemsCount() - 1;
            }
        }
        else
        {
            // fix position
            pos = Math.max(pos, 0);
            pos = Math.min(pos, mAdapter.getItemsCount() - 1);
        }

        int offset = mScrollingOffset;
        if (pos != mCurrentItem)
        {
            setCurrentItem(pos, false);
        }
        else
        {
            invalidate();
        }

        // update offset
        mScrollingOffset = offset - count * getItemHeight();
        if (mScrollingOffset > getHeight())
        {
            mScrollingOffset = mScrollingOffset % getHeight() + getHeight();
        }
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener()
    {


        @Override
		public boolean onDown(MotionEvent e)
        {
            if (mIsScrollingPerformed)
            {
                mScroller.forceFinished(true);
                clearMessages();
                return true;
            }
            return false;
        }

        @Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY)
        {
            startScrolling();
            doScroll((int) -distanceY);
            return true;
        }

        @Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY)
        {
            mLastScrollY = mCurrentItem * getItemHeight() + mScrollingOffset;
            int maxY = mIsCyclic ? 0x7FFFFFFF : mAdapter.getItemsCount()
                    * getItemHeight();
            int minY = mIsCyclic ? -maxY : 0;
            mScroller.fling(0, mLastScrollY, 0, (int) -velocityY / 2, 0, 0, minY,
                    maxY);
            setNextMessage(MESSAGE_SCROLL);
            return true;
        }
    };

    private void setNextMessage(int message)
    {
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }

    private void clearMessages()
    {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }

    private Handler animationHandler = new Handler()
    {
        @Override
		public void handleMessage(Message msg)
        {
            mScroller.computeScrollOffset();
            int currY = mScroller.getCurrY();
            int delta = mLastScrollY - currY;
            mLastScrollY = currY;
            if (delta != 0)
            {
                doScroll(delta);
            }

            // scrolling is not finished when it comes to final Y
            // so, finish it manually
            if (Math.abs(currY - mScroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                currY = mScroller.getFinalY();
                mScroller.forceFinished(true);
            }
            if (!mScroller.isFinished())
            {
                animationHandler.sendEmptyMessage(msg.what);
            }
            else if (msg.what == MESSAGE_SCROLL)
            {
                justify();
            } else {
                finishScrolling();
            }
        }
    };

    private void justify()
    {
        if (mAdapter == null)
        {
            return;
        }
        mLastScrollY = 0;
        int offset = mScrollingOffset;
        int itemHeight = getItemHeight();
        boolean needToIncrease = offset > 0 ? mCurrentItem < mAdapter
                .getItemsCount() : mCurrentItem > 0;
        if ((mIsCyclic || needToIncrease)
                && Math.abs((float) offset) > (float) itemHeight / 2)
        {
            if (offset < 0)
                offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
            else
                offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
        }
        if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING)
        {
            mScroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
            setNextMessage(MESSAGE_JUSTIFY);
        }
        else
        {
            finishScrolling();
        }
    }

    private void startScrolling()
    {
        if (!mIsScrollingPerformed)
        {
            mIsScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }
    }

    void finishScrolling()
    {
        if (mIsScrollingPerformed)
        {
            notifyScrollingListenersAboutEnd();
            mIsScrollingPerformed = false;
        }
        invalidateLayouts();
        invalidate();
    }

    public void scroll(int itemsToScroll, int time)
    {
        mScroller.forceFinished(true);

        mLastScrollY = mScrollingOffset;
        int offset = itemsToScroll * getItemHeight();

        mScroller.startScroll(0, mLastScrollY, 0, offset - mLastScrollY, time);
        setNextMessage(MESSAGE_SCROLL);

        startScrolling();
    }

    public interface WheelAdapter
    {
        public int getItemsCount();
        public String getItem(int index);
        public int getMaximumLength();
    }

    public interface OnWheelChangedListener
    {
        void onChanged(WheelView wheel, int oldValue, int newValue);
    }

    public interface OnWheelScrollListener
    {
        void onScrollingStarted(WheelView wheel);
        void onScrollingFinished(WheelView wheel);
    }
    
    public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
}
}
