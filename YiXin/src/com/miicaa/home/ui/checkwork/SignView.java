package com.miicaa.home.ui.checkwork;

import java.util.Calendar;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.org.DateTimePopup;
import com.miicaa.home.ui.org.DateTimePopup.DateTimeStyle;
import com.miicaa.home.ui.org.DateTimePopup.OnNumChangeListener;
import com.miicaa.home.ui.org.OnheadViewClickListener;

@EViewGroup(R.layout.sign_view)
public class SignView extends LinearLayout{
	
	public  static int SIGN_IN = 0;
	public  static int SIGN_OUT = 1;
	
	int state_;
	Context mContext;
	
	@ViewById(R.id.title)
	TextView titleText;
	@ViewById(R.id.signBtn)
	Button signButton;
	@ViewById(R.id.signRemind)
	Button signRemindButton;
	@ViewById(R.id.progressBar)
	ProgressBar progressBar;
	@ViewById(R.id.imageView)
	ImageView imageView;

	public SignView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
		super(context,attrs);
	}

	public SignView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public SignView(Context context) {
//		super(context);
		super(context, null);
	}
	
	public SignView create(int state){
		state_ = state;
		String title = "";
		if(state_ == SIGN_IN){
			titleText.setText("上班");
			signButton.setText("今日签到");
			signRemindButton.setText("签到提醒");
		}else if(state_ == SIGN_OUT){
			titleText.setText("下班");
			signButton.setText("今日签退");
			signRemindButton.setText("签退提醒");
		}
		return this;
	}
	
	public SignView setSignButtonDate(String dateStr){
		/*
		 * 已经签到或签退
		 */
		signButton.setText(dateStr);
		signButton.setTextColor(
				mContext.getResources().getColor(R.color.today_sigin_color));
		signButton.setClickable(false);
		return this;
	}
	
	String workTime;
	public SignView setWorkTimeText(String workTime){
		String text = "";
		if(state_ == SIGN_IN){
			text = "上班";
		}else if(state_ == SIGN_OUT){
			text = "下班";
		}
		if(workTime != null){
			this.workTime = workTime;
			text = text+"\t"+workTime;
		}
		titleText.setText(text);
		return this;
	}
	
	public String getWorkTime(){
		return this.workTime;
	}
	
	public SignView setSignButtonClickable(Boolean clickable){
		signButton.setClickable(clickable);
		if(clickable){
			signButton.setTextColor(mContext.getResources().getColor(R.color.today_sigin_color));
		}else{
			signButton.setTextColor(Color.GRAY);
		}
		return this;
	}
	 
	public SignView setSignImageResouse(int resId){
		imageView.setImageResource(resId);
		return this;
	}
	
	public SignView setVisiable(int visiable){
		imageView.setVisibility(visiable);
		return this;
	}
	
	public SignView setRemindNum(int num){
		changeRemindBtnText(num);
		return this;
	}
	
	public  SignView setRemindNum(String num){
		if(num != null)
		num = num.length()  > 0 ? num : "0";
		else
			num = "0";
		int minute = Integer.parseInt(num);
		setRemindNum(minute);
		return this;
	}
	
	@Click(R.id.signBtn)
	void signBtnClick(){
			if(listener_ != null){
				listener_.onSignButtonClick();
			}
	}
	
	@Click(R.id.signRemind)
	void signRemindClick(){
//		switch (state_) {
//		case SIGN_IN:
//			break;
//		case SIGN_OUT:
//			break;
//		default:
//			break;
//		}
		showDatePick();
	}
	
	private void showDatePick(){
		 DateTimePopup.builder(mContext)
         .setDateTimeStyle(DateTimeStyle.eNumber)
         .setCancelText("清空")
         .setCommitText("设置")
         .setMinNum(0)
         .setMaxNum(60)
         .setOnheadViewClickListener(new OnheadViewClickListener() {
			@Override
			public void commitClick(Calendar calendar) {
			}
			@Override
			public void cancleClick() {
				changeRemindBtnText(0);
			}

			@Override
			public void commitClick(int num) {
				changeRemindBtnText(num);
				commitRemindTime(num);
			}
		})
         .setOnNumChangeListener(new OnNumChangeListener() {
			
			@Override
			public void onNumChange(int num) {
				changeRemindBtnText(num);
			}
		})
         .show(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
}
	
	private void commitRemindTime(int num){
		if(listener_ != null){
			listener_.onSignRemindButtonClick(num);
		}
	}
	
	private void changeRemindBtnText(int num){
		String str = null;
		String count  = String.valueOf(num);
		if(state_ == SIGN_IN){
			str = "上班前";
		}else if(state_ == SIGN_OUT){
			str = "下班前";
		}
		 str = str+count+"分钟提醒我";
		 SpannableStringBuilder sb = new SpannableStringBuilder();
		 if(num == 0 || num == -1){
			 if(state_ == SIGN_IN){
			 str = "签到提醒";
			 }else{
				 str = "签退提醒";
			 }
			 sb.append(str);
		 }else{
			 sb.append(str);
			 sb.setSpan(new ForegroundColorSpan(Color.BLUE), str.indexOf(count), 
					 str.indexOf(count)+count.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		 }
		signRemindButton.setText(sb);
	}
	
	
	OnSignViewContentChangeListener listener_;
	public interface OnSignViewContentChangeListener{
		void onSignButtonClick();
		void onSignRemindButtonClick(int num);
	}
	
	public void setOnSignViewContentChangeListener(OnSignViewContentChangeListener listener){
		this.listener_ = listener;
	}
}
