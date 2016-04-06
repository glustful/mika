package com.yxst.epic.yixin.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yxst.epic.yixin.data.dto.model.Member;

public class LoginResponse extends Response {

	private static final long serialVersionUID = -1333609560593757271L;

	/**
	 * 登录token
	 */
	@JsonProperty(value = "token")
	public String Token;

	@JsonProperty(value = "uid")
	public String Uid;
	
	@JsonProperty(value = "member")
	public Member Member;

}
