package com.miicaa.detail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.avos.avoscloud.LogUtil.log;



public class ProgressListInfo {
	
	private static String TAG = "ProgressListInfo";
	
	JSONObject progress;
	public boolean havemore;
	public String btnflag;
	public Long lastdate;
	public String usercode;
	public Integer star;
	public String username;
	public ArrayList<DetailList> detailist;
	
	
	public ProgressListInfo(JSONObject progress){
		this.progress = progress;
		try{
			save();
		}catch(JSONException e){
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	void save() throws JSONException{
		JSONArray list;
		havemore = progress.optBoolean("haveMore");
		btnflag = progress.optString("btnFlag");
		lastdate = progress.optLong("lastAddDate");
		usercode = progress.optString("userCode");
		star = progress.optInt("star");
		username = progress.optString("userName");
		list = progress.optJSONArray("progress");
		detailist = islistnull(list)?null:savedetailList(list);
		
	}
	ArrayList<DetailList> savedetailList(JSONArray list) throws JSONException{
		ArrayList<DetailList> detailLists = new ArrayList<DetailList>();
		
		for(int i = 0; i < list.length(); i++){
			DetailList dList = new DetailList();
			dList.save(list.optJSONObject(i));
			detailLists.add(dList);
		}
		return detailLists;
		
	}
	
	public static boolean islistnull(JSONArray list){
		return list == null || list.length()== 0 ? true:false;
	}
	
	
	

}
