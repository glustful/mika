package com.miicaa.detail;

import java.util.HashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.org.StyleDialog;
import com.miicaa.utils.AllUtils;

@EActivity(R.layout.continue_progress_activity)
public class ContinueProgressActivity extends Activity {
	
	static String TAG = "ContinueProgressActivity";
	
@ViewById(R.id.headView)
RelativeLayout head;
@ViewById(R.id.cEdit)
EditText contentEdit;
@Extra
String dataId;
@Extra
String userCode;
@Extra
String userName;

StyleDialog dialog;

	@AfterViews
	void afterViews(){
	Log.d(TAG, "head head head" + (head != null ?head.toString():"NULLLLLL"));
	Button backBtn = (Button)head.findViewById(R.id.cancleButton);
	backBtn.setVisibility(View.VISIBLE);
	TextView title = (TextView)head.findViewById(R.id.headTitle);
	Button commitBtn = (Button)head.findViewById(R.id.commitButton);
	title.setText("继续执行");
	commitBtn.setText("提交");
	backBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	});
	commitBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String content = contentEdit.getText().toString();
			if("".equals(content.trim())){
				Toast.makeText(ContinueProgressActivity.this, "请填写继续执行原因", Toast.LENGTH_SHORT).show();
				return;
			}
			dialog.show();
			continueProgress(dataId, content, userCode, userName);
		}
	});
}

@AfterViews
void afterView(){
	dialog = (StyleDialog) AllUtils.getMaualStyleDialog(this);
}



void continueProgress(String dataId,String content,
		String userCode,String userName){
	String url = "/home/phone/thing/continuearrange";
	HashMap<String, String> param = new HashMap<String, String>();
	param.put("dataId", dataId);
	param.put("content", content);
	param.put("userCode", userCode);
    param.put("userName", userName);
    Log.d(TAG, "dataId:"+dataId+"\n"+"content:"+content+"\n"+
    "userCode:"+userCode+"\n"+"userName:"+userName+"\n");
    
    new RequestAdpater(){

		@Override
		public void onReponse(ResponseData data) {
			// TODO Auto-generated method stub
			if(data.getResultState() == ResultState.eSuccess){
				setResult(100);
				finish();
			}else{
				Toast.makeText(ContinueProgressActivity.this, "继续执行失败，请检查网络连接."+data.getMsg(), Toast.LENGTH_SHORT).show();
				
			}
			dialog.dismiss();
			
		}

		@Override
		public void onProgress(ProgressMessage msg) {
			// TODO Auto-generated method stub
			
		}
    	
    }.setUrl(url)
    .addParam(param)
    .notifyRequest();
}
	
	
}
