package com.yxst.epic.yixin.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yxst.epic.yixin.push.service.PushCliService;
import com.yxst.epic.yixin.push.util.Utils;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

	private static final String TAG = "ConnectivityChangeReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive()");
		Log.d(TAG, "onReceive() NetworkUtil.isNetworkAvailable(context):" + Utils.isNetworkAvailable(context));

		if (Utils.isNetworkAvailable(context)) {
//			AlarmReceiver.alarm(context);
			PushCliService.startService(context);
		} else {
			//AlarmReceiver.cancel(context);
			PushCliService.stopService(context);
		}
	}

}
