package com.yxst.epic.yixin.data.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yxst.epic.yixin.data.dto.model.Member;

public class CreateQunResponse extends Response {

	public static final long serialVersionUID = -1905946076282832331L;

	public String ChatRoomName;

	@JsonProperty(value = "topic")
	public String Topic;

	@JsonProperty(value = "memberCount")
	public int MemberCount;

	@JsonProperty(value = "memberList")
	public List<Member> MemberList;

}
