package com.miicaa.home.ui.checkwork;

import java.util.Calendar;
import java.util.Date;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.ui.org.DateTimePopup;
import com.miicaa.home.ui.org.DateTimePopup.DateTimeStyle;
import com.miicaa.home.ui.org.DateTimePopup.OnDateTimeChange;
import com.miicaa.home.ui.org.OnheadViewClickListener;
import com.miicaa.utils.AllUtils;

@EViewGroup(R.layout.screen_checkwork_view)
public class CheckWorkScreenLayout extends LinearLayout{
	
	public final static int MY_CHECKWORK = 0;
	public final static int ALL_CHECKWORK = 1;
	public final static int TONGJI_CHECKWORK = 2;
	
	Date beginDate;
	Date endDate;
	
	Context mContext;
	int state_;
	
	@SystemService
	LayoutInflater inflater;
	
	@ViewById(R.id.beginDateBtn)
	Button beginDateButton;
	@ViewById(R.id.endDateBtn)
	Button endDateButton;
	@ViewById(R.id.nameTextView)
	TextView  nameTextView;
	@ViewById(R.id.nameBtn)
	Button nameButton;
	@ViewById(R.id.orgTextView)
	TextView orgTextView;
	@ViewById(R.id.orgBtn)
	Button orgButton;
	@ViewById(R.id.bottomView)
	RelativeLayout bottomLayout;
	
	Button completeButton;
	Button clearButton;
	
	Toast toast;

	public CheckWorkScreenLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public CheckWorkScreenLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		
	}
	
	public CheckWorkScreenLayout(Context context) {
		super(context, null);
		this.mContext = context;
	}
	
	public CheckWorkScreenLayout setState(int state){
		state_ = state;
		showViews();
		return this;
	}
	
	@AfterInject
	void afterInject(){
		
	}
	@AfterViews
	void afterViews(){
		toast = Toast.makeText(mContext, "开始时间必须大于结束时间", Toast.LENGTH_SHORT);
		clearButton = (Button)bottomLayout.findViewById(R.id.clearBtn);
		completeButton = (Button)bottomLayout.findViewById(R.id.completeBtn);
		clearButton.setOnClickListener(onClickListener);
		completeButton.setOnClickListener(onClickListener);
	}
	
	private void showViews(){
		switch (state_) {
		case MY_CHECKWORK:
			nameTextView.setVisibility(View.GONE);
			nameButton.setVisibility(View.GONE);
			orgButton.setVisibility(View.GONE);
			orgTextView.setVisibility(View.GONE);
			break;
		case ALL_CHECKWORK:
			nameTextView.setVisibility(View.VISIBLE);
			nameButton.setVisibility(View.VISIBLE);
			orgButton.setVisibility(View.VISIBLE);
			orgTextView.setVisibility(View.VISIBLE);
			break;
		case TONGJI_CHECKWORK:
			nameTextView.setVisibility(View.VISIBLE);
			nameButton.setVisibility(View.VISIBLE);
			orgButton.setVisibility(View.VISIBLE);
			orgTextView.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	@Click(R.id.beginDateBtn)
	void beginDateClick(){
		setScreenDateStr(DateStatus.eBegin);
//		if(listener_ != null){
//			listener_.beginDateClick(beginDateStr);
//		}
	}
	
	@Click(R.id.endDateBtn)
	void endDateClick(){
		setScreenDateStr(DateStatus.eEnd);
//		if(listener_ != null){
//			listener_.beginDateClick(endDateStr);
//		}
	}
	
	@Click(R.id.nameBtn)
	void nameButtonClick(){
		if(listener_ != null){
			listener_.nameButtonClick();
		}
	}
	@Click(R.id.orgBtn)
	void orgButtonClick(){
		if(listener_ != null){
			listener_.orgButtonClick();
		}
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.clearBtn:
				if(listener_ != null){
					listener_.onClearClick();
				}
				break;
			case R.id.completeBtn:
				if(listener_ != null){
					listener_.onCompleteClick();
				}
				break;
			default:
				break;
			}
		}
	};
	

	public void setAllViewText(String userName,String orgName,
		String beginDate,String endDate){
		setUserNames(userName);
		setOrgNames(orgName);
		setBeginDate(beginDate);
		setEndDate(endDate);
	}
	
	public void setUserNames(String names){
		nameButton.setText(names);
	}
	
	public void setOrgNames(String names){
		orgButton.setText(names);
	}
	
	public void setBeginDate(String date){
//		beginDateStr = date;
		beginDateButton.setText(date);
	}
	
	public void setEndDate(String date){
//		endDateStr = date;
		endDateButton.setText(date);
	}
	
//	String beginDateStr = null;
//	String endDateStr = null;
	private void setScreenDateStr(final DateStatus status_){
		DateTimePopup.builder(mContext)
		.setDateTimeStyle(DateTimeStyle.eDate)
		.setCancelText("清空")
		.setCommitText("设置")
		.setOnheadViewClickListener(new OnheadViewClickListener() {
			
			@Override
			public void commitClick(int num) {
				
			}
			
			@Override
			public void commitClick(Calendar calendar) {
				Date date= calendar.getTime();
				setDate(date, status_);
			}
			
			@Override
			public void cancleClick() {
				setDate(null, status_);
			}
		})
		.setOnDateTimeChangeListener(new OnDateTimeChange() {
			
			@Override
			public void onDateTimeChange(Calendar c, DateTimeStyle style) {
				Date date= c.getTime();
				setDate(date, status_);
			}
		}).show(Gravity.BOTTOM|Gravity.RIGHT, 0, 0);
	}
	
	private void setDate(Date date,DateStatus status_){
		String dateStr = "";
		if(date != null){
		 dateStr = AllUtils.getYearTime(date.getTime());
		}
		if(status_.equals(DateStatus.eBegin)){
			beginDate = date;
			setBeginDate(dateStr);
			if(listener_ != null){
				listener_.beginDateClick(dateStr);
			}
		}else if(status_.equals(DateStatus.eEnd)){
			endDate = date;
			
//			if(beginDate != null && (endDate != null && (endDate.getTime() <= beginDate.getTime()))){
//				toast.show();
//				return;
//			}
			setEndDate(dateStr);
			if(listener_ != null){
				listener_.endDateClick(dateStr);
			}
		}
	}
	
	enum DateStatus{
		eBegin,
		eEnd
	}
	
	OnCheckWorkScreenCotentClickListener listener_;
	public interface OnCheckWorkScreenCotentClickListener{
		void beginDateClick(String date);
		void endDateClick(String date);
	    void nameButtonClick();
	    void orgButtonClick();
	    void onCompleteClick();
	    void onClearClick();
	}
	
	public void setOnCheckWorkScreenCotentClickListener(OnCheckWorkScreenCotentClickListener listener){
		this.listener_ = listener;
	}
	
}
