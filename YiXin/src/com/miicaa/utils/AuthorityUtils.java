package com.miicaa.utils;

import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;
import org.json.JSONObject;

import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

import com.miicaa.base.request.MRequest;
import com.miicaa.base.request.response.BaseResopnseData;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.preference.CachePrefs_;

public class AuthorityUtils {

	private static String TAG = "AuthorityUtils";
	
	public static AuthorityUtils instance;
	MRequest mRequest;
	AuthoResponseData mResponseData;
	
	public AuthorityUtils(){
		mRequest = new MRequest("/home/phone/authority/getCurrentUsedFunction");
		mResponseData = new AuthoResponseData();
		mResponseData.setOnResponseListener(mOnResponseListener);
	}
	
	public void requestAuth(OnAuthcationListener listener){
		onAuthcationListener = listener;
		mResponseData.reseAuth();
		mRequest.executeRequest(mResponseData);
	}
	
	public static  AuthorityUtils getInstance(){
		if(instance ==null){
			instance = new AuthorityUtils();
		}
		return instance;
	}
	
	BaseResopnseData.OnResponseListener mOnResponseListener = new BaseResopnseData.OnResponseListener() {
		
		@Override
		public void onResponseError(String errMsg, String errCode) {
			Log.d(TAG, "onResponseError : "+errMsg+":"+errCode);
			if(mResponseData.requestNumber < 3){
				mRequest.executeRequest();
				mResponseData.requestNumber++;
			}else{
				if(onAuthcationListener != null)
					onAuthcationListener.onAuthcationFailed(errMsg, errCode);
			}
		}
		
		@Override
		public void onReponseComplete() {
			mResponseData.requestNumber = 0;
			//将各种付费权限设置好
			MyApplication.getInstance().setAuthority(AuthorityState.ePhoto, 
					mResponseData.photoAuth ? AllUtils.PAYFOR_USER : AllUtils.NORMAL_User);
			MyApplication.getInstance().setAuthority(AuthorityState.eSubTask, 
					mResponseData.subTaskAuth ? AllUtils.PAYFOR_USER : AllUtils.NORMAL_User);
			MyApplication.getInstance().setAuthority(AuthorityState.eApproveProcess, 
					mResponseData.approveProcessAuth ? AllUtils.PAYFOR_USER : AllUtils.NORMAL_User);
			if(onAuthcationListener != null)
				onAuthcationListener.onAuthcationSuccess();
		}
		
		@Override
		public void onProgress(float count) {
			
		}
		
		@Override
		public void onPreExecute() {
			
		}
		
		@Override
		public void onNoneData() {
			
		}
	};
	
	class AuthoResponseData extends BaseResopnseData{

		int requestNumber = 0;
		JSONObject dataObject;
		boolean photoAuth;
		boolean subTaskAuth;
		boolean approveProcessAuth;
		
		@Override
		public void saveData(Object data) {
			if(data instanceof JSONObject){
				dataObject = (JSONObject)data;
			}
		}

		@Override
		public void parseData() {
			if(dataObject == null){
				return ;
			}
			if(dataObject.optBoolean("canUseAllFunction")){
				photoAuth = true;
				subTaskAuth = true;
				approveProcessAuth = true;
			}else{
				JSONArray authArray = dataObject.optJSONArray("functions");
				if(authArray != null){
					for (int i = 0 ; i < authArray.length(); i++){
						JSONObject json = authArray.optJSONObject(i);
						String mask = json.optString("setOtherId");
						if("1".equals(mask)){
							subTaskAuth = true;
						}else if("2".equals(mask)){
							approveProcessAuth = true;
						}else if("3".equals(mask)){
							photoAuth = true;
						}
					}
				}
			}
		}
		
		void reseAuth(){
			photoAuth = false;
			subTaskAuth = false;
			approveProcessAuth = false;
		}
		
	}
	
	public interface OnAuthcationListener{
		void onAuthcationSuccess();
		void onAuthcationFailed(String errMsg,String errCode);
	}
	
	OnAuthcationListener onAuthcationListener;
	public void setOnAuthcationListener(OnAuthcationListener listener){
		this.onAuthcationListener = listener;
	}
	
	
	public enum AuthorityState{
		ePhoto,
		eSubTask,
		eApproveProcess
	}
	
}
