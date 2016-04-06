package com.yxst.epic.yixin.utils;

import android.content.Context;

import com.yxst.epic.yixin.data.dto.request.BaseRequest;
import com.yxst.epic.yixin.preference.CachePrefs_;

public class CacheUtils {

	public static BaseRequest getBaseRequest(Context context) {
		CachePrefs_ cachePrefs = new CachePrefs_(context);
		
		BaseRequest request = new BaseRequest();
		
//		request.DeviceID = Utils.getDeviceId(context);
		request.DeviceID = Utils.getAndroidDeviceId(context);
		request.Token = cachePrefs.token().get();
		request.Uid = cachePrefs.uid().get();
		
		return request;
	}
}
