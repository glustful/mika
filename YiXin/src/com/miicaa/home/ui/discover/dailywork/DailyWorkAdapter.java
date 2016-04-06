package com.miicaa.home.ui.discover.dailywork;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.ui.discover.DiscoverAdapter;
import com.miicaa.home.ui.discover.DisoverData;
import com.miicaa.home.ui.org.LableGroup;
import com.miicaa.home.ui.report.ReportDetailActivity_;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.ViewHolder;

public class DailyWorkAdapter extends DiscoverAdapter {
	

	public DailyWorkAdapter(Context context){
		super(context);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DisoverData disoverData = discoverDataList.get(position);
		if(convertView == null)
			convertView = inflater.inflate(R.layout.detection_cell_view, null);
		LableGroup lableGroup = ViewHolder.get(convertView, R.id.detection_cell_lableGroup);
		ImageView headImageView = ViewHolder.get(convertView, R.id.detection_cell_id_head);
		TextView nameTextView = ViewHolder.get(convertView, R.id.detection_cell_id_name);
		ImageView planImageView = ViewHolder.get(convertView, R.id.detection_cell_id_plan);
		ImageView remindImageView = ViewHolder.get(convertView, R.id.detection_cell_id_remind);
		ImageView attchmentImageView = ViewHolder.get(convertView, R.id.detection_cell_id_attechment);
		TextView titleTextView = ViewHolder.get(convertView, R.id.detection_cell_id_title);
		TextView descriPtionTextView = ViewHolder.get(convertView, R.id.detection_cell_id_momo);
		
		lableGroup.removeAllViews();
		if(disoverData.labels != null){
			for(int i = 0 ; i < disoverData.labels.length ;i++){
				lableGroup.addView(AllUtils.getLabelView(mContext, disoverData.labels[i]));
			}
		}
		
		if(disoverData.userCode != null)
			Tools.setHeadImg(disoverData.userCode, headImageView);
		nameTextView.setText(disoverData.userName != null ? disoverData.userName : "");
		if(disoverData.planTime != null && disoverData.planTime > 0)
			planImageView.setVisibility(View.VISIBLE);
		else
			planImageView.setVisibility(View.GONE);
		if(disoverData.remindTime != null && disoverData.remindTime > 0)
			remindImageView.setVisibility(View.VISIBLE);
		else
			remindImageView.setVisibility(View.GONE);
		if(disoverData.hasAttchment != null && disoverData.hasAttchment)
			attchmentImageView.setVisibility(View.VISIBLE);
		else
			attchmentImageView.setVisibility(View.GONE);
		titleTextView.setText(disoverData.title != null ? disoverData.title : "");
		descriPtionTextView.setText(disoverData.descriPtion != null ? disoverData.descriPtion : "");
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ReportDetailActivity_.intent(mContext)
				.dataId(disoverData.id)
				.isDiscover(true)
				.startForResult(100);
				
			}
		});
		
		return convertView;
		
		
	}

}
