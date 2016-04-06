package com.miicaa.home.data;

import org.json.JSONObject;

/**
 * Created by Administrator on 13-12-17.
 */
public interface OnFinish {
	public void onSuccess(JSONObject res);

	public void onFailed(String msg);
}
