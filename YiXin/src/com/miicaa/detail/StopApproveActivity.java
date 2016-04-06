package com.miicaa.detail;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import android.app.ProgressDialog;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.base.request.MRequest;
import com.miicaa.base.request.response.BaseResopnseData;
import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;
import com.miicaa.utils.AllUtils;

@EActivity(R.layout.activity_checkwork_beizhu)
public class StopApproveActivity extends MainActionBarActivity{

	private static int zishu = 140;
	
	@ViewById(R.id.editText)
	EditText editText;
	@ViewById(R.id.zishuTextView)
	TextView zishuTextView;
	
	@Extra
	String dataId;
	
	BaseResopnseData mResponseData;
	MRequest mRequest;
	ProgressDialog mProgrssDialog;
	Toast mToast;
	
	@AfterViews
	void afterView(){
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mProgrssDialog = AllUtils.getNormalMiicaaDialog(this);
		mProgrssDialog.setMessage("正在执行，请稍后");
		mResponseData = new StopApproveResponseData();
		mResponseData.setOnResponseListener(mOnResponseListener);
		mRequest = new MRequest("/home/phone/thing/stopapprove");
		mRequest.addParam("dataId", dataId);
		InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(zishu);
	    editText.setFilters(new InputFilter[]{lengthFilter});
	}
	
	@TextChange(R.id.editText)
	void onTextChangesOnEditText(CharSequence text, TextView hello, int before, int start, int count) {
		String content = String.valueOf(editText.getText().toString().length()) + "/"+zishu;
		zishuTextView.setText(content);
	 }
	
	
	BaseResopnseData.OnResponseListener  mOnResponseListener = new BaseResopnseData.OnResponseListener() {
		
		@Override
		public void onResponseError(String errMsg, String errCode) {
			mProgrssDialog.dismiss();
			mToast.setText(errCode+":"+errMsg);
			mToast.show();
		}
		
		@Override
		public void onReponseComplete() {
			mProgrssDialog.dismiss();
			mToast.setText("成功终止！");
			mToast.show();
			setResult(RESULT_OK);
			finish();
		}
		
		@Override
		public void onProgress(float count) {
			
		}
		
		@Override
		public void onPreExecute() {
			mProgrssDialog.show();
		}
		
		@Override
		public void onNoneData() {
			mProgrssDialog.dismiss();
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
		return "终止审批";
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
		String reason = editText.getText().toString().trim();
		mRequest.addParam("reason", reason);
		mRequest.executeRequest(mResponseData);
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
	
	class StopApproveResponseData extends BaseResopnseData{

		@Override
		public void saveData(Object data) {
			
		}

		@Override
		public void parseData() {
			
		}
		}

	@Override
	protected void activityYMove() {
		super.activityYMove();
		AllUtils.hiddenSoftBorad(this);
	}
	
	

}
