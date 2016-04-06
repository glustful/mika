package com.miicaa.home.ui.pay;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;

@EActivity(R.layout.layout_pay_bill_detail)
public class BillDetailActivity extends Activity {

	@Extra
	String json;
	JSONObject jsonObject;
	
	@ViewById(R.id.pay_cancleButton)
	Button cancel;
	@ViewById(R.id.pay_headTitle)
	TextView title;
	@ViewById(R.id.bill_detail_img)
	ImageView headImg;
	@ViewById(R.id.bill_detail_name)
	TextView name;
	@ViewById(R.id.bill_detail_state)
	TextView state;
	@ViewById(R.id.bill_detail_normal_title)
	TextView normalTitle;
	@ViewById(R.id.bill_detail_normal_price)
	TextView normalPrice;
	@ViewById(R.id.bill_detail_normal_length)
	TextView normalLength;
	@ViewById(R.id.bill_detail_five_title)
	TextView fiveTitle;
	@ViewById(R.id.bill_detail_five_price)
	TextView fivePrice;
	@ViewById(R.id.bill_detail_five_length)
	TextView fiveLenght;
	@ViewById(R.id.bill_detail_label)
	TextView label;
	@ViewById(R.id.bill_detail_orderid)
	TextView orderId;
	@ViewById(R.id.bill_detail_tradeTime)
	TextView trandeTime;
	@ViewById(R.id.bill_detail_useTime)
	TextView useTime;
	@ViewById(R.id.bill_detail_expTime)
	TextView expTime;
	@ViewById(R.id.bill_detail_total)
	TextView total;
	
	Context mContext;
	long starDay = 0;
	long endDay = 0;
	String status;
	
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
		title.setText("订单详情");
		if(json!=null){
			try {
				jsonObject = new JSONObject(json);
				initPackage(jsonObject.optJSONArray("goods"));
				initView(jsonObject);
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		
		}
	}
	
	private void initView(JSONObject jsonObject2) {
		
		Tools.setHeadImgWithoutClick(jsonObject2.optString("userCode"), headImg);
		name.setText(jsonObject2.optString("userName"));
		orderId.setText(jsonObject2.optString("id"));
		trandeTime.setText(PayUtils.formatData("yyyy-MM-dd HH:mm:ss", jsonObject2.optLong("tradeDate", 0)));
		useTime.setText(PayUtils.formatData("yyyy-MM-dd", starDay));
		expTime.setText(PayUtils.formatData("yyyy-MM-dd", endDay));
		
		total.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.valueOf(jsonObject2.optDouble("needPay",0))));
		if(status.equals("00")){
			long nowTime = jsonObject2.optLong("nowTime",0);
			if(endDay<nowTime){
				state.setText("历史套餐");
			}else if(starDay>nowTime){
				state.setText("未使用套餐");
			}else{
				state.setText("使用中套餐");
			}
		}else{
			state.setText("已中止套餐");
		}
	}

	private void initPackage(JSONArray optJSONArray) {
		
		if(optJSONArray==null||optJSONArray.length()<1){
			
			return;
		}
		int length = optJSONArray.length();
		for(int i=0;i<length;i++){
			try{
			JSONObject obj = optJSONArray.optJSONObject(i);
			starDay = obj.optLong("startday");
			endDay = obj.optLong("endday");
			status = obj.optString("status", "10");
			
			String type = obj.optString("setType");
			if(!obj.isNull("fiveSetNumber")&&obj.optInt("fiveSetNumber", 0)>0){
				int num = obj.optInt("fiveSetNumber",0);
				fiveTitle.setText(obj.optString("setName")+"("+num+"个)");
				int num1 = num*obj.optInt("upperLimit", 0);
				if(type.equals("0")){
					fivePrice.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", obj.optDouble("perMonth",0)*num))+"/"+num1+"人/月");
					label.setText("开通时长(月)");
				}else if(type.equals("1")){
					fivePrice.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", (obj.optDouble("perMonth",0)*12*num)))+"/"+num1+"人/年");
					label.setText("开通时长(年)");
				}else if(type.equals("2")){
					fivePrice.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", obj.optDouble("perDay",0)*num))+"/"+num1+"人/天");
					label.setText("开通时长(天)");
				}
				fiveLenght.setText(String.valueOf(obj.optInt("quality", 0)));
				if(length==1){
					((LinearLayout)normalLength.getParent()).removeView(normalLength);
					((LinearLayout)normalPrice.getParent()).removeView(normalPrice);
					((LinearLayout)normalTitle.getParent()).removeView(normalTitle);
					
				}
			}
			else{
				normalTitle.setText(obj.optString("setName"));
				
				if(type.equals("0")){
					label.setText("开通时长(月)");
					normalPrice.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(obj.optDouble("perMonth",0))+"/月");
				}else if(type.equals("1")){
					label.setText("开通时长(年)");
					normalPrice.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(obj.optDouble("perYear",0))+"/年");
				}
				normalLength.setText(String.valueOf(obj.optInt("quality", 0)));
			}
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
}
