package com.miicaa.home.ui.pay;

import java.util.HashMap;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.miicaa.common.base.Utils;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;

@EActivity(R.layout.layout_pay_account_activity)
public class AccountRechargeActivity extends Activity {
	

	
	@Override
	protected void onResume() {
		
		super.onResume();
		Utils.hiddenSoftBorad(mContext);
	}
	@ViewById(R.id.pay_headTitle)
	TextView headTitle;//标题
	@ViewById(R.id.pay_account_bank)
	LinearLayout bank_layout;
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.pay_person_people)
	TextView peopleNmae;
	@ViewById(R.id.pay_person_phone)
	EditText peoplePhone;
	@ViewById(R.id.pay_person_email)
	EditText peopleEmail;
	@ViewById(R.id.pay_bank_name)
	EditText bankName;
	@ViewById(R.id.pay_bank_peoplename)
	EditText bankPeopleName;
	@ViewById(R.id.pay_bank_account)
	EditText bankAccount;
	@ViewById(R.id.pay_account_select)
	SelectFaceValueView selectValue;
	
	@ViewById(R.id.pay_rgway)
	RadioGroup rgWay;
	@ViewById(R.id.pay_id_account)
	RadioButton account;
	@ViewById(R.id.pay_id_alipay)
	RadioButton rbAlipay;
	@ViewById(R.id.pay_id_wx)
	RadioButton rbwx;
	
	@ViewById(R.id.pay_account_commit)
	Button commit;
	/*@ViewById(R.id.bank_seller_name)
	TextView sellerName;
	@ViewById(R.id.bank_seller_no)
	TextView sellerNo;
	@ViewById(R.id.bank_seller_no_copy)
	TextView sellerNoCopy;*/
	Context mContext;
	float mFaceValue;
	String mWay = "";
	String mName = "";
	String mPhone = "";
	String mEmail = "";
	String mBankName;
	String mBankPeopleName;
	String mBankAccount;
	String mBankSellerName;
	String mBankSellerNo;
	
	@Click(R.id.pay_cancleButton)
	void cancel(){
		
		finish();
	}
	
	@Touch(R.id.topView)
	boolean touch(MotionEvent event,View v){
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			Utils.hiddenSoftBorad(mContext);
		}
		return false;
	}

	@Click(R.id.pay_id_account)
	void account(){
		mWay = "account";
		bank_layout.setVisibility(View.GONE);
	}
	
	@Click(R.id.pay_id_alipay)
	void alipay(){
		mWay = "alipay";
		bank_layout.setVisibility(View.GONE);
	}
	
	@Click(R.id.pay_id_wx)
	void wx(){
		mWay = "wx";
		bank_layout.setVisibility(View.GONE);
	}
	
	@Click(R.id.pay_id_bank)
	void bank(final View rb){
		mWay = "bank";
		/*if(mBankSellerName==null || mBankSellerName.equals("")){
			PayUtils.requestBankInfo(mContext, new OnResultListener() {
				
				@Override
				public void onSuccess(Object obj) {
					if(obj instanceof JSONObject){
						JSONObject json = (JSONObject) obj;
						mBankSellerName = json.optString("cardName");
						mBankSellerNo = json.optString("cardNo");
						sellerName.setText(mBankSellerName);
						sellerNo.setText(mBankSellerNo);
						sellerNoCopy.setText(mBankSellerNo);
						bank_layout.setVisibility(View.VISIBLE);
					}else{
						((RadioButton)rb).setChecked(false);
						PayUtils.showToast(mContext, "类型转换错误，请联系管理员", 1000);
					}
					
				}
				
				@Override
				public void onFaild(String msg) {
					((RadioButton)rb).setChecked(false);
					PayUtils.showToast(mContext, ""+msg, 1000);
					
				}
			});
		}else{*/
			bank_layout.setVisibility(View.VISIBLE);
		//}
		
	}
	
	@Click(R.id.pay_account_commit)
	void commit(){
		if(isCommit()){
			
			if(mWay.equals("alipay")){
				commitBill("00");
			
			}else if(mWay.equals("wx")){
				commitBill("40");
			}else if(mWay.equals("bank")){
				commitBill("10");
			}
		}
	}

	@AfterInject
	void initData() {
		
	
		this.mContext = this;
		
		
	}
	
	@AfterViews
	void initUI() {
		bank_layout.setVisibility(View.GONE);
		back.setText("支付管理");
		headTitle.setText("账户充值");
		account.setVisibility(View.GONE);
		String str = rbAlipay.getText().toString();
		Spannable WordtoSpan = new SpannableString(str);   
		 
		WordtoSpan.setSpan(new AbsoluteSizeSpan((int) (rbAlipay.getTextSize()-2)), str.lastIndexOf("\n")+1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY), str.lastIndexOf("\n")+1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		rbAlipay.setText(WordtoSpan); 
		str = rbwx.getText().toString();
		WordtoSpan = new SpannableString(str);   
		 
		WordtoSpan.setSpan(new AbsoluteSizeSpan((int) (rbwx.getTextSize()-2)), str.lastIndexOf("\n")+1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY), str.lastIndexOf("\n")+1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		rbwx.setText(WordtoSpan); 
		
		if(PayUtils.mUserName == null || PayUtils.mUserName.equals("")){
			PayUtils.requestUserInfo(mContext,new OnResultListener() {
				
				@Override
				public void onSuccess(Object obj) {
					peopleNmae.setText(PayUtils.mUserName.equals("")?AccountInfo.instance().getLastUserInfo().getName():PayUtils.mUserName);
					peoplePhone.setText(PayUtils.mCallPhone.equals("")?PayUtils.mPhone:PayUtils.mCallPhone);
					peopleEmail.setText(PayUtils.mEmail);
					
				}
				
				@Override
				public void onFaild(String msg) {
					
					PayUtils.showToast(mContext, ""+msg, 1000);
				}
			});
		}else{
			
			peopleNmae.setText(PayUtils.mUserName);
			peoplePhone.setText(PayUtils.mCallPhone.equals("")?PayUtils.mPhone:PayUtils.mCallPhone);
			peopleEmail.setText(PayUtils.mEmail);
		}
	}
	
	
	
	@Override
	public void finish() {
		super.finish();
		
		PayMainActivity_.intent(mContext)
		.flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
		.start();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
	
	boolean isCommit(){
		
		
		try{
		mFaceValue = Float.parseFloat(selectValue.money);
		}catch(Exception e){
			mFaceValue = 0f;
		}
		
		if(mFaceValue<=0)
		{
			
			PayUtils.showToast(mContext, "金额不能为空", 1000);
			return false;
		}
		if(mWay.equals("")){
			PayUtils.showToast(mContext, "请选择支付方式", 1000);
			return false;
		}
		if(mWay.equals("bank")){
			mBankName = bankName.getText().toString();
			if(mBankName==null || mBankName.equals("")){
				PayUtils.showToast(mContext, "开户行不能为空", 1000);
				return false;
			}
			mBankPeopleName = bankPeopleName.getText().toString();
			if(mBankPeopleName==null || mBankPeopleName.equals("")){
				PayUtils.showToast(mContext, "开户名不能为空", 1000);
				return false;
			}
			
			mBankAccount = bankAccount.getText().toString();
			if(mBankAccount==null || mBankAccount.equals("")){
				PayUtils.showToast(mContext, "银行账户不能为空", 1000);
				return false;
			}
		}
		mName = peopleNmae.getText().toString();
		if(mName==null || mName.equals("")){
			PayUtils.showToast(mContext, "联系人不能为空", 1000);
			return false;
		}
		mPhone = peoplePhone.getText().toString();
		if(mPhone==null || mPhone.equals("")){
			PayUtils.showToast(mContext, "手机或座机不为空", 1000);
			return false;
		}
		if(!PayUtils.matchers(mPhone, 1)){
			PayUtils.showToast(mContext, "联系人手机或座机格式不正确", 1000);
			return false;
		}
		
		
		mEmail = peopleEmail.getText().toString();
		if(mEmail==null || mEmail.equals("")){
			PayUtils.showToast(mContext, "邮箱地址不能为空", 1000);
			return false;
		}
		if(!PayUtils.matchers(mEmail, 2)){
			PayUtils.showToast(mContext, "联系人电子邮箱格式不正确", 1000);
			return false;
		}
		return true;
	}
	
	private void clean(){
	
		mFaceValue = 0f;
		
		bankName.setText("");
		bankPeopleName.setText("");
		bankAccount.setText("");
		selectValue.clean();
		
	}
	
	@SuppressWarnings("serial")
	private void commitBill(String way){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("withInvoice", "0");
		map.put("amount", String.valueOf(mFaceValue));
		map.put("contacts", mName);
		map.put("email", mEmail);
		map.put("phone", mPhone);
		map.put("rechargeWay", way);
		map.put("selfCard", "0");
		if(way.equals("10")){
			map.put("bankName", mBankName);
			map.put("cardName", mBankPeopleName);
			map.put("cardNo", mBankAccount);
		}
		String url = mContext.getString(R.string.pay_commit_bill_url);
		PayUtils.showDialog(mContext);
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				PayUtils.closeDialog();
				
				if(data.getResultState() == ResultState.eSuccess){
					
					if(mWay.equals("alipay")){
						new AlipayUtils(mContext).alipay(data.getJsonObject(),mFaceValue);
					}
					else if(mWay.equals("wx")){
						new AlipayUtils(mContext).wxPay(data.getJsonObject(),mFaceValue);
					}else if(mWay.equals("bank")){
						paySuccess();
					}
				}else{
					PayUtils.showToast(mContext, ""+data.getMsg(), 1000);
				}
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}
		.addParam(map)
		.setUrl(url)
		.notifyRequest();
	}
	protected void paySuccess() {
		
       AlertDialog dialog = new AlertDialog.Builder(mContext)
       .setTitle("提示")
       .setMessage("您的汇款请求我们已经收到，我们将尽快处理")
       
       .setPositiveButton("确定", new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			clean();
			
		}
	})
	
       .create();
       dialog.setOnDismissListener(new OnDismissListener() {
		
		@Override
		public void onDismiss(DialogInterface dialog) {
			clean();
			
		}
	});
       
       dialog.show();
		
	}
	public interface OnResultListener{
		void onSuccess(Object obj);
		void onFaild(String msg);
	}
}
