package com.miicaa.home.data.service.notifyData;

public abstract class BaseNotifyData {
	
	public String title;
	public String operType;
	public String name;

	public BaseNotifyData(String title,String operType,String name){
		this.title = title;
		this.operType = operType;
		this.name = name;
	}
	
	public void parseNotify(){
//		if("add".equals(operType))
	}
	
	public abstract void addNotify();
	public abstract void deleteNotify();
	public abstract void updateNotify();
}
