package com.miicaa.home.ui.pay;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.common.base.WheelView.WheelAdapter;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;

public class MyFundAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<JSONObject> jsonObjects;
	LayoutInflater inflater;
	ArrayList<String> reasons = new ArrayList<String>();
	

	public MyFundAdapter(Context context, ArrayList<JSONObject> data) {
		this.mContext = context;
		this.jsonObjects = new ArrayList<JSONObject>();
		this.jsonObjects.addAll(data);
		this.inflater = LayoutInflater.from(mContext);
		reasons.add("其他原因");
		reasons.add("不想充了");
		reasons.add("换个充值方式");
		reasons.add("信息填写错误，重新充值");
		
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.layout_pay_myfund_list_item, null);
			holder = new ViewHolder();
			holder.findView(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		initData(holder, jsonObjects.get(position));
		return convertView;
	}

	private void initData(final ViewHolder holder, JSONObject jsonObject) {
		String title = jsonObject.optString("userName");
		String type = jsonObject.optString("tradeType");
		if (type.equals("00")) {
			title += "-充值";
		} else {
			title += "-" + jsonObject.optString("memo");
		}
		holder.title.setText(title);
		Tools.setHeadImgWithoutClick(jsonObject.optString("userCode"),
				holder.headImg);
		holder.date.setText(PayUtils.formatData("yyyy-MM-dd HH:mm:ss",
				jsonObject.optLong("tradeDate", 0)));
		String flags = jsonObject.optString("creditFlag");

		holder.money.setText(PayUtils.cleanZero((flags.equals("0") ? "-" : flags.equals("1") ? "+"
				: "") + jsonObject.optDouble("amount",0)));
		String status = jsonObject.optString("status");
		String flag = "";
		if (!jsonObject.isNull("flag"))
			flag = jsonObject.optString("flag");

		holder.bottom.setVisibility(View.GONE);
		holder.cancel.setTag(jsonObject.optString("tradeId"));
		holder.commit.setTag(jsonObject);
		if (status.equals("0")) {
			if (type.equals("00")) {
				holder.bottom.setVisibility(View.VISIBLE);
				holder.cancel.setOnClickListener(null);
				holder.commit.setOnClickListener(null);
				holder.state.setText("待充值");
				holder.state.setTextColor(mContext.getResources().getColor(
						R.color.paypRed));
				holder.money.setTextColor(mContext.getResources().getColor(
						R.color.paypRed));
				if (flag.equals("10")) {
					holder.cancel.setVisibility(View.VISIBLE);
					holder.state.setText("等待付款");
					holder.cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//onCancel(v);
							closeTrade(v.getTag().toString(),"");
							
						}
					});
					holder.commit.setVisibility(View.GONE);
				} else if(type.equals("00")||type.equals("40")){
					holder.cancel.setVisibility(View.GONE);
					holder.commit.setVisibility(View.VISIBLE);
					
					holder.commit.setOnClickListener(commitListener);
				}else{
					holder.bottom.setVisibility(View.GONE);
				}
			} else {
				holder.state.setText("交易中");
				holder.state.setTextColor(mContext.getResources().getColor(
						R.color.payGray));
				holder.money.setTextColor(mContext.getResources().getColor(
						R.color.payGray));
			}
		} else if (status.equals("1")) {
			holder.state.setText("交易成功");
			holder.state.setTextColor(mContext.getResources().getColor(
					R.color.payGray));
			holder.money.setTextColor(mContext.getResources().getColor(
					R.color.payGray));
		} else if (status.equals("2")) {
			if (type.equals("00")) {
			holder.state.setText("充值已取消");
			}else{
			holder.state.setText("交易关闭");
			}
			holder.state.setTextColor(mContext.getResources().getColor(
					R.color.payGray));
			holder.money.setTextColor(mContext.getResources().getColor(
					R.color.payGray));
		} else {
			holder.state.setText("未知状态");
			holder.state.setTextColor(mContext.getResources().getColor(
					R.color.payGray));
			holder.money.setTextColor(mContext.getResources().getColor(
					R.color.payGray));
		}
	}

	class ViewHolder {
		ImageView headImg;
		TextView title;
		TextView date;
		TextView money;
		TextView state;
		Button cancel;
		Button commit;
		LinearLayout bottom;

		public void findView(View view) {
			headImg = (ImageView) view.findViewById(R.id.myfund_item_head);
			title = (TextView) view.findViewById(R.id.myfund_item_title);
			date = (TextView) view.findViewById(R.id.myfund_item_date);
			money = (TextView) view.findViewById(R.id.myfund_item_money);
			state = (TextView) view.findViewById(R.id.myfund_item_state);
			cancel = (Button) view.findViewById(R.id.pay_myfund_cancel);
			commit = (Button) view.findViewById(R.id.pay_myfund_submit);
			bottom = (LinearLayout) view.findViewById(R.id.bottom_layout);
		}
	}

	public void refresh(ArrayList<JSONObject> jsonObjects2) {
		this.jsonObjects.clear();
		this.jsonObjects.addAll(jsonObjects2);
		this.notifyDataSetChanged();
	}

	
		/*public void onCancel(final View v) {
			new WheelViewPopup.WheelViewMenuBuilder(mContext)
			.setAdapter(new TextAdapter(reasons))
			.setTextSize(60)
			.setOnItemChangeListener(new OnItemChangeListener() {
				
				@Override
				public void onChange(String item) {
					closeTrade(v.getTag().toString(),item);
					
				}
			})
			.show();
		}*/
	

	View.OnClickListener commitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(v.getTag() instanceof JSONObject){
				JSONObject json = (JSONObject) v.getTag();
				commitBill(json.optString("tradeId"),json.optString("flag"),(float)json.optDouble("amount",0));
			}
		}
	};
	
	private void commitBill(String id,final String way,final float mFaceValue){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		
		String url = mContext.getString(R.string.pay_commit_bill_url);
		PayUtils.showDialog(mContext);
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				PayUtils.closeDialog();
				
				if(data.getResultState() == ResultState.eSuccess){
					System.out.println("result="+data.getMRootData());
					if(way.equals("00")){
						new AlipayUtils(mContext).alipay(data.getJsonObject(),mFaceValue);
					}
					else if(way.equals("40")){
						new AlipayUtils(mContext).wxPay(data.getJsonObject(),mFaceValue);
					}else{
						PayUtils.showToast(mContext, "此充值方式不支持", 1000);
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
	
	private void closeTrade(String tradeId,String reason){
		PayUtils.showDialog(mContext);
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				PayUtils.closeDialog();
				if(data.getResultState()==ResultState.eSuccess){
					PayUtils.showToast(mContext, "取消订单成功", 1000);
					
					if(MyFundActivity.instance != null){
					MyFundActivity.instance.resetList();
					MyFundActivity.instance.requestFirst();
					}
					
				}else{
					PayUtils.showToast(mContext, "取消订单失败："+data.getMsg(), 1000);
				}
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
				
			}
		}.addParam("tradeId", tradeId)
		.setUrl(mContext.getString(R.string.pay_closetrade_url))
		.notifyRequest();
	}
	
	class TextAdapter implements WheelAdapter{

		ArrayList<String> items ;
		int length = 0;
		
		public TextAdapter(ArrayList<String> data){
			this.items = new ArrayList<String>();
			this.items.addAll(data);
			for(String s:this.items){
				if(s.length()>length){
					length = s.length();
				}
			}
		}
		
		@Override
		public int getItemsCount() {
			
			return this.items.size();
		}

		@Override
		public String getItem(int index) {
			
			return this.items.get(index);
		}

		@Override
		public int getMaximumLength() {
			
			return length;
		}
		
	}
}
