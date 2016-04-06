package com.yxst.epic.yixin.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetOrgUserListRequest extends Request {

	@JsonProperty(value = "orgid")
	public String orgid;

}
