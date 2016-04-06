package com.miicaa.detail;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.miicaa.home.R;

@EViewGroup(R.layout.progress_foot_view)
public class ProgressFootView extends LinearLayout{
    public static ProgressFootView instance;
	@ViewById(R.id.progress)
	LinearLayout progress;
	@ViewById(R.id.imgP)
	ImageView progressview;
	@ViewById(R.id.plan)
	LinearLayout plan;
	@ViewById(R.id.remind)
	LinearLayout remind;
//	@View
	
	Boolean yes = false;
	OnProgressClickListener listener;
	Context context;
	@SuppressLint("NewApi")
	public ProgressFootView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		instance = this;
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public ProgressFootView(Context context, AttributeSet attrs) {
		super(context, attrs);
		instance = this;
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public ProgressFootView(Context context) {
		super(context);
		instance = this;
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	@AfterInject
	void afterInject(){
		
	}
	@SuppressWarnings("deprecation")
	@AfterViews
	void afterView(){
		progressview.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.matter_progress));
	}
	
	public static ProgressFootView getInstance(){
		return instance;
	}
	
	@Click(R.id.progress)
	void progressClick(){
		listener.progressClick();
	}
	@Click(R.id.plan)
	void planClick(){
		listener.planClick();
	}
	@Click(R.id.remind)
	void remindClick(){
		listener.remindClick();
	}
	
	public interface OnProgressClickListener{
		void progressClick();
		void planClick();
		void remindClick();
	}
	
	public void setOnProgressClickListener(OnProgressClickListener listener){
		this.listener = listener;
	}
	
	//不允许填写进展
	public void nodoProgress(Boolean yes){
		if(yes){
		progress.setVisibility(View.GONE);
		}else{
			progress.setVisibility(View.VISIBLE);
		}
	
	}
	
	public void nodoPlanNRemindTime(Boolean yes){
		if(yes){
		  plan.setVisibility(View.GONE);
		  remind.setVisibility(View.GONE);
		}else{
			plan.setVisibility(View.VISIBLE);
			remind.setVisibility(View.VISIBLE);
		}
	}

	public void howShow(){
		if(!progress.isShown()&&!plan.isShown()&&!remind.isShown()){
			this.setVisibility(View.GONE);
		}
		Log.d("ProgressFootView","howShown :"+this.isShown());
	}
}
