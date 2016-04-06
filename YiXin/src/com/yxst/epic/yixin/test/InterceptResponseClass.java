package com.yxst.epic.yixin.test;

import android.util.Log;

import com.yxst.epic.yixin.data.dto.response.Response;

public class InterceptResponseClass {

	public static final String TAG = "InterceptClass";
	
	public void before() {
		Log.d(TAG, "before()");
	}
	
	public void after() {
		Log.d(TAG, "after()");
		
		
	}
	
	public void im(Response response) {
		Log.d(TAG, "im()");
		Log.d(TAG, "im() response:" + response);
	}
}
