package com.miicaa.base.base;

import java.io.Serializable;

import org.json.JSONArray;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse implements Serializable{

	/**
	 * ��¼��Ϣ�ص�
	 */
	private static final long serialVersionUID = 378490671190430520L;
	OnResponse onResponse;
	@JsonProperty(value = "data")
	JSONArray jsarry;
	
	public void checkData(){
		if(jsarry == null){
			onResponse.onFiald();
		}else{
			onResponse.onSuccess(jsarry);
		}
	}
	 
	

}
