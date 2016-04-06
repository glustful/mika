package com.miicaa.home.ui.menu;

import org.json.JSONObject;

public class ScreenApproveType {

	public String id;
	public String content;
	public String type;
	JSONObject approve;
	public ScreenApproveType(JSONObject approve){
		this.approve = approve;
	}
	
	public ScreenApproveType save(){
		id = approve.isNull("id")?null:approve.optString("id");
		content = approve.isNull("content")?null:approve.optString("content");
		type = approve.isNull("type")?null:approve.optString("type");
		return this;
	}
}
