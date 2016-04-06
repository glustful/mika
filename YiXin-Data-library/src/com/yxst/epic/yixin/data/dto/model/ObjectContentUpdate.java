package com.yxst.epic.yixin.data.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ObjectContentUpdate extends ObjectContent{

	private static final long serialVersionUID = 9005458797382013670L;
	
	public String fileName;
	public int versionCode;
	public String versionName;
	public String url;
}
