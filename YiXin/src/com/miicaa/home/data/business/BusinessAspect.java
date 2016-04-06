package com.miicaa.home.data.business;

import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseAspect;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-12-20.
 */
public class BusinessAspect extends ResponseAspect {
	@Override
	public Boolean onResponse(ResponseData data, RequestAdpater adpater) {
		// todo

		return super.onResponse(data, adpater);
	}
}
