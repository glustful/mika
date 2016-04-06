package com.miicaa.home.ui.pay;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;

public class BillRecordAdapter extends BaseAdapter {

	ArrayList<JSONObject> jData ;
	Context mContext;
	LayoutInflater inflater;
	
	public BillRecordAdapter(Context mContext, ArrayList<JSONObject> jsonObjects) {
		this.mContext = mContext;
		this.jData = new ArrayList<JSONObject>();
		this.jData.addAll(jsonObjects);
		this.inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		
		return jData.size();
	}

	@Override
	public Object getItem(int position) {
		
		return jData.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.layout_pay_bill_list_item, null);
			holder = new ViewHolder();
			holder.mapViewId(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		initData(holder,jData.get(position));
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				JSONObject obj = jData.get(position);
				if(obj.optString("goodType").equals(BillRecordActivity.GOOD_TYPE_SET)){
					BillDetailActivity_.intent(mContext)
					.json(jData.get(position).toString())
					.start();
				}else if(obj.optString("goodType").equals(BillRecordActivity.GOOD_TYPE_GOOD)){
					BillGoodDetailActivity_.intent(mContext)
					.json(jData.get(position).toString())
					.start();
				}
				
				
			}
		});
		return convertView;
	}

	private void initData(ViewHolder holder, JSONObject jsonObject) {
		
		holder.billNo.setText(jsonObject.optString("id"));
		Tools.setHeadImgWithoutClick(jsonObject.optString("userCode"), holder.headImg);
		holder.peopleName.setText(jsonObject.optString("userName"));
		holder.billDate.setText(PayUtils.formatData("yyyy-MM-dd HH:mm:ss", jsonObject.optLong("tradeDate", 0)));
		holder.billTotal.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(jsonObject.optDouble("needPay",0)));
		if(jsonObject.optString("goodType").equals(BillRecordActivity.GOOD_TYPE_SET)){
		initPackage(holder.billPackage,jsonObject.optJSONArray("goods"));
		}else if(jsonObject.optString("goodType").equals(BillRecordActivity.GOOD_TYPE_GOOD)){
			initGoodPackage(holder.billPackage,jsonObject.optJSONArray("goods"));
		}
	}

	private void initGoodPackage(LinearLayout billPackage,
			JSONArray optJSONArray) {
		billPackage.removeAllViews();
		if(optJSONArray==null||optJSONArray.length()<1){
			billPackage.setVisibility(View.GONE);
			return;
		}
		billPackage.setVisibility(View.VISIBLE);
		
		for(int i=0;i<optJSONArray.length();i++){
			try{
			JSONObject obj = optJSONArray.optJSONObject(i);
			View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pay_bill_list_item_package, null);
			TextView title = (TextView) view.findViewById(R.id.bill_package_title);
			TextView price = (TextView) view.findViewById(R.id.bill_package_price);
			TextView count = (TextView) view.findViewById(R.id.bill_package_count);
			
			String type = obj.optString("setType");
			/*if(!obj.isNull("fiveSetNumber")&&obj.optInt("fiveSetNumber", 0)>0){
				int num = obj.optInt("fiveSetNumber",0);
				title.setText("购买套餐-"+obj.optString("setName")+"("+num+"个)");
				int num1 = num*obj.optInt("upperLimit", 0);
				if(type.equals("0")){
					price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", obj.optDouble("perMonth",0)*num))+"/"+num1+"人/月");
				}else if(type.equals("1")){
					price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", (obj.optDouble("perMonth",0)*12*num)))+"/"+num1+"人/年");
					
				}else if(type.equals("2")){
					price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", obj.optDouble("perDay",0)*num))+"/"+num1+"人/天");
				}
				
			}
			else{*/
				title.setText(""+obj.optString("setOtherName"));
				if(type.equals("0")){
					price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(obj.optDouble("perMonth",0))+"/月");
				}else if(type.equals("1")){
					price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(obj.optDouble("perYear",0))+"/年");
				}
			//}
			count.setText("X"+obj.optInt("quality", 0));
			billPackage.addView(view,new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	private void initPackage(LinearLayout billPackage, JSONArray optJSONArray) {
		billPackage.removeAllViews();
		if(optJSONArray==null||optJSONArray.length()<1){
			billPackage.setVisibility(View.GONE);
			return;
		}
		billPackage.setVisibility(View.VISIBLE);
		
		for(int i=0;i<optJSONArray.length();i++){
			try{
			JSONObject obj = optJSONArray.optJSONObject(i);
			View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pay_bill_list_item_package, null);
			TextView title = (TextView) view.findViewById(R.id.bill_package_title);
			TextView price = (TextView) view.findViewById(R.id.bill_package_price);
			TextView count = (TextView) view.findViewById(R.id.bill_package_count);
			
			String type = obj.optString("setType");
			if(!obj.isNull("fiveSetNumber")&&obj.optInt("fiveSetNumber", 0)>0){
				int num = obj.optInt("fiveSetNumber",0);
				title.setText("购买套餐-"+obj.optString("setName")+"("+num+"个)");
				int num1 = num*obj.optInt("upperLimit", 0);
				if(type.equals("0")){
					price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", obj.optDouble("perMonth",0)*num))+"/"+num1+"人/月");
				}else if(type.equals("1")){
					price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", (obj.optDouble("perMonth",0)*12*num)))+"/"+num1+"人/年");
					
				}else if(type.equals("2")){
					price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", obj.optDouble("perDay",0)*num))+"/"+num1+"人/天");
				}
				
			}
			else{
				title.setText("购买套餐-"+obj.optString("setName"));
				if(type.equals("0")){
					price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(obj.optDouble("perMonth",0))+"/月");
				}else if(type.equals("1")){
					price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(obj.optDouble("perYear",0))+"/年");
				}
			}
			count.setText("X"+obj.optInt("quality", 0));
			billPackage.addView(view,new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void refresh(ArrayList<JSONObject> jsonObjects) {
		this.jData.clear();
		this.jData.addAll(jsonObjects);
		this.notifyDataSetChanged();
	}
	
	class ViewHolder{
		TextView billNo;
		TextView peopleName;
		ImageView headImg;
		TextView billDate;
		LinearLayout billPackage;
		TextView billTotal;
		
		public void mapViewId(View v) {
			billNo = (TextView) v.findViewById(R.id.bill_no);
			peopleName = (TextView) v.findViewById(R.id.bill_name);
			headImg = (ImageView) v.findViewById(R.id.bill_head_img);
			billDate = (TextView) v.findViewById(R.id.bill_date);
			billPackage = (LinearLayout) v.findViewById(R.id.bill_package);
			billTotal = (TextView) v.findViewById(R.id.bill_total);
		}
	}

}
