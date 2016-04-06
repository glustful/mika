package com.miicaa.base.EverInfo;

import java.util.Date;

public class LoginUserInfo {

	    private String userEmail = "";
	    private String userPassword = "";
	    private Boolean loginAuto = false;
	    private String loginType = "";
	    private Date lastTime;
//	    private OrgInfo lastOrgInfo;
//	    private UserInfo lastUserInfo;
	    public void setUserEmail(String email){
	    	userEmail = email;
	    }
	    public void setuserPassword(String passWord){
	    	userPassword = passWord;
	    	
	    }
	    public void setLoginType(String loginType){
	    	this.loginType = loginType;
	    }
	    public void setLastTime(Date lastTime){
	    	this.lastTime = lastTime;
	    }
	    public String getUserEmail(){
	    	return userEmail;
	    }
	    public String getUserPassword(){
	    	return userPassword;
	    }
	    public String getLoginType(){
	    	return loginType;
	    }
	    public Date getLastTime(){
	    	return lastTime;
	    }
}
