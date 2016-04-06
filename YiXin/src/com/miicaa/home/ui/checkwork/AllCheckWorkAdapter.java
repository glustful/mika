package com.miicaa.home.ui.checkwork;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.ViewHolder;

public class AllCheckWorkAdapter extends BaseAdapter{

	LayoutInflater inflater;
	Context mContext;
	Long nowDate;
	String nowDateStr;
	ArrayList<CheckWorkDetailContent> detailContents;
	public AllCheckWorkAdapter(Context context){
		this.mContext = context;
		detailContents = new ArrayList<CheckWorkDetailContent>();
		inflater = LayoutInflater.from(context);
	}
	
	public void refesh(ArrayList<CheckWorkDetailContent> detailContents,Long nowDateStr){
		this.nowDate = nowDateStr;
		this.nowDateStr = AllUtils.getYearTime(nowDate);
		this.detailContents.clear();
		this.detailContents.addAll(detailContents);
		notifyDataSetChanged();
	}
	
	public void addMore(ArrayList<CheckWorkDetailContent> detailContents){
		this.detailContents.addAll(detailContents);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return detailContents.size();
	}

	@Override
	public Object getItem(int position) {
		return detailContents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.checkwork_content_view, null);
		}
		CheckWorkDetailContent detailContent = detailContents.get(position);
		TextView title = ViewHolder.get(convertView, R.id.title);
		TextView signInTextView = ViewHolder.get(convertView, R.id.signInText);
		TextView signInWhereTextView = ViewHolder.get(convertView, R.id.signInWhereText);
		TextView signOutTextView = ViewHolder.get(convertView, R.id.signOutText);
		TextView signOutWhereTextView = ViewHolder.get(convertView, R.id.signOutWhereText);
		title.setText(detailContent.name + detailContent.dateStr);
		if(detailContent.signInDate == null || detailContent.signInDate.length() == 0){
			if(nowDateStr != null && detailContent.date.equals(nowDateStr)){
				signInTextView.setText("");
			}else{
			signInTextView.setText("未签到");
			signInTextView.setTextColor(mContext.getResources().getColor(R.color.checkwork_nosign_color));
			}
		}else if(detailContent.signInStatusStr != null && 
				detailContent.signInStatusStr.length() > 0){
			SpannableStringBuilder sb = new SpannableStringBuilder();
			sb.append(detailContent.signInDate);
			String status =  detailContent.signInStatusStr;
			sb.append(status);
			int nosignColor = mContext.getResources().getColor(R.color.checkwork_nosign_color);
			sb.setSpan(new ForegroundColorSpan(nosignColor) , 
					detailContent.signInDate.length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			signInTextView.setText(sb);
		}else{
			signInTextView.setTextColor(Color.BLACK);
		signInTextView.setText(detailContent.signInDate);
		}
		if(detailContent.WhereIsIp(detailContent.signInWhere)){
			setTextDrawableNull(signInWhereTextView);
			signInWhereTextView.setText(detailContent.signInWhere);
		}else if(detailContent.signInWhere == null || detailContent.signInWhere.length() == 0){
			setTextDrawableNull(signInWhereTextView);
			signInWhereTextView.setText("");
		}else {
			MyCheckWorkAdapter.setTextDrawableWithLocation(signInWhereTextView,mContext);
			signInWhereTextView.setText(detailContent.signInWhere);
		}
		
		if(detailContent.signOutDate == null || detailContent.signOutDate.length() == 0){
			if(nowDateStr != null && detailContent.date.equals(nowDateStr)){
				signOutTextView.setText("");
			}else{
			signOutTextView.setText("未签退");
			signOutTextView.setTextColor(mContext.getResources().getColor(R.color.checkwork_nosign_color));
			}
		}else if(detailContent.signOutStatusStr != null && 
				detailContent.signOutStatusStr.length() > 0){
			SpannableStringBuilder sb = new SpannableStringBuilder();
			sb.append(detailContent.signOutDate);
			String status =  detailContent.signOutStatusStr ;
			sb.append(status);
			int nosignColor = mContext.getResources().getColor(R.color.checkwork_nosign_color);
			sb.setSpan(new ForegroundColorSpan(nosignColor) , 
					detailContent.signOutDate.length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			signOutTextView.setText(sb);
//			signOutTextView.setTextColor(mContext.getResources().getColor(R.color.checkwork_nosign_color));
		}else{
		signOutTextView.setText(detailContent.signOutDate);
		signOutTextView.setTextColor(Color.BLACK);
		}
		if(detailContent.WhereIsIp(detailContent.signOutWhere)){
			setTextDrawableNull(signOutWhereTextView);
			signOutWhereTextView.setText(detailContent.signOutWhere);
		}else if(detailContent.signOutWhere == null || detailContent.signOutWhere.length() == 0){
			setTextDrawableNull(signOutWhereTextView);
			signOutWhereTextView.setText("");
		}else{
			MyCheckWorkAdapter.setTextDrawableWithLocation(signOutWhereTextView,mContext);
			signOutWhereTextView.setText(detailContent.signOutWhere);
		}
		return convertView;
	}
	
	private void setTextDrawableNull(TextView textView){
		textView.setCompoundDrawables(null, null, null, null);
	}

}
