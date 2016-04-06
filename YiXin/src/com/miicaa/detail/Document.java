package com.miicaa.detail;

import java.io.Serializable;

import org.json.JSONObject;

public 	class Document implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8692503823377959727L;

	String id;
	String title;
	String ext;
	int size;
	String infoid;
	int status;
	String orgcode;
	String appun;
	String usercode;
	String username;
	Long uploadtime;
	String fileid;
	boolean canread;//
	String sizestr;
	public Document(){
//		this.document = document;
//			save( document);
	}
	
	void save(JSONObject document) {
		id = document.optString("id");
		title = document.optString("title");
		ext = document.optString("ext");
		size = document.optInt("size");
		infoid = document.optString("infoId");
		status = document.optInt("status");
		orgcode = document.optString("orgCode");
		appun = document.optString("appUn");
		usercode = document.optString("userCode");
		username = document.optString("userName");
		uploadtime = document.optLong("uploadTime");
		fileid = document.optString("fileId");
		canread = document.optBoolean("canRed");
		sizestr = document.optString("sizeStr");
	}
}