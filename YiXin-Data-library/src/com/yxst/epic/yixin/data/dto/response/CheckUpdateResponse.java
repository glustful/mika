package com.yxst.epic.yixin.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yxst.epic.yixin.data.dto.model.Msg;

public class CheckUpdateResponse extends Response {

	private static final long serialVersionUID = 5033737217934061340L;

	public boolean isUpdate;
	
	@JsonProperty(value="msg")
	public Msg Msg;
	
}
