package com.miicaa.home.ui.matter.approveprocess;

import java.util.ArrayList;
import java.util.List;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.ui.menu.SelectPersonInfo;
import com.miicaa.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class FixedProcessSingleAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater inflater;
	List<SelectPersonInfo> processPersonList;
	
	public FixedProcessSingleAdapter(Context context){
		this.mContext = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		processPersonList = new ArrayList<SelectPersonInfo>();
	}
	
	public void refresh(List<SelectPersonInfo> selList){
		processPersonList.clear();
		processPersonList.addAll(selList);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return processPersonList.size();
	}

	@Override
	public Object getItem(int position) {
		return processPersonList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final SelectPersonInfo info = processPersonList.get(position);
		
		if(convertView == null)
			convertView = inflater.inflate(R.layout.fixed_process_chidview, null);
		
		ImageView headImageView = ViewHolder.get(convertView, R.id.headImageView);
		CheckBox mCheckBox = ViewHolder.get(convertView, R.id.childCheckBox);
		mCheckBox.setText(info.mName);
		mCheckBox.setChecked(info.isSelect);
		mCheckBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox me = (CheckBox)v;
				info.isSelect = me.isChecked();
			}
		});
		Tools.setHeadImg(info.mCode, headImageView);
		return convertView;
	}

}
