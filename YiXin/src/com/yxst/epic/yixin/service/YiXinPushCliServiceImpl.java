package com.yxst.epic.yixin.service;

import android.text.TextUtils;
import android.util.Log;

import com.miicaa.base.pay.wx.Util;
import com.miicaa.home.data.old.UserAccount;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.data.rest.YixinHost;
import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.push.service.PushCliService;
import com.yxst.epic.yixin.utils.Utils;

public class YiXinPushCliServiceImpl extends PushCliService {

	private static final String TAG = "MiicaaPushCliServiceImpl";

	@Override
	protected String getHost() {
//		return "pushcomet.miicaa.com";
		return YixinHost.PushHost;
//		return "192.168.1.16";
//		return "192.168.1.22";
	}

	@Override
	protected int getPort() {
		return YixinHost.YixinPort;
//		return 8093;
	}

	@Override
	protected String getKey() {
		String user_id = MyApplication.getInstance().getUid();
		if (TextUtils.isEmpty(user_id)) {
			user_id = "anonymous";
		}
		
//		String real_device_id = Utils.getDeviceId(this);
		String real_device_id = Utils.getAndroidDeviceId(this);
		String device_type = "Android";
		
		String device_id = device_type + '-' + real_device_id;
		
		String session_id = user_id + '_' + device_id;
		
		String key = session_id + "@user";
		
		return key;
	}

	@Override
	protected long getMid() {
		long lastMid = DBManager.getInstance(this).getLastMid(MyApplication.getInstance().getLocalUserName());
		Log.d(TAG, "lastMid:" + lastMid);
		if(lastMid == 0){
			lastMid = Utils.getMid7Day();
		}
		return lastMid;
	}

	@Override
	public void onCreate() {
		addPushMessageListener(new MiicaaPushMessageListener(this), new MiicaaPushMessageFilter());
//		addPushMessageListener(new MiicaaPushMessageListener(this), new MiicaaPushMessageFilter());
		super.onCreate();
	}
	
}
