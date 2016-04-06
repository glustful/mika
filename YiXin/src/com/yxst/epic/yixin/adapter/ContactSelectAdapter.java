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
import com.yxst.epic.yixin.listener.OnMemberDeleteListener;
import com.yxst.epic.yixin.utils.Utils;
import com.yxst.epic.yixin.view.ContactItemSelectView;
import com.yxst.epic.yixin.view.ContactItemSelectView_;

@EBean
public class ContactSelectAdapter extends BaseAdapter {

	@RootContext
	Context context;

	@Override
	public int getCount() {
		return mMemberList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMemberList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactItemSelectView view;

		if (convertView == null) {
			view = ContactItemSelectView_.build(context);
		} else {
			view = (ContactItemSelectView) convertView;
		}

		view.bind((Member) getItem(position));
		view.setOnMemberDeleteListener(mOnMemberDeleteListener);

		return view;
	}

	private OnMemberDeleteListener mOnMemberDeleteListener;
	
	public void setOnMemberDeleteListener(OnMemberDeleteListener l) {
		mOnMemberDeleteListener = l;
		this.notifyDataSetChanged();
	}
	
	private List<Member> mMemberList = new ArrayList<Member>();

	public void addMember(Member member) {
		int index = Utils.listIndexOf(mMemberList, member.Uid);
		if (index == -1) {
			mMemberList.add(member);
		} else {
			mMemberList.set(index, member);
		}
		this.notifyDataSetChanged();
	}
	
	public void addMembers(List<Member> members) {
		if (members != null) {
			mMemberList.addAll(members);
			this.notifyDataSetChanged();
		}
	}

	public void removeMember(Member member) {
		if (member != null) {
			mMemberList.remove(Utils.listGet(mMemberList, member.Uid));
			this.notifyDataSetChanged();
		}
	}

	public List<Member> getMembers() {
		return mMemberList;
	}
}
