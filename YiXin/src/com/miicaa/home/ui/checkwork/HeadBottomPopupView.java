package com.miicaa.home.ui.checkwork;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.miicaa.common.base.PopupItem;
import com.miicaa.home.R;
import com.miicaa.utils.ViewHolder;

public class HeadBottomPopupView {

	Context mContext;
	ArrayList<PopupItem> mItems; 
	LayoutInflater inflater;
	Resources resources;
	View popView;
	PopupWindow mPopupWindow;
	MyBottomPopupAdapter adapter;
	
	public HeadBottomPopupView(Context context){
		inflater = LayoutInflater.from(context);
		this.mContext = context;
		resources = mContext.getResources();
	}
	public HeadBottomPopupView setItems(ArrayList<PopupItem> items){
		mItems.clear();
		mItems.addAll(items);
		return this;
	}
	
	@SuppressLint({ "InflateParams", "NewApi" })
	public HeadBottomPopupView build(){
//		mItems = new ArrayList<PopupItem>();
		mItems = (mItems != null) ? mItems : new ArrayList<PopupItem>();
		adapter = new MyBottomPopupAdapter();
		popView = inflater.inflate(R.layout.normal_list_view, null);
		popView.setBackgroundColor(resources.getColor(R.color.checkwork_popmenu_color));
		ListView listView= (ListView)popView.findViewById(R.id.listView);		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(listItemClickListener);
		mPopupWindow = new PopupWindow(popView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				if(clickListener_ != null){
					clickListener_.popDismiss();
				}
			}
		});
		return this;
	}
	
	public static HeadBottomPopupView build(Context context){
		HeadBottomPopupView v = new HeadBottomPopupView(context);
		v.build();
		return v;
	}
	
	
	public HeadBottomPopupView setItems(PopupItem item){
		mItems.add(item);
		return this;
	}
	
	OnBottomPopWindowItemClickListener clickListener_;
	public HeadBottomPopupView setOnBottomPopWindowClickListener(OnBottomPopWindowItemClickListener listener){
		clickListener_ = listener;
		return this;
	}
	public void showBottom(View parent){
		adapter.notifyDataSetChanged();
		int xoff = -parent.getMeasuredWidth()/4;
//		int height = parent.getHeight();
//		mPopupWindow.showAtLocation(parent, Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
		mPopupWindow.showAsDropDown(parent, xoff, 0);
	}
	
	
	
	OnItemClickListener listItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			PopupItem item = (PopupItem)adapter.getItem(position);
			mPopupWindow.dismiss();
			if(clickListener_ != null){
				clickListener_.itemClick(item);
			}
		}
	};
	class MyBottomPopupAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressLint({ "InflateParams", "NewApi" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.bottom_popupwindow_view, null);
			}
			TextView textView = ViewHolder.get(convertView, R.id.textView);
			String str = mItems.get(position).mContent;
			textView.setText(str);
			convertView.setBackground(resources.getDrawable(R.drawable.checkwork_pop_menu_selector));
			return convertView;
		}
		
	}
	
	public interface OnBottomPopWindowItemClickListener{
		void itemClick(PopupItem item);
		void popDismiss();
	}
	
}
