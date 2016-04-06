package com.miicaa.common.base;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;

import com.miicaa.home.R;
import com.miicaa.home.ui.photoGrid.Bimp;

/**
 * Created by Administrator on 13-12-5.
 */
public class PictureLayout extends ViewGroup
{
    final int mMargin = 20;
    // 负责得到滚动属性的对象
    private Scroller mScroller;
    // 负责触摸的功能类
    private VelocityTracker mVelocityTracker;
    // 滚动的起始X坐标
    private int mScrollX = 0;
    // 默认显示第几屏
    private int mCurrentScreen = 0;
    // 滚动结束X坐标
    private float mLastMotionX;
    // //LogCat打印的标签
    private int mMaxLenghtX = 0;
    private static final int SNAP_VELOCITY = 1000;


    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;
    private  boolean isAdd = true;
    private ArrayList<String> arrFile = new ArrayList<String>();

    private int mTouchState = TOUCH_STATE_REST;

    private int mTouchSlop = 0;

    private OndelWebPicListener ondelWebPicListener;
    Context mContext = null;

    PicturButton mAddButton;
    PicturButton.OnPictureListener mOnPictureListener = null;
    ArrayList<PicturButton> mPictureChildren;
    FrameLayout mFrameLayout;
    ImageView mImageView;

    public PictureLayout(Context context) {
        super(context);
        mScroller = new Scroller(context);
        mContext = context;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mPictureChildren = new ArrayList<PicturButton>();
        mAddButton = new PicturButton(mContext);
//        setBackgroundDrawable(getResources().getDrawable(R.drawable.an_round_all_normal));
        this.addView(mAddButton.getRootView());
    }

    public PictureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mContext = context;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mPictureChildren = new ArrayList<PicturButton>();
//        this.setLayoutParams(new LayoutParams(
//                LayoutParams.MATCH_PARENT,
//
//                LayoutParams.WRAP_CONTENT));
//        setBackgroundDrawable(getResources().getDrawable(R.drawable.an_round_all_normal));

        mCurrentScreen = attrs.getAttributeIntValue(
                "http://schemas.android.com/apk/res/com.example.viewgroup",
                "default_screen", 0);
        mAddButton = new PicturButton(mContext);
        addView(mAddButton.getRootView());
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();

        switch (action) {
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
    public boolean onTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        final int action = event.getAction();
        final float x = event.getX();

        switch (action) {
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
                } else if (deltaX > 0) {
                    final int availableToScroll = getChildAt(getChildCount() - 1)
                            .getRight() - mScrollX - getWidth();
                    if (availableToScroll > 0) {
                        scrollBy(Math.min(availableToScroll, deltaX), 0);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:

                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();

                if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {

                    snapToScreen(mCurrentScreen - 1);
                } else if (velocityX < -SNAP_VELOCITY
                        && mCurrentScreen < getChildCount() - 1) {

                    snapToScreen(mCurrentScreen + 1);
                } else {
                    snapToDestination();
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:

                mTouchState = TOUCH_STATE_REST;
        }
        mScrollX = this.getScrollX();

        return true;
    }

    public void setOnPictureListener(PicturButton.OnPictureListener onPictureListener)
    {
        mOnPictureListener = onPictureListener;
        mAddButton.setImageListerner(onPictureListener);
    }

    private void snapToDestination() {
        final int screenWidth = getWidth();
        final int whichScreen = (mScrollX + (screenWidth / 2)) / screenWidth;

        snapToScreen(whichScreen);
    }

    public void snapToScreen(int whichScreen)
    {
        int width = getWidth();
        final int newX = whichScreen * width;
        if(newX + width  > mMaxLenghtX)
            return;
        mCurrentScreen = whichScreen;

        final int delta = newX - mScrollX;
        mScroller.startScroll(mScrollX, 0, delta, 0, Math.abs(delta) * 2);
        invalidate();
    }

    public void setToScreen(int whichScreen) {
        int width = getWidth();
        final int newX = whichScreen * width;
        if(newX + width  > mMaxLenghtX)
            return;
        mCurrentScreen = whichScreen;

        mScroller.startScroll(newX, 0, 0, 0, 10);
        invalidate();
    }


    public int getPictrueCount()
    {
        return mPictureChildren.size();
    }
    
    @SuppressWarnings("deprecation")
	public void addPictrue(PicturButton picture,final String fileLocal,final String
    		fid){
    	  addView(picture.getRootView());
          picture.setImageListerner(mOnPictureListener);
           BadgeView bv = new BadgeView(mContext,picture.getRootView());
          bv.setBadgeMargin(0);
          bv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.an_arrange_person_delete));
          bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
          bv.show();
              bv.setOnClickListener(new OnClickListener() {
                  @Override
                  public void onClick(View view) {

                	  BadgeView badgeView = (BadgeView)view;
                      if ( fileLocal != null){
                          removeView((View) badgeView.getParent());
                          Bimp.drr.remove(fileLocal);
                      }else if (fid != null){
                          ondelWebPicListener.onDelWeb(fid,(View)badgeView.getParent());
                      }
                  }
              });
    }

    public void addPictrue(PicturButton picture,final Bitmap bitmap,
                           final String fileLocal,final String fid)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        BadgeView bv = null;
        if (picture != null && bitmap != null) {

            addView(picture.getRootView());
            bv = new BadgeView(mContext,picture.getRootView());
            mPictureChildren.add(picture);
            picture.setImageListerner(mOnPictureListener);

        }else if (bitmap != null){
            mFrameLayout = (FrameLayout)inflater.inflate(R.layout.picture_button_layout,null);
            mImageView = (ImageView)mFrameLayout.findViewById(R.id.picture_button_add);
            mImageView.setImageBitmap(bitmap);
            addView(mFrameLayout);
            bv = new BadgeView(mContext,mFrameLayout);
        }else if (picture != null){
            addView(picture.getRootView());
            picture.setImageListerner(mOnPictureListener);
            return;
        }

        bv.setBadgeMargin(0);
        bv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.an_arrange_person_delete));
        bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        final BadgeView badgeView = bv;
        bv.show();
            bv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    if ( fileLocal != null){
                        removeView((View) badgeView.getParent());
                       
                        Bimp.drr.remove(fileLocal);
                    }else if (fid != null){
                        ondelWebPicListener.onDelWeb(fid,(View)badgeView.getParent());
                    }
                }
            });

    }

    public interface OndelWebPicListener{
        public void onDelWeb(String fid,View delView);
    }
   
    public void setOnDelWebPicListener(OndelWebPicListener onDelWebPicListener){
        this.ondelWebPicListener = onDelWebPicListener;
    }

   

    public ArrayList<String> getArrFile(){
        return arrFile;
    }


    public String getPicturePaths()
    {
        String res = "";
        JSONObject jRes = new JSONObject();
        JSONArray jPathArray = new JSONArray();
        try
        {
            for(int i =0; i < mPictureChildren.size(); i++)
            {
                JSONObject jPath = new JSONObject();
                jPath.put("path", mPictureChildren.get(i).mPath);
                jPathArray.put(jPath);
            }
            jRes.put("array",jPathArray);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jRes.toString();
    }

//    public void setchildren(ArrayList<String> datas)
//    {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        for(int i =0; i < datas.size(); i++)
//        {
//            ImageView imageView = new ImageView(mContext);
//
//            //View view = inflater.inflate(com.yxtech.moa2.R.layout.comm_fill_normal_view, null);
//            TextView cTextView = new TextView(this.mContext);
//            cTextView.setText(datas.get(i));
//            cTextView.setBackgroundColor(Color.parseColor("#FF0000"));
//           // addView()
//            this.addView(cTextView,getChildCount() -1);
//        }
//    }

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


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);
        for(int i = 0;i < getChildCount();i++) {
            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, heightMeasureSpec);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
           
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset()) {
            mScrollX = mScroller.getCurrX();
            scrollTo(mScrollX, 0);
            postInvalidate();
        }
    }

}
