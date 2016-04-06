package com.miicaa.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * 
 * @author LM
 * 下载头像到本地。
 */

public class DownUImageSer extends Service{
	
	HeadBinder binder = new HeadBinder();
	
	
	
	//递归请求，以防线程池溢出
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		return binder;
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	
	
	

}
