package com.miicaa.home.ui.report;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miicaa.home.R;

@EActivity(R.layout.report_check_of_range)
public class CheckOfRange extends Activity {

	@Extra
	String type;
	
	@ViewById(R.id.pay_headTitle)
	TextView title;
	@ViewById(R.id.pay_commitButton)
	Button commit;
	@ViewById(R.id.title)
	TextView content;
	
	@Click(R.id.pay_cancleButton)
	void cancel(){
		finish();
	}
	
	@AfterViews
	void initUI(){
		commit.setVisibility(View.INVISIBLE);
		title.setText("查看范围");
		if(type.equals("0")){
			content.setText("公开");
		}else if(type.equals("1")){
			content.setText("仅点评人可见");
		}
	}
}
