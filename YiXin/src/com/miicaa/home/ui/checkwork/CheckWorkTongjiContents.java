package com.miicaa.home.ui.checkwork;


public abstract class CheckWorkTongjiContents {

	public CheckScreenValue timeType;
	public String name;
	public String later;
	public String leaved;
	public String nosignInCount;
	public String nosignOutCount;

	public CheckWorkTongjiContents save(String later,String leaved,
			String nosignIn,String nosignOut){
		this.later = later;
		this.leaved = leaved;
		this.nosignInCount = nosignIn;
		this.nosignOutCount = nosignOut;
		return this;
	}
	
	public CheckWorkTongjiContents addName(String name){
		this.name = name;
		return this;
	}
	
	public CheckWorkTongjiContents saveTimeType(CheckScreenValue type){
		this.timeType = type;
		return this;
	}
	public  void OnDateButtonClick(){
		if(timeType == null){
			return;
		}
		OnDateButtonClick(timeType);
		timeType = null;
	};
	
	public abstract void OnDateButtonClick(CheckScreenValue timeType);
}
