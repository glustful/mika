package com.yxst.epic.yixin.data.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yxst.epic.yixin.data.dto.model.Member;

public class CreateQunRequest extends Request {

	// @JsonProperty(value = "creatorId")
	// public String CreatorId;

	@JsonProperty(value = "topic")
	public String Topic;

	@JsonProperty(value = "memberCount")
	public int MemberCount;

	@JsonProperty(value = "memberList")
	public List<Member> MemberList;

	public void addUid(String uid) {
		Member member = new Member();
		member.Uid = uid;
		addMember(member);
	}
	
	public void addMember(Member member) {
		if (this.MemberList == null) {
			this.MemberList = new ArrayList<Member>();
		}
		this.MemberList.add(member);
	}
	
	public void addMembers(List<Member> members) {
		if (this.MemberList == null) {
			this.MemberList = new ArrayList<Member>();
		}
		this.MemberList.addAll(members);
	}
}
