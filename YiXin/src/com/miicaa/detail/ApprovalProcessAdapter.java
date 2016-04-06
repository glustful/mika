package com.miicaa.detail;

import java.util.ArrayList;
import java.util.List;

import com.aps.m;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.ui.menu.SelectPersonInfo;
import com.miicaa.utils.ViewHolder;
import com.yxst.epic.yixin.model.Content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ApprovalProcessAdapter extends BaseAdapter{

	Context mContext;
	LayoutInflater inflater;
	List<ApprovalProcessItem> apList;
	
    public ApprovalProcessAdapter(Context context) {
    	apList = new ArrayList<ApprovalProcessItem>();
    	mContext = context;
    	inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
    
    public void refresh(List<ApprovalProcessItem> aps){
    	apList = aps;
    	notifyDataSetChanged();
    }
	
	@Override
	public int getCount() {
		return apList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ApprovalProcessItem item = apList.get(position);
		if(convertView == null)
			convertView = inflater.inflate(R.layout.approval_process_view, null);

		TextView processTextView = ViewHolder.get(convertView, R.id.processTextView);
		processTextView.setText(item.numApprove);
		TextView description = ViewHolder.get(convertView, R.id.descriPtTextView);
		description.setText(item.description);
		GridView gridView = ViewHolder.get(convertView, R.id.gridView);
		gridView.setAdapter(new ProcessGriAdapter().refresh(item.personList));
		return null;
	}
	
	
	class ProcessGriAdapter extends BaseAdapter{

		List<SelectPersonInfo> personInfoList;
		
		public ProcessGriAdapter() {
			personInfoList = new ArrayList<SelectPersonInfo>();
		}
		
		public ProcessGriAdapter refresh(List<SelectPersonInfo> personInfos){
			personInfoList = personInfos;
			return this;
		}
		
		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return personInfoList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SelectPersonInfo personInfo = personInfoList.get(position);
			if(convertView == null)
				convertView = inflater.inflate(R.layout.approve_processitem_gridadapter_view, null);
			
			ImageView imageView = ViewHolder.get(convertView, R.id.headImageView);
			TextView nameTextView = ViewHolder.get(convertView, R.id.nameTextView);
			Tools.setHeadImg(personInfo.mCode, imageView);
			nameTextView.setText(personInfo.mName);
			
			return convertView;
		}
		
	}

}
