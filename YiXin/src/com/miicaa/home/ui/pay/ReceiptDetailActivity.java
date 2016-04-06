package com.miicaa.home.ui.pay;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.miicaa.base.RoundImage.CircularImage;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;

@EActivity(R.layout.layout_pay_receipt_detail)
public class ReceiptDetailActivity extends Activity {

	@Extra
	String jsonStr;
	
	@ViewById(R.id.pay_cancleButton)
	Button cancel;
	@ViewById(R.id.pay_headTitle)
	TextView title;
	@ViewById(R.id.receipt_detail_head)
	CircularImage headImg;
	@ViewById(R.id.receipt_detail_name)
	TextView name;
	@ViewById(R.id.receipt_detail_money)
	TextView money;
	@ViewById(R.id.receipt_detail_header)
	TextView header;
	@ViewById(R.id.receipt_detail_address)
	TextView address;
	@ViewById(R.id.receipt_detail_code)
	TextView code;
	@ViewById(R.id.receipt_detail_people)
	TextView people;
	@ViewById(R.id.receipt_detail_phone)
	TextView phone;
	@ViewById(R.id.receipt_detail_email)
	TextView email;
	
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
	void initUI(){
		cancel.setText("返回");
		title.setText("发票详情");
		initView();
	}
	
	private void initView() {
		try{
			JSONObject obj = new JSONObject(jsonStr);
			Tools.setHeadImgWithoutClick(obj.optString("userCode"), headImg);
			name.setText(obj.optString("userName"));
			money.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(obj.optDouble("balance", 0)));
			header.setText(obj.optString("header"));
			address.setText(obj.optString("address2"));
			code.setText(obj.optString("postcode"));
			people.setText(obj.optString("contacts"));
			phone.setText(obj.optString("phone"));
			email.setText(obj.optString("email"));
		}catch(Exception e){
			
		}
		
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
}
