package com.miicaa.detail;

import java.util.ArrayList;
import java.util.List;

import com.miicaa.home.ui.menu.SelectPersonInfo;

public class ApprovalProcessItem {

	public CharSequence numApprove;
	public CharSequence description;
	public List<SelectPersonInfo> personList;
	public Boolean isDid;
	
	public ApprovalProcessItem(){
		personList = new ArrayList<SelectPersonInfo>();
	}
	
}
