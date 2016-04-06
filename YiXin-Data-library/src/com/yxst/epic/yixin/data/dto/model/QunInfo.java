package com.yxst.epic.yixin.data.dto.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QunInfo implements Serializable {

	private static final long serialVersionUID = 2276196719712248214L;

	public String id;
	
	public String creatorId;
	
	public String creatorUserName;
	
	public String name;
	
	public String description;
	
	public int maxMember;
	
	public String avatar;
	
	public String created;
	
	public String updated;
	
	public String tenantId;
}
