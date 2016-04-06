package com.yxst.epic.yixin.view;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

public class CalendarIUScrollView extends ScrollView {
	private ViewGroup contentView;
	private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = null;
	private View firstView, secondView;
	private float startY, startX;
	private boolean canPullDown;
	private boolean whetherToDispatchHorizontalMoveEventToFirstView;
	
	public CalendarIUScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				contentView = (ViewGroup) CalendarIUScrollView.this.getChildAt(0);
				firstView = contentView.getChildAt(0);
				secondView = contentView.getChildAt(1);
				if (onGlobalLayoutListener != null) {
					CalendarIUScrollView.this.getViewTreeObserver()
							.removeGlobalOnLayoutListener(
									onGlobalLayoutListener);
					onGlobalLayoutListener = null;
					if (onGetHeightListener != null) {
						onGetHeightListener.onGetHeight(CalendarIUScrollView.this
								.getHeight());
					}
				}
			}

		};

		this.getViewTreeObserver().addOnGlobalLayoutListener(
				onGlobalLayoutListener);

	}

	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
		return 0;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		if (secondView.getVisibility() != View.VISIBLE
				|| onFirstChildOnTopListener == null) {
			return super.dispatchTouchEvent(ev);
		}

		final int action = ev.getAction();
		if (action == MotionEvent.ACTION_UP) {
			return super.dispatchTouchEvent(ev);
		} else if (action == MotionEvent.ACTION_DOWN) {
			startX = ev.getX();
			startY = ev.getY();
			/****
			 * 第一个视图在顶端，初始触电在第二个视图以下，则允许"向下的MOVE动作"传递给第二个视图
			 * (若你的ListView有下拉刷新功能会有用)
			 ****/
			canPullDown = (getScrollY() == 0) && startY >= secondView.getTop();
			whetherToDispatchHorizontalMoveEventToFirstView = startY < secondView
					.getTop();
			onEndListener.onScrollEnd(this, firstView, 0);
			return super.dispatchTouchEvent(ev);
		} else if (action == MotionEvent.ACTION_MOVE) {
			float nowX = ev.getX();
			float deltaX = nowX - startX;
			float nowY = ev.getY();
			float deltaY = nowY - startY;

			/********** 若横向距离差大于纵向距离差且初始触点在第一个视图范围内，则只把Move事件传给第一个视图 ************/
			if (whetherToDispatchHorizontalMoveEventToFirstView
					&& Math.abs(deltaX) > Math.abs(deltaY)
					&& startY + getScrollY() < secondView.getTop()) {
				ev.setLocation(ev.getX(), ev.getY() + getScrollY());
				firstView.dispatchTouchEvent(ev);
				return true;
			}

			/***** 上拉 *****/
			if (deltaY < 0) {
				/********* 第二个视图充满整个屏幕 ***********/
				
				if (getScrollY() >= secondView.getTop()) {
					
					secondView.dispatchTouchEvent(ev);
					return true;
				}
				/***** 下拉 *****/
			} else {
				
				/****** 第一个视图顶住顶部且初始触点在第二个视图之下,则把事件传递给第二个视图 ********/
				if (getScrollY() == 0) {
					if (canPullDown) {
						float newY = ev.getY() - firstView.getBottom();
						if (newY >= 0) {
							ev.setLocation(ev.getX(), newY);
						}
						secondView.dispatchTouchEvent(ev);
						return true;
					}
					/***** 第一个视图没有顶住顶部，且第二个视图还没有种的第一个子视图未顶住顶部 ，则把事件传递给第二个视图 ***********/
				} else if (!onFirstChildOnTopListener.onFirstChildOnTop()) {
					if (getScrollY() < secondView.getTop()) {
						float newY = ev.getY() - firstView.getBottom();
						if (newY >= 0) {
							ev.setLocation(ev.getX(), newY);
						}
					}
					secondView.dispatchTouchEvent(ev);
					return true;
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	public interface OnGetHeightListener {
		public void onGetHeight(int height);
	}
	
	public interface OnScrollChangeListener{
		public void onScrollChange(View v,View v1,int x,int y,int oldx,int oldy);
	}

	public interface OnScrollEndListener {
		public boolean onScrollEnd(View v,View view,int y);
	}
	
	private OnGetHeightListener onGetHeightListener;
	private OnScrollChangeListener onScrollChangeListener;
	private OnScrollEndListener onEndListener;
	public void setOnScrollEndListener(OnScrollEndListener l){
		this.onEndListener = l;
	}
	public void setOnScrollChangeListener(OnScrollChangeListener l){
		this.onScrollChangeListener = l;
	}
	
	@Override
	public void fling(int velocityY) {
		// TODO Auto-generated method stub
		super.fling(velocityY);
		onEndListener.onScrollEnd(this, firstView,velocityY);
	}
	
	@Override
	protected void onScrollChanged(int x,int y,int oldx,int oldy){
		super.onScrollChanged(x,y,oldx,oldy);
		if(onScrollChangeListener != null)
			onScrollChangeListener.onScrollChange(this,firstView, x, y, oldx, oldy);
	}

	public void setOnGetHeightListener(OnGetHeightListener l) {
		onGetHeightListener = l;
	}

	public interface OnFirstChildOnTopListener {
		public boolean onFirstChildOnTop();
	}

	public OnFirstChildOnTopListener onFirstChildOnTopListener;

	public void setOnFirstChildOnTopListener(OnFirstChildOnTopListener l) {
		onFirstChildOnTopListener = l;
	}

	public void invalidateView() {
		//secondView = contentView.getChildAt(1);
		
	}
}