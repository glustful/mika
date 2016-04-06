package com.yxst.epic.yixin.data.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ObjectContentApp101 extends ObjectContent{

	private static final long serialVersionUID = 885201866274669264L;
	
	public String appId;
	public String sendAppId;
	public Data data;

	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Data {
		public String title;
		public String content;
	}
}
