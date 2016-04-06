package com.miicaa.base.share.industry;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.miicaa.home.HiddenSoftActivity;
import com.miicaa.home.R;
import com.miicaa.home.ui.pay.PayUtils;

@EActivity(R.layout.pinned_activity_main)
public class IndustryActivity extends HiddenSoftActivity {

	@Bean
	IndustryAdapter sectionedAdapter;
	@ViewById(R.id.pinnedListView)
	PinnedHeaderListView listView;
	@ViewById(R.id.pay_headTitle)
	TextView title;
	@ViewById(R.id.pay_commitButton)
	Button commit;
	
	@Click(R.id.pay_cancleButton)
	void cancel(){
		this.finish();
	}
	
	@Click(R.id.pay_commitButton)
	void commit(){
		JSONObject obj = sectionedAdapter.getSelected();
		if(obj==null){
			PayUtils.showToast(this, "请选择行业类别后再提交", 3000);
			return;
		}
		Intent data = new Intent();
		data.putExtra("data", obj.toString());
		setResult(Activity.RESULT_OK, data);
		finish();
	}

	@AfterViews
	void initUI() {
		title.setText("选择行业");
		commit.setText("提交");
		commit.setVisibility(View.VISIBLE);
		listView.setAdapter(sectionedAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		initData();
	}

	@Background
	void initData() {
		try {
			JSONArray classes = new JSONArray(IndustryMain.industryClass);
			JSONObject industry = new JSONObject(IndustryMain.secondJson);
			sectionedAdapter.refresh(classes,industry);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
