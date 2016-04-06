package com.miicaa.detail;

import java.io.Serializable;

import org.json.JSONObject;

public class ResourcesArticle implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = -5177538556199379058L;
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
		
		public ResourcesArticle(){
//			this.article = document;
//			save(document);
		}
	    void save(JSONObject article) {
		id = article.optString("id");
		title = article.optString("title");
		ext = article.optString("ext");
		size = article.optInt("size");
		infoid = article.optString("infoId");
		status = article.optInt("status");
		orgcode = article.optString("orgCode");
		appun = article.optString("appUn");
		usercode = article.optString("userCode");
		username = article.optString("userName");
		uploadtime = article.optLong("uploadTime");
		fileid = article.optString("fileId");
		canread = article.optBoolean("canRed");
		sizestr = article.optString("sizeStr");
		imgsurl = article.optString("imageSUrl");
		showtitle = article.optString("showTitle");
		fileurl = article.optString("fileUrl");
		iconclass = article.optString("iconClass");
	}
	
}