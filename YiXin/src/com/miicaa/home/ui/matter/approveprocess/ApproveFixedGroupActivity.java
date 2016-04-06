package com.miicaa.home.ui.matter.approveprocess;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.aps.m;
import com.miicaa.base.request.MRequest;
import com.miicaa.base.request.response.BaseResopnseData;
import com.miicaa.base.request.response.BaseResopnseData.OnResponseListener;
import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;
import com.miicaa.home.ui.discover.dailywork.BaseReportType;
import com.miicaa.home.ui.menu.SelectPersonInfo;

@EActivity(R.layout.activity_approve_fix_create)
public class ApproveFixedGroupActivity extends MainActionBarActivity{

	private static String TAG = "ApproveFixedGroupActivity";
	
	@ViewById(R.id.listView)
	ExpandableListView listView;
	
	@Extra
	String dataId;
	@Extra
	ArrayList<SelectPersonInfo> mSelectPersonList;
	
	
	MRequest mRequest;
	FixedGroupResponseData mResponseData;
	FixedProcessAdapter mFixedProcessAdapter;
	
	
	

		@AfterViews
	void afterView(){
		mFixedProcessAdapter = new FixedProcessAdapter(this);
		listView.setAdapter(mFixedProcessAdapter);
		mResponseData = new FixedGroupResponseData();
		mRequest = new MRequest("/home/phone/thing/getapprover4change");
		mRequest.addParam("dataId", dataId);
		mResponseData.setOnResponseListener(mOnResponseListener);
        mRequest.executeRequest(mResponseData);
	}
	
	@Override
	public String showBackButtonStr() {
		return null;
	}

	@Override
	public Boolean showBackButton() {
		return true;
	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public Boolean showTitleButton() {
		return true;
	}

	@Override
	public String showTitleButtonStr() {
		return "选择人员";
	}

	@Override
	public void titleButtonClick(View v) {
		
	}

	@Override
	public Boolean showRightButton() {
		return true;
	}

	@Override
	public String showRightButtonStr() {
		return "确认";
	}

	@Override
	public void rightButtonClick(View v) {
		ArrayList<SelectPersonInfo> infos = new ArrayList<SelectPersonInfo>();
		FixProcessPersonGroup group = mFixedProcessAdapter.mPersonGroup;
		if(group != null){
			for(SelectPersonInfo child : group.children){
				if(child.isSelect)
					infos.add(child);
			}
		}
		Intent i = new Intent();
		i.putParcelableArrayListExtra("process", infos);
		i.putExtra("step", group != null ? group.step : -1);
		setResult(RESULT_OK, i);
		finish();
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
	
	OnResponseListener mOnResponseListener = new OnResponseListener() {
		
		@Override
		public void onResponseError(String errMsg, String errCode) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onReponseComplete() {
			mFixedProcessAdapter.refresh(mResponseData.processGroupList);
			for(int i = 0; i < mFixedProcessAdapter.getGroupCount(); i++){
				listView.expandGroup(i);
			}
		}
		
		@Override
		public void onProgress(float count) {
			
		}
		
		@Override
		public void onPreExecute() {
			
		}
		
		@Override
		public void onNoneData() {
			
		}
	};
	
	class FixedGroupResponseData extends BaseResopnseData{

		JSONArray dataArray;
		List<FixProcessPersonGroup> processGroupList;
		
		public FixedGroupResponseData(){
			processGroupList = new ArrayList<FixProcessPersonGroup>();
		}
		
		@Override
		public void saveData(Object data) {
			if(data instanceof JSONObject){
				Log.d(TAG, "saveData jsonObjectdata:"+(JSONObject)data);
			}else if(data instanceof JSONArray){
				dataArray = (JSONArray)data;
				Log.d(TAG, "saveData jsonArraydata:"+(JSONArray)data);
			}
		}

		@Override
		public void parseData() {
			if(dataArray != null)
			{
				
				for(int i = 0 ; i < dataArray.length(); i++){
					JSONObject json = dataArray.optJSONObject(i);
					String groupName = json.optString("title");
					String  groupId = json.optString("id");
					FixProcessPersonGroup personGroup = new FixProcessPersonGroup(groupName, groupId);
					personGroup.step = json.optInt("step");
					JSONArray userArray = json.optJSONArray("participantUsers");
					List<SelectPersonInfo> personList = new ArrayList<SelectPersonInfo>();
					if(userArray == null)
						continue;
					for(int j = 0 ; j < userArray.length(); j++){
						JSONObject userJson = userArray.optJSONObject(j);
					    String name = userJson.optString("name");
					    String code = userJson.optString("code");
					    Log.d(TAG, "parseData name:"+name+"code:"+code);
					    SelectPersonInfo info = new SelectPersonInfo(name ,code );
					    isSelect(info);
					    personList.add(info);
					}
					personGroup.children = personList;
					processGroupList.add(personGroup);
				}
			}
		}
		
	}
	
	private boolean isSelect(SelectPersonInfo meInfo){
		if(mSelectPersonList == null)
			return false;
		for(SelectPersonInfo info : mSelectPersonList){
			if(info.mCode.equals(meInfo.mCode)){
				meInfo.checkEnable = info.checkEnable;
				meInfo.isSelect = true;
				return true;
			}
		}
		return false;
	}

}
