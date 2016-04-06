package com.miicaa.home.ui.home;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.menu.ScreenType;
import com.miicaa.utils.ViewHolder;

public class MatterScreenViewAdapter extends BaseAdapter{

	Context context;
	static String TAG = "MatterScreenViewAdapter";
	ArrayList<MatterScreenType> type = new ArrayList<MatterScreenType>();
	ScreenType screenType;
	public  MatterScreenViewAdapter(Context context){
	    this.context = context;
	    screenType = ScreenType.getInstance();
	}
	
	public void refresh(ArrayList<MatterScreenType> type){
		this.type.clear();
		this.type.addAll(type);
		Log.d(TAG, "refresh type content:"+type);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		Log.d(TAG,"getCount type content"+type);
		return type.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if(convertView == null)
			convertView = LayoutInflater.from(context).inflate(R.layout.top_singleline_textview, null);
		
		Log.d(TAG, "getView textBtn text :"+type.get(position).content);
		TextView textBtn = ViewHolder.get(convertView, R.id.textBtn);
		textBtn.setText(type.get(position).content);
		textBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeToType(type.get(position));
			}
		});
		
		return convertView;
	}
	
	private void removeToType(MatterScreenType type){
		try {
			type.removeType(screenType);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "error"+e.getMessage());
		}finally{
			if(removeTypeListener != null){
				removeTypeListener.removeType();
			}
		}
	}
	
	public interface OnRemoveTypeListener{
		void removeType();
	}
	
	OnRemoveTypeListener removeTypeListener;
	public void setOnRemoveTypeListener(OnRemoveTypeListener listener){
		this.removeTypeListener = listener;
	}

}
