package com.miicaa.base.share.select;

import java.io.Serializable;

public class Kind implements Serializable{
	@Override
	public boolean equals(Object o) {
		if(o instanceof Kind){
			Kind tmp = (Kind) o;
			if(tmp.getCode().equals(getCode()))
				return true;
		}
		return false;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String code;
	private String name;
	private boolean isCheck;
	public Kind(String id,String code, String name, boolean isCheck) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.isCheck = isCheck;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	@Override
	public String toString() {
		return "Kind [code=" + code + ", name=" + name + ", isCheck=" + isCheck
				+ "]";
	}
	
}
