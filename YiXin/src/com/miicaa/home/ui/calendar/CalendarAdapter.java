package com.miicaa.home.ui.calendar;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miicaa.detail.MatterDetailAcrtivity_;
import com.miicaa.home.R;
import com.miicaa.home.ui.report.ReportDetailActivity_;
import com.miicaa.utils.AllUtils;

public class CalendarAdapter extends BaseAdapter{
	
		private Context mContext;
		private ArrayList<CalendarEntity> adapterData;

		public CalendarAdapter(Context mContext, ArrayList<CalendarEntity> adapterData) {
			this.mContext = mContext;
			this.adapterData = adapterData;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.adapterData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.adapterData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view =  convertView;
			if(view == null){
				view = LayoutInflater.from(this.mContext).inflate(R.layout.calendar_list_item, null);
			}
			final CalendarEntity entity = this.adapterData.get(position);
			TextView tv = (TextView) view.findViewById(R.id.calendar_list_title);
			tv.setText(entity.getTitle());
			tv.setTextColor(entity.getmColor());
			
			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(AllUtils.reporteType.equals(entity.getDataType())){
						 ReportDetailActivity_.intent(mContext)
		      		   .dataId(entity.getId())
		      		   .startForResult(100);
					}else{
					MatterDetailAcrtivity_.intent(mContext)
             	   .dataId(entity.getId())
             	   .dataType(entity.getDataType())
             	   .operateGroup(entity.getOperateGroup())
             	   .status(entity.getStatus())
             	   .startForResult(100);
					}
				}
			});
			return view;
		}

}
