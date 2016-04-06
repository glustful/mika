package com.miicaa.home.ui.pay;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;

public class ReceiptAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<JSONObject> jsonObjects;
	LayoutInflater inflater;
	ArrayList<String> reasons = new ArrayList<String>();
	
	public ReceiptAdapter(Context mContext, ArrayList<JSONObject> jsonObjects) {
		this.mContext = mContext;
		this.jsonObjects = new ArrayList<JSONObject>();
		this.jsonObjects.addAll(jsonObjects);
		this.inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		
		return jsonObjects.size();
	}

	@Override
	public Object getItem(int position) {
		
		return this.jsonObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.layout_pay_receipt_list_item, null);
			holder = new ViewHolder();
			holder.findView(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		initData(holder, jsonObjects.get(position));
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ReceiptDetailActivity_.intent(mContext)
				.jsonStr(jsonObjects.get(position).toString())
				.start();
				
			}
		});
		return convertView;
	}

	private void initData(final ViewHolder holder, JSONObject jsonObject) {
		try{
			Tools.setHeadImgWithoutClick(jsonObject.optString("userCode"), holder.headImg);
			holder.title.setText(jsonObject.optString("userName"));
			holder.date.setText(PayUtils.formatData("yyyy-MM-dd HH:mm:ss", jsonObject.optLong("tradeDate",0)));
			holder.money.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(jsonObject.optDouble("balance",0)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	class ViewHolder {
		ImageView headImg;
		TextView title;
		TextView date;
		TextView money;
		

		public void findView(View view) {
			headImg = (ImageView) view.findViewById(R.id.receipt_item_head);
			title = (TextView) view.findViewById(R.id.receipt_item_title);
			date = (TextView) view.findViewById(R.id.receipt_item_date);
			money = (TextView) view.findViewById(R.id.receipt_item_money);
			
		}
	}

	public void refresh(ArrayList<JSONObject> jsonObjects2) {
		this.jsonObjects.clear();
		this.jsonObjects.addAll(jsonObjects2);
		this.notifyDataSetChanged();
	}

}
