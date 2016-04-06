package com.miicaa.home.ui.matter.approveprocess;

import java.util.ArrayList;
import java.util.List;

import com.aps.m;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.ui.menu.SelectPersonInfo;
import com.miicaa.utils.ViewHolder;
import com.yxst.epic.yixin.model.Content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ApprovalProcessAdapter extends BaseAdapter{

	private static String TAG = "ApprovalProcessAdapter";
	
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ApprovalProcessItem item = apList.get(position);
		Log.d(TAG, "item"+item.toString());
		if(convertView == null)
			convertView = inflater.inflate(R.layout.approval_process_view, null);

		ImageView leftTline = ViewHolder.get(convertView, R.id.leftTLine);
		ImageView leftBline = ViewHolder.get(convertView, R.id.leftBline);
		View contentLayout = ViewHolder.get(convertView, R.id.cotentLayout);
		
		Resources resources = mContext.getResources();
		if(position + 1 <= item.currentStep){
			leftTline.setImageDrawable(resources.getDrawable(R.drawable.approve_process_midline_did));
//			leftBline.setBackgroundColor(resources.getColor(R.color.process_vitline_select));
//			leftBline.setImageResource(resources.getColor(R.color.process_vitline_select));
			leftBline.setImageDrawable(resources.getDrawable(R.drawable.approve_process_top_did));
			contentLayout.setBackground(resources.getDrawable(R.drawable.approve_process_content_did));
		}else{
			leftTline.setImageDrawable(resources.getDrawable(R.drawable.approve_process_midline));
//			leftBline.setImageResource(resources.getColor(R.color.process_vitline));
//			leftBline.setBackgroundColor(resources.getColor(R.color.process_vitline));
			leftBline.setImageDrawable(resources.getDrawable(R.drawable.approve_process_top));
			contentLayout.setBackground(resources.getDrawable(R.drawable.approve_process_content));
		}
		
		if(position == apList.size()-1){
			leftBline.setImageDrawable(null);
			if(position + 1 == item.currentStep){
				leftTline.setImageDrawable(resources.getDrawable(R.drawable.approve_process_bottomline_did));
			}else{
			leftTline.setImageDrawable(resources.getDrawable(R.drawable.approve_process_bottomline));
			}
		}
		TextView processTextView = ViewHolder.get(convertView, R.id.processTextView);
		processTextView.setText(item.numApprove);
		TextView description = ViewHolder.get(convertView, R.id.descriPtTextView);
		description.setText(item.description);
		GridView gridView = ViewHolder.get(convertView, R.id.gridView);
		gridView.setAdapter(new ProcessGriAdapter(item.personList));
//		leftBline.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//				gridView.getMeasuredHeight()));
		return convertView;
	}
	
	
	class ProcessGriAdapter extends BaseAdapter{

		List<SelectPersonInfo> personInfoList;
		
		public ProcessGriAdapter(List<SelectPersonInfo> personInfos) {
			personInfoList = personInfos;
		}
		
		public ProcessGriAdapter refresh(List<SelectPersonInfo> personInfos){
			personInfoList = personInfos;
			notifyDataSetChanged();
			return this;
		}
		
		@Override
		public int getCount() {
			return personInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SelectPersonInfo personInfo = personInfoList.get(position);
			Log.d(TAG, "grid person getView : personInfo:"+personInfo.toString());
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
