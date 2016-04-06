package com.miicaa.detail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;

import android.view.View;
import android.widget.ListView;

@EActivity(R.layout.n_list_view)
public class ApprovalProcessListActivity extends MainActionBarActivity{
	
	@ViewById(android.R.id.list)
	ListView list;
	
	ApprovalProcessAdapter mProcessAdapter;
	
	
	
	@AfterViews
	void afterView(){
		mProcessAdapter = new ApprovalProcessAdapter(this);
		list.setAdapter(mProcessAdapter);
	}

	@Override
	public String showBackButtonStr() {
		return null;
	}

	@Override
	public Boolean showBackButton() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void backButtonClick(View v) {
		
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
	
	

}
