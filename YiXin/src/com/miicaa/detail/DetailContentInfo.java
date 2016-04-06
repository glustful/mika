package com.miicaa.detail;

import java.io.Serializable;

import org.json.JSONObject;

public class DetailContentInfo implements Serializable{

	/**
	 * 传送详情页的数据
	 */
	
	private static final long serialVersionUID = 462059198682661150L;
	JSONObject discuss;
	public String usercode;
	public String username;
	public String content;
	public String time;
	public String from;
	public String contenthtml;
	public String id;
	
	public DetailContentInfo(JSONObject discuss){
		this.discuss = discuss;
	}
	
	public DetailContentInfo save(){
		content = discuss.isNull("content")?"":discuss.optString("content");
		contenthtml = discuss.isNull("contentHtml")?"":discuss.optString("contentHtml");
//		info.from
		this.time = discuss.isNull("addDateStr")?null:discuss.optString("addDateStr");
//		this.time = AllUtils.getnormalTime(time);
		usercode = discuss.isNull("userCode")?"":discuss.optString("userCode");
		username = discuss.isNull("userName")?"":discuss.optString("userName");
		
		id = discuss.isNull("id")?null:discuss.optString("id");
		return this;
	}

}
