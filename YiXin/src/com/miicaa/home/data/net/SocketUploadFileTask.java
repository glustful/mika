package com.miicaa.home.data.net;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.miicaa.base.request.UploadFileItem;
import com.miicaa.base.request.response.BaseResopnseData;
import com.miicaa.base.request.response.BaseResopnseData.OnFileResponseListener;
import com.miicaa.common.base.FormFile;

import android.os.AsyncTask;
import android.util.Log;

public  class  SocketUploadFileTask  extends  AsyncTask<Integer, Integer, ArrayList<String>>{

	private static String TAG = "SocketUploadFileTask";
	
	List<UploadFileItem> fileItemList;
	 String mUrl;
	 SocketHttpRequester socketHttpRequester;
	 OnFileResponseListener responseListener;
     public SocketUploadFileTask(List<UploadFileItem> fileItems,String url,final BaseResopnseData.OnFileResponseListener onResponseListener) {
    	 this.mUrl = url;
    	 this.fileItemList = fileItems;
    	 this.responseListener = onResponseListener;
    	 socketHttpRequester = new SocketHttpRequester();
	}
	@Override
	protected ArrayList<String> doInBackground(Integer... params) {
		final ArrayList<String> failedPath = new ArrayList<String>();
		for(int i = 0;  i < fileItemList.size(); i++){
			final int fileCount = i + 1;
			final UploadFileItem item = fileItemList.get(i);
//			Log.d(TAG, "doInBackground item path:"+item.path +"item params"+item.param+"mUrl :"+mUrl);
			final String path = item.path;
			File uploaFile = new File(path);
			socketHttpRequester.post(mUrl, item.param, new FormFile[]{new FormFile(uploaFile, "file", null)}, 
					new SocketHttpRequester.OnProgressListener() {
						
						@Override
						public void onProgress(int progress) {
							if(responseListener != null)
								responseListener.onProgress(progress,fileCount,item.path);
						}
					}, new SocketHttpRequester.OnCompleteListener() {
						
						@Override
						public void onSuccess(String json) {
							Log.d(TAG, "upload success data:"+json);
						}
						
						@Override
						public void onFailed() {
							failedPath.add(path);
						}
					});
		}
		return failedPath;
	}

	@Override
	protected void onPreExecute() {
		if(responseListener != null)
			responseListener.onPreExecute();
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		if(result.size() == 0){
			if(responseListener != null){
				responseListener.onReponseComplete(null);
			}else{
				responseListener.onFileReponseFile(result);
			}
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled(ArrayList<String> result) {
		super.onCancelled(result);
	}

	
}
