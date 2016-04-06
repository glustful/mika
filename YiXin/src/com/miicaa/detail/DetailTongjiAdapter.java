package com.miicaa.detail;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.utils.ViewHolder;

public class DetailTongjiAdapter extends BaseAdapter{

	ArrayList<ProgressTongjiInfo.DoStatus> doinfos;
	
	Context context;
	public DetailTongjiAdapter(Context context){
		this.context = context;
	}
	
	public void refresh(ArrayList<ProgressTongjiInfo.DoStatus> doinfos){
		this.doinfos = doinfos;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return doinfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup viewgroup) {
		// TODO Auto-generated method stub
		ProgressTongjiInfo.DoStatus info = doinfos.get(position);
		if(convertview == null){
			
			convertview = LayoutInflater.from(context).inflate(R.layout.matter_do_progress_detail, null);
			
		}
		TextView content = ViewHolder.get(convertview, R.id.content);
		TextView time = ViewHolder.get(convertview, R.id.time);
		TextView complete = ViewHolder.get(convertview, R.id.complete);
		TextView from = ViewHolder.get(convertview, R.id.from);
//		content.setText(info.);
		return convertview;
		
		
		
	}

}
