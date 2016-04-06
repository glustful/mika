package com.miicaa.service;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.guidepage.GuidePageActivity;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.AllUtils.OnAllUtilsListener;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.Log;

public class VersionPanduanService extends Service{

	private static String TAG  = "VersionPanduanService";
    public static  VersionState VERSION_STATE = VersionState.netError;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, TAG+"onCreate!!!");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
	 if(intent == null)
		 return;
		
		boolean panduan = intent.getBooleanExtra("version", false);
		if(!panduan)
			return;
		Log.d(TAG, TAG+"onstart!!!");
	    paduanVersionCode();
		super.onStart(intent, startId);
	}
	
	
	  private void paduanVersionCode(){
	    	AllUtils.panduanVersionCode(this, new OnAllUtilsListener() {
	    		
	    		@Override
	    		public void onResponse(ResponseData data) {
	    			if(data.getResultState() == ResultState.eSuccess){
	    				JSONObject jsonObject = data.getMRootData();
	    				Log.d(TAG, "panduanVersionCode jsonObject:"+jsonObject);
	    				Boolean isData = jsonObject.optBoolean("data");
	    				if(isData){
	    				 VERSION_STATE = VersionState.enable;
	    				 stopSelf();
	    				}else{
	    					VERSION_STATE = VersionState.unenable;
	    					sendTimer();
	    				}
	    			}else{
	    				VERSION_STATE = VersionState.netError;
	    				stopSelf();
	    			}
	    		}

				@Override
				public void onFailed(String errCode, String errMsg) {
					stopSelf();
				}
	    	});
	    	}
	
	
	public static void startService(Context context){
		Intent in = new Intent(context,VersionPanduanService.class);
		Log.d(TAG, TAG+"startService!!");
		context.startService(in);
	}
	
	Timer timer;
	int timeCount = 0;
	 private void sendTimer(){
		 
	        TimerTask task = new TimerTask() {
	            @Override
	            public void run()
	            {
	            	Intent i = new Intent();
					i.setAction(AllUtils.version_reciver);
					sendBroadcast(i);
					timeCount++;
					Log.d(TAG, "sendTimer tuice:"+timeCount);
					if(timeCount == 4){
						timer.cancel();
						stopSelf();
					}
	            }
	        };
	        if (timer == null)
	        {
	        	 timer = new Timer();
	        	 timer.schedule(task, 200, 2000);
	        }
	        
	       
	        
	    }
	 
	 public enum VersionState{
		 netError,
		 enable,
		 unenable
	 }
	 
}
