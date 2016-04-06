package com.miicaa.home.data.service;

import org.json.JSONObject;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.miicaa.common.base.Debugger;

public class CacheUpdateSrv extends Service {
	private static final String TAG = "Cache_Service";
	private static CacheUpdateSrv doingInstance = null;
	private TodoList todoList = null;

	public static void Update(Application app) {
		Stop();

		Intent intent = new Intent("CacheUpdateSrv");
		intent.putExtra("command", "update");
		app.startService(intent);
	}

	public static void Stop() {
		if (doingInstance != null) {
			doingInstance.stopSelf();
			doingInstance = null;
		}
	}

	public CacheUpdateSrv() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		Debugger.d(TAG, "onBind");
		return null;
	}

	@Override
	public void onCreate() {
		Debugger.d(TAG, "onCreate");

		todoList = new TodoList(new com.miicaa.home.data.OnFinish() {
			@Override
			public void onSuccess(JSONObject res) {
				// 在更新完成后，立即结束本服务
				Stop();
			}

			@Override
			public void onFailed(String msg) {

			}
		});

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Debugger.d(TAG, "onDestroy");
		doingInstance = null;
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
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

	public void doUpdate() {
		Debugger.d(TAG, "doUpdate");
		doingInstance = this;

		new Thread(new Runnable() {
			@Override
			public void run() {
				todoList.run();
			}
		}).run();
	}
}
