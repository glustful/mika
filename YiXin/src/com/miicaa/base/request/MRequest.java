package com.miicaa.base.request;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.renderscript.FieldPacker;
import android.util.Log;

import com.miicaa.base.request.response.BaseResopnseData;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.data.net.SocketUploadFileTask;

public class MRequest {
	
	private static String TAG = "MRequest";

	public String url = "";
	public HashMap<String, String> hashParam;
	public String uploadUrl = "";
	public List<String> mFilePathList;
	public ArrayList<UploadFileItem> uploadFileList;
	private BaseResopnseData mResponseData;
	
	public MRequest(String url){
		mFilePathList = new ArrayList<String>();
		uploadFileList = new ArrayList<UploadFileItem>();
		hashParam = new HashMap<String, String>();
		this.url = url;
//		uploadParam = new HashMap<String, String>();
		Log.d(TAG, "MRequest url:"+url);
	}
	
	public MRequest(String url, HashMap<String, String> param){
		mFilePathList = new ArrayList<String>();
		hashParam = new HashMap<String, String>();
//		uploadParam = new HashMap<String, String>();
		hashParam.putAll(param);
	}
	
	public MRequest(String url,String uploadUrl){
		mFilePathList = new ArrayList<String>();
		this.url = url;
		this.uploadUrl = uploadUrl;
	}
	
	public MRequest(){
		uploadFileList = new ArrayList<UploadFileItem>();
		mFilePathList = new ArrayList<String>();
		hashParam = new HashMap<String, String>();
	}
	
	public void addFilePath(String path){
		mFilePathList.add(path);
	}
	
	public void addFilePath(List<String> path){
		mFilePathList.addAll(path);
	}
	
	public void addParam(String key,String value){
		hashParam.put(key, value);
	}
	
	public void addParam(HashMap<String, String> param){
		hashParam.putAll(param);
	}
	
	public void addUrl(String url){
		this.url = url;
	}
	
	public void addUploadUrl(String url){
		this.uploadUrl = url;
	}
	
	public void executeRequest(final BaseResopnseData reponseData){
		
		mResponseData = reponseData;
		if(mResponseData.onResponseListener != null)
			mResponseData.onResponseListener.onPreExecute();
		Log.d(TAG, "executeRequest param"+hashParam+"...url:"+url);
		new RequestAdpater() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 5627132828806005325L;

			@Override
			public void onReponse(ResponseData data) {
				Log.d(TAG, "onReponse ResponseData:"+data.getData());
				Log.d(TAG, "onResponseData message:"+data.getMsg());
				if(data.getResultState().equals(ResultState.eSuccess)){
					reponseData.saveData(data.getData());
					reponseData.parseData();
					reponseData.onResponseListener.onReponseComplete();
				}else{
					if(reponseData.onResponseListener != null)
						reponseData.onResponseListener.onResponseError(data.getMsg(), String.valueOf(data.getCode()));
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				if(msg.getTotal() > 0 && msg.getTransSize() > 0 && reponseData.onResponseListener != null){
					reponseData.onResponseListener.onProgress(msg.getProgress());
				}
			}
		}.setUrl(url)
		.addParam(hashParam)
		.notifyRequest();
	}
	
	public void executeRequest(){
	    executeRequest(mResponseData);
	}
	
	@SuppressWarnings("unchecked")
	public void executeSocketFileRequest(final BaseResopnseData.OnFileResponseListener onFileResponseListener){
//		SocketUploadFileTask task = new SocketUploadFileTask(uploadFileList, uploadUrl, 
//				onFileResponseListener);
//		task.execute(1000);
		Log.d(TAG, "upload File list size:"+uploadFileList.size());
		new HttpFileUpload()
		.setParam(uploadFileList, uploadUrl, onFileResponseListener)
		.isContinuous(false)
		.execute();
	}
	
	
	public long getFilesLength(){
		long length = 0;
		for(String path : mFilePathList){
			File f = new File(path);
			if(f.exists())
				length += f.length();
		}
		return length;
		
	}
	
	
}
