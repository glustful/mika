package com.miicaa.home.ui.matter.approveprocess;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.miicaa.base.request.MRequest;
import com.miicaa.base.request.response.BaseResopnseData;
import com.miicaa.base.request.response.BaseResopnseData.OnResponseListener;
import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;
import com.miicaa.home.ui.menu.SelectPersonInfo;

@EActivity
public class ApproveFixedSingleActivity extends MainActionBarActivity{
	
	private static String TAG =  "ApproveFixedSingleActivity";
	
	@Extra
	String dataType;
	@Extra
	ArrayList<String> mSelectCodeList;
	
//	@ViewById(R.id.listView)
	ListView listView;
    
	FixedProcessResponseData mRespondata;
	MRequest mRequest;
	FixedProcessSingleAdapter processSingleAdapter;
	
	
     @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		processSingleAdapter = new FixedProcessSingleAdapter(this);
		Log.d(TAG, "dataType:"+dataType);
		setContentView(R.layout.activity_n_list);
		listView = (ListView)findViewById(R.id.listView);
		afterView();
	}

	//	@AfterViews
	void afterView(){
		listView.setAdapter(processSingleAdapter);
		mRespondata = new FixedProcessResponseData();
		mRespondata.setOnResponseListener(mOnResponseListener);
		mRequest = new MRequest("/home/phone/thing/getapprover4add");
		mRequest.addParam("approveType", dataType);
		mRequest.executeRequest(mRespondata);
	}
	
	OnResponseListener mOnResponseListener = new OnResponseListener() {
		
		@Override
		public void onResponseError(String errMsg, String errCode) {
			
		}
		
		@Override
		public void onReponseComplete() {
			processSingleAdapter.refresh(mRespondata.processPeronList);
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
		ArrayList<SelectPersonInfo> infoList = new ArrayList<SelectPersonInfo>();
		for(SelectPersonInfo info :mRespondata.processPeronList){
			if(info.isSelect){
				infoList.add(info);
			}
		}
		Intent i = new Intent();
		i.putParcelableArrayListExtra("process", infoList);
		setResult(RESULT_OK, i);
		finish();
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
	
	
	class FixedProcessResponseData extends BaseResopnseData{

		JSONArray dataArray;
		List<SelectPersonInfo> processPeronList;
		
		 public FixedProcessResponseData() {
			 processPeronList = new ArrayList<SelectPersonInfo>();
		}
		
		@Override
		public void saveData(Object data) {
			 dataArray = (JSONArray)data;
		}

		@Override
		public void parseData() {
			if(dataArray == null) {
				return;
			}
			for(int i = 0 ; i < dataArray.length(); i++ ){
				try {
					JSONObject json = dataArray.getJSONObject(i);
					String name = json.optString("name");
					String code = json.optString("code");
					SelectPersonInfo info = new SelectPersonInfo(name, code);
					info.isSelect = isSelect(code);
					processPeronList.add(info);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	private boolean isSelect(String userCode){
		if(mSelectCodeList == null)
			return false;
		for(String code : mSelectCodeList){
			if(userCode.equals(code)){
				return true;
			}
		}
		return false;
	}

}
