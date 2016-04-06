package com.miicaa.home.ui.matter;

public class MatterApproveCommitRequest extends MatterCommitRequest{

	public String content;
	public String title;
	public String id;
	public String approveType;
	public String labelStr;
	public String clientName;
	public String creatorCode;
	public String creatorName;
	public String dataType;
	public String todoUserSet;
	public String copyUserSet;

	
	
	public MatterApproveCommitRequest(){
		super();
		url = "";
	}

	@Override
	public void saveParam() {
		hashParam.put("clientName", clientName);
		hashParam.put("content", content);
		hashParam.put("creatorCode", creatorCode);
		hashParam.put("creatorName", creatorName);
		hashParam.put("dataType", dataType);
		hashParam.put("id", id);
		hashParam.put("labelStr", labelStr);
		hashParam.put("title", title);
		hashParam.put("approveType", approveType);
		hashParam.put("approveTypeName", approveType);
		hashParam.put("todoUsersSet", todoUserSet);
		hashParam.put("copyUsersSet", copyUserSet);
		super.saveParam();
	}
	
	
}
