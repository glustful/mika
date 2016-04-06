package com.miicaa.home.ui.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import com.miicaa.home.BaseKeyVaule;

public class ScreenType implements Serializable{
	
	static String TAG = "ScreenType";
	
	private static ScreenType instance;
/**
	 * 
	 */
	private static final long serialVersionUID = -2118726463027617833L;
ArrayList<String> srclist = new ArrayList<String>();
ArrayList<String> arrangelist = new ArrayList<String>();
ArrayList<String> approvallist = new ArrayList<String>();
ArrayList<String> createUserList = new ArrayList<String>();
ArrayList<String> todoUserList = new ArrayList<String>();
ArrayList<String> tmpSrcList = new ArrayList<String>();
ArrayList<String> tmpArrangeList = new ArrayList<String>();
ArrayList<String> tmpApprovalList = new ArrayList<String>();
ArrayList<String> tmpCreateUserList = new ArrayList<String>();
ArrayList<String> tmpTodoUserList = new ArrayList<String>();
ArrayList<BaseKeyVaule> tmpReportList = new ArrayList<BaseKeyVaule>();
ArrayList<BaseKeyVaule> reportList = new ArrayList<BaseKeyVaule>();
String tmpTodoUserNames;
String todoUserNames;
String createUserNames;
String tmpCreateUserNames;
String tmpBeginTime;
String tmpEndTime;
String tmpReport;
String report;
public String srcCodes;
public String arrangeCodes;
public String approvalCodes;
public String begintime;
public String endtime;
public String createuser;
public String todouser;


public static  ScreenType getInstance(){
	if(instance == null){
		instance = new ScreenType();
	}
	return instance;
}


//public void addTodoUser(String user){
//	todoUserlist.add(user);
//}
public void addSrcCode(String srcCode){
	tmpSrcList.add(srcCode);
	
}
public void cancleSrcCode(String srcCode){
	tmpSrcList.remove(srcCode);
}
public void addArrangeCode(String arrangeCode){
	tmpArrangeList.add(arrangeCode);
}
public void cancleArrangeCode(String arrangeCode){
	tmpArrangeList.remove(arrangeCode);
}
public void addApprovalCode(String approvalCode){
	tmpApprovalList.add(approvalCode);
}
public void cancleApprovalCode(String approvalCode){
	tmpApprovalList.remove(approvalCode);
}
public void addReportType(BaseKeyVaule reportType){
	tmpReportList.add(reportType);
}
public void removeReportType(BaseKeyVaule reportType){
	tmpReportList.remove(reportType);
}
public void setbeginTime(String beginTime){
	tmpBeginTime = beginTime;
}
public void setendTime(String endTime){
	tmpEndTime = endTime;
}
public void setcreateUser(ArrayList<String> createUsers,String createUserNames){
	tmpCreateUserList.clear();
	tmpCreateUserList.addAll(createUsers);
	tmpCreateUserNames = createUserNames;
}
public void settodoUser(ArrayList<String> todoUsers,String todoUserNames){
	tmpTodoUserList.clear();
	tmpTodoUserList.addAll(todoUsers);
	tmpTodoUserNames = todoUserNames;
}
public void setSrcList(ArrayList<String> srcList){
	this.srclist.clear();
	this.srclist.addAll(srcList);
}

public void setArrangeList(ArrayList<String> arrangeList){
	this.arrangelist.clear();
	this.arrangelist.addAll(arrangeList);
}
public void setApprovalList(ArrayList<String> approveList){
	this.approvallist.clear();
	this.approvallist.addAll(approveList);
}

public void setReportList(ArrayList<BaseKeyVaule> reportList){
	this.reportList.clear();
	this.reportList.addAll(reportList);
}


public void saveScreenCondition(){
	createuser = listToString(tmpCreateUserList);
	todouser = listToString(tmpTodoUserList);
	begintime = tmpBeginTime;
	endtime = tmpEndTime;
	srcCodes = listToString(tmpSrcList);
	arrangeCodes = listToString(tmpArrangeList);
	approvalCodes = listToString(tmpApprovalList);
	report = getBsKvStr(false, tmpReportList);
	saveToNew(arrangelist, tmpArrangeList);
	saveToNew(approvallist,tmpApprovalList);
	saveToNew(srclist,tmpSrcList);
	saveToNew(createUserList,tmpCreateUserList);
	saveToNew(todoUserList,tmpTodoUserList);
	saveToNew(reportList,tmpReportList);
	createUserNames = tmpCreateUserNames;
	todoUserNames = tmpTodoUserNames;
}

public void convertTmpCondition(){
    tmpBeginTime = begintime;
    tmpEndTime = endtime;
	saveToNew(tmpArrangeList, arrangelist );
	saveToNew(tmpApprovalList,approvallist);
	saveToNew(tmpSrcList,srclist);
	saveToNew(tmpCreateUserList,createUserList);
	saveToNew(tmpTodoUserList,todoUserList);
	saveToNew(tmpReportList,reportList);
	tmpCreateUserNames = createUserNames  ;
	tmpTodoUserNames = todoUserNames  ;
}

private <T> void saveToNew(ArrayList<T> nowList,ArrayList<T> tmpList){
	assert nowList != null;
	nowList.clear();
	nowList.addAll(tmpList);
}

public void removeCreatorUsers(){
	createUserList.clear();
	tmpCreateUserList.clear();
	tmpCreateUserNames = "";
	createUserNames = "";
	createuser = listToString(createUserList);
}

public void removeTodoUsers(){
	todoUserList.clear();
	tmpTodoUserList.clear();
	tmpTodoUserNames = "";
	todoUserNames = "";
	todouser = listToString(todoUserList);
}

public void removeSrcCodes(){
	srclist.clear();
	tmpSrcList.clear();
	srcCodes = listToString(srclist);
}

public void removeReports(){
	reportList.clear();
	tmpReportList.clear();
	report = getBsKvStr(false, reportList);
}

public void removeArrangeCodes(){
	arrangelist.clear();
	tmpArrangeList.clear();
	arrangeCodes = listToString(arrangelist);
}

public void removeApprovalCodes(){
	approvallist.clear();
	tmpApprovalList.clear();
	approvalCodes = listToString(approvallist);
}

public void removeBeginTime(){
	begintime = "";
	tmpBeginTime = "";
}

public void removeEndTime(){
	endtime = "";
	tmpEndTime = "";
}

public void removeAllTypes(){
	removeApprovalCodes();
	removeArrangeCodes();
	removeBeginTime();
	removeCreatorUsers();
	removeEndTime();
	removeSrcCodes();
	removeTodoUsers();
	removeReports();
}


public void removeAllTmpTypes(){
	removeTmpApprovalCodes();
	removeTmpArrangeCodes();
	removeTmpBeginTime();
	removeTmpCreatorUsers();
	removeTmpEndTime();
	removeTmpSrcCodes();
	removeTmpTodoUsers();
	removeTmpReports();
}

public void removeTmpCreatorUsers(){
	tmpCreateUserList.clear();
	tmpCreateUserNames = "";
}

public void removeTmpTodoUsers(){
	tmpTodoUserList.clear();
	tmpTodoUserNames = "";
}

public void removeTmpSrcCodes(){
	tmpSrcList.clear();
}

public void removeTmpArrangeCodes(){
	tmpArrangeList.clear();
}

public void removeTmpApprovalCodes(){
	tmpApprovalList.clear();
}

public void removeTmpReports(){
	tmpReportList.clear();
}

public void removeTmpBeginTime(){
	tmpBeginTime = "";
}

public void removeTmpEndTime(){
	tmpEndTime = "";
}


String listToString(ArrayList<String> strlist){
	
	if(strlist != null && strlist.size() > 0){
		String str = "";
		for (String s : strlist){
			str += s+",";
		}
		return str.substring(0, str.length()-1);
	}
	return null;
	
}

public ArrayList<String> getSrcList(){
	return srclist;
	
}

public ArrayList<String> getArrangeList(){
	return arrangelist;
	
}
public ArrayList<String> getApprovalList(){
	return approvallist;
}
public String getBeginTime(){
	return begintime;
}
public String getEndTime(){
	return endtime;
}

public ArrayList<String> getTodoUser(){
	return todoUserList;
}

public ArrayList<String> getTmpTodoUser(){
	return tmpTodoUserList;
}

public ArrayList<String> getCreateUser(){
	return createUserList;
}

public ArrayList<BaseKeyVaule> getTmpReportList(){
	return tmpReportList;
}

public ArrayList<String> getTmpCreateUser(){
	return tmpCreateUserList;
}

public String getTodoUserStr(){
	Log.d(TAG,"getTodoUserStr :"+todoUserNames);
	return todoUserNames;
}
public String getCreateUserStr(){
	Log.d(TAG, "getCreateUserStr :"+createUserNames);
	return createUserNames;
}
public String getsrcCodesStr(){
	String str = "";
	for(String type : srclist){
		if("arrange".equals(type))
			str += "任务,";
		else if("approval".equals(type))
			str += "审批,";
		else if("report".equals(type))
			str += "工作报告,";
			
	}
	if(str.length() > 0)
	str = str.substring(0, str.length()-1);
	return str;
}
public String getArrangeCodesStr(){
	String str = "";
	for(String type : arrangelist){
		if("1".equals(type))
			str += "公事,";
		else if("0".equals(type))
			str += "私事,";
	}
	if(str.length() > 0)
	str = str.substring(0, str.length()-1);
	return str;
}
public String getApprovalCodesStr(){
	return approvalCodes;
}

public String getReportNamesStr(){
	return getBsKvStr(true, reportList);
}

public HashMap<String,String> getScreenCondition(){
	HashMap<String, String> condition = new HashMap<String, String>();
	condition.put("srcCodes", srcCodes!=null?srcCodes:"");
	condition.put("arrangeTypes", arrangeCodes!=null?arrangeCodes:"");
	condition.put("approveTypes", approvalCodes!=null?approvalCodes:"");
	condition.put("createStartTime", begintime!=null?begintime:"");
	condition.put("createEndTime", endtime!=null?endtime:"");
	condition.put("originators", createuser!=null?createuser:"");
	condition.put("transactor", todouser!=null?todouser:"");
	condition.put("reportTypes", report != null ? report : "");
	return condition;
}

public int getTypeCount(){
	int count = 0;
	count += addType(srcCodes);
	count += addType(approvalCodes);
	count += addType(arrangeCodes);
	count += addType(begintime);
	count += addType(endtime);
	count += addType(createuser);
	count += addType(todouser);
	count += addType(report);
	return count;
}

private int addType(String str){
	return isCunzai(str) ? 1 :0;
}

private Boolean isCunzai(String str){
	return (str == null || "".equals(str.trim())) ? false : true;
}


private String getBsKvStr(Boolean isName,ArrayList<BaseKeyVaule> bsKV){
	String s = "";
	for( BaseKeyVaule keyVaule : bsKV){
		if(isName)
			s += keyVaule.mName + ",";
		else
			s += keyVaule.mCode + ",";
	} 
	if(s.length() > 0)
		s = s.substring(0,s.length() -1);
	
	return s;
}

}
