package com.yxst.epic.yixin.adapter;

import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.listener.OnMemberCheckedChangedListener;
import com.yxst.epic.yixin.utils.Utils;
import com.yxst.epic.yixin.view.ContactItemSectionView;
import com.yxst.epic.yixin.view.ContactItemSectionView_;
import com.yxst.epic.yixin.view.ContactItemView;
import com.yxst.epic.yixin.view.ContactItemView_;

@EBean
public class ContactListAdapter extends SectionedBaseAdapter {

	@RootContext
	Context context;

	boolean isSelectMode;

	private OnMemberCheckedChangedListener mOnMemberCheckedChangedListener;

	@AfterInject
	void afterInject() {
		// ACache.get(context).getAsObject("");
	}

	public void setOnMemberCheckedChangedListener(
			OnMemberCheckedChangedListener l) {
		mOnMemberCheckedChangedListener = l;
		this.notifyDataSetChanged();
	}

	private Map<String, List<Member>> map;

	public void changeData(Map<String, List<Member>> map) {
		this.map = map;
		this.notifyDataSetChanged();
	}

	public Map<String, List<Member>> getData() {
		return this.map;
	}

	public void setIsSelectMode(boolean isSelectMode) {
		this.isSelectMode = isSelectMode;
		this.notifyDataSetChanged();
	}

	public Object getItemBySection(int section) {
		Map.Entry<String, List<Member>> entry = Utils.mapGet(map, section);
		if (entry != null) {
			return entry.getKey();
		}
		return null;
	}

	@Override
	public Object getItem(int section, int position) {
		Map.Entry<String, List<Member>> entry = Utils.mapGet(map, section);
		if (entry != null) {
			List<Member> memberList = entry.getValue();
			if (memberList != null) {
				return memberList.get(position);
			}
		}
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		return 0;
	}

	@Override
	public int getSectionCount() {
		return map == null ? 0 : map.size();
	}

	@Override
	public int getCountForSection(int section) {
		Map.Entry<String, List<Member>> entry = Utils.mapGet(map, section);
		List<Member> memberList = entry.getValue();
		if (memberList != null) {
			return memberList.size();
		}
		return 0;
	}

	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {
		ContactItemView view;

		if (convertView == null) {
			view = ContactItemView_.build(context);
		} else {
			view = (ContactItemView) convertView;
		}

		Member member = (Member) getItem(section, position);
		view.setOnMemberCheckedChangedListener(null);
		view.bind(member, isSelectMode, isSelected(member), isLocked(member));
		view.setDividerVisible(position != 0);
		view.setOnMemberCheckedChangedListener(mOnMemberCheckedChangedListener);

		return view;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		ContactItemSectionView view;

		if (convertView == null) {
			view = ContactItemSectionView_.build(context);
		} else {
			view = (ContactItemSectionView) convertView;
		}

		String key = (String) getItemBySection(section);
		view.bind(key);

		return view;
	}

	private List<Member> mSelectMembers;

	public void setSelectMembers(List<Member> selectMembers) {
		mSelectMembers = selectMembers;
		this.notifyDataSetChanged();
	}

	private boolean isSelected(Member member) {
		if (mSelectMembers != null) {
			return Utils.listContains(mSelectMembers, member.Uid);
		}
		return false;
	}

	private boolean isLocked(Member member) {
		if (lockMembers != null) {
			return Utils.listContains(lockMembers, member.Uid);
		}
		return false;
	}

	private List<Member> lockMembers;

	public void setLockMembers(List<Member> lockMembers) {
		this.lockMembers = lockMembers;
		this.notifyDataSetChanged();
	}
}
