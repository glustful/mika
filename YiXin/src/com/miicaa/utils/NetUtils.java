package com.miicaa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.LogUtil.log;
import com.miicaa.common.base.DatabaseOption;
import com.miicaa.common.base.Tools;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.storage.LocalPath;

public class NetUtils {

	private static String TAG = "NetUtils";
	
	
	
	
	public static void refresContact(final OnRefreshCompleteListener onRefreshCompleteListener){
		if(AccountInfo.instance().getLastUserInfo() == null || AccountInfo.instance().getLastOrgInfo() == null)
			return;
//		DatabaseOption.getIntance().setValue(AccountInfo.instance().getLastUserInfo().getCode()+"userInit", 
//				"00");
		new RequestAdpater() {
            @Override
            public void onReponse(final ResponseData data) {
                //onResponse.onResponse(data);
                
                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                	final Handler handler = new Handler(){

						@Override
						public void handleMessage(Message msg) {
							onRefreshCompleteListener.refreshComplete();
							super.handleMessage(msg);
						}
                		
                	};
                	new Thread( new Runnable() {
						
						@Override
						public void run() {
							if(AccountInfo.instance().getLastUserInfo() != null && 
		                			AccountInfo.instance().getLastUserInfo().getOrgCode() != null)
							   UserInfo.deleteByOrgCode(AccountInfo.instance().getLastUserInfo().getOrgCode());
			                    Tools.initUsersData(data.getJsonArray());
//			                    DatabaseOption.getIntance()
//			                            .setValue(AccountInfo.instance().getLastUserInfo().getCode()
//			                                    + "userInit", "01");
			                    handler.obtainMessage(0, null).sendToTarget();
						}
					}).start();
                   
                }else{
                	onRefreshCompleteListener.refreshField(data.getMsg());
                }

            }

            @Override
            public void onProgress(ProgressMessage msg) {
            }
        }.setUrl("/mobile/mobile/oug/getAll")
                .addParam("type", "user")
                .addParam("orgCode", AccountInfo.instance().getLastOrgInfo().getCode())
                .notifyRequest();
    
	}
	
	
	public interface OnReloginListener{
		void success();
		void failed();
	}
	
	public static CookieStore getCookie(){
		 File cookieFile = new File(LocalPath.intance().cacheBasePath + "co");
         String res = "";
         try {
             FileInputStream fin = new FileInputStream(cookieFile);

             int length = fin.available();

             byte[] buffer = new byte[length];
             fin.read(buffer);

             res = EncodingUtils.getString(buffer, "UTF-8");
             fin.close();
             if (res != null && !"".equals(res.trim())) {
                 BasicCookieStore basicCookieStore = new BasicCookieStore();
                 JSONArray array = new JSONArray(res);
                 for (int i = 0; i < array.length(); i++) {
                     JSONObject o = array.getJSONObject(i);
                     String name = o.optString("name");
                     String value = o.optString("value");
                     if (name != null && value != null) {
                         BasicClientCookie basicClientCookie = new BasicClientCookie(name, value);
                         basicClientCookie.setComment(o.optString("comment"));
                         basicClientCookie.setDomain(o.optString("domain"));
                         basicClientCookie.setPath(o.optString("path"));
                         basicClientCookie.setVersion(o.optInt("version"));
                         basicClientCookie.setSecure(o.optBoolean("secure"));
                         basicCookieStore.addCookie(basicClientCookie);
                     }
                 }
                 return basicCookieStore;
         }
         }catch(Exception e){
        	 e.printStackTrace();
        	 try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	 }
         return null;
         
         }
	
	public static void uploadFileNormal(ArrayList<String> pathList){
		
	}
	

}
