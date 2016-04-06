package com.miicaa.base.share.round;

public enum RoundKinds {
	PUBLIC("00","公开",true),
	PRIVATE("10","仅自己",true),
	UNIT("20","指定部门",false),
	GROUP("30","指定职位",false),
	PEOPLE("40","指定人员",false);
	RoundKinds(String code,String content,boolean onlyOne){
		this.code = code;
		this.content = content;
		this.onlyOne = onlyOne;
	}
	String code;
	String content;
	boolean onlyOne;
	
	public static RoundKinds getKind(String code){
		for(RoundKinds item :values()){
			if(item.code.equals(code))
				return item;
		}
		return null;
	}
}
