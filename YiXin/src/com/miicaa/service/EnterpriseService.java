package com.miicaa.service;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.mapcore2d.en;
import com.avos.avoscloud.LogUtil.log;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.EntirpiseInfo;
import com.miicaa.home.provider.EnterpiceProvider;
import com.miicaa.home.ui.enterprise.EnterpriseLocation;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

public class EnterpriseService extends Service implements AMapLocationListener,Runnable{

//	public EnterpriseService() {
//		super("com.miicaa.service.EnterpriseService");
//	}

	private static String TAG = "EnterpriseService";
	
	private LocationManagerProxy mLocationManagerProxy;
    private AMapLocation aMapLocation;
    private Handler mHandler = new Handler();
    EnterpriseLocation enterpriseLocation;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		enterpriseLocation = EnterpriseLocation.getInstance();
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		startLocation(this);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	
	
	@SuppressWarnings("deprecation")
	protected void startLocation(Context context){
        mLocationManagerProxy.setGpsEnable(true);
        //网络GPs混合定位
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				mLocationManagerProxy.requestLocationUpdates(
						LocationProviderProxy.AMapNetwork, 2000, 10, EnterpriseService.this);
			}
		}).start();
        
        mHandler.postDelayed(this, 12000);
	}
	
	

	@Override
	public void onLocationChanged(Location location) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	@Override
	public void run() {
		if (aMapLocation == null) {
			enterpriseLocation.setLocationType(EnterpriseLocation.LocationFailed);
			enterpriseLocation.setIsYunnan(this,false);
			enterpriseLocation.setIsReaded(this, true);
			stopLocation();// 销毁掉定位
		}
		stopSelf();
		
	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		this.aMapLocation = arg0;
		String province = aMapLocation.getProvince();//省
		log.d(TAG, "省："+province);
		if("云南省".equals(province)){
			enterpriseLocation.setLocationType(EnterpriseLocation.YunnanNoReaded);
			enterpriseLocation.setIsYunnan(this,true);
			if(AccountInfo.instance().getLastUserInfo() != null){
				String eMail = AccountInfo.instance().getLastUserInfo().getEmail();
				String userCode = AccountInfo.instance().getLastUserInfo().getCode();
				EntirpiseInfo entirpiseInfo = EntirpiseInfo.findByCodeMail(this, userCode, eMail);
				if(entirpiseInfo != null && entirpiseInfo.locationType != EnterpriseLocation.YunanReadedSuccess
					&&entirpiseInfo.locationType != EnterpriseLocation.YunnanNoReaded
					&&entirpiseInfo.locationType != EnterpriseLocation.YunnanReadedCancel){
					entirpiseInfo.gonggaoCount = -1;
					entirpiseInfo.locationType = EnterpriseLocation.YunnanNoReaded;
					EntirpiseInfo.saveOrUpdateLcation(this, entirpiseInfo);
					getContentResolver().notifyChange(EnterpiceProvider.CONTENT_URI, null);
				}
			}
		}else{
			enterpriseLocation.setLocationType(EnterpriseLocation.NotYunnan);
			enterpriseLocation.setIsYunnan(this,false);
			enterpriseLocation.setIsReaded(this, true);
		}
		stopLocation();
	}
	
	
	@SuppressWarnings("deprecation")
	private void stopLocation(){
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.removeUpdates(this);
			mLocationManagerProxy.destory();
		}
		mLocationManagerProxy = null;
	}
	
	public static void start(Context context){
	   Intent i = new Intent(context, EnterpriseService.class);
	   context.startService(i);
	}

}
