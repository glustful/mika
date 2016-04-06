package com.miicaa.home.ui.checkwork;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.utils.ViewHolder;

@SuppressLint("InflateParams")
public class TongjiCheckWorkAdapter extends BaseAdapter{
	
	LayoutInflater inflater;
	Context mContext;
	ArrayList<CheckWorkTongjiContents> tongjiList;
	
	public TongjiCheckWorkAdapter(Context context){
		this.mContext = context;
		tongjiList = new ArrayList<CheckWorkTongjiContents>();
		inflater = LayoutInflater.from(context);
	}
	
	public void refresh(ArrayList<CheckWorkTongjiContents> tonjiList){
		this.tongjiList.clear();
		this.tongjiList.addAll(tonjiList);
		notifyDataSetChanged();
	}
	
	public void addMore(ArrayList<CheckWorkTongjiContents> tongjiList){
		this.tongjiList.addAll(tongjiList);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return tongjiList.size() + 1;
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
		if(convertView == null){
			convertView = inflater.inflate(R.layout.chekwork_tongji_adapter_view, null);
		}
		int height = (int) mContext.getResources().getDimension(R.dimen.tongji_item_height);
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,height);
		convertView.setLayoutParams(lp);
		if(position %2 != 0){
			convertView.setBackgroundColor(mContext.getResources()
					.getColor(R.color.checkwork_tongjiitemview_color));
		}else{
			convertView.setBackgroundColor(Color.WHITE);
		}
		TextView nameTextView = ViewHolder.get(convertView, R.id.nameTextView);
		TextView laterTextView = ViewHolder.get(convertView, R.id.lateTextView);
		TextView leavedTextView = ViewHolder.get(convertView, R.id.leavedTextView);
		TextView nosignInTextView = ViewHolder.get(convertView, R.id.nosignInTextView);
		TextView nosignOutTextView = ViewHolder.get(convertView, R.id.nosignOutTextView);
		if(position > 0){
			CheckWorkTongjiContents tongjiCotnent = tongjiList.get(position-1);
			nameTextView.setText(tongjiCotnent.name);
			laterTextView.setText(tongjiCotnent.later);
			leavedTextView.setText(tongjiCotnent.leaved);
			nosignInTextView.setText(tongjiCotnent.nosignInCount);
			nosignOutTextView.setText(tongjiCotnent.nosignOutCount);
		}else{
			nameTextView.setText(R.string.write_familayname);
			laterTextView.setText(R.string.tongji_checkwork_later);
			leavedTextView.setText(R.string.tongji_checkwork_leaved);
			nosignInTextView.setText(R.string.tongji_checkwork_nosignIn);
			nosignOutTextView.setText(R.string.tongji_checkwork_nosignOut);
		}
		return convertView;
	}

}
