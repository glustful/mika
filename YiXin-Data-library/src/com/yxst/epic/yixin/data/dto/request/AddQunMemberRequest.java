package com.yxst.epic.yixin.data.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.yxst.epic.yixin.data.dto.model.Member;

public class AddQunMemberRequest extends Request {

	public String ChatRoomName;
	
	public List<Member> memberList;
	
	public void addMember(Member member) {
		if (this.memberList == null) {
			this.memberList = new ArrayList<Member>();
		}
		this.memberList.add(member);
	}
}
