package com.miicaa.service;

import java.util.Timer;
import java.util.TimerTask;

import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.NetUtils;
import com.miicaa.utils.OnRefreshCompleteListener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ContactRefreshService extends Service{

	private static String TAG = "ContactRefreshService";
	
	private static int TIME_TASK = 5000*60;//五分钟
	private static int TIME_SU = 1000*60;//不要延时启动任务
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if(intent == null)
			return;
		if(intent.getBooleanExtra("refresh", false)){
			Log.d(TAG,"refresh concat is start!");
			timerToRefresh();
		}
	}

	@Override
	public void onDestroy() {
		if(timer != null){
			timer.cancel();
		}
		super.onDestroy();
	}
	
	Timer timer;
	TimerTask timerTask;
	private void timerToRefresh(){
		if(timerTask == null){
			timerTask = new TimerTask() {
				
				@Override
				public void run() {
					Log.d(TAG,"refresh concat is running!");
					NetUtils.refresContact(new OnRefreshCompleteListener() {
						
						@Override
						public void refreshField(String msg) {
							
						}
						
						@Override
						public void refreshComplete() {
							
						}
					});
				}
			};
		}
		if(timer == null){
			timer = new Timer();
			timer.schedule(timerTask, TIME_SU,TIME_TASK);
		}
	}
	
	public static void init(Context context){
		Intent intent = new Intent(context,ContactRefreshService.class);
		intent.putExtra("refresh", true);
		context.startService(intent);
	}
	
	public static void stop(Context context){
		Intent intent = new Intent(context, ContactRefreshService.class);
		context.stopService(intent);
	}

	
}
