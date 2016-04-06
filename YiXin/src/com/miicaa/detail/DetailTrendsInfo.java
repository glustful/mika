package com.miicaa.detail;

import org.json.JSONArray;
import org.json.JSONObject;

import com.miicaa.utils.AllUtils;

public class DetailTrendsInfo {
	JSONObject trend;
	public String entityOldValue;
	public String createTime;
	public String appId;
	public String operatorId;
	public String state;
	public String entityId;
	public String entityValueType;
	public String type;
	public String actType;
	public String id;
	public String createTimeUI;
	public String operation;
	public String outOperatorId;
	public String observerName;
	public String entityNewValue;
	public String operatorImg;
	public String observerId;
	public String outOperatorRole;
	public String outOperatorName;
	public String entityNewKey;
	public String entityOldKey;
	public String newKeyValMapList;
	public JSONArray detailList;
	public String clientInfo;
	public String companyName;
	/**
	 * 这个就是动态需要显示的内容
	 */
	public String allInfo;
	public String entityPropertyDeep;
	public String entityName;
	public String appName;
	public String entityDetail;
	public String operatorName;
	public String companyId;
	public String entityLink;
	public String observerType;
	public String oldKeyValMapList;
	
	public DetailTrendsInfo(JSONObject trend){
		this.trend = trend;
	}
	
	public DetailTrendsInfo save(){
		operatorName = trend.isNull("operatorName")?"":trend.optString("operatorName");
		allInfo = trend.isNull("allInfo")?"":trend.optString("allInfo");
		Long createTime = trend.isNull("createTime")?0:trend.optLong("createTime");
		this.createTime = AllUtils.getnormalTime(createTime);
		operatorId = trend.isNull("operatorId")?"":trend.optString("operatorId");
		clientInfo = trend.isNull("clientInfo")?"":trend.optString("clientInfo");
		return this;
	}
}
