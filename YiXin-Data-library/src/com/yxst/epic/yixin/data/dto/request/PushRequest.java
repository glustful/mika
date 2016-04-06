package com.yxst.epic.yixin.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yxst.epic.yixin.data.dto.model.Msg;


public class PushRequest extends Request {

	@JsonProperty(value="msg")
	public Msg Msg;

}
