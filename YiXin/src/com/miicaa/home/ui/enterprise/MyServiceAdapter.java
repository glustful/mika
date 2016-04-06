package com.miicaa.home.ui.enterprise;

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
import com.miicaa.home.ui.pay.PayUtils;

public class MyServiceAdapter extends BaseAdapter {

	ArrayList<JSONObject> jData ;
	Context mContext;
	LayoutInflater inflater;
	 /**
     * 待支付状态
     */
    public static final String STATUS_WAIT = "10";
    /**
     * 已支付待提问状态
     */
    public static final String STATUS_QUES = "20";
    /**
     * 已提问待评价状态
     */
    public static final String STATUS_COM = "30";
    /**
     * 已评价（结束）
     */
    public static final String STATUS_FINISH = "40";
    
  
	 String MyServiceStatus(String tmp){
		 String last = null;
		 if(tmp.equals(STATUS_WAIT)){
			last =  "待支付";
		 }else if(tmp.equals(STATUS_QUES)){
			 last =  "待填写问题";
		 }else if(tmp.equals(STATUS_COM)){
			 last =  "待评价";
		 }else if(tmp.equals(STATUS_FINISH)){
			 last =  "已评价";
		 }
	return last;
	 
   }
	
	public MyServiceAdapter(Context mContext, ArrayList<JSONObject> jsonObjects) {
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
			convertView = inflater.inflate(R.layout.enterprice_myservice_item, null);
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
				String status = obj.optString("status");
				String url = "";
				if(status.equals(STATUS_WAIT)){
					new CommandCall(mContext, obj).call();
					return;
				}else if(status.equals(STATUS_QUES)){
					url = mContext.getString(R.string.enterprice_question_url);
				}else if(status.equals(STATUS_COM)){
					url = mContext.getString(R.string.enterprice_comment_url);
				}else if(status.equals(STATUS_FINISH)){
					url = mContext.getString(R.string.enterprice_finish_url);
				}
				if(url.equals("")){
					PayUtils.showToast(mContext, "状态未知", 3000);
					return;
				}
				url += obj.optString("id");
				EnterpriceMainActivity_.intent(mContext)
				.url(url)
				.start();
			}
		});
		return convertView;
	}

	private void initData(ViewHolder holder, JSONObject jsonObject) {
		
		holder.title.setText(jsonObject.optString("name"));
		
		holder.state.setText(MyServiceStatus(jsonObject.optString("status")));
		holder.date.setText(PayUtils.formatData("yyyy-MM-dd", jsonObject.optLong("orderTime", 0)));
		holder.price.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(jsonObject.optDouble("amount",0)));
		
	}

	

	public void refresh(ArrayList<JSONObject> jsonObjects) {
		this.jData.clear();
		this.jData.addAll(jsonObjects);
		this.notifyDataSetChanged();
	}
	
	class ViewHolder{
		TextView title;
		TextView date;
		
		TextView state;
		
		TextView price;
		
		public void mapViewId(View v) {
			title = (TextView) v.findViewById(R.id.title);
			date = (TextView) v.findViewById(R.id.date);
			
			state = (TextView) v.findViewById(R.id.state);
			
			price = (TextView) v.findViewById(R.id.price);
		}
	}

}
