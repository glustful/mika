package com.yxst.epic.yixin.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchRequest extends Request {

	public static final String SEARCH_TYPE_APP = "app";
	public static final String SEARCH_TYPE_USER = "user";
	
	@JsonProperty(value="searchKey")
	public String SearchKey;
	
	@JsonProperty(value="searchType")
	public String SearchType;
	
	@JsonProperty(value="offset")
	public int Offset;
	
	@JsonProperty(value="limit")
	public int Limit;
	
}
