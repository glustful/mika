package com.miicaa.home.ui.contactList;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.miicaa.home.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ContactListAdapter extends BaseAdapter{

	private static final int USER_INFO = 0;
	private static final int WORD_INFO = 1;
	ArrayList<Object> contactList;
	
	LayoutInflater inflater;
	public ContactListAdapter(Context context,ArrayList<Object> contactDatas){
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object object = contactList.get(position);
		if(getItemViewType(position) == USER_INFO){
			if(convertView == null){
				convertView = inflater.inflate(R.layout.contact_adapter_view, null);
			}
		}else if(getItemViewType(position) == WORD_INFO){
			if(convertView == null){
				convertView = inflater.inflate(R.layout.contact_word_view, null);
			}
		}
		return convertView;
	}

}
