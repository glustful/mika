package com.miicaa.home.ui.home;

import android.util.Log;

import com.miicaa.home.ui.home.removeScreen.RemoveApprovalTypes;
import com.miicaa.home.ui.home.removeScreen.RemoveArrangeTypes;
import com.miicaa.home.ui.home.removeScreen.RemoveBeginTime;
import com.miicaa.home.ui.home.removeScreen.RemoveCreateUserType;
import com.miicaa.home.ui.home.removeScreen.RemoveEndTime;
import com.miicaa.home.ui.home.removeScreen.RemoveReportTypes;
import com.miicaa.home.ui.home.removeScreen.RemoveScreenType;
import com.miicaa.home.ui.home.removeScreen.RemoveSrcTypes;
import com.miicaa.home.ui.home.removeScreen.RemoveTodoUserType;
import com.miicaa.home.ui.menu.ScreenType;

public  class MatterScreenType {
	
	static String TAG = "MatterScreenType";

	public static String ApprovalCodes = RemoveApprovalTypes.class.getName();
	public static String ArrangeCodes = RemoveArrangeTypes.class.getName();
	public static String CreateUsers = RemoveCreateUserType.class.getName();
	public static String TodoUsers = RemoveTodoUserType.class.getName();
	public static String SrcCodes = RemoveSrcTypes.class.getName();
	public static String BeginTime = RemoveBeginTime.class.getName();
	public static String EndTime = RemoveEndTime.class.getName();
	public static String ReportCodes = RemoveReportTypes.class.getName();
	
	public String type;
	public String content;
	
	public MatterScreenType(String content,String type){
		this.content = content;
		this.type = type;
	}
	
	
	public void removeType(ScreenType screenType) throws Exception{
		try{
			Class<?> cls = Class.forName(type);
			RemoveScreenType removeType = (RemoveScreenType)cls.newInstance();
			removeType.removeType(screenType);
			
		}catch(Exception e){
//			Toast.makeText(context, text, duration)
			Log.e(TAG, "文件不存在！"+type);
			e.printStackTrace();
		}
	}
	
	
	
	
}
