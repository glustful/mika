package com.yxst.epic.yixin.model;

import com.yxst.epic.yixin.data.dto.model.Member;

public class ContentQun extends Content {
	public String realContent;
	public Member member; 
	
	public ContentQun(String content) {
		super(content);
		
		this.realContent = getRealContent(content);
		
		String namesContent = getNamesContent(content);
		if (namesContent != null) {
			String[] names = namesContent.split("\\|");
			if (names != null && names.length == 3) {
				Member member = new Member();
				member.UserName = names[0];
				member.Name = names[1];
				member.NickName = names[2];
				member.DisplayName = names[2];
				this.member = member;
			}
		}
	}
	
	private String getRealContent(String rawContent) {
		try {
			if (rawContent.contains("&&")) {
				String realContent = rawContent.substring(rawContent.indexOf("&&") + 2);
				return realContent;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rawContent;
	}
	
	private String getNamesContent(String rawContent) {
		try {
			String namesContent = rawContent.substring(0, rawContent.indexOf("&&"));
			return namesContent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
