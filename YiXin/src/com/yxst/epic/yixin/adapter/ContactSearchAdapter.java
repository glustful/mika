package com.yxst.epic.yixin.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.view.ContactItemView;
import com.yxst.epic.yixin.view.ContactItemView_;

@EBean
public class ContactSearchAdapter extends BaseAdapter {

	@RootContext
	Context context;

	private List<Member> members = new ArrayList<Member>();
	
	@Override
	public int getCount() {
		return members.size();
	}

	@Override
	public Object getItem(int position) {
		return members.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactItemView view;

		if (convertView == null) {
			view = ContactItemView_.build(context);
		} else {
			view = (ContactItemView) convertView;
		}

		Member member = (Member) getItem(position);
		view.bind(member);
		view.setDividerVisible(position != 0);
		
		return view;
	}

	public void addMembers(List<Member> members) {
		this.members.addAll(members);
		this.notifyDataSetChanged();
	}

	public void clear() {
		this.members.clear();
		this.notifyDataSetChanged();
	}

}
