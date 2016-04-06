package com.yxst.epic.yixin.data.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ObjectContentApp103 extends ObjectContent {

	private static final long serialVersionUID = 2773571983409745258L;

	public String appId;
	
	public String content;
}
