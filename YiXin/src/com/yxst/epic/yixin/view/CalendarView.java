package com.yxst.epic.yixin.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.miicaa.home.ui.calendar.CustomDate;
import com.miicaa.home.ui.calendar.CustomDate.State;
import com.miicaa.home.ui.calendar.DateUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class CalendarView extends View {

	private static final String TAG = "CalendarView";

	private static final int TOTAL_COL = 7;
	private static final int TOTAL_ROW = 6;

	private Paint mCirclePaint;
	private Paint mTextPaint;
	private int mViewWidth;
	private int mViewHight;
	private int mCellSpace;
	private int mHCellSpace;
	private int mVCellSpace;
	private Row rows[] = new Row[TOTAL_ROW];
	private static CustomDate mShowDate;// 自定义的日期 包括year month day

	private static final int WEEK = 7;
	private CalendarCallBack mCallBack;// 回调
	private int touchSlop;
	private boolean callBackCellSpace;

	public interface CalendarCallBack {

		void clickDate(CustomDate date);// 回调点击的日期

	}

	// 返回当前显示屏的最早时间
	public String getEarlyDate() {
		Calendar ca = Calendar.getInstance();
		
		ca.set(mShowDate.year, mShowDate.month-1, 1, 0, 0, 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(ca.getTime());
	}

	// 返回当前显示屏的最晚时间
	public String getLastDate() {
		Calendar ca = Calendar.getInstance();
		ca.set(mShowDate.year, mShowDate.month-1, ca.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(ca.getTime());
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);

	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public CalendarView(Context context) {
		super(context);
		init(context);
	}

	public CalendarView(Context context, CalendarCallBack mCallBack) {
		super(context);

		this.mCallBack = mCallBack;
		init(context);
	}

	public void setCallBack(CalendarCallBack callback) {
		this.mCallBack = callback;
		// this.mCallBack.clickDate(this.mClickCell.date);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < TOTAL_ROW; i++) {
			if (rows[i] != null)
				rows[i].drawCells(canvas);
		}
	}

	private void init(Context context) {
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint.setStyle(Paint.Style.FILL);
		mCirclePaint.setColor(Color.parseColor("#F24949"));
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		initDate();

	}

	private void initDate() {

		mShowDate = new CustomDate();

		fillDate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mViewWidth = w;
		mViewHight = h;
		mCellSpace = Math.min(mViewHight / TOTAL_ROW, mViewWidth / TOTAL_COL);
		mHCellSpace = mViewWidth / TOTAL_COL;
		mVCellSpace = mViewHight / TOTAL_ROW;

		mTextPaint.setTextSize(mCellSpace / 3);
	}

	private Cell mClickCell;
	private float mDownX;
	private float mDownY;

	private boolean isSkip = false;

	/*
	 * 
	 * 触摸事件为了确定点击的位置日期
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = event.getX();
			mDownY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			float disX = event.getX() - mDownX;
			float disY = event.getY() - mDownY;
			if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
				int col = (int) (mDownX / mHCellSpace);
				int row = (int) (mDownY / mVCellSpace);
				measureClickCell(col, row);
			}
			break;
		}
		return true;
	}

	private void measureClickCell(int col, int row) {
		if (col >= TOTAL_COL || row >= TOTAL_ROW)
			return;
		if (mClickCell != null) {
			
			rows[mClickCell.j].cells[mClickCell.i] = mClickCell;
			System.out.println("click="+mClickCell +"s="+mClickCell.states);
		}
		if (rows[row] != null) {
			mClickCell = new Cell(rows[row].cells[col].date,
					rows[row].cells[col].getState(), rows[row].cells[col].i,
					rows[row].cells[col].j);
			rows[row].cells[col].setState(State.CLICK_DAY);
			CustomDate date = rows[row].cells[col].date;
			
			date.week = col;
			mShowDate.year = date.year;
			mShowDate.month = date.month;
			mShowDate.day = date.day;
			mCallBack.clickDate(date);
			invalidate();
		}
	}

	// 组
	class Row {
		public int j;

		Row(int j) {
			this.j = j;
		}

		public Cell[] cells = new Cell[TOTAL_COL];

		public void drawCells(Canvas canvas) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null)
					cells[i].drawSelf(canvas);
			}

		}
	}

	// 单元格
	class Cell {
		private CustomDate date;
		private State states;
		public int i;
		public int j;

		public Cell(CustomDate date, State state, int i, int j) {
			super();
			this.date = date;
			this.date.setState(state);
			this.states = state;
			this.i = i;
			this.j = j;
		}

		// 绘制一个单元格 如果颜色需要自定义可以修改
		public void drawSelf(Canvas canvas) {
			switch (states) {
			case CURRENT_MONTH_DAY:
				mTextPaint.setColor(Color.parseColor("#80000000"));
				break;
			case NEXT_MONTH_DAY:
			case PAST_MONTH_DAY:
				mTextPaint.setColor(Color.parseColor("#40000000"));
				break;
			case TODAY:
				mCirclePaint.setColor(Color.parseColor("#21bcd8"));
				mTextPaint.setColor(Color.parseColor("#fffffe"));
				canvas.drawCircle((float) (mHCellSpace * (i + 0.5)),
						(float) ((j + 0.5) * mVCellSpace), mCellSpace / 2,
						mCirclePaint);
				break;
			case CLICK_DAY:
				mCirclePaint.setColor(Color.parseColor("#F38fa9"));
				
				mTextPaint.setColor(Color.parseColor("#fffffe"));
				canvas.drawCircle((float) (mHCellSpace * (i + 0.5)),
						(float) ((j + 0.5) * mVCellSpace), mCellSpace / 2,
						mCirclePaint);
				break;
			}
			// 绘制文字
			System.out.println("i="+i+"j="+j+"d="+date.day);
			String content = date.day + "";
			canvas.drawText(content,
					(float) ((i + 0.5) * mHCellSpace - mTextPaint
							.measureText(content) / 2), (float) ((j + 0.7)
							* mVCellSpace - mTextPaint.measureText(content, 0,
							1) / 2), mTextPaint);
		}

		public State getState() {
			return states;
		}

		public void setState(State states) {
			this.states = states;
			//this.date.state = states;
		}
	}

	

	/**
	 * 填充日期的数据
	 */
	private void fillDate() {

		fillMonthDate();

	}

	/**
	 * 填充月份
	 */
	private void fillMonthDate() {
		int monthDay = DateUtil.getCurrentMonthDay();
		int lastMonthDays = DateUtil.getMonthDays(mShowDate.year,
				mShowDate.month - 1);
		int currentMonthDays = DateUtil.getMonthDays(mShowDate.year,
				mShowDate.month);
		int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year,
				mShowDate.month);
		boolean isCurrentMonth = false;
		if (DateUtil.isCurrentMonth(mShowDate)) {
			isCurrentMonth = true;
		}
		int day = 0;
		mClickCell = null;
		for (int j = 0; j < TOTAL_ROW; j++) {
			rows[j] = new Row(j);
			for (int i = 0; i < TOTAL_COL; i++) {
				int postion = i + j * TOTAL_COL;
				if (postion >= firstDayWeek
						&& postion < firstDayWeek + currentMonthDays) {
					day++;
					
					if (isCurrentMonth && day == monthDay) {
						CustomDate date = CustomDate.modifiDayForObject(
								mShowDate, day);
						if(mClickCell == null)
						mClickCell = new Cell(date, State.TODAY, i, j);
						date.week = i;
						// mCallBack.clickDate(date);
						rows[j].cells[i] = new Cell(date, State.TODAY, i, j);
						continue;
					}
					if(isSkip){
						
						
						if(day == mShowDate.day){
							isSkip = false;
							CustomDate date = CustomDate.modifiDayForObject(
									mShowDate, day);
							
							date.week = i;
							this.mClickCell = new Cell(date, State.CURRENT_MONTH_DAY, i, j);
							rows[j].cells[i] = new Cell(date, State.CLICK_DAY, i, j);
							continue;
						}
					}
					rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(
							mShowDate, day), State.CURRENT_MONTH_DAY, i, j);
				} else if (postion < firstDayWeek) {
					rows[j].cells[i] = new Cell(new CustomDate(mShowDate.year,
							mShowDate.month - 1, lastMonthDays
									- (firstDayWeek - postion - 1)),
							State.PAST_MONTH_DAY, i, j);
				} else if (postion >= firstDayWeek + currentMonthDays) {
					rows[j].cells[i] = new Cell((new CustomDate(mShowDate.year,
							mShowDate.month + 1, postion - firstDayWeek
									- currentMonthDays + 1)),
							State.NEXT_MONTH_DAY, i, j);
				}
			}
		}
	}

	public void update() {
		fillDate();
		invalidate();
	}

	public boolean backToday() {
		this.mClickCell = null;
		boolean flag = false;
		if(DateUtil.isCurrentMonth(CalendarView.mShowDate))
			flag = true;
		initDate();
		invalidate();
		return flag;
	}

	// 向右滑动
	public void rightSilde() {  
		this.mClickCell = null;
		if (mShowDate.month == 12) {
			mShowDate.month = 1;
			mShowDate.year += 1;
		} else {
			mShowDate.month += 1;
		}
		this.isSkip  = true;
		update();
	}

	// 向左滑动
	public void leftSilde() {
		this.mClickCell = null;
		if (mShowDate.month == 1) {
			mShowDate.month = 12;
			mShowDate.year -= 1;
		} else {
			mShowDate.month -= 1;
		}
		this.isSkip  = true;
		update();
	}

	public CharSequence getMShowDate() {
		
		return CalendarView.mShowDate.year + "年" + mShowDate.month + "月";
	}

	public CustomDate skipDate(Calendar selectedTime) {
		this.mClickCell = null;
		mShowDate.year = selectedTime.get(Calendar.YEAR);
		mShowDate.month = selectedTime.get(Calendar.MONTH)+1;
		mShowDate.day = selectedTime.get(Calendar.DAY_OF_MONTH);
		this.isSkip  = true;
		update();
		return mShowDate;
	}

	public CharSequence getMShowTimeDate() {
		if(DateUtil.isToday(mShowDate)){
		return CalendarView.mShowDate.year + "年" + mShowDate.month + "月" + mShowDate.day + "日" + " 今天";
		}else{
			return CalendarView.mShowDate.year + "年" + mShowDate.month + "月" + mShowDate.day + "日";
		}
		
	}

	public CustomDate getLeftDate() {
		Calendar ca = Calendar.getInstance();
		int i = 0,j=0;
		ca.set(mShowDate.year, mShowDate.month-1, mShowDate.day-1);
		if(mClickCell != null){
			j = mClickCell.i;
			i = mClickCell.j;
			rows[i].cells[j] = mClickCell;
			System.out.println("state="+mClickCell);
			//rows[i].cells[j].setState(State.CURRENT_MONTH_DAY);
			if(j>0){
				mClickCell = new Cell(rows[i].cells[j-1].date,
						rows[i].cells[j-1].getState(), j-1,
						i);
				rows[i].cells[j-1].setState(State.CLICK_DAY);
			}else if(i>0){
				mClickCell = new Cell(rows[i-1].cells[6].date,
						rows[i-1].cells[6].getState(), 6,
						i-1);
				rows[i-1].cells[6].setState(State.CLICK_DAY);
			}
		}
		mShowDate.year = ca.get(Calendar.YEAR);
		mShowDate.month = ca.get(Calendar.MONTH)+1;
		mShowDate.day = ca.get(Calendar.DAY_OF_MONTH);
		invalidate();
		return new CustomDate(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH)+1, ca.get(Calendar.DAY_OF_MONTH));
	}

	public CustomDate getRightDate() {
		Calendar ca = Calendar.getInstance();
		int i = 0,j=0;
		ca.set(mShowDate.year, mShowDate.month-1, mShowDate.day+1);
		if(mClickCell != null){
			j = mClickCell.i;
			i = mClickCell.j;
			rows[i].cells[j] = mClickCell;
			System.out.println("right="+mClickCell.date);
			if(j<6){
				
				mClickCell = new Cell(rows[i].cells[j+1].date,
						rows[i].cells[j+1].getState(), j+1,
						i);
				rows[i].cells[j+1].setState(State.CLICK_DAY);
			}else if(i<5){
				mClickCell = new Cell(rows[i+1].cells[0].date,
						rows[i+1].cells[0].getState(), 0,
						i+1);
				rows[i+1].cells[0].setState(State.CLICK_DAY);
			}
		}
		System.out.println("right==="+mClickCell.date);
		mShowDate.year = ca.get(Calendar.YEAR);
		mShowDate.month = ca.get(Calendar.MONTH)+1;
		mShowDate.day = ca.get(Calendar.DAY_OF_MONTH);
		invalidate();
		return new CustomDate(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH)+1, ca.get(Calendar.DAY_OF_MONTH));
	}
}
