package com.miicaa.utils.fileselect;

import java.util.ArrayList;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.miicaa.home.R;

@EActivity(R.layout.android_test)
public class TestFileActivity extends Activity{
	@ViewById(R.id.btn)
	Button btn;
	@ViewById(R.id.textView)
	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Click(R.id.btn)
	void click(){
		FileListActivity_.intent(TestFileActivity.this)
		.startForResult(1);
	}
	
	@OnActivityResult(1)
	void activityresult(int resultcode,Intent data){
		if(resultcode == RESULT_OK){
			@SuppressWarnings("unchecked")
			ArrayList<MyFileItem> items = (ArrayList<MyFileItem>) data.getSerializableExtra("data");
			textView.setText("name :"+items.get(0).name+"path :"+items.get(0).path);
		}
	}

	
}
