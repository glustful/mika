package com.miicaa.home.ui.matter.approveprocess;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import com.aps.m;
import com.miicaa.base.request.MRequest;
import com.miicaa.base.request.response.BaseResopnseData;
import com.miicaa.common.base.Tools;
import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.ui.discover.dailywork.BaseReportType;
import com.miicaa.home.ui.matter.approveprocess.FixProcessPersonGroup;
import com.miicaa.home.ui.menu.SelectPersonInfo;
import com.miicaa.utils.AllUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.activity_approve_process)
public class ApprovalProcessListActivity extends MainActionBarActivity{
	
	private static String TAG = "ApprovalProcessListActivity";
	
	@Extra
	String dataType;
	@Extra
	String dataId;
	
	
	Toast mToast;
	MRequest mRequest;
	ProcessPersonReponseData mResponseData;
	ApprovalProcessAdapter mProcessAdapter;
	View topView;
	ImageView headImageView;
	ImageView topLineImageView;
	TextView  headContentTextView;
	TextView headNameTextView;
	
	
	
	@ViewById(android.R.id.list)
	ListView list;
	
	@AfterViews
	void afterView(){
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mProcessAdapter = new ApprovalProcessAdapter(this);
		list.setAdapter(mProcessAdapter);
	    topView = LayoutInflater.from(this).inflate(R.layout.process_list_headview, null);
		headContentTextView = (TextView)topView.findViewById(R.id.contentTextView);
		headImageView = (ImageView) topView.findViewById(R.id.headImageView);
		topLineImageView = (ImageView)topView.findViewById(R.id.topLine);
		headNameTextView = (TextView)topView.findViewById(R.id.headNameTextView);
		mRequest = new MRequest("/home/phone/thing/getflowmodel");
		if(dataType != null){
		mRequest.addParam("type", dataType);
		}else if(dataId != null){
		mRequest.addParam("dataId", dataId);
		}
		
		mResponseData = new ProcessPersonReponseData();
		mResponseData.setOnResponseListener(mResponseListener);
		mRequest.executeRequest(mResponseData);
	}


	BaseResopnseData.OnResponseListener mResponseListener = new BaseResopnseData.OnResponseListener() {
		
		@Override
		public void onResponseError(String errMsg, String errCode) {
			mToast.setText(errCode+":"+errMsg);
		}
		
		@Override
		public void onReponseComplete() {
			Tools.setHeadImg(mResponseData.updateCode, headImageView);
			headNameTextView.setText(mResponseData.updateName);
			String updateTime = AllUtils.getnormalTime(mResponseData.updateTime);
			headContentTextView.setText(mResponseData.updateName+" "+updateTime);
			mProcessAdapter.refresh(mResponseData.mProcessItemList);
			if(mResponseData.step > 0){
				topLineImageView.setImageDrawable(getResources().getDrawable(R.drawable.approve_process_top_did));
			}
			list.addHeaderView(topView);
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
		return "查看审批流程";
	}

	@Override
	public void titleButtonClick(View v) {
		
	}

	@Override
	public Boolean showRightButton() {
		return false;
	}

	@Override
	public String showRightButtonStr() {
		return null;
	}

	@Override
	public void rightButtonClick(View v) {
		
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
	
	class ProcessPersonReponseData extends BaseResopnseData{

		List<ApprovalProcessItem> mProcessItemList;
		String updateCode;
		String updateName;
		long updateTime;
		int step = -1;
		JSONArray dataArray;
		public ProcessPersonReponseData(){
			mProcessItemList = new ArrayList<ApprovalProcessItem>();
		}
		
		@Override
		public void saveData(Object data) {
			if(data instanceof JSONObject){
				Log.d(TAG, "saveData JSONobject data :"+(JSONObject)data);
			}else if(data instanceof JSONArray){
				Log.d(TAG, "saveData JsonArray data:"+(JSONArray)data);
				dataArray = (JSONArray)data;
			}
		}

		@Override
		public void parseData() {
			for(int i = 0 ; i < dataArray.length();i++){
				JSONObject json = dataArray.optJSONObject(i);
				if(updateCode == null)
					updateCode = json.optString("updateUser");
				if(updateTime != json.optLong("updateTime"))
					updateTime = json.optLong("updateTime");
				ApprovalProcessItem item = new ApprovalProcessItem();
				item.description = json.optString("description");
				item.numApprove = json.optString("title");
				item.number = json.optInt("step");
				item.currentStep = Integer.parseInt(json.optString("currentStep"));
				if(step < 0){
					step = item.currentStep;
				}
				JSONArray personArray = json.optJSONArray("participantUsers");
				if(personArray != null)
				for(int j = 0 ; j < personArray.length(); j++){
					JSONObject personJson = personArray.optJSONObject(j);
					String name = personJson.optString("name");
					String code = personJson.optString("code");
					SelectPersonInfo info = new SelectPersonInfo(name, code);
					item.personList.add(info);
				}
				mProcessItemList.add(item);
			}
			if(updateCode != null){
				updateName = UserInfo.findByCodeForUserName(updateCode);
			}
			
		}
		
	}
	

}
