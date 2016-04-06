package com.miicaa.home.ui.discover;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class DiscoverAdapter extends BaseAdapter{
	
	protected Context mContext;
	protected LayoutInflater inflater;
	protected Resources resources;
	protected List<DisoverData> discoverDataList;

	public DiscoverAdapter(Context context){
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		resources = mContext.getResources();
		discoverDataList = new ArrayList<DisoverData>();
	}
	
	
	public void refresh(List<DisoverData> dataList){
		discoverDataList.clear();
		discoverDataList.addAll(dataList);
		notifyDataSetChanged();
	}
	
	public void add(List<DisoverData> dataList){
		discoverDataList.addAll(dataList);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return discoverDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	

}
