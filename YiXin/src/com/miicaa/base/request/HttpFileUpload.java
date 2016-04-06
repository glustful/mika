package com.miicaa.base.request;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.RecoverySystem.ProgressListener;
import android.util.Log;

import com.aps.u;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.task.PriorityObjectBlockingQueue;
import com.miicaa.base.request.response.BaseResopnseData;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.utils.NetUtils;

public class HttpFileUpload {
	
	private static String TAG = "HttpFileUpload";
	
	String mUrl = UserAccount.mSeverHost;
	ArrayList<UploadFileItem> uploadFileItemList;
	BaseResopnseData.OnFileResponseListener mOnFileResponseListener;
	HttpUtils httpUtils;
	boolean isContinuous = false;
	MyUploadNsTask myUploadNsTask;
	Executor mThreadPoolExecutor;
	private static final int Default_Uploadpool = 3;
	private static final int MAXIMUM_POOL_SIZE = 256;
	private static final int KEEP_ALIVE = 1;
	
	 private static final ThreadFactory sThreadFactory = new ThreadFactory() {
	        private final AtomicInteger mCount = new AtomicInteger(1);

	        @Override
	        public Thread newThread(Runnable r) {
	            return new Thread(r, "PriorityExecutor #" + mCount.getAndIncrement());
	        }
	    };
	    
	    private final BlockingQueue<Runnable> mPoolWorkQueue = new PriorityObjectBlockingQueue<Runnable>();
	
	public HttpFileUpload(){
		
	}
	public HttpFileUpload(ArrayList<UploadFileItem> items,String url,BaseResopnseData.OnFileResponseListener onFileResponseListener){
		setParam(items, url, onFileResponseListener);
	}
	
	public HttpFileUpload setParam(ArrayList<UploadFileItem> items,String url,BaseResopnseData.OnFileResponseListener onFileResponseListener){
		mUrl += url;
		uploadFileItemList = items;
		mOnFileResponseListener = onFileResponseListener;
		httpUtils = new HttpUtils();
		httpUtils.configCookieStore(NetUtils.getCookie());
		Log.d(TAG, "cookie"+NetUtils.getCookie());
		httpUtils.configTimeout(50000);//秒的超时时间
		myUploadNsTask = new MyUploadNsTask();
		mThreadPoolExecutor = new ThreadPoolExecutor(
	                Default_Uploadpool,
	                MAXIMUM_POOL_SIZE,
	                KEEP_ALIVE,
	                TimeUnit.SECONDS,
	                mPoolWorkQueue,
	                sThreadFactory);
		return this;
	}
	
	public HttpFileUpload isContinuous(boolean continuous){
		this.isContinuous = continuous;
		return this;
	}
	
	
	public HttpFileUpload execute(){
		if(uploadFileItemList.size() == 0){
			mOnFileResponseListener.onReponseComplete(null);
			return this;
		}
		RequestParams requestParams = new RequestParams();
		for(Map.Entry<String, String> p:putParam.entrySet()){
			requestParams.addBodyParameter(p.getKey(), p.getValue());
		}
		if(isContinuous){
			for(Map.Entry<String, String> param:uploadFileItemList.get(0).param.entrySet()){
				requestParams.addBodyParameter(param.getKey(), param.getValue());
			}
			
			int ims = 0;
			for(UploadFileItem item: uploadFileItemList){
				File file = new File(item.path);
				if(file.exists()){
				Log.d(TAG, "this upload file is :"+file.getPath());
				requestParams.addBodyParameter("file"+ims,file);
				}
				ims++;
			}
			Log.d(TAG, "requestParam:");
			execute(requestParams);
		}else{
			//开辟线程池，最大256条线程池，多了就到任务队列里面等待
			myUploadNsTask.executeOnExecutor(mThreadPoolExecutor, 100);
		}
		return this;
	}
	
	private  void execute(RequestParams requestParams){
		Log.d(TAG, "mUrl is:"+mUrl);
		httpUtils.send(HttpMethod.POST, 
				mUrl, requestParams,new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						mOnFileResponseListener.onResponseError(arg0.getMessage(), arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Log.d(TAG, "upload ok!"+arg0.result);
						mOnFileResponseListener.onReponseComplete(arg0.result);
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if(total > 0){
						Double p = (double) (((double)current)/total);
						long progress = (long) (p * 100);
						Log.d(TAG, "onLoading current:"+current+"total:"+total
								+"progress:"+progress);
//						if(isUploading)
							mOnFileResponseListener.onProgress(progress, -1, null);
						}
						super.onLoading(total, current, isUploading);
					}

					@Override
					public void onStart() {
						mOnFileResponseListener.onPreExecute();
						super.onStart();
					}
					
					
				});
	}
	
	class  MyUploadNsTask  extends  AsyncTask<Integer, Long, HashMap<String,HttpResponse>>{

		private String thisFileName;
		private int thisFileNum;
		String responseMessage;
		int reponseCode;
		long progressTemp = 0;
		long timeTmp = 0;
		
		@Override
		protected HashMap<String,HttpResponse> doInBackground(Integer... params) {
			//httpClient使用的是和httUtils一个，上传已经默认使用multipart，要改代码的话请不要重复添加
			DefaultHttpClient httpClient = (DefaultHttpClient) httpUtils.getHttpClient();
			httpClient.setCookieStore(NetUtils.getCookie());
			Log.d(TAG, "backound is running! params:"+params);
			try{
//				List<String> responseList = new ArrayList<String>();
				HashMap<String, HttpResponse> responseMap = new HashMap<String, HttpResponse>();
			for(int i = 0; i < uploadFileItemList.size(); i++){
				progressTemp = 0;
				final UploadFileItem item  = uploadFileItemList.get(i);
				HttpPost httpPost = new HttpPost(mUrl);
				File file = new File(item.path);
				thisFileName = item.path;
				thisFileNum = i+1;
				MultipartEntity multipartEntity = new 
						MyHttpUploadEntity(new ProgressOutStreamListener() {
					
							//上传进度
					@Override
					public void transfer(Double num) {
						File file = new File(item.path);
						if(file.exists()){
//							int progress = (int) (num/file.length()*100);
							long time = System.currentTimeMillis();
							long progress = (long) (num / file.length() *100);
							if((timeTmp == 0 ||timeTmp+600 <= time) && progress <= 100){
							Log.d(TAG, "Onprogress length:"+file.length()+"progressLength:"
									+num+"progress:"+progress);
							mOnFileResponseListener.onProgress(progress, thisFileNum, thisFileName);
							progressTemp = progress;
							timeTmp = time;
							}

						}
//						onProgressUpdate(num);
					}
				});
				for(Map.Entry<String, String> param:item.param.entrySet()){
					StringBody body = new StringBody(param.getValue());
					multipartEntity.addPart(param.getKey(), body);
				}
				if(file.exists()){
				FileBody fileBody = new FileBody(file);
				multipartEntity.addPart("file", fileBody);
				}
				 httpPost.setEntity(multipartEntity);
				 HttpResponse reponse = httpClient.execute(httpPost);
				 int reponseCode = reponse.getStatusLine().getStatusCode();
//				 if(reponseCode > 199 && reponseCode < 400){
					 this.reponseCode = reponseCode;
					 this.responseMessage = "ok";
					 responseMap.put(item.path, 
							 reponse);
//					 responseList.add(new String(EntityUtils.toByteArray(reponse.getEntity())));
//					 Log.d(TAG, "reponse ok！："+EntityUtils.toByteArray(reponse.getEntity()));
//					 return  EntityUtils.toByteArray(reponse.getEntity());
//				 }else{
					this.reponseCode = reponseCode;
//					Log.d(TAG,"请求错误!");
//					mOnFileResponseListener.onResponseError("附件："+thisFileName+"上传失败！", 
//							"请求错误!"+reponseCode);
//				 }
					
			}
			return responseMap;
		}catch(Exception e){
			reponseCode = -1;
			this.responseMessage = e.getMessage();
			Log.d(TAG, "exception:"+e.getMessage());
//			e.printStackTrace();
			return null;
//			mOnFileResponseListener.onResponseError("附件："+thisFileName+"上传失败！", 
//					e.getMessage()+reponseCode);
		}
		}

		@Override
		protected void onPreExecute() {
			mOnFileResponseListener.onPreExecute();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(HashMap<String, HttpResponse> result) {
			List<String> failedList = new ArrayList<String>();
			if(result == null){
				mOnFileResponseListener.onResponseError(responseMessage, String.valueOf(reponseCode));
			}else{
				try{
				for(Map.Entry<String, HttpResponse> res: result.entrySet()){
//					Log.d(TAG, "response result:"+EntityUtils.toString(res.getValue().getEntity()));
					HttpResponse response = res.getValue();
					int responseCode = response.getStatusLine().getStatusCode();
					if(responseCode >199 && responseCode < 400){
					JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
					if(!json.optBoolean("succeed",false)){
						failedList.add(res.getKey());
					}
					}else{
						failedList.add(res.getKey());
					}
				}
				if(failedList.size()>0){
					mOnFileResponseListener.onResponseError("附件上传失败！", 
							"请求错误!"+reponseCode);
					mOnFileResponseListener.onFileReponseFile(failedList);
				}else{
					mOnFileResponseListener.onReponseComplete(true);
				}
				}catch(Exception e){
					e.printStackTrace();
					Log.d(TAG, "to json Error!");
					mOnFileResponseListener.onResponseError("解析json错误！",e.getMessage());
				}
//				try {
//					JSONObject json = new JSONObject(result);
//					Log.d(TAG, "response json:"+json);
//					if(json.optBoolean("succed")){
//						mOnFileResponseListener.onReponseComplete(json);
//					}else{
//						mOnFileResponseListener.onResponseError(responseMessage, String.valueOf(reponseCode));
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
			}
			super.onPostExecute(result);
			
			
		}

		@Override
		protected void onProgressUpdate(Long... values) {
			File file = new File(thisFileName);
			if(file.exists()){
				int progress = (int) (values[0] / file.length());
				mOnFileResponseListener.onProgress(progress, thisFileNum, thisFileName);
			}
			
			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled(HashMap<String, HttpResponse> result) {
			super.onCancelled(result);
		}

		
	}
	
	
	

	class MyHttpUploadEntity extends MultipartEntity{

		ProgressOutStreamListener mOutOfStreamListener;
		
		public MyHttpUploadEntity(ProgressOutStreamListener listener) {
			mOutOfStreamListener = listener;
		}
		
		@Override
		public void writeTo(OutputStream outstream) throws IOException {
			super.writeTo(new MyProgressOutputStream(outstream, mOutOfStreamListener));
		}
//		
//		public MyHttpUploadEntity setOutStreamListener(ProgressOutStreamListener listener){
//			this.mOutOfStreamListener = listener;
//			return this;
//		}
		
	}
	
	HashMap<String, String> putParam = new HashMap<String, String>();
	public HttpFileUpload addParam(String key,String value){
		putParam.put(key, value);
		return this;
	}
	
	public static HttpFileUpload instance;
	public static HttpFileUpload getInstance(){
		if(instance == null)
			instance = new HttpFileUpload();
		
		return instance;
	}

}
