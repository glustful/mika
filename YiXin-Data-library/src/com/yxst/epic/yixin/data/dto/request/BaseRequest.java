package com.yxst.epic.yixin.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseRequest {

	/*public String tenantId;*/
	
	/**
	 * 用户唯一ID
	 */
	@JsonProperty(value="uid")
	public String Uid;

	/**
	 * 设备唯一ID
	 */
	@JsonProperty(value="deviceID")
	public String DeviceID;

	@JsonProperty(value="deviceType")
	public String DeviceType = "android";
	
	/**
	 * 登录后的Token
	 */
	@JsonProperty(value="token")
	public String Token;

//	@JsonProperty(value="customer_id")
//	public String customer_id = "1";
//	public String customer_id = "2";
}
