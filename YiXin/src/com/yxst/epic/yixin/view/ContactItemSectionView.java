package com.yxst.epic.yixin.view;

import java.util.List;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.listener.OnMembersCheckedChangedListener;

@EViewGroup(R.layout.list_item_contact_section)
public class ContactItemSectionView extends LinearLayout {

	@ViewById(R.id.tv_name)
	TextView mNameTV;
	
	@ViewById
	CheckBox cb;
	
	public ContactItemSectionView(Context context) {
		super(context);
	}
	public ContactItemSectionView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void bind(int textResId) {
		mNameTV.setText(textResId);
	}
	public void bind(String key) {
		mNameTV.setText(key);
	}
	
	public void setVisible(boolean visible) {
		cb.setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	@CheckedChange(R.id.cb)
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (mOnMembersCheckedChangedListener != null) {
			mOnMembersCheckedChangedListener.onMembersCheckedChanged(members,
					isChecked);
		}
	}
	
	OnMembersCheckedChangedListener mOnMembersCheckedChangedListener;
	public void setOnMembersCheckedChangedListener(OnMembersCheckedChangedListener l) {
		mOnMembersCheckedChangedListener = l;
	}
	List<Member> members;
	public void setMembers(List<Member> members) {
		this.members = members;
	}
}
