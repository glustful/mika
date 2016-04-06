package com.miicaa.base.request.response;

import java.util.List;


public abstract class BaseResopnseData {

	public abstract void saveData(Object data);
	public abstract void parseData();
	
	public interface OnResponseListener{
		void onResponseError(String errMsg,String errCode);
		void onNoneData();
		/*执行进度*/
		void onProgress(float count);
		/*请求完成*/
		void onReponseComplete();
		void onPreExecute();
	}
	
	public OnResponseListener onResponseListener;
	public void setOnResponseListener(OnResponseListener listener){
		this.onResponseListener = listener;
	}
	
	
	
	public OnFileResponseListener onFileResponseListener;
	public  interface OnFileResponseListener{
		void onResponseError(String errMsg,String errCode);
		void onNoneData();
		/*执行进度*/
		void onProgress(float progress,int count,String fileName);
		/*请求完成*/
		 <T>  void onReponseComplete (T result);
		void onPreExecute(); 
		void onFileReponseFile(List<String> filePaths);
	}
	
	public void setOnFileResponseListener(OnFileResponseListener onFileResponseListener){
		this.onFileResponseListener = onFileResponseListener;
	}
	
	
}
