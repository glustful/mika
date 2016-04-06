package com.miicaa.detail;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.BottomScreenPopup.BottomBuilder;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.home.R;

@EViewGroup(R.layout.progress_foot_view)
public class ApprovalFootView extends RelativeLayout{
	public static ApprovalFootView instance;
	@ViewById(R.id.imgP)
	ImageView approval;
	@ViewById(R.id.textP)
	TextView apprText;
	@ViewById(R.id.progress)
	LinearLayout approve;
	@ViewById(R.id.plan)
	LinearLayout plan;
	@ViewById(R.id.remind)
	LinearLayout remind;

	Boolean yes = false;
	Boolean approveShown = false;
	Boolean remindPlanShown = false;
	OnApprovalClickLintener listener;
	Context context;
	ArrayList<PopupItem> items;
	public BottomBuilder builder;
	
	

	public ApprovalFootView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		items = new ArrayList<PopupItem>();
		instance = this;
		// TODO Auto-generated constructor stub
		
	}

	public ApprovalFootView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		instance = this;
		// TODO Auto-generated constructor stub
	}

	public ApprovalFootView(Context context) {
		super(context);
		this.context = context;
		instance = this;
		// TODO Auto-generated constructor stub
	}
	
	@AfterInject
	void afterinject(){
		
		items = new ArrayList<PopupItem>();
		
	}
	
	public static  ApprovalFootView getInstance(){
		// TODO Auto-generated constructor stub
		return instance;
	}
	
	@SuppressWarnings("deprecation")
	@AfterViews
	void afterViews(){
		approval.setBackgroundDrawable(getResources().getDrawable(R.drawable.matter_approval));
		apprText.setText("审批");
//		if(yes){
//			approval.setVisibility(View.GONE);
//		}
	}
	
	@Click(R.id.progress)
	void approvalClick(){
		if(items.size() == 0){
//			listener.approvalClickWithOutItems();
			return;
		}
			
		builder = BottomScreenPopup.builder(context)
		.setItems(items)
		.setMargin(false)
		.setDrawable(R.drawable.white_color_selector)
		
		.setOnMessageListener(new OnMessageListener() {
			
			@Override
			public void onClick(PopupItem msg) {
				// TODO Auto-generated method stub
				listener.approvalClick(msg);
			}
		});
		builder.show();
		
		
	}
	
	@Click(R.id.plan)
	void planClick(){
		listener.planClick();
	}
	
	@Click(R.id.remind)
	void remindClick(){
		listener.remindClick();
	}
	
	boolean isMsg(String type,PopupItem msg){
		return type.equals(msg)?true:false;
	}
	
	public interface OnApprovalClickLintener{
		void approvalClick(PopupItem msg);
		void approvalClickWithOutItems();
		void planClick();
		void remindClick();
	}
	
	public void setonApprovalClickListener(OnApprovalClickLintener listener){
		this.listener = listener;
	}
	
	public void setPopItems(ArrayList<PopupItem> items){
		this.items.clear();
		this.items.addAll(items);
	}
	
	//不能填写审批的时候
	public void nodoApprove(Boolean yes){
		approveShown = !yes;
		if(yes){
			approve.setVisibility(View.GONE);
		}else{
			approve.setVisibility(View.VISIBLE);
		}
		Log.d(TAG, "nodoApprove approve shown:"+approve.isShown());
	}
	
	
	public void nodoPlanNRemindTime(Boolean yes){
		remindPlanShown = !yes;
		if(yes){
		  plan.setVisibility(View.GONE);
		  remind.setVisibility(View.GONE);
		}else{
			plan.setVisibility(View.VISIBLE);
			remind.setVisibility(View.VISIBLE);
		}
		Log.d(TAG, "nodoPlanNRemindTime: plan shown:"+plan.isShown()+"remind shown:"+remind.isShown());
	}
	
	public void howShow(){
		if(!approveShown && !remindPlanShown){
			this.setVisibility(View.GONE);
		}
		Log.d(TAG, "howShow:"+this.isShown());
	}
 
    static String TAG = "ApprovalFootView";
	
	
}
