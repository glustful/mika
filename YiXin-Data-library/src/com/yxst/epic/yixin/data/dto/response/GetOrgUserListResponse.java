package com.yxst.epic.yixin.data.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yxst.epic.yixin.data.dto.model.Member;

public class GetOrgUserListResponse extends Response {

	private static final long serialVersionUID = 2676195480956844749L;

	@JsonProperty(value = "memberCount")
	public int MemberCount;

	@JsonProperty(value = "memberList")
	public List<Member> MemberList;

}
