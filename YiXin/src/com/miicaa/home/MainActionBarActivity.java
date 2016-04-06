package com.miicaa.home;

import org.androidannotations.annotations.EActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@EActivity
public abstract class MainActionBarActivity extends ActionBarActivity{

	String showBackStr = null;
	Boolean isShowBackButton = false;
	View headView;
	ActionBar actionBar;
	Button rightButton;
	Button titleButton;
	Button backButton;

//	@SystemService
	LayoutInflater inflater;
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getSupportActionBar();
		inflater = 	LayoutInflater.from(this);
		headView = inflater.inflate(R.layout.normal_action_head, null);
		backShow();
	}
	
//	int touchY;
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		if(ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_UP){
//		}else if(ev.getAction() == MotionEvent.ACTION_UP){
//			if(touchY != ev.getY()){
//				activityYMove();
//				return true;
//			}
//		}
////		Log.d("PictureShowActivity","PictureShowActivity dispatchTouchEvent return : ....."+
////		super.dispatchTouchEvent(ev));
//		return false;
//	}
//	
	
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if(event.getAction() == MotionEvent.ACTION_DOWN){
//			touchY = (int) event.getY();
//		}else if(event.getAction() == MotionEvent.ACTION_UP){
//			if(touchY != event.getY()){
//				return true;
//			}
//		}
////		Log.d("PictureShowActivity","PictureShowActivity dispatchTouchEvent return : ....."+
////		super.dispatchTouchEvent(ev));
//		return false;
//		}



	private void backShow(){
		if(showHeadView() == null || !showHeadView()){
			return;
		}
		headView.setVisibility(View.VISIBLE);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		if(showBackButtonStr() != null || showBackButton()){
			showBackStr = showBackButtonStr();
			 backButton =(Button) headView.findViewById(R.id.leftBtn);
			backButton.setVisibility(View.VISIBLE);
			if(showBackStr != null){
				backButton.setText(showBackStr);
			}
			backButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					backButtonClick(v);
				}
			});
		}
		if(showTitleButtonStr() != null||showTitleButton()){
		    titleButton = (Button)headView.findViewById(R.id.titleBtn);
			titleButton.setVisibility(View.VISIBLE);
			titleButton.setText(showTitleButtonStr() != null ? showTitleButtonStr() : "miicaa");
			titleButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					titleButtonClick(v);
				}
			});
		}
		if(showRightButtonStr() != null || showRightButton()){
		    rightButton = (Button)headView.findViewById(R.id.rightBtn);
			rightButton.setVisibility(View.VISIBLE);
			rightButton.setText(showRightButtonStr() != null ? showRightButtonStr() : "提交");
			rightButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					rightButtonClick(v);
				}
			});
		}
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.gravity = lp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.LEFT;
		actionBar.setCustomView(headView, lp);
	}
	
	public void setRightButtonClickable(Boolean clickable){
		if(rightButton != null){
			rightButton.setClickable(clickable);
		}
	}
	
	public View getCenterButton(){
		return titleButton;
	}
	
	public View getRightButton(){
		return rightButton;
	}
	
	public View getLeftButton(){
		return backButton;
	}
	
	public ActionBar getMainActionBar(){
		return actionBar;
	}
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		Log.d("FramMainActionBarActivity", "dispatchTouchEvent ev :"+ev.getAction());
		if(ev.getAction() == MotionEvent.ACTION_MOVE ){
			Log.d("FramMainActionBarActivity", "dispatchTouchEvent actionmove touchY:"+ev.getY());
			activityYMove();
//			touchY = (int) ev.getY();
		}
//		}else if( ev.getAction() == MotionEvent.ACTION_UP){
//			Log.d("FramMainActionBarActivity", "dispatchTouchEvent actionUp touchY:"+ev.getY());
//			activityYMove();
////		Log.d("PictureShowActivity","PictureShowActivity dispatchTouchEvent return : ....."+
//		}
//		super.dispatchTouchEvent(ev));
		return super.dispatchTouchEvent(ev);
	}
	
	protected void activityYMove(){
		
	}
	
	public abstract String showBackButtonStr();
	public abstract Boolean showBackButton();
	public abstract void backButtonClick(View v);
	public abstract Boolean showTitleButton();
	public abstract String showTitleButtonStr();
	public abstract void titleButtonClick(View v);
	public abstract Boolean showRightButton();
	public abstract String showRightButtonStr();
	public abstract void rightButtonClick(View v);
	public abstract Boolean showHeadView();
	
	
}
