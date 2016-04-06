package com.miicaa.base.share.contact;

import com.miicaa.home.ui.contactList.SamUser;

public class Contact extends SamUser {

	private String phone;
	private String email;
	private String qq;
	private String industry;
	public Contact(String code, String name, String phone, String email,
			String qq, String industry) {
		super(code, name);
		this.phone = phone;
		this.email = email;
		this.qq = qq;
		this.industry = industry;
	}
	public Contact(String code, String name) {
		super(code, name);
		
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getemail() {
		return email;
	}
	public void setemail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	@Override
	public String toString() {
		return "Contact [phone=" + phone + ", email=" + email + ", qq=" + qq
				+ ", industry=" + industry + ", mCode=" + getmCode() + ", mName="
				+ getmName() + "]";
	}
	
}
