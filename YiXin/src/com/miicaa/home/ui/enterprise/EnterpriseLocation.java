package com.miicaa.home.ui.enterprise;


import android.content.Context;
import android.content.SharedPreferences;
public class EnterpriseLocation {

	public static EnterpriseLocation instance_;
    private static String yunnan = "yunnan";
    private static String readed = "read";
    private static String nobg = "nobg";
	private static String enterprise = "enterprise";
	/*
	 * locationType:保存地理信息标志和读写情况
	 * gonggaoCount:公告有没有数据，且公告数据为几条
	 * 0000：定位失败，或者还未定位到
	 * 5399：定位的不是云南省
	 * 5301：云南且未读
	 * 5302：云南且已读但是guide被关闭
	 * 5303：云南已读且点击进入了企业服务
	 */
	public static final int  LocationFailed = 0000;
	public static final int NotYunnan = 5399;
	public static final int YunnanNoReaded = 5301;
	public static final int YunnanReadedCancel = 5302;
	public static final int YunanReadedSuccess = 5303;
	private  int locationType;
    private  int gonggaoCount;
	
	
	public EnterpriseLocation(){
	}
	
	public static EnterpriseLocation getInstance(){
		if(instance_ == null)
			instance_ = new EnterpriseLocation();
		return instance_;
	}
	
	public boolean getIsYunnan(Context context){
		SharedPreferences sharedPreferences = getPerferences(context);
		return sharedPreferences.getBoolean(yunnan, false);
	}
	public boolean getIsReaded(Context context){
		SharedPreferences sharedPreferences = getPerferences(context);
		return sharedPreferences.getBoolean(readed, false);
	}
	
	public boolean getNobg(Context context){
		SharedPreferences sharedPreferences = getPerferences(context);
		return sharedPreferences.getBoolean(nobg, true);
	}
	
	public void setIsReaded(Context context ,boolean isReaded){
		SharedPreferences sharedPreferences = getPerferences(context);
		setIsRead(sharedPreferences, isReaded);
	}
	
	public void setNoBg(Context context ,boolean hasbg){
		SharedPreferences sharedPreferences = getPerferences(context);
		setNoBg(sharedPreferences, hasbg);
	}
	
	
	
	public void setIsYunnan(Context context,boolean isYunnan){
		SharedPreferences sharedPreferences = getPerferences(context);
		setIsYunnan(sharedPreferences, isYunnan);
	}
	
	private void setIsRead(SharedPreferences shPreferences,boolean isReaded){
		SharedPreferences.Editor editor = shPreferences.edit();
		editor.putBoolean(readed, isReaded);
		editor.commit();
	}
	
	private void setNoBg(SharedPreferences sharedPreferences,boolean hasbg){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(nobg, hasbg);
		editor.commit();
	}
	
	private void setIsYunnan(SharedPreferences shPreferences,boolean isYunnan){
		SharedPreferences.Editor editor = shPreferences.edit();
		editor.putBoolean(yunnan, isYunnan);
		editor.commit();
	}
	
	private SharedPreferences getPerferences(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(enterprise, 
				Context.MODE_PRIVATE);
		return sharedPreferences;
	}
	
	public void setLocationType(int locationType){
		this.locationType = locationType;
	}
	
	public void setGonggaoCount(int gonggaoCount){
		this.gonggaoCount = gonggaoCount;
	}
	
	public int getLocationType(){
		return locationType;
	}
//	
	public int getGonggaoCount(){
		return gonggaoCount;
	}
	
	
	
}
