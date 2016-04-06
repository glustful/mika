package com.miicaa.home.data.business.org;

import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.old.UserAccount;

/**
 * Created by Administrator on 13-12-17.
 */
public class UserRequset {
	public static Boolean requestUserHead(final OnBusinessResponse onResponse, String userCode) {

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
		}.setUrl("/home/phone/personcenter/showhead")
				.setRequestMethod(RequestAdpater.RequestMethod.eGet)
                .setRequestType(RequestAdpater.RequestType.eFileDown)
				.addParam("usercode", userCode)
				.setLocalDir(UserAccount.getLocalDir("imgCache/"))
				.setFileName(userCode+".jpg")
				.notifyRequest();
	}

	public static Boolean requestPersonInfo(final OnBusinessResponse onResponse, String userCode) {

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
		}.setUrl("/home/phone/personcenter/getpersoninfo")
				.addParam("userCode", userCode)
				.notifyRequest();
	}

	public static Boolean requestPersonCommit(final OnBusinessResponse onResponse,
											  String userId,
											  String userCode,
											  String userName,
											  String gender,
											  String qq,
											  String cellphone,
											  String phone,
											  String addr,
											  String birthday,
											  String email,
											  String unit,
											  String group) {

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
		}.setUrl("/home/phone/personcenter/edituserinfo")
				.addParam("userId", userId)
				.addParam("userCode", userCode)
				.addParam("name", userName)
				.addParam("gender", gender)
				.addParam("qq", qq)
				.addParam("cellphone", cellphone)
				.addParam("phone", phone)
				.addParam("addr", addr)
				.addParam("email", email)
				.addParam("birthday", birthday)
				.addParam("unit", unit)
				.addParam("group", group)
				.notifyRequest();
	}

	public static Boolean requestPersonCommit(final OnBusinessResponse onResponse,
											  String userId,
											  String userCode,
											  String key,
											  String val) {
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
		}.setUrl("/home/phone/personcenter/edituserinfo")
				.addParam("userId", userId)
				.addParam("userCode", userCode)
				.addParam(key, val)
				.notifyRequest();
	}

	public static Boolean requestPwdCheck(final OnBusinessResponse onResponse, String email, String psw) {

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
		}.setUrl("/home/phone/personcenter/checkpsw")
				.addParam("email", email)
				.addParam("psw", psw)
				.notifyRequest();
	}

	public static Boolean requestChangePwd(final OnBusinessResponse onResponse, String email, String oldPsw, String newPsw) {

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
		}.setUrl("/home/phone/personcenter/changepsw")
				.addParam("email", email)
				.addParam("oldPsw", oldPsw)
				.addParam("newPsw", newPsw)
				.notifyRequest();
	}

	public static Boolean requestEditField(final OnBusinessResponse onResponse) {
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
		}.setUrl("/home/phone/personcenter/editfields")
				.notifyRequest();
	}
}
