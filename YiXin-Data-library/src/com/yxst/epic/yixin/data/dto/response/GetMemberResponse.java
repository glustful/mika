package com.yxst.epic.yixin.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yxst.epic.yixin.data.dto.model.Member;

public class GetMemberResponse extends Response {

	private static final long serialVersionUID = 6840544300239008166L;

	@JsonProperty(value="member")
	public Member Member;
	
}
