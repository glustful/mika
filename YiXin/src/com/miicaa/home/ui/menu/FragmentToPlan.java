package com.miicaa.home.ui.menu;

import org.json.JSONObject;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;


public class FragmentToPlan {
	Context mContext;
	View rootView;
	ListView planWhatView;
	SelectInf selectInf;
	String srcCode;
	PlanAdapter adapter;
	final static String TAG = "MATTER_TO_PLAN";
	
	
	public FragmentToPlan(Context context) {
		
		mContext = context;
		srcCode = FragmentToScreen.ALL;
		adapter = new PlanAdapter();
		initPlanView();
		getDayCount();
		// TODO Auto-generated constructor stub
	}

	public void setSelectInf(SelectInf inf){
		selectInf = inf;
	}
	
	void initPlanView(){
		rootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_to_plan, 
				null);
		planWhatView = (ListView)rootView.findViewById(R.id.fragment_plan_list);
		planWhatView.setAdapter(adapter);
		planWhatView.setOnItemClickListener(whatClickListener);
		
	}
	
	public View getPlanView(){
		return (rootView != null)?rootView:null; 
	}
	
	OnItemClickListener whatClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			adapter.setBackP(arg2);
			if(selectInf != null){
			selectInf.selectWhat(arg2);
			}
			// TODO Auto-generated method stub
			
		}
	};
	
	
	
	
	class PlanAdapter extends BaseAdapter{
		
		int p = -1;
		
		
		public void setBackP(int p){
			this.p = p;
			this.notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			int planCount = 5;
			return planCount;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.plan_text_layout, null);
				holder.planText = (TextView)convertView.findViewById(R.id.plan_text);
				convertView.setTag(holder);
				
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			CharSequence what = null;
			switch (position) {
			case 0:
				what = getChangeConut("全部", totalCount);
				break;
			case 1:
				what = getChangeConut("未计划", noPlanCount );
				break;
			case 2:
				what = getChangeConut("今天", todayCount);
				break;
			case 3:
				what = getChangeConut("明天",tomoCount);
				break;
			case 4:
				what = getChangeConut("以后", futCount);
				break;
				
			default:
				break;
			}
			holder.planText.setText(what); 
			
            if(position == p){
//				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.gridSeletcolor));
            	holder.planText.setTextColor(mContext.getResources().getColor(R.color.plantextcolor));
			}else{
//				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
				holder.planText.setTextColor(mContext.getResources().getColor(R.color.alittle_white));
			}
			return convertView;
			
		}
		
		class ViewHolder{
			TextView planText;
		}
		
	}
	
	String totalCount = "";
	String noPlanCount = "";
	String todayCount = "";
	String tomoCount = "";
	String futCount = "";
	void getDayCount(){
		  String url = "/home/phone/thing/gettodocount";
		  new RequestAdpater() {
			  
			@Override
			public void onReponse(ResponseData data) {
				// TODO Auto-generated method stub
				Log.d(TAG,"data:"+data.getMsg());
				if(data.getResultState() == ResultState.eSuccess){
					JSONObject cJs = data.getJsonObject();
					if(cJs != null){
						
					}
                         totalCount = cJs.optString("totalCount");
                         todayCount = cJs.optString("todayCount");
                         tomoCount = cJs.optString("tomorrowCount");
                         futCount = cJs.optString("futureCount");
                         noPlanCount = cJs.optString("unPlanCount");
                         adapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}.setUrl(url)
		.addParam("srcCode",srcCode)
		.notifyRequest();
	}
	
	public interface SelectInf{
		void selectWhat(int position);
	}
	
	public void setSrcCode(String srcCode){
		this.srcCode = srcCode;
		getDayCount();
	}
	
	CharSequence getChangeConut(String day,String dayCount){
		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append(day+" ("+dayCount+")");
//		sb.setSpan(what, start, end, flags);
		return sb;
	}

}
