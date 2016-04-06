package com.miicaa.detail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgressTongjiInfo {
	JSONObject statistical;
	public Integer notdonum;
	public Integer todonum;
	public Integer donenum;
	public ArrayList<DoStatus> notdos;
	public ArrayList<DoStatus> todos;
	public ArrayList<DoStatus> dones;
	
	public ProgressTongjiInfo(JSONObject statistical){
		this.statistical = statistical;
		save();
	}
	
	void save(){
		notdonum = statistical.optInt("notdonum");
		todonum = statistical.optInt("todonum");
		donenum = statistical.optInt("donenum");
		notdos = ProgressListInfo.islistnull(statistical.optJSONArray("notdo"))?null:
			setDoStatusList(statistical.optJSONArray("notdo"));
		todos = ProgressListInfo.islistnull(statistical.optJSONArray("todo"))?null:
			setDoStatusList(statistical.optJSONArray("todo"));
		dones = ProgressListInfo.islistnull(statistical.optJSONArray("done"))?null:
			setDoStatusList(statistical.optJSONArray("done"));
	}
	
	ArrayList<DoStatus> setDoStatusList(JSONArray list){
		ArrayList<DoStatus> statuss = new ArrayList<ProgressTongjiInfo.DoStatus>();
		for(int i = 0; i < list.length();i++){
			JSONObject dost = list.optJSONObject(i);
			DoStatus doStatus = new DoStatus(dost);
			statuss.add(doStatus);
		}
		return statuss;
	}
	
	class DoStatus{
		JSONObject dostatus;
		String id;
		String usercode;
		String username;
		Long createTime;
		Long endTime;
		String status;
		Long plantime;
		Integer star;
		String templateFlag;
		String topflag;
		String orgcode;
		String operateGroup;
		String typeflag;
		public DoStatus(JSONObject dostatus){
			this.dostatus = dostatus;
			try{
				save();
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		
		void save() throws JSONException{
			id = dostatus.optString("id");
			usercode = dostatus.optString("userCode");
			username = dostatus.optString("userName");
			createTime = dostatus.optLong("createTime");
			endTime = dostatus.optLong("endTime");
			status = dostatus.optString("status");
			plantime = dostatus.optLong("planTime");
			star = dostatus.optInt("star");
			templateFlag = dostatus.optString("templateFlag");
			topflag = dostatus.optString("orgcode");
			operateGroup = dostatus.optString("operateGroup");
			typeflag = dostatus.optString("typeFlag");
		}
		
	}

}
