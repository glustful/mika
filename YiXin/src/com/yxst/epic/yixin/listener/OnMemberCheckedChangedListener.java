package com.yxst.epic.yixin.listener;

import com.yxst.epic.yixin.data.dto.model.Member;

public interface OnMemberCheckedChangedListener {

	public void onMemberCheckedChanged(Member member, boolean isChecked);
}
