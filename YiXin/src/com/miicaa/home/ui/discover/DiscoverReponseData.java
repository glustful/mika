package com.miicaa.home.ui.discover;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.miicaa.base.request.response.BaseResopnseData;

public abstract class DiscoverReponseData extends BaseResopnseData{

	private static String TAG = "DiscoverReponseData";
	
	JSONArray resultArray;
	List<DisoverData> discoverList;
	
	public DiscoverReponseData() {
		discoverList = new ArrayList<DisoverData>();
	}

	@Override
	public void saveData(Object data) {
		JSONObject jsonObject = (JSONObject) data;
		resultArray = jsonObject.optJSONArray("workDTO");
	}

	@Override
	public void parseData() {
		discoverList.clear();
		if(resultArray.length() == 0){
			if(onResponseListener != null)
				onResponseListener.onNoneData();
			return;
		}
		for(int i = 0; i < resultArray.length(); i++){
			DisoverData data = parseObject(resultArray.optJSONObject(i));
			if(data != null)
			discoverList.add(data);
		}
		if(onResponseListener != null)
			onResponseListener.onReponseComplete();
	}
	
	protected abstract DisoverData parseObject(JSONObject json);
	
}
