package com.miicaa.home.ui.org;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LableViewAdapter extends BaseAdapter{

	static String TAG = "LableViewAdapter";
	
	Context context;
	ArrayList<String> labelStrs;
	
	 public LableViewAdapter(Context context) {
		// TODO Auto-generated constructor stub
		 this.context = context;
		 labelStrs = new ArrayList<String>();
	}
	 
	 public void refresh(ArrayList<String> labelStrs){
		 this.labelStrs.clear();
		 this.labelStrs.addAll(labelStrs);
		 Log.d(TAG, "labelStrs"+"---"+labelStrs.size());
		 this.notifyDataSetChanged();
	 }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.d(TAG, "count --- "+labelStrs.size());
		return labelStrs != null ? labelStrs.size() :0 ;
//		return 4;
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		Log.d(TAG, "position ----"+position);
//		TextView v = (TextView)LayoutInflater.from(context).inflate(R.layout.action_title, null);
//		v.setTextColor(context.getResources().getColor(R.color.blue));
//		convertView = v;
//		LabelView v = null;
		LabelView v = null;
		if(convertView == null){
			v = new LabelView(context);
			convertView = v;
		}else{
			v = (LabelView)convertView;
		}
		
		v.ChangeText(context, labelStrs.get(position));
		return convertView;
	}

}
