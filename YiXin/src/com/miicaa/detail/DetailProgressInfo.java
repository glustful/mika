package com.miicaa.detail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author LM
 * all progress information
 *
 */
public class DetailProgressInfo {
	JSONArray arraylist;
	JSONObject tongji;
	public ArrayList<ProgressListInfo> progressList;
	public ProgressTongjiInfo tongjiInfo;
	public DetailProgressInfo(JSONArray arrylist,JSONObject tongji){
		this.arraylist = arrylist;
		this.tongji = tongji;
	}
	
	DetailProgressInfo save(){
		progressList = ProgressListInfo.islistnull(arraylist)?null:setProgressList(arraylist);
		tongjiInfo = tongji != null ?new ProgressTongjiInfo(tongji):null;
		return this;
	}
	
	ArrayList<ProgressListInfo> setProgressList(JSONArray list){
		ArrayList<ProgressListInfo> listInfos = new ArrayList<ProgressListInfo>();
		for(int i = 0; i < list.length(); i++){
			JSONObject progress = list.optJSONObject(i);
			ProgressListInfo info = new ProgressListInfo(progress);
			listInfos.add(info);
		}
		return listInfos;
	}
	

}
