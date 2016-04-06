package com.miicaa.home.data.service;

import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.miicaa.common.base.Debugger;

public class CacheCtrlSrv extends Service {
	private static final String TAG = "Cache_Service";
	private static Application app = null;
	private NetCheckReceiver netCheckReceiver = null;
	public static Boolean isNetBreak = false;

	private class NetCheckReceiver extends BroadcastReceiver {
		// android 中网络变化时所发的Intent的名字
		private static final String netACTION = "android.net.conn.CONNECTIVITY_CHANGE";
		protected void registerAction() {
			IntentFilter filter = new IntentFilter();
			filter.addAction(netACTION);
			app.registerReceiver(this, filter);
		}

		protected void unRregisterAction() {
			app.unregisterReceiver(this);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if (netACTION.equals(intent.getAction())) {
				Debugger.d(TAG, netACTION);
				// Intent中ConnectivityManager.EXTRA_NO_CONNECTIVITY这个关键字表示着当前是否连接上了网络
				// true 代表网络断开   false 代表网络没有断开
				isNetBreak = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
				doConnChanged(isNetBreak);
			}
		}
	}

	public static void init(Application application) {
		app = application;
		Intent intent = new Intent("CacheCtrlSrv");
		app.startService(intent);
	}

	public static void Update() {
		Intent intent = new Intent("CacheCtrlSrv");
		intent.putExtra("command", "update");
		app.startService(intent);
	}

	public CacheCtrlSrv() {

	}

	@Override
	public IBinder onBind(Intent intent) {
		Debugger.d(TAG, "onBind");
		return null;
	}

	@Override
	public void onCreate() {
		Debugger.d(TAG, "onCreate");

		// 注册网络监听对象
		netCheckReceiver = new NetCheckReceiver();
		netCheckReceiver.registerAction();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Debugger.d(TAG, "onDestroy");

		// 反注册网络监听对象
		netCheckReceiver.unRregisterAction();

		super.onDestroy();
	}

	@Override
	public int onStartCommand(android.content.Intent intent, int flags, int startId) {
		Debugger.d(TAG, "onStartCommand");
		if (intent != null) {
			String command = intent.getStringExtra("command");
			doCommand(command);
		}
		return START_STICKY;
	}

	public void doCommand(String command) {
		Debugger.d(TAG, "doCommand: " + command);
		if ("update".equals(command)) {
			doUpdate();
		}
	}

	public void doConnChanged(boolean isBreak) {
		if (isBreak) {
			Debugger.d(TAG, "disconnected");
		} else {
			Debugger.d(TAG, "connected");
			doUpdate();
		}
	}

	public void doUpdate() {
		Debugger.d(TAG, "doUpdate");
		CacheUpdateSrv.Update(app);
	}
}
