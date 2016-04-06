package com.yxst.epic.yixin.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PushResponse extends Response {

	private static final long serialVersionUID = -1045667322634119822L;

	@JsonProperty(value="msgID")
	public String MsgID;

	@JsonProperty(value="clientMsgId")
	public String ClientMsgId;

	public long mid;
}
