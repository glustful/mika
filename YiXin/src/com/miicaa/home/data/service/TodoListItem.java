package com.miicaa.home.data.service;

import java.util.Date;

import com.miicaa.common.base.Debugger;
import com.miicaa.common.base.JSONValue;

/**
 * Created by Administrator on 13-12-16.
 */
public class TodoListItem {
	private static final String TAG = "TodoListItem";
	public static final String DATA_TYPE_OUG = "oug";
	public static final String OPER_TYPE_ADD = "add";
	public static final String OPER_TYPE_DELETE = "delete";
	/*
	{"succeed":true,"code":-1,"data":[
		{"id":"12",
		"userCode":"U=f0136f1e",
		"clientCode":"11111111111",
		"operType":"add",
		"dataType":"oug",
		"param":"{\"dataType\":\"user\",\"ids\":[\"23242574420586128\",\"23184890828691701\"]}",
		"createTime":1387973748000,
		"dataId":null},
		{"id":"13","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"delete","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23184890828691701\"]}","createTime":1387973807000,"dataId":null},{"id":"14","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23184890828691701\"]}","createTime":1387973833000,"dataId":null},{"id":"15","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"delete","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23242574420582031\",\"23242574420586128\",\"23184890828691701\"]}","createTime":1387973849000,"dataId":null},{"id":"16","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23184890828691701\"]}","createTime":1387973880000,"dataId":null},{"id":"17","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23242574420586128\"]}","createTime":1387973883000,"dataId":null},{"id":"18","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23242574420582031\"]}","createTime":1387973887000,"dataId":null},{"id":"19","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"codes\":[\"U=92277981011369\"]}","createTime":1387974126000,"dataId":null},{"id":"20","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"delete","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23284795341541297\",\"23284795341541306\",\"23284795341541315\",\"23284795341541324\",\"23284795341541332\"]}","createTime":1387974235000,"dataId":null},{"id":"21","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"codes\":[\"U=92486609335754\"]}","createTime":1387974335000,"dataId":null},{"id":"22","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"delete","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23284795341541380\",\"23284795341541389\",\"23284795341541398\",\"23284795341541406\",\"23284795341541416\"]}","createTime":1387974375000,"dataId":null},{"id":"23","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"codes\":[\"U=92566597108847\"]}","createTime":1387974488000,"dataId":null},{"id":"24","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"delete","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23284795341541465\",\"23284795341541476\"]}","createTime":1387974496000,"dataId":null},{"id":"25","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"codes\":[\"U=92686412071538\"]}","createTime":1387974542000,"dataId":null},{"id":"26","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"delete","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23284795341541511\",\"23284795341541517\"]}","createTime":1387974835000,"dataId":null},{"id":"27","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"codes\":[\"U=93137484821311\"]}","createTime":1387974985000,"dataId":null},{"id":"29","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"delete","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23284795341541575\",\"23284795341541582\",\"23284795341541589\"]}","createTime":1387975431000,"dataId":null},{"id":"31","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"delete","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23284795341541664\",\"23284795341541669\",\"23284795341541679\"]}","createTime":1387975561000,"dataId":null},{"id":"33","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"delete","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23284795341541705\",\"23284795341541711\"]}","createTime":1387976028000,"dataId":null},{"id":"35","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"codes\":[\"U=94215070677975\",\"U=94215124105629\"]}","createTime":1387976063000,"dataId":null},{"id":"37","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"delete","dataType":"oug","param":"{\"dataType\":\"user\",\"ids\":[\"23284795341541766\",\"23284795341541775\"]}","createTime":1387976097000,"dataId":null},{"id":"39","userCode":"U=f0136f1e","clientCode":"11111111111","operType":"add","dataType":"oug","param":"{\"dataType\":\"user\",\"codes\":[\"U=94267604627653\",\"U=94267653284488\"]}","createTime":1387976161000,"dataId":null}]}
	*/
	private String id;
	private String userCode;
	private String clientCode;
	private String operType;
	private String dataType;
	private String param;
	private Date createTime;
	private String dataId;

	public String getId() {
		return id;
	}

	public String getUserCode() {
		return userCode;
	}

	public String getClientCode() {
		return clientCode;
	}

	public String getOperType() {
		return operType;
	}

	public String getDataType() {
		return dataType;
	}

	public String getParam() {
		return param;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getDataId() {
		return dataId;
	}

	public TodoListItem(JSONValue item) {
		id = item.getString("id");
		userCode = item.getString("userCode");
		clientCode = item.getString("clientCode");
		operType = item.getString("operType");
		dataType = item.getString("dataType");
		param = item.getString("param");
		createTime = item.getDate("createTime");
		dataId = item.getString("dataId");
	}

	protected void exec() {
	}

	protected void log() {
		String msg = String.format("TodoListItem[id:%s, operType:%s dataType:%s] has finish", id, operType, dataType);
		Debugger.d(TAG, msg);
	}

	public void run() {
		String msg = String.format("TodoListItem[id:%s, operType:%s dataType:%s] is running", id, operType, dataType);
		Debugger.d(TAG, msg);

		exec();
		log();
	}
}
