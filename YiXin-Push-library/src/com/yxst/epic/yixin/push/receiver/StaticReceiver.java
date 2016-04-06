package com.yxst.epic.yixin.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yxst.epic.yixin.push.service.PushCliService;

public class StaticReceiver extends BroadcastReceiver {

	private static final String TAG = "StaticReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		Log.d(TAG, "onReceive() action:" + action);
		
		PushCliService.startService(context);
	}

}
