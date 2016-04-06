package com.miicaa.home.ui.pay;

import java.util.Date;

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

@EActivity(R.layout.layout_pay_buy_fivepackage_activity)
public class BuyFivePackageActivity extends Activity {
	
	public static final int PACKAGE = 0;
	public static final int FIVEPACKAGE = 1;
	double mDMoney = 0;
	@Extra
	long endTime;
	@Extra
	double accountmoney;
	
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.pay_headTitle)
	TextView headTitle;
	
	@ViewById(R.id.pay_package_five)
	TableLayoutPackageView tlpFivePackage;
	@ViewById(R.id.buy_fivetimelength)
	TextView timeLenght;
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
			
				long nowTime = new Date().getTime();
				long tmp = endTime - nowTime;
				tmp = tmp/(24*60*60*1000)+1;
				
			
	        PayMentActivity_.intent(mContext)
	        .type(""+tlpFivePackage.selectType)
	        .packageJSON("")
	        .fiveJSON(fiveJSON)
	        .mCount(mCount)
	        .mTimeLength((int)tmp)
	        .mTotal(mDMoney)
	        .packageValue(0)
	        .accountmoney(accountmoney)
	        .fiveValue(tlpFivePackage.getValue(mCount))
	        .start();
	        ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
		}
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
	
	int mCount = 1;

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
		headTitle.setText("五人包");
		
		countEdit.setText("1");
		moneyTotal.setText(mContext.getString(R.string.symbol)+"0");
		String str = mContext.getString(R.string.payProtocol);
		SpannableString spStr = new SpannableString(str);
		ClickableSpan clickSpan = new NoLineClickSpan(str); //设置超链接
		spStr.setSpan(clickSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		introduce.setText(spStr);
		introduce.setMovementMethod(LinkMovementMethod.getInstance());
		
		tlpFivePackage.addRelationWidget(countEdit);
		tlpFivePackage.addRelationWidget(countMinus);
		tlpFivePackage.addRelationWidget(findViewById(R.id.buy_count_plus));
		tlpFivePackage.setEndTime(endTime);
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
				//initPackage(data.getJsonObject().optJSONArray("defaultSet"));
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
		if(!tlpFivePackage.isReady()){
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
	
		mDMoney += tlpFivePackage.getValue(mCount);
		return mContext.getString(R.string.symbol) + PayUtils.cleanZero(String.format("%.2f", mDMoney));
	}
	
	protected void initFivePackage(JSONArray optJSONArray) {
		tlpFivePackage.addChilds(optJSONArray,FIVEPACKAGE);
		tlpFivePackage.defaultEnable(TableLayoutPackageView.TYPE_DAY);
		long nowTime = new Date().getTime();
		long tmp = endTime - nowTime;
		tmp = tmp/(24*60*60*1000)+1;
		timeLenght.setText(String.valueOf(tmp));
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
