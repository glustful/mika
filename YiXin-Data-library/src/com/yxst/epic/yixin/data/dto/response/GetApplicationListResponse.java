package com.yxst.epic.yixin.data.dto.response;

import java.util.List;

import com.yxst.epic.yixin.data.dto.model.Member;


//@JsonIgnoreProperties(ignoreUnknown=true)
public class GetApplicationListResponse extends Response {

	private static final long serialVersionUID = 5412666525061779465L;

	public int memberCount;
	
	public List<Member> memberList;
}
