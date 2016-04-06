package com.miicaa.detail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.miicaa.common.base.Utils;
import com.miicaa.common.base.Utils.CallBackListener;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.business.matter.MatterInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.org.ArrangementDetailInfo;
import com.miicaa.utils.AllUtils;

public class OldMatterTop implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8488562715505456726L;
	String TAG = "OldMatterTop";
	
	public ArrangementDetailInfo mDetailCell;
	public MatterInfo mInfo;
	Context context;
	String id;
	GetMatterInfoListener listener;
	ArrayList<AccessoryInfo> pngInfos = new ArrayList<AccessoryInfo>();
    ArrayList<AccessoryInfo> fileInfos = new ArrayList<AccessoryInfo>();
	public OldMatterTop(Context context,String id){
		this.context = context;
		mDetailCell = new ArrangementDetailInfo(context, id);
		mInfo = new MatterInfo();
		this.id = id;
	}
	
	 @SuppressWarnings("serial")
	public void requestMatter(String type) {
//	        loading("");//加载loading动画
	        String url = "/home/phone/thing/approveread";//审批
	        if ("1".equals(type)) {
	            url = "/home/phone/thing/arrangeread";//任务
	        }else{
	        	requestAppPerson();
	        }
	        new RequestAdpater() {
	            @Override
	            public void onReponse(ResponseData data) {//接收数据
	                if (data.getResultState() == ResponseData.ResultState.eSuccess) {//成功响应
	                	
	                    convertToMatter(data.getJsonObject());
	                } else {
//	                    Toast.makeText(getContext(), data.getResultState().name(), Toast.LENGTH_SHORT).show();
//	                    Toast.makeText(getContext(),data.getMsg(), Toast.LENGTH_SHORT).show();
//	                    intent.setComponent(new ComponentName("com.miicaa.home", "com.miicaa.home.ui.login.HomeLoginActivity_"));
//	                    intent.setAction(Intent.ACTION_VIEW);
	                	listener.onResponseFailed(data.getMsg());
	                }
	            }

	            @Override
	            public void onProgress(ProgressMessage msg) {
	            }
	        }.setUrl(url)
	                .addParam("dataId", id)
	                .notifyRequest();
	    }
	 
	 void requestAppPerson(){
	    	Utils.approvalUseData(context, this.id, new CallBackListener() {
				
				@Override
				public void callbackNull() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void callBackJson(JSONArray jsonArray) {
					// TODO Auto-generated method stub
					setTodoPerson(jsonArray);
				}
				
				@Override
				public void callBack(ResponseData data) {
					// TODO Auto-generated method stub
					
				}
			});
	    }
	 
	 protected void setTodoPerson(JSONArray jsonArray) {
		listener.setTodoPerson(jsonArray);
		
	}

	private void convertToMatter(JSONObject jRow) {
	        if (jRow == null) {
	            return;
	        }
//	        Log.d(TAG, "convertToMatter jRow:"+jRow);
	        MatterInfo info = new MatterInfo();
	        ArrayList<String> labels = new ArrayList<String>();
	        info.setIsFixedProcess(!jRow.isNull("process"));
	        if(!jRow.isNull("autoFinish")){
	        	info.setAutoFinish(jRow.optString("autoFinish"));
	        }else{
	        	info.setAutoFinish("");
	        }
	        if(!jRow.isNull("workReportId")){
	        	info.setReportId(jRow.optString("workReportId"));
	        }
	        if (!jRow.isNull("id")) {
	            info.setId(jRow.optString("id"));
	        }
	        if(!jRow.isNull("operateGroup")){
	        	info.setOperateGroup(jRow.optString("operateGroup"));
	        }
	        if (!jRow.isNull("title")) {
	            info.setTitle(jRow.optString("title"));
	        }
	        if (!jRow.isNull("content")) {
	            info.setContent(jRow.optString("content"));
	        }
	        if (!jRow.isNull("arrangeType")) {
	            info.setArrangeType(jRow.optString("arrangeType"));
	        }
	        if (!jRow.isNull("peopleNum")) {
	            info.setPeopleNum(jRow.optLong("peopleNum"));
	        }
	        if (!jRow.isNull("startTime")) {
	            info.setStartTime(new Date(jRow.optLong("startTime")));//开始时间
	        }
	        if (!jRow.isNull("endTime")) {
	            info.setEndTime(new Date(jRow.optLong("endTime")));//结束时间
	        }
	        if(!jRow.isNull("flowRecordNum")){
	        	info.setFlowRecordNum(jRow.optLong("flowRecordNum"));
	        }
	        if(!jRow.isNull("progressNum")){
	        	info.setProgressNum(jRow.optLong("progressNum"));
	        }
	        if(!jRow.isNull("activityNum")){
	        	info.setActivityNum(jRow.optLong("activityNum"));
	        }
	        if(!jRow.isNull("discussionNum")){
	        	info.setDiscussionNum(jRow.optLong("discussionNum"));
	        }
	        if (!jRow.isNull("planTime")) {
	            info.setPlanTime(jRow.optLong("planTime"));//结束时间
	        }
	        if(!jRow.isNull("planTimeEnd")){
	        	info.setPlanTimeEnd(jRow.optLong("planTimeEnd"));
	        }
	        if (!jRow.isNull("secrecy")) {
	            info.setSecrecy(jRow.optString("secrecy"));
	        }
//	        Log.d(TAG, " info.setSecrecy:"+jRow.optString("secrecy"));
	        if (!jRow.isNull("creatorCode")) {
	            info.setCreatorCode(jRow.optString("creatorCode"));
	        }
	        if (!jRow.isNull("creatorName")) {
	            info.setCreatorName(jRow.optString("creatorName"));
	        }
	        if (!jRow.isNull("dataVersion")) {
	            info.setDataVersion(jRow.optString("dataVersion"));
	        }
	        if (!jRow.isNull("createTime")) {
	            info.setCreateTime(new Date(jRow.optLong("createTime")));//创建时间
	        }
	        if (!jRow.isNull("updateTime")) {
	            info.setLastUpdateTime(new Date(jRow.optLong("updateTime")));//更新时间
	        }
	        if (!jRow.isNull("dataType")) {
	            info.setDataType(jRow.optString("dataType"));
	        }

	        if (!jRow.isNull("srcName")) {
	            info.setSrcName(jRow.optString("srcName"));
	        }
	        
	        if(!jRow.isNull("todoId")){
	        	info.setTodoId(jRow.optString("todoId"));
	        }
	        /*
	         * 判断是否是办理人
	         */
	        if(!jRow.isNull("todoUsers")&&jRow.optJSONArray("todoUsers").length()>0){
	        	 for (int i = 0; i < jRow.optJSONArray("todoUsers").length(); i++){
		                JSONObject js = jRow.optJSONArray("todoUsers").optJSONObject(i);
		                if(AccountInfo.instance().getLastUserInfo().getCode().equals(js.optString("userCode"))){
		                	info.IsTodoUser(true);
		                	break;
		                }
		            }
		            
	        }
	        if (!jRow.isNull("clientName")) {
	            info.setClientName(jRow.optString("clientName"));
	        }
	        if(!jRow.isNull("srcCode")){
	        	info.setSrcCode(jRow.optString("srcCode"));
	        }
	        if (!jRow.isNull("status")) {
	            info.setStatus(jRow.optString("status"));
	        }
	        if (!jRow.isNull("approveType")) {
	            info.setApproveTypeName(jRow.optString("approveType"));
	        }
	        if (!jRow.isNull("lastApproveStatus")) {
	            info.setLastApproveStatus(jRow.optString("lastApproveStatus"));
	        }
	        if (!jRow.isNull("orgcode")) {
	            info.setOrgcode(jRow.optString("orgcode"));
	        }
	        if (!jRow.isNull("repeatId")) {
	            info.setRepeatId(jRow.optString("repeatId"));
	        }
	        if (!jRow.isNull("repeatStr")) {
	            info.setRepeatStr(jRow.optString("repeatStr"));
	        }
	        if (!jRow.isNull("todoStatusStr")) {
	            info.setTodoStatusStr(jRow.optString("todoStatusStr"));
	        }
	        if(!jRow.isNull("todoStatus")){
	            info.setTodoStatus(jRow.optString("todoStatus"));
	        }
	        if (!jRow.isNull("labels")){
	            for (int i = 0; i < jRow.optJSONArray("labels").length(); i++){
	                JSONObject js = jRow.optJSONArray("labels").optJSONObject(i);
	                labels.add(js.optString("label"));
	            }
	            info.setLabels(labels);
	        }
	        if (!jRow.isNull("remindTime")){
	            info.setRemindTime(new Date(jRow.optLong("remindTime")));
	        }
	        
	        
	        if (!jRow.isNull("approveTypeName")) {
	            info.setApproveTypeName(jRow.optString("approveTypeName"));
	        }
	        
	       
	        if (!jRow.isNull("userCode")) {
	            info.setUserCode(jRow.optString("userCode"));
	        }
	        //
	        if(!jRow.isNull("reportTitle")){
	        	
	            info.setReportTitle(jRow.optString("reportTitle"));
	        } 
	        
	        if(!jRow.isNull("hasObserved")){
	        	
	            info.setObserve(jRow.optString("hasObserved"));
	        }
	        if (!jRow.isNull("flowRecordNum")) {
	            info.setFlowRecordNum(jRow.optLong("flowRecordNum"));
	        }
	        if(!jRow.isNull("parentId")){
	        	info.setParentTask(true);
	        	JSONObject obj = jRow.optJSONObject("parentWork");
	        	Log.d(TAG, "parentWork:"+obj);
	        	if(!jRow.isNull("parentWork")){
	        		info.setParentTask(obj.toString());
	        	}else{
	        		info.setParentTask(false);
	        	}
	        }else{
	        	info.setParentTask(false);
	        }
	        info.setChildTaskCount(jRow.optInt("subtaskNum", 0));
	        
	        mInfo = info;
	        listener.getMatterInfo(info);
	        mDetailCell.setMatterData(info);
	        requestDetaiAttechment(info.getId());//获取附件
	        if(!AllUtils.approvalType.equals(info.getDataType()))
	        requestWorkDetaiAttechment(info.getId());//获取附件
	    }
	 
	 @SuppressWarnings("serial")
	private void requestDetaiAttechment(String id) {
	        String url = "/home/phone/thing/getsingleattach";//附件的网络位置

	        new RequestAdpater() {
	            @Override
	            public void onReponse(ResponseData data) {

	                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
	                    getDetailAttechment(data.getJsonArray(),false);
	                } else {
	                	
	                }
//	                loaded();
	            }

	            @Override
	            public void onProgress(ProgressMessage msg) {
	            }
	        }.setUrl(url)
	                .addParam("dataId", id)
	                .notifyRequest();
	    }


	   
	    
	    @SuppressWarnings("serial")
		private void requestWorkDetaiAttechment(String id) {
		        String url = "/home/phone/thing/getworkattach";//附件的网络位置

		        new RequestAdpater() {
		            @Override
		            public void onReponse(ResponseData data) {

		                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
		                    getDetailAttechment(data.getJsonArray(),true);
		                } else {
//		                	Log.d(TAG, "requestWorkDetaiAttechment result message"
//		                			+data.getMsg()+"code:"+data.getCode()
//		                			+"json:"+data.getData());
		                }
//		                loaded();
		            }

		            @Override
		            public void onProgress(ProgressMessage msg) {
		            }
		        }.setUrl(url)
		                .addParam("dataId", id)
		                .notifyRequest();
		    }


		    private void getDetailAttechment(JSONArray jDataArray,boolean isWork) {//获取附件
		        if (jDataArray == null) {
		            return;
		        }
		        ArrayList<AccessoryInfo> attechs = new ArrayList<AccessoryInfo>();
		        
		        for (int i = 0; i < jDataArray.length(); i++) {
		            JSONObject jrow = jDataArray.optJSONObject(i);
		            Log.d(TAG, "getDetailAttechment json:"+jrow);
		            AccessoryInfo info = new AccessoryInfo();
		            info.setId(jrow.optString("id"));
		            info.setTitle(jrow.optString("title"));
		            info.setExt(jrow.optString("ext"));
		            info.setSize(jrow.optLong("size"));
		            info.setInfoId(jrow.optString("infoId"));
		            info.setFileId(jrow.optString("fileId"));
		            info.setUserCode(jrow.optString("userCode"));
		            info.setUserName(jrow.optString("userName"));
		            info.setUpLoadTime(jrow.optLong("uploadTime"));
		           if(isWork){
		            if ("png".equalsIgnoreCase(info.getExt()) ||
		                    "jpg".equalsIgnoreCase(info.getExt()) ||
		                    "gif".equalsIgnoreCase(info.getExt()) ||
		                    "bmp".equalsIgnoreCase(info.getExt()) ||
		                    "jpeg".equalsIgnoreCase(info.getExt())) {
		            	
		            	pngInfos.add(info);
		            	
		            } else {
		            	fileInfos.add(info);
		            }
		           }
		            attechs.add(info);
		        }
		        if(isWork){
		        listener.getAttachmentData(attechs);
		        }else{
		        mDetailCell.setAttachmentData(attechs);//加入附件
		        }
		    }
	    
//	    public Integer getAttcount(){
//	    	return 
//	    }
	    
	    public interface GetMatterInfoListener{
	    	void getMatterInfo(MatterInfo info);
	    	
	    	void onResponseFailed(String msg);

			void setTodoPerson(JSONArray jsonArray);

			void getAttachmentData(ArrayList<AccessoryInfo> attechs);
	    }
	    
	    public void setGetMatterInfoListener(GetMatterInfoListener listener){
	    	this.listener = listener;
	    }
	    
	    
	    public ArrayList<String> getPictureId(){
	    	ArrayList<String> ids = new ArrayList<String>();
	    	for(AccessoryInfo info : pngInfos){
	    		ids.add(info.getFileId());
	    	}
	    	return ids;
	    }
	    
	    
}
