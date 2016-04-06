package com.miicaa.detail;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.detail.DetailProgressFragment.PrgressDoPeople;
import com.miicaa.home.R;
import com.miicaa.utils.ViewHolder;

public class ProgressGridAdapter extends BaseAdapter{
	Context context;
	ArrayList<PrgressDoPeople> infos;
	HashMap<String, Boolean> mShowMap;
	public ProgressGridAdapter(Context context){
		infos = new ArrayList<PrgressDoPeople>();
		mShowMap = new HashMap<String, Boolean>();
		this.context = context;
	}

	public void refresh(ArrayList<PrgressDoPeople> infos,HashMap<String, Boolean> showMap){
		this.infos.clear();
		this.infos.addAll(infos);
		mShowMap = showMap;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos != null ?infos.size():0;
	}

	@Override
	public String getItem(int arg0) {
		// TODO Auto-generated method stub
		return infos.get(arg0).usercode;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertview, ViewGroup viewgroup) {
		
		if(convertview == null){
			convertview = LayoutInflater.from(context).inflate(R.layout.progress_do_people, null);
		}
		ImageView head = ViewHolder.get(convertview, R.id.userhead);
		TextView name = ViewHolder.get(convertview, R.id.username);
		RatingBar rating = ViewHolder.get(convertview, R.id.gridStar);
		if(infos.get(position).star != null && infos.get(position).star > 0){
			rating.setVisibility(View.VISIBLE);
			rating.setRating(infos.get(position).star);
		}else{
			rating.setVisibility(View.GONE);
		}
		if(mShowMap.containsKey(infos.get(position).usercode) && mShowMap.get(infos.get(position).usercode)){
			convertview.setBackgroundColor(context.getResources().getColor(R.color.gridSeletcolor));
		}else{
			convertview.setBackgroundColor(Color.TRANSPARENT);
		}
		Tools.setHeadImg(infos.get(position).usercode, head);
		name.setText(infos.get(position).username);
		return convertview;
	}
	
	

}
