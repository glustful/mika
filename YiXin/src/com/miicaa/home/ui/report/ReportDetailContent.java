package com.miicaa.home.ui.report;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.miicaa.common.base.Utils;
import com.miicaa.common.base.Utils.CallBackListener;
import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.pay.PayUtils;

public class ReportDetailContent {
	public ReportDetailContentView mView;
	//public MatterInfo mInfo;
	Context context;
	String id;
	GetMatterInfoListener listener;
	ArrayList<AccessoryInfo> pngInfos = new ArrayList<AccessoryInfo>();
    ArrayList<AccessoryInfo> fileInfos = new ArrayList<AccessoryInfo>();
	public ReportDetailContent(Context context,String id){
		this.context = context;
		mView = new ReportDetailContentView(context, id);
		//mInfo = new MatterInfo();
		this.id = id;
	}
	
	 @SuppressWarnings("serial")
	public void requestReport() {
	        
	        String url = context.getString(R.string.report_detail_url);
	       
	        new RequestAdpater() {
	            @Override
	            public void onReponse(ResponseData data) {//接收数据
	            	
	                if (data.getResultState() == ResponseData.ResultState.eSuccess) {//成功响应
	                    convertToReport(data.getJsonObject());
	                } else {

	                   PayUtils.showToast(context, "加载失败", 1000);
	                   ((Activity)context).finish();
	                }
	            }

	            @Override
	            public void onProgress(ProgressMessage msg) {
	            }
	        }.setUrl(url)
	                .addParam("workId", id)
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

	private void convertToReport(JSONObject jsonObject) {
	        if (jsonObject == null) {
	            return;
	        }
	     pngInfos.clear();
	     fileInfos.clear();
	        ReportDetailInfo info = new ReportDetailInfo();
	        ArrayList<String> labels = new ArrayList<String>();
	        ArrayList<String> labelsId = new ArrayList<String>();
	        JSONArray tmpArray = jsonObject.optJSONArray("reportInfo");
	        if(tmpArray == null || tmpArray.length() == 0)
	        	return ;
	        JSONObject jRow = tmpArray.optJSONObject(0).optJSONObject("workDTO");
	      
	        if(jRow == null)
	        	return;
	        if (!jRow.isNull("id")) {
	            info.setId(jRow.optString("id"));
	        }
	        if(!jRow.isNull("operateGroup")){
	        	info.setOperateGroup(jRow.optString("operateGroup"));
	        }
	        if (!jRow.isNull("title")) {
	            info.setTitle(jRow.optString("title"));
	        }
	        
	        
	        if (!jRow.isNull("planTime")) {
	            info.setPlanTime(jRow.optLong("planTime"));//结束时间
	        }
	       
	        if(!jRow.isNull("planTimeEnd")){
	        	info.setPlanTimeEnd(jRow.optLong("planTimeEnd"));
	        }
	        if (!jRow.isNull("arrangeType")) {
	            info.setArrangeType(jRow.optString("arrangeType"));
	        }
	        if (!jRow.isNull("secrecy")) {
	            info.setSecrecy(jRow.optString("secrecy"));
	        }
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
	        if(!jRow.isNull("autoFinish")){
	        	info.setAutoFinish(jRow.optString("autoFinish"));
	        }else{
	        	info.setAutoFinish("");
	        }
	        if(!jRow.isNull("todoId")){
	        	info.setTodoId(jRow.optString("todoId"));
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
	                labelsId.add(js.optString("id"));
	            }
	            info.setLabels(labels);
	            info.setLabelsId(labelsId);
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
		       
	        if (!jRow.isNull("startTime")) {
	            info.setStartTime(new Date(jRow.optLong("startTime")));//开始时间
	        }
	        if (!jRow.isNull("endTime")) {
	            info.setEndTime(new Date(jRow.optLong("endTime")));//结束时间
	        }
	        if(!jRow.isNull("hasObserved")){
	        	
	            info.setObserve(jRow.optString("hasObserved"));
	        }
	        if(!jRow.isNull("todoUsers")){
	        	info.setCommenter(jRow.optJSONArray("todoUsers").toString());
	        }
	        if(!jRow.isNull("reportType")){
	        	info.setReportType(jRow.optString("reportType"));
	        }
	        JSONObject reportDto = jsonObject.optJSONArray("reportInfo").optJSONObject(0).optJSONObject("reportDTO");
	        if(!reportDto.isNull("id")){
	        	info.setReportId(reportDto.optString("id"));
	        }
	        if(!reportDto.isNull("summarizeStartTime")){
	        	info.setSummriazeStartTime(reportDto.optLong("summarizeStartTime", 0));
	        }
	        if(!reportDto.isNull("planTimeEnd")){
	        	info.settomorrowEndTime(reportDto.optLong("planTimeEnd", 0));
	        }
	        if(!reportDto.isNull("summarizeEndTime")){
	        	info.setSummriazeEndTime(reportDto.optLong("summarizeEndTime", 0));
	        }
	        if(!reportDto.isNull("planStartTime")){
	        	info.settomorrowStartTime(reportDto.optLong("planStartTime", 0));
	        }
	        if (!reportDto.isNull("description")) {
	            info.setDesc(reportDto.optString("description"));
	        }
	        info.setActivityNum(reportDto.optLong("activityNum", 0));
	        info.setRange(reportDto.optString("checkTheRange"));
	        info.setCommentNum(reportDto.optLong("commentOnTheNumberOf", 0));
	        info.setDiscussionNum(reportDto.optLong("discussionNum", 0));
	        JSONArray arr = reportDto.optJSONArray("reportList");
	       ArrayList<String> today = new ArrayList<String>();
	       ArrayList<String> tomorrow = new ArrayList<String>();
	        for(int i=0;i<arr.length();i++){
	        	JSONObject obj = arr.optJSONObject(i);
	        	if(obj.optString("type").equals(WorkReportActivity.SUMMARIZE)){
	        		today.add(obj.toString());
	        	}else {
	        		tomorrow.add(obj.toString());
	        	}
	        }
	        info.setTodayList(today);
	        info.setTomorrowList(tomorrow);
	        listener.getMatterInfo(info);
	        mView.setMatterData(info);
	        requestDetaiAttechment(info.getId());//获取附件
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
		    	ArrayList<AccessoryInfo> attechs = new ArrayList<AccessoryInfo>();
		    	if (jDataArray == null) {
		        	if(isWork){
				        listener.getAttachmentData(attechs);
		        	}
		            return;
		        }
		        
		        
		        for (int i = 0; i < jDataArray.length(); i++) {
		            JSONObject jrow = jDataArray.optJSONObject(i);
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
		        	mView.setAttachmentData(attechs);//加入附件
		        }
		    }
	    

	    
	    public interface GetMatterInfoListener{
	    	void getMatterInfo(ReportDetailInfo info);

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
