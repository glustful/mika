package com.miicaa.home.data.business.org;

import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-12-27.
 */
public class OrgRequest {
	/*
	http://10.180.120.147/mobile/mobile/oug/getAll?type=unit&orgCode=O%3Dynyx
		type有
			user：用户
			unitUser：用户部门关系
			unit：部门
			group：职位
			groupUser：职位用户关系
	 */

	public static Boolean requestUsers(final OnBusinessResponse onResponse, OrgInfo org) {
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
		}.setUrl("/mobile/mobile/oug/getAll")
				.addParam("type", "user")
				.addParam("orgCode", org.getCode())
				.notifyRequest();
	}

	public static Boolean requestUnits(final OnBusinessResponse onResponse, OrgInfo org) {
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
		}.setUrl("/mobile/mobile/oug/getAll")
				.addParam("type", "unit")
				.addParam("orgCode", org.getCode())
				.notifyRequest();
	}

	public static Boolean requestUnitUsers(final OnBusinessResponse onResponse, OrgInfo org) {
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
		}.setUrl("/mobile/mobile/oug/getAll")
				.addParam("type", "unitUser")
				.addParam("orgCode", org.getCode())
				.notifyRequest();
	}
	public static Boolean requestGroups(final OnBusinessResponse onResponse, OrgInfo org) {
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
		}.setUrl("/mobile/mobile/oug/getAll")
				.addParam("type", "group")
				.addParam("orgCode", org.getCode())
				.notifyRequest();
	}

	public static Boolean requestGroupUsers(final OnBusinessResponse onResponse, OrgInfo org) {
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
		}.setUrl("/mobile/mobile/oug/getAll")
				.addParam("type", "groupUser")
				.addParam("orgCode", org.getCode())
				.notifyRequest();
	}
}
