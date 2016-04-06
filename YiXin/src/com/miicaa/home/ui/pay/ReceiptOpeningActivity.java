package com.miicaa.home.ui.pay;

import java.util.HashMap;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miicaa.common.base.Utils;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.pay.AccountRechargeActivity.OnResultListener;

@EActivity(R.layout.layout_pay_receipt_opening)
public class ReceiptOpeningActivity extends Activity {
	@Extra
	double mCurrentUseable;
	
	@ViewById(R.id.pay_headTitle)
	TextView headTitle;//标题
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.pay_commitButton)
	Button commit;
	@ViewById(R.id.receipt_opening_useMoney)
	TextView useMoney;
	@ViewById(R.id.receipt_opening_money)
	EditText money;
	@ViewById(R.id.receipt_opening_header)
	EditText header;
	@ViewById(R.id.receipt_opening_address)
	EditText address;
	@ViewById(R.id.receipt_opening_postcode)
	EditText postcode;
	@ViewById(R.id.receipt_opening_phone)
	EditText phone;
	@ViewById(R.id.receipt_opening_email)
	EditText email;
	@ViewById(R.id.receipt_opening_people)
	TextView people;
	
	Context mContext;
	String mMoney;
	String mHeader;
	String mAddress;
	String mPostCode;
	String mPeople;
	String mPhone;
	String mEmail;
	HashMap<String,String> map = new HashMap<String, String>();
	
	
	@Touch(R.id.topView)
	boolean touch(MotionEvent event,View v){
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			Utils.hiddenSoftBorad(mContext);
		}
		return false;
	}
	
	@AfterTextChange(R.id.receipt_opening_money)
	void onBefore(Editable et){
		try{
		float f = Float.parseFloat(et.toString());
		if(f>mCurrentUseable){
			PayUtils.showToast(mContext, "发票金额不能超过可开金额", 2000);
			money.setText(et.subSequence(0, et.length()-1));
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Click(R.id.pay_cancleButton)
	void cancel(){
		finish();
	}
	
	@Click(R.id.pay_commitButton)
	void commit(){
		if(isCommit()){
			PayUtils.showDialog(mContext);
			new RequestAdpater() {
				
				@Override
				public void onReponse(ResponseData data) {
					PayUtils.closeDialog();
					if(data.getResultState()==ResultState.eSuccess){
						if(ReceiptRecordActivity.instance!=null){
							ReceiptRecordActivity.instance.resetList();
							ReceiptRecordActivity.instance.requestMatter();
						}
						finish();
					}else{
						PayUtils.showToast(mContext, ""+data.getMsg(), 2000);
					}
					
				}
				
				@Override
				public void onProgress(ProgressMessage msg) {
					// TODO Auto-generated method stub
					
				}
			}
			.setUrl(mContext.getString(R.string.pay_receipt_commit_url))
			.addParam(map)
			.notifyRequest();
		}
	}
	
	@AfterInject
	void initData(){
		this.mContext = this;
		
	}
	
	@AfterViews
	void initUI() {

		back.setText("返回");
		headTitle.setText("索取发票");
		commit.setVisibility(View.VISIBLE);
		commit.setText("索取");
		initView();
	}
	
	

	private void initView() {
		useMoney.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", mCurrentUseable)));
		if(PayUtils.mUserName == null || PayUtils.mUserName.equals("")){
			PayUtils.requestUserInfo(mContext,new OnResultListener() {
				
				@Override
				public void onSuccess(Object obj) {
					people.setText(PayUtils.mUserName.equals("")?AccountInfo.instance().getLastUserInfo().getName():PayUtils.mUserName);
					phone.setText(PayUtils.mCallPhone.equals("")?PayUtils.mPhone:PayUtils.mCallPhone);
					email.setText(PayUtils.mEmail);
					
				}
				
				@Override
				public void onFaild(String msg) {
					
					PayUtils.showToast(mContext, ""+msg, 1000);
				}
			});
		}else{
			
			people.setText(PayUtils.mUserName);
			phone.setText(PayUtils.mCallPhone.equals("")?PayUtils.mPhone:PayUtils.mCallPhone);
			email.setText(PayUtils.mEmail);
		}
	}
	
	private boolean isCommit(){
		map.clear();
		mMoney = money.getText().toString();
		if(mMoney==null || mMoney.equals("")){
			PayUtils.showToast(mContext, "发票金额不能为空", 1000);
			return false;
		}
		map.put("balance", mMoney);
		mHeader = header.getText().toString();
		if(mHeader==null || mHeader.equals("")){
			PayUtils.showToast(mContext, "发票抬头不能为空", 1000);
			return false;
		}
		map.put("header", mHeader);
		mAddress = address.getText().toString();
		if(mAddress==null || mAddress.equals("")){
			PayUtils.showToast(mContext, "发票地址不能为空", 1000);
			return false;
		}
		map.put("address2", mAddress);
		mPostCode = postcode.getText().toString();
		if(mPostCode==null || mPostCode.equals("")){
			PayUtils.showToast(mContext, "邮编不能为空", 1000);
			return false;
		}
		map.put("postcode", mPostCode);
		mPeople = people.getText().toString();
		if(mPeople==null || mPeople.equals("")){
			PayUtils.showToast(mContext, "发票联系人不能为空", 1000);
			return false;
		}
		map.put("contacts", mPeople);
		mPhone = phone.getText().toString();
		if(mPhone==null || mPhone.equals("")){
			PayUtils.showToast(mContext, "联系人手机或座机不能为空", 1000);
			return false;
		}
		if(!PayUtils.matchers(mPhone, 1)){
			PayUtils.showToast(mContext, "联系人手机或座机格式不正确", 1000);
			return false;
		}
		map.put("phone", mPhone);
		mEmail = email.getText().toString();
		if(mEmail==null || mEmail.equals("")){
			PayUtils.showToast(mContext, "联系人电子邮箱不能为空", 1000);
			return false;
		}
		if(!PayUtils.matchers(mEmail, 2)){
			PayUtils.showToast(mContext, "联系人电子邮箱格式不正确", 1000);
			return false;
		}
		map.put("email", mEmail);
		return true;
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
	
	
}
