package com.miicaa.home.ui.pay;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.miicaa.home.R;

@EActivity(R.layout.layout_pay_othercombo_activity)
public class OtherComboActivity extends Activity {
	
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.pay_headTitle)
	TextView headTitle;
	Context mContext;  
	
	@Click(R.id.pay_cancleButton)
	void cancel(){
		finish();
	}
	
	@AfterInject
	void initData(){
		this.mContext = this;
	}

	
	
	@AfterViews
	void initUI() {
		
		back.setText("返回");
		headTitle.setText("其它已购买套餐");
	}
	
	
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
}
