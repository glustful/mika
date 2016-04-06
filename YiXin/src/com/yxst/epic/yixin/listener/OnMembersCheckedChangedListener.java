package com.yxst.epic.yixin.listener;

import java.util.List;

import com.yxst.epic.yixin.data.dto.model.Member;

public interface OnMembersCheckedChangedListener {

	public void onMembersCheckedChanged(List<Member> members, boolean isChecked);
}
