package com.miicaa.home;

public class BaseKeyVaule {

	public String mName;
	public String mCode;
	public Boolean isSelect = false;
	
	public BaseKeyVaule(String name , String code){
		this.mName = name;
		this.mCode = code;
	}
	
	public void setSelect(Boolean select){
		isSelect = select;
	}
}
