package com.miicaa.home.ui.checkwork;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.checkwork.SignView.OnSignViewContentChangeListener;

@EViewGroup(R.layout.sign_layout)
public class SignLayout extends LinearLayout{

	Context mContext;
	
	static String SIGN_IN = "SignIn";
	static String SIGN_OUT = "SignOut";	
	Toast toast;
	
	@ViewById(R.id.signInView)
	SignView signInView;
	@ViewById(R.id.signOutView)
	SignView signOutView;
	
	public SignLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
//		super
	}

	public SignLayout(Context context, AttributeSet attrs) {
//		super(context, attrs);
		super(context, attrs, 0);
		mContext = context;
	}

	public SignLayout(Context context) {
//		super(context);
		super(context,null);
		mContext = context;
	}
	
	@SuppressLint("ShowToast")
	@AfterInject
	void afterInject(){
		toast = Toast.makeText(mContext, "成功设置提醒!", Toast.LENGTH_SHORT);
	}
	
	@AfterViews
	void aferView(){
		signInView.create(SignView.SIGN_IN)
		.setVisiable(View.GONE);
		signOutView.create(SignView.SIGN_OUT)
		.setVisiable(View.GONE)
		.setSignButtonClickable(false);
		signInView.setOnSignViewContentChangeListener(new OnSignViewContentChangeListener() {
			
			
			@Override
			public void onSignButtonClick() {
				CheckWorkSignActivity_.intent(mContext)
				.signState(CheckWorkSignActivity.SIGNIN)
				.startForResult(CheckWorkActivity.START_FORSIGN);
			}

			@Override
			public void onSignRemindButtonClick(int num) {
				setRemindTime(num, SIGN_IN);
			}
		});
		signOutView.setOnSignViewContentChangeListener(new OnSignViewContentChangeListener() {
			
		
			
			@Override
			public void onSignButtonClick() {
				CheckWorkSignActivity_.intent(mContext)
				.signState(CheckWorkSignActivity.SIGNOUT)
				.nowTime(nowDate)
				.xiabanTimeStr(signOutView.getWorkTime())
				.startForResult(CheckWorkActivity.START_FORSIGN);
			}

			@Override
			public void onSignRemindButtonClick(int num) {
				setRemindTime(num, SIGN_OUT);
			}
		});
	}
	
	public SignLayout setSignInContent(String date,String remindNum,String beginTime){
		if(date != null && date.length() > 0){
			signInView.setSignButtonDate(date)
			.setSignImageResouse(R.drawable.today_has_sigin)
			.setVisiable(View.VISIBLE);
			signOutView.setSignButtonClickable(true);
		}
		signInView.setRemindNum(remindNum);
		signInView.setWorkTimeText(beginTime);
		return this;
		
	}
	
	Long nowDate;
	public SignLayout addNowDate(Long nowDate){
		this.nowDate = nowDate;
		return this;
	}
	
	public SignLayout setSignOutContent(String date,String remindNum,String endTime){
		if(date != null && date.length() > 0){
			signOutView.setSignImageResouse(R.drawable.today_has_sigout);
			signOutView.setVisiable(View.VISIBLE);
			signOutView.setSignButtonDate(date);
			signOutView.setSignButtonClickable(true);
		}
		signOutView.setRemindNum(remindNum);
		signOutView.setWorkTimeText(endTime);
		return this;
	}
	
	private void setRemindTime(int num,final String action){
		String minute = String.valueOf(num);
		String url ="/attendance/phone/attend/setRemind";
		new RequestAdpater() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -8395523780535118173L;

			@Override
			public void onReponse(ResponseData data) {
				if(data.getResultState().equals(ResultState.eSuccess)){
					toast.setText("成功设置提醒！");
					toast.show();
				}else{
					toast.setText("失败"+data.getMsg());
					toast.show();
					if(action.equals(SIGN_IN)){
						signInView.setRemindNum(-1);
					}else if(action.equals(SIGN_OUT)){
						signOutView.setRemindNum(-1);
					}
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("minute",minute)
		.addParam("action",action)
		.notifyRequest();
	}

}
