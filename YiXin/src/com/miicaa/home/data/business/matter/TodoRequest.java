package com.miicaa.home.data.business.matter;

import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-12-27.
 */
public class TodoRequest {
	// http://10.180.120.147/home/phone/thing/getallwork
	public static Boolean requestAllWork(final OnBusinessResponse onResponse) {
		if (onResponse == null)
			return false;
		return new RequestAdpater() {
			@Override
			public void onReponse(ResponseData data) {
				onResponse.onResponse(data);
			}

			@Override
			public void onProgress(ProgressMessage msg) {
				onResponse.onProgress(msg);
			}
		}.setUrl("/home/phone/thing/getallwork")
				.notifyRequest();
	}
}
