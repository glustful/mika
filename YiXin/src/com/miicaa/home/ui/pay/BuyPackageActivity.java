package com.miicaa.home.ui.pay;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.home.ui.pay.TableLayoutPackageView.ClickListener;

@EActivity(R.layout.layout_pay_buy_package_activity)
public class BuyPackageActivity extends Activity {
	
	public static final int PACKAGE = 0;
	public static final int FIVEPACKAGE = 1;
	public static final int FIRSTBUY = 2;
	public static final int CONTINUEBUY = 3;
	double mDMoney = 0;
	@Extra
	double accountmoney;
	@Extra
	int type;
	
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.pay_headTitle)
	TextView headTitle;
	@ViewById(R.id.buy_tip)
	TextView tip;
	@ViewById(R.id.buy_label)
	TextView label;
	@ViewById(R.id.buy_label1)
	TextView label1;
	@ViewById(R.id.pay_package_package)
	TableLayoutPackageView tlpPackage;
	@ViewById(R.id.pay_package_five)
	TableLayoutPackageView tlpFivePackage;
	
	@ViewById(R.id.buy_time_length_edit)
	EditText timeLenghtEdit;
	@ViewById(R.id.buy_timelength1)
	TextView timelenght1;
	@ViewById(R.id.buy_time_length_minus)
	ImageButton timeLengthMinus;
	@ViewById(R.id.buy_count_edit)
	EditText countEdit;
	@ViewById(R.id.buy_count_minus)
	ImageButton countMinus;
	@ViewById(R.id.buy_money_total)
	TextView moneyTotal;
	@ViewById(R.id.buy_protocol_checkbox)
	CheckBox checkbox;
	@ViewById(R.id.buy_protocol_introduce)
	TextView introduce;
	
	@Click(R.id.buy_time_length_minus)
	void timeLengthMinus(){
		int tmp = Integer.parseInt(timeLenghtEdit.getText().toString());
		if(tmp<=1)
			return;
		timeLenghtEdit.setText(String.valueOf(tmp-1));
	}
	@Click(R.id.buy_time_length_plus)
	void timeLengthPlus(){
		
		String tmp = timeLenghtEdit.getText().toString();
		timeLenghtEdit.setText(String.valueOf(Integer.parseInt(tmp)+1));
	}
	@Click(R.id.buy_count_minus)
	void countMinus(){
		int tmp = Integer.parseInt(countEdit.getText().toString());
		if(tmp<=1)
			return;
		countEdit.setText(String.valueOf(tmp-1));
	}
	@Click(R.id.buy_count_plus)
	void countPlus(){
		countEdit.setText(String.valueOf(Integer.parseInt(countEdit.getText().toString())+1));
		
	}
	@Click(R.id.buy_commit)
	void commit(){
		if(isCommit()){
		
	        PayMentActivity_.intent(mContext)
	        .type(""+tlpPackage.selectType)
	        .packageJSON(packageJSON)
	        .fiveJSON(fiveJSON)
	        .mCount(mCount)
	        .mTimeLength(mTimeLength)
	        .mTotal(mDMoney)
	        .packageValue(tlpPackage.getValue(mTimeLength))
	        .fiveValue(tlpFivePackage.getValue(mCount*mTimeLength))
	        .accountmoney(accountmoney)
	        .start();
	        ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
		}
	}
	
	@AfterTextChange(R.id.buy_time_length_edit)
	void timeLengthChange(Editable tv){
		try{
		mTimeLength = Integer.parseInt(tv.toString());
		}catch(Exception e){
			mTimeLength = 1;
			timeLenghtEdit.setText("1");
		}
		if(mTimeLength<=1){
			timeLengthMinus.setEnabled(false);
		}else{
			timeLengthMinus.setEnabled(true);
		}
		timelenght1.setText(String.valueOf(mTimeLength));
		moneyTotal.setText(getValue());
	}
	
	@AfterTextChange(R.id.buy_label)
	void onChange(Editable tv){
		
		label1.setText(label.getText());
		
	}
	
	@AfterTextChange(R.id.buy_count_edit)
	void countChange(Editable tv){
		try{
			mCount = Integer.parseInt(tv.toString());
			}catch(Exception e){
				mCount = 1;
				countEdit.setText("1");
			}
			if(mCount<=1){
				countMinus.setEnabled(false);
			}else{
				countMinus.setEnabled(true);
			}
			moneyTotal.setText(getValue());
	}
	
	Context mContext;  
	int mTimeLength = 1;
	int mCount = 1;
	String packageJSON = "";
	String fiveJSON = "";
	
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
		
		back.setText("支付管理");
		switch(type){
		case FIRSTBUY:
			headTitle.setText("购买套餐");
			tip.setVisibility(View.GONE);
			break;
		case CONTINUEBUY:
			headTitle.setText("继续购买");
			tip.setVisibility(View.VISIBLE);
			break;
		}
		
		timeLenghtEdit.setText("1");
		countEdit.setText("1");
		moneyTotal.setText(mContext.getString(R.string.symbol)+"0");
		String str = mContext.getString(R.string.payProtocol);
		SpannableString spStr = new SpannableString(str);
		ClickableSpan clickSpan = new NoLineClickSpan(str); //设置超链接
		spStr.setSpan(clickSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		introduce.setText(spStr);
		introduce.setMovementMethod(LinkMovementMethod.getInstance());
		tlpPackage.addRelationWidget(tlpFivePackage);
		tlpPackage.addRelationWidget(timeLenghtEdit);
		tlpPackage.addRelationWidget(timeLengthMinus);
		tlpPackage.addRelationWidget(findViewById(R.id.buy_time_length_plus));
		tlpPackage.setLabel(label);
		tlpFivePackage.addRelationWidget(countEdit);
		tlpFivePackage.addRelationWidget(countMinus);
		tlpFivePackage.addRelationWidget(findViewById(R.id.buy_count_plus));
		
		tlpPackage.setListener(new ClickListener() {
			
			@Override
			public void onClick(CompoundButton cb) {
				moneyTotal.setText(getValue());
				packageJSON = ((JSONObject)cb.getTag(R.id.tag_json)).toString();
			}
		});
		tlpFivePackage.setListener(new ClickListener() {
			
			@Override
			public void onClick(CompoundButton cb) {
				moneyTotal.setText(getValue());
				if(cb.isChecked())
					fiveJSON = ((JSONObject)cb.getTag(R.id.tag_json)).toString();
				else{
					fiveJSON = "";
				}
			}
		});
		PayUtils.showDialog(mContext);
		requestData();
	}
	
	private void requestData(){
		String url = mContext.getString(R.string.pay_package_url);
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				PayUtils.closeDialog();
				if(data.getResultState()==ResultState.eSuccess){
				initPackage(data.getJsonObject().optJSONArray("defaultSet"));
				initFivePackage(data.getJsonObject().optJSONArray("fiveSet"));
				}else{
					PayUtils.showToast(mContext, ""+data.getMsg(), 1000);
				}
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
				
			}
		}.setUrl(url)
		.notifyRequest();
	}
	
	private boolean isCommit(){
		if(!tlpPackage.isReady()){
			PayUtils.showToast(mContext, "套餐未选择", 1000);
			return false;
		}
		if(!checkbox.isChecked()){
			PayUtils.showToast(mContext, "请先阅读侎佧服务用协议", 1000);
			return false;
		}
		return true;
	}
	
	private String getValue(){
		mDMoney = 0;
		mDMoney += tlpPackage.getValue(mTimeLength);
		System.out.println("money="+mDMoney);
		mDMoney += tlpFivePackage.getValue(mCount*mTimeLength);
		System.out.println("money1="+tlpFivePackage.getValue(mCount*mTimeLength));
		return mContext.getString(R.string.symbol) + PayUtils.cleanZero(String.format("%.2f", mDMoney));
	}
	
	protected void initFivePackage(JSONArray optJSONArray) {
		tlpFivePackage.addChilds(optJSONArray,FIVEPACKAGE);
		
	}

	protected void initPackage(JSONArray optJSONArray) {
		tlpPackage.addChilds(optJSONArray,PACKAGE);
		
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
	
	//无下划线超链接，使用textColorLink、textColorHighlight分别修改超链接前景色和按下时的颜色
	private class NoLineClickSpan extends ClickableSpan { 
	    String text;

	    public NoLineClickSpan(String text) {
	        super();
	        this.text = text;
	    }

	    @Override
	    public void updateDrawState(TextPaint ds) {
	        ds.setColor(ds.linkColor);
	        ds.setUnderlineText(false); //<span style="color: red;">//去掉下划线</span>
	    }

	    @Override
	    public void onClick(View widget) { 
	    	String url = UserAccount.mSeverHost+mContext.getString(R.string.miicaaprotocol);
	    	Intent intent = new Intent();        
	        intent.setAction("android.intent.action.VIEW");    
	        Uri content_url = Uri.parse(url);   
	        intent.setData(content_url);  
	        startActivity(intent);
	    }
	}
}
