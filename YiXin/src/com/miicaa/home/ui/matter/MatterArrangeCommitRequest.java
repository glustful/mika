package com.miicaa.home.ui.matter;

import java.util.HashMap;

public class MatterArrangeCommitRequest extends MatterCommitRequest{
	
	public String content;
	public String title;
	public String id;
	public String arrangeType;
	public String labelStr;
	public String clientName;
	public String startTime;
	public String endTime;
	public String autoFinish;
	public String creatorCode;
	public String creatorName;
	public String dataType;
	public String secrecy;
	public String copyUser;
	public String todoUser;
	public String reportId;
	public String reportListId;
	public String parentId;
	public String repeatFlag;
	
	
//	public MatterArrangeCommitRequest(String url) {
//		super(url);
//		hashParam.put("clientCode", 
//				AccountInfo.instance().getClientId());
//	}
	
	public MatterArrangeCommitRequest(){
		super();
		url = "";
	}
	
	@Override
	public void addParam(HashMap<String, String> param){
		hashParam.putAll(param);
	}
	
	public void changeTodoUserSet(String todoUser){
		hashParam.put("todoUsersSet", todoUser);
	}
	
	public void changeCopyUserSet(String copyUser){
		hashParam.put("copyUsersSet", copyUser);
	}
	
	public void saveOfficalParam(){
		hashParam.put("secrecy", secrecy);
		hashParam.put("todoUsersSet", todoUser);
		hashParam.put("copyUsersSet", copyUser);
		saveParam();
	}
	
	public void saveParentId(){
		hashParam.put("parentId", parentId);
	}
	
	public void saveReportParam(){
		hashParam.put("reportId", reportId);
		hashParam.put("reportListId", reportListId);
	}
	
	public void savePersonParam(){
		saveParam();
	}
	
	@Override
	public void saveParam(){
		hashParam.put("autoFinish", autoFinish);
		hashParam.put("clientName", clientName);
		hashParam.put("content", content);
		hashParam.put("creatorCode", creatorCode);
		hashParam.put("creatorName", creatorName);
		hashParam.put("dataType", dataType);
		hashParam.put("endTime", endTime);
		hashParam.put("id", id);
		hashParam.put("labelStr", labelStr);
		hashParam.put("startTime", startTime);
		hashParam.put("title", title);
		hashParam.put("arrangeType", arrangeType);
	}



}
