package com.miicaa.detail;

import org.json.JSONObject;

import android.util.Log;

public class DetailApprovalInfo {

	static String TAG = "DetailApprovalInfo";
	
	 public String group;
     public String username;
     public String usercode;
     public String content;
     public String createtime;
     public String endtime;
     public String id;
     public String status;
     public String flag;
     JSONObject approval;
     
     public DetailApprovalInfo(JSONObject approval,String group){
    	 this.approval = approval;
    	 this.group = group;
     }
     
     public DetailApprovalInfo save(){
    	 
    	 username = approval.isNull("userName")?null:approval.optString("userName");
    	 usercode = approval.isNull("userCode")?null:approval.optString("userCode");
    	 content = approval.isNull("content")?null:approval.optString("content");
    	 
    	 endtime = approval.isNull("endTimeStr")?null:approval.optString("endTimeStr");
    	 if(endtime == null){
    	 Log.d(TAG, "endtime:......"+"null");
    	 }
//    	 Long time = approval.optLong("createTime");
    	 createtime = approval.isNull("createTimeStr")?null:approval.optString("createTimeStr");
    	 id = approval.isNull("id")?null:approval.optString("id");
    	 status = approval.isNull("status")?null:approval.optString("status");
    	 flag = approval.isNull("flag")?null:approval.optString("flag");
    	 return this;
     }
}
