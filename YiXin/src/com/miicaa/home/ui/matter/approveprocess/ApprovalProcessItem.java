package com.miicaa.home.ui.matter.approveprocess;

import java.util.ArrayList;
import java.util.List;

import com.miicaa.home.ui.menu.SelectPersonInfo;

public class ApprovalProcessItem {

	public CharSequence numApprove;
	public CharSequence description;
	public List<SelectPersonInfo> personList;
	public Boolean isDid;
	public int number;
	public int currentStep;
	
	public ApprovalProcessItem(){
		personList = new ArrayList<SelectPersonInfo>();
	}

	@Override
	public String toString() {
//		return super.toString();
		return "numApprove" + numApprove + "description:"+description
				+"personList:"+personList.size()+"number:"+number;
	}
	
	
	
}
