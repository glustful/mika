package com.yxst.epic.yixin.data.dto.response;

import java.util.List;

import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.model.QunInfo;

public class AddQunMemberResponse extends Response {

	private static final long serialVersionUID = 5632693654978546765L;

	public List<Member> memberList;
	
	public QunInfo quninfo;
}
