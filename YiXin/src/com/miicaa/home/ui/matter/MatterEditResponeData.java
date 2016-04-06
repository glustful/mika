package com.miicaa.home.ui.matter;

import org.json.JSONObject;

import com.miicaa.base.request.response.BaseResopnseData;

public class MatterEditResponeData extends BaseResopnseData{
	
	String infoId;
//	JSONObject jsonData;

	@Override
	public void saveData(Object data) {
		infoId = (String) data;
	}

	@Override
	public void parseData() {
	}

}
