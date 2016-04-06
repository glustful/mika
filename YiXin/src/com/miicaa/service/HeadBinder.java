package com.miicaa.service;

import java.util.ArrayList;

import android.content.ContentValues;
import android.os.Binder;

import com.miicaa.common.base.OnEachRow;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.data.business.org.UserInfoSql;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.RequestAdpater.RequestMethod;
import com.miicaa.home.data.net.RequestAdpater.RequestType;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.old.UserAccount;


public class HeadBinder extends Binder{
	
	
	
	public void startDownImg(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				downImage();
			}
		}).start();
	}
	
	private void downImage(){
		String url = "/home/pc/personcenter/showhead";
		ArrayList<String> userCodes = new ArrayList<String>();
		UserInfo.usersInOrg(AccountInfo.instance().getLastOrgInfo(), new OnEachRow() {
			@SuppressWarnings("unchecked")
			@Override
			public void eachRow(ContentValues row, Object cbData) {
				// TODO Auto-generated method stub
				UserInfo user = UserInfoSql.fromRow(row);
				String userCode = user.getCode();
				((ArrayList<String>)cbData).add(userCode);
			}
		}, userCodes);
		
		if(userCodes.size() == 0){
			return;
		}
		for(int i = 0 ; i < userCodes.size(); i++){
			String userCode = userCodes.get(i);
			String fileName = userCode + ".jpg";
			String bufDir = UserAccount.getLocalDir("imgCache/");
			new RequestAdpater() {
				
				@Override
				public void onReponse(ResponseData data) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgress(ProgressMessage msg) {
					// TODO Auto-generated method stub
					
				}
			}.setUrl(url)
			 .setRequestMethod(RequestMethod.eGet)
			 .setRequestType(RequestType.eFileDown)
			 .addParam("usercode",userCode)
			 .setLocalDir(bufDir)
			 .setFileName(fileName)
			 .notifyRequest();
		}
	}
}
