package com.miicaa.home.ui.matter.approveprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.miicaa.home.ui.menu.SelectPersonInfo;

public class FixProcessPersonGroup {

	CharSequence groupName;
	String groupId;
	int step;
	boolean isSelect = false;
	Integer childrenSelectCount = 0;
	List<SelectPersonInfo> children;
	
	
	public FixProcessPersonGroup(CharSequence groupName,String groupId){
		this.groupId = groupId;
		this.groupName = groupName;
		children = new ArrayList<SelectPersonInfo>();
	}
	
}
