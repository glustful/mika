package com.yxst.epic.yixin.model;

import com.yxst.epic.yixin.data.dto.model.Member;


public abstract class Content {
	public String content;
	
	/*public*/ Content(String content) {
		this.content = content;
	}
	
	public static Content createContent(String remoteUserName, String content) {
		if (Member.isTypeQun(remoteUserName)) {
			return new ContentQun(content);
		} else {
			return new ContentNormal(content);
		}
//		return null;
	}
}

