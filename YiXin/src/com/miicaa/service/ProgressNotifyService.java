package com.miicaa.service;

import java.util.ArrayList;
import java.util.List;

import com.miicaa.base.request.HttpFileUpload;
import com.miicaa.base.request.UploadFileItem;
import com.miicaa.base.request.response.BaseResopnseData.OnFileResponseListener;
import com.miicaa.home.data.net.SocketUploadFileTask;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.utils.AllUtils;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ProgressNotifyService extends Service{


	
	public  static int progressNotifyId = 0x110;
	
	private static String TAG = "ProgressNotifyService";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "service is create!");
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if(intent == null)
			return;
		final ArrayList<UploadFileItem> uploadFileItemList = intent.getParcelableArrayListExtra("upload");
		final String dataTitle = intent.getStringExtra("title");
		final String dataType = intent.getStringExtra("dataType");
		String url = intent.getStringExtra("url");
		
		progressNotifyId ++;
		Log.d(TAG, TAG+ "onStart! notifyId:"+progressNotifyId);
		if(uploadFileItemList != null && url != null && dataTitle != null){
			final ProgressNotifyCation notifyCation = new ProgressNotifyCation(this);
//			String type = intent.getStringExtra("type");
//			if(type != null && "cancelAll".equals(type)){
//				notifyCation.cancelAllNotify();
//				return;
//			}
			
			notifyCation.setContent("miicaa");
			notifyCation.setIntentClass(FramMainActivity.class);
			notifyCation.setNoticationFlags(Notification.DEFAULT_ALL);
			new HttpFileUpload()
		.setParam(uploadFileItemList, url, new OnFileResponseListener() {
			
			@Override
			public void onResponseError(String errMsg, String errCode) {
				StringBuilder sb = new StringBuilder();
				if(AllUtils.arrangeType.equals(dataType))
					sb.append("任务:");
				else if(AllUtils.approvalType.equals(dataType))
					sb.append("审批:");
				else if(AllUtils.reporteType.equals(dataType))
					sb.append("工作报告:");
				
				sb.append(dataTitle+"附件上传失败！原因："+errMsg+errCode);
				notifyCation.setContent("");
				notifyCation.completeProgress(progressNotifyId,sb);
				stopSelf();
			}
			
			@Override
			public <T> void onReponseComplete(T result) {
				StringBuilder sb = new StringBuilder();
				if(AllUtils.arrangeType.equals(dataType))
					sb.append("任务:");
				else if(AllUtils.approvalType.equals(dataType))
					sb.append("审批:");
				else if(AllUtils.reporteType.equals(dataType))
					sb.append("工作报告:");
				
				sb.append(dataTitle+"附件上传完成！");
				notifyCation.completeProgress(progressNotifyId,sb);
				stopSelf();
			}
			
			@Override
			public void onProgress(final float progress,final int count,final String fileName) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						notifyCation.setContent(count + "/" + uploadFileItemList.size());
						notifyCation.setProgressContent("正在上传:"+fileName);
						int p  = (int)progress;
						notifyCation.setProgress(progressNotifyId,p);
					}
				}).start();
				
			}
			
			@Override
			public void onPreExecute() {
				notifyCation.setContent("等待上传...");
				notifyCation.notifyShow(progressNotifyId);
			}
			
			@Override
			public void onNoneData() {
				
			}
			
			@Override
			public void onFileReponseFile(List<String> filePaths) {
				StringBuilder sb = new StringBuilder();
				if(AllUtils.arrangeType.equals(dataType))
					sb.append("任务:");
				else if(AllUtils.approvalType.equals(dataType))
					sb.append("审批:");
				else if(AllUtils.reporteType.equals(dataType))
					sb.append("工作报告:");
				
				sb.append(dataTitle+filePaths.size()+"个附件上传失败！");
				notifyCation.completeProgress(progressNotifyId,sb);
				stopSelf();
			}
		}).isContinuous(false)
		.execute();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	
	
	
	
	public static void startService(Context context){
		Intent i = new Intent(context,ProgressNotifyService.class);
		context.startService(i);
	}



}
