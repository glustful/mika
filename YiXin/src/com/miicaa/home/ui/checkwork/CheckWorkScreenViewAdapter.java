package com.miicaa.home.ui.checkwork;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.utils.ViewHolder;

public class CheckWorkScreenViewAdapter extends BaseAdapter{
	
	LayoutInflater inflater;
	Context mContext;
	ArrayList<CheckScreenValue> types;
//	LinkedHashMap<String, String> typesMap;
	
	public CheckWorkScreenViewAdapter(){
	}
	
	public CheckWorkScreenViewAdapter(Context context){
		this.mContext = context;
		types = new ArrayList<CheckScreenValue>();
		inflater = LayoutInflater.from(mContext);
	}
	
	public void refresh(ArrayList<CheckScreenValue> types){
		this.types.clear();
		this.types.addAll(types);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return types.size();
	}

	@Override
	public Object getItem(int position) {
		return types.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = inflater.inflate(R.layout.top_singleline_textview, null);
		
		TextView textBtn = ViewHolder.get(convertView, R.id.textBtn);
		textBtn.setText(types.get(position).mValue);
		return convertView;
	}

}
