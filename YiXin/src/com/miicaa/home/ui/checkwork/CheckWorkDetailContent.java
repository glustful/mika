package com.miicaa.home.ui.checkwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.miicaa.utils.AllUtils;


public class CheckWorkDetailContent implements Serializable{
	/**
	 * 传值给详情页
	 */
	private static final long serialVersionUID = 7793715556813170671L;
	public String name;
	public String dateStr;
	public String signInDate;
	public String signInWhere;
	public String signOutDate;
	public String signOutWhere;
	public String signInBeizhu;
	public String signOutBeizhu;
	public String mId;
	public String signInStatusStr;
	public String signOutStatusStr;
	public String dataUserCode;
	public Long   dateL;
	public String date;
	public String signInLongitude;//经度
	public String signInLatitude;//纬度
	public String signOutLongitude;//经度
	public String signOutLatitude;//纬度
	public ArrayList<String> singInFileIdList;
	public ArrayList<String> singOutFileIdList;
	
	public CheckWorkDetailContent(){
		this.singInFileIdList = new ArrayList<String>();
		this.singOutFileIdList = new ArrayList<String>();
	}
	
	public CheckWorkDetailContent build(String name ,String dateStr,String signInDate,
			String signInWhere,String signOutDate,String signOutWhere){
		this.name = name;
		this.dateStr = dateStr;
		this.signInDate = signInDate;
		this.signInWhere = signInWhere;
		this.signOutDate = signOutDate;
		this.signOutWhere = signOutWhere;
		return this;
	}
	
	public CheckWorkDetailContent addBeizhu(String signInBeizhu,String signOutBeizhu){
		this.signInBeizhu = signInBeizhu;
		this.signOutBeizhu = signOutBeizhu;
		return this;
	}
	
	public CheckWorkDetailContent addStatusStr(String signInStatusStr,String signOutStatusStr){
		this.signInStatusStr = signInStatusStr;
		this.signOutStatusStr = signOutStatusStr;
		return this;
	}
	
	public CheckWorkDetailContent addDataUserCode(String userCode){
		this.dataUserCode = userCode;
		return this;
	}
	
	public CheckWorkDetailContent addId(String id){
		this.mId = id;
		return this;
	}
	
	public CheckWorkDetailContent addDate(Long date){
		this.dateL = date;
		this.date = AllUtils.getYearTime(dateL);
		return this;
	}
	
	public CheckWorkDetailContent addItude(String signInLongitude,String signInLatitude
			,String signOutLongitude,String signOutLatitude){
		this.signInLongitude = signInLongitude;
		this.signInLatitude = signInLatitude;
		this.signOutLongitude = signOutLongitude;
		this.signOutLatitude = signOutLatitude;
		return this;
	}
	
	public CheckWorkDetailContent addsignInFileIdList(ArrayList<String> list){
		this.singInFileIdList.addAll(list);
		return this;
	}
	

	public void addsignInFileIdList(String list){
		singInFileIdList.add(list);
	}
	
	public CheckWorkDetailContent addsignOutFileIdList(ArrayList<String> list){
		this.singOutFileIdList.addAll(list);
		return this;
	}
	

	public void addsignOutFileIdList(String list){
		singOutFileIdList.add(list);
	}
	
	public Boolean WhereIsIp(String where){
		//正则式判断是否为一个ip地址
		 String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])"
		 		+ "(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
	      
	      Pattern pat = Pattern.compile(rexp);  
	      
	      Matcher mat = pat.matcher(where);  
	      
	      boolean ipAddress = mat.find();

	      return ipAddress;
	}
}
