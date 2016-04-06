package com.yxst.epic.yixin.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetMemberRequest extends Request {

	@JsonProperty(value="userName")
	public String UserName;
	
}
