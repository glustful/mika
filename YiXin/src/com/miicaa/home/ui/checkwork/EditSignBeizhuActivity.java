package com.miicaa.home.ui.checkwork;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.utils.AllUtils;

@EActivity(R.layout.activity_checkwork_beizhu)
public class EditSignBeizhuActivity extends MainActionBarActivity{

	static String TAG = "EditSignBeizhuActivity";
	Button backButton;
	static int zishu = 140;
	
	public final static String SIGNIN = "SignIn";
	public final static String SIGNOUT = "SignOut";
	
	@Extra
	String titleText;
	@Extra
	String signInOrSignOutStr;
	@Extra
	String mId;
	@Extra
	String huixian;
	
	ProgressDialog progressDialog;
	
	@ViewById(R.id.editText)
	EditText editText;
	@ViewById
	TextView zishuTextView;
	
	@Override
	public String showBackButtonStr() {
		return "取消";
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
		return titleText;
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
		return "提交";
	}

	@Override
	public void rightButtonClick(View v) {
		AllUtils.hiddenSoftBorad(EditSignBeizhuActivity.this);
		progressDialog.show();
		setRightButtonClickable(false);
		final String remarks = editText.getText().toString();
		requestEditBeizhu(signInOrSignOutStr, remarks,mId,new OnResponseListener() {

			@Override
			public void onResponse(ResponseData data) {
				setRightButtonClickable(true);
				progressDialog.dismiss();
//				Log.d(TAG, "onResponse :"+data.getCode() +data.getMsg()+data.getJsonObject()+data.getJsonArray());
				if(data.getResultState() == ResultState.eSuccess){
					Intent i = new Intent();
					i.putExtra("remark", remarks);
					setResult(RESULT_OK, i);
					finish();
				}
			}
			
		});
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
	
	
	@AfterViews
	void afterViews(){
		InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(zishu);
	    editText.setFilters(new InputFilter[]{lengthFilter});
		backButton = (Button)getLeftButton();
		progressDialog = AllUtils.getNormalMiicaaDialog(EditSignBeizhuActivity.this);
		progressDialog.setMessage("正在提交备注信息");
		MyCheckWorkAdapter.setTextDrawableNull(backButton);
		editText.setText(huixian != null ? huixian :"");
	}
	@TextChange(R.id.editText)
	void onTextChangesOnEditText(CharSequence text, TextView hello, int before, int start, int count) {
	    // Something Here
		String content = String.valueOf(editText.getText().toString().length()) + "/140";
		zishuTextView.setText(content);
	 }
	
//	String remark = "";
	public static void requestEditBeizhu(String signInOrSignOutStr,String remarks,String id,
			final OnResponseListener listener_){
		Log.d(TAG, "requestEditBeizhu:"+"action:"+signInOrSignOutStr+"\n remarks:"+
				remarks + "\n id:"+id);
		String url = "/attendance/phone/attend/setRemarks";
		new RequestAdpater() {
			private static final long serialVersionUID = -5969321027287111963L;

			@Override
			public void onReponse(ResponseData data) {
				if(listener_ != null){
					listener_.onResponse(data);
				}
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("id",id)
		.addParam("remarks",remarks)
		.addParam("action",signInOrSignOutStr)
		.notifyRequest();
	}
	
	public interface OnResponseListener{
		void onResponse(ResponseData data);
	}

}
