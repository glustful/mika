package com.miicaa.detail;

import java.io.Serializable;

import org.json.JSONObject;

public class ResourcesImage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5308090830620051528L;
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
	String imgsurl;
	String showtitle;
	String fileurl;
	String iconclass;
	public ResourcesImage() {
//			save(document);
	}
	
	void save(JSONObject img){
		id = img.optString("id");
		title = img.optString("title");
		ext = img.optString("ext");
		size = img.optInt("size");
		infoid = img.optString("infoId");
		status = img.optInt("status");
		orgcode = img.optString("orgCode");
		appun = img.optString("appUn");
		usercode = img.optString("userCode");
		username = img.optString("userName");
		uploadtime = img.optLong("uploadTime");
		fileid = img.optString("fileId");
		canread = img.optBoolean("canRed");
		sizestr = img.optString("sizeStr");
		imgsurl = img.optString("imageSUrl");
		showtitle = img.optString("showTitle");
		fileurl = img.optString("fileUrl");
		iconclass = img.optString("iconClass");
	}
}
