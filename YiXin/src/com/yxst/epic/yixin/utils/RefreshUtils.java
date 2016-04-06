package com.yxst.epic.yixin.utils;

import com.yxst.epic.yixin.service.MiicaaPushMessageListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;


public class RefreshUtils {

	boolean hasMore = false;
	
	int DELAY_TIME = 2 * 1000;
	long mLastCallTime = 0l;
	
	Context mContext;
	Uri		mUri;
	
	@SuppressLint("HandlerLeak") 
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			mContext.getContentResolver().notifyChange(mUri, null);
			
			MiicaaPushMessageListener.notification(mContext);
			
			super.handleMessage(msg);
		}
		
	};
	
	final int MSG_REFRESH = 0;
	
	public void INeedNotify(Context aContext,Uri aUri){
		
		mUri = aUri;
		mContext = aContext;
		
		long currentTimeMillis = System.currentTimeMillis();
		if( currentTimeMillis - mLastCallTime < DELAY_TIME ){
			mHandler.removeMessages(MSG_REFRESH);
			mHandler.sendEmptyMessageDelayed(MSG_REFRESH, DELAY_TIME);
		}else{
			mLastCallTime = System.currentTimeMillis();
			mHandler.removeMessages(MSG_REFRESH);
			mHandler.sendEmptyMessage(MSG_REFRESH);
		}
		
	}
	
	
}
