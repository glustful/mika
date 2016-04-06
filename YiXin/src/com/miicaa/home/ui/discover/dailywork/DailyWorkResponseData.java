package com.miicaa.home.ui.discover.dailywork;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.miicaa.home.ui.discover.DiscoverReponseData;
import com.miicaa.home.ui.discover.DisoverData;

public class DailyWorkResponseData extends DiscoverReponseData{
	
	static String TAG = "DailyWorkResponseData";

	@Override
	protected DisoverData parseObject(JSONObject json) {
		Log.d(TAG, "parseObject json:"+json);
		DisoverData data = new DisoverData();
		data.dataType = json.optString("dataType");
		data.descriPtion = json.optString("content");
		
		Object documents = json.isNull("documents") ? null : json.opt("documents");
		if(documents == null)
			data.hasAttchment = false;
		else
			data.hasAttchment = true;
		
		data.id = json.optString("id");
		data.planTime = json.isNull("planTime") ? null : json.optLong("planTime");
		data.remindTime = json.isNull("remindTime") ? null : json.optLong("remindTime");
		data.title = json.optString("title");
		data.userCode = json.optString("creatorCode");
		data.userName = json.optString("creatorName");
		
		JSONArray labelsArray = json.optJSONArray("labels");
		
		if(labelsArray != null && labelsArray.length() > 0){
			data.labels = new String[labelsArray.length()];
			for(int i = 0 ; i < labelsArray.length();i++){
				data.labels[i] = labelsArray.optJSONObject(i).optString("label");
			}
		}
		return data;
	}

}
