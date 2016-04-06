package com.miicaa.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.miicaa.utils.NetUtils;
import com.miicaa.utils.OnRefreshCompleteListener;


public class InitNewDataSer extends Service{

	SharedPreferences cachep;
	final static String ver = "2.0.0";
	String nVer;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		cachep = this.getSharedPreferences("dataser", MODE_PRIVATE);
		nVer = cachep.getString("refresh", "1.0.0");
		if(!ver.equals(nVer)){
			beginToInit();
			SharedPreferences.Editor e = cachep.edit();
			e.putString("refresh", ver);
			e.commit();
		}else{
			
		}
		return null;
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
	
	void beginToInit(){
		NetUtils.refresContact(new OnRefreshCompleteListener() {
			
			@Override
			public void refreshComplete() {
				// TODO Auto-generated method stub
				Log.d("REFRESHIMG", "ISOK");
			}

			@Override
			public void refreshField(String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "错误:初始化数据库错误!", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
