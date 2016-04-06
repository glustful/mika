package com.yxst.epic.yixin.data.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yxst.epic.yixin.data.dto.model.Member;

public class SearchResponse extends Response {

	private static final long serialVersionUID = -2369027069327315694L;

	@JsonProperty(value = "count")
	public int Count;

	@JsonProperty(value = "memberListSize")
	public int MemberListSize;

	@JsonProperty(value = "memberList")
	public List<Member> MemberList;
}
