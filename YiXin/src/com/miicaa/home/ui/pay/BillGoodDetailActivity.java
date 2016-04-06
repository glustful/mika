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

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@EActivity(R.layout.layout_pay_bill_good_detail)
public class BillGoodDetailActivity extends Activity {
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
	@ViewById(R.id.bill_detail_orderid)
	TextView orderId;
	@ViewById(R.id.bill_detail_tradeTime)
	TextView trandeTime;
	@ViewById(R.id.bill_detail_total)
	TextView total;
	@ViewById(R.id.package_list)
	LinearLayout packageList;
	
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
		
		
		total.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.valueOf(jsonObject2.optDouble("needPay",0))));
		
	}

	private void initPackage(JSONArray optJSONArray) {
		packageList.removeAllViews();
		if(optJSONArray==null||optJSONArray.length()<1){
			
			return;
		}
		
		int length = optJSONArray.length();
		for(int i=0;i<length;i++){
			packageList.addView(getView(optJSONArray.optJSONObject(i)));
			
		}
	}
	
	public View getView(JSONObject jsonObject) {
		ViewHolder holder = null;
		View convertView;
		
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_pay_bought_package_item, null);
			holder = new ViewHolder();
			holder.kind = (TextView) convertView.findViewById(R.id.pay_package_kind);
			holder.people = (TextView) convertView.findViewById(R.id.pay_package_people);
			holder.store = (TextView) convertView.findViewById(R.id.pay_package_store);
			holder.timelength = (TextView) convertView.findViewById(R.id.pay_package_timelength);
			holder.lenght_label = (TextView) convertView.findViewById(R.id.pay_package_timelength_label);
			holder.start = (TextView) convertView.findViewById(R.id.pay_package_timestart);
			holder.end = (TextView) convertView.findViewById(R.id.pay_package_timeend);
			
		initData(holder,jsonObject);
		return convertView;
	}
	
	private void initData(ViewHolder holder, JSONObject jsonObject) {
		String desc = jsonObject.optString("setOtherName");
		
		
			holder.kind.setText(desc);
		
		holder.start.setText(PayUtils.formatData("yyyy-MM-dd", jsonObject.optLong("startDay",0)));
		holder.end.setText(PayUtils.formatData("yyyy-MM-dd", jsonObject.optLong("endDay",0)));
		
		if(jsonObject.optString("setType").equals("0")){
			holder.lenght_label.setText("开通时长（月）：");
			
		}else if(jsonObject.optString("setType").equals("1")){
			holder.lenght_label.setText("开通时长（年）：");
		}else if(jsonObject.optString("setType").equals("2")){
			holder.lenght_label.setText("开通时长（天）：");
		}
		holder.timelength.setText(String.valueOf(jsonObject.optInt("quality",0)));
	}

	class ViewHolder{
		TextView kind;
		TextView people;
		TextView store;
		TextView lenght_label;
		TextView timelength;
		TextView start;
		TextView end;
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
}
