package com.miicaa.home.ui.matter.approveprocess;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.view.View;
import android.widget.ExpandableListView;

import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;

@EActivity(R.layout.activity_approve_fix_create)
public class ApproveFixedCreateActivity extends MainActionBarActivity{

	@ViewById(R.id.listView)
	ExpandableListView ListView;
	
	
	@Override
	public String showBackButtonStr() {
		return null;
	}

	@Override
	public Boolean showBackButton() {
		return null;
	}

	@Override
	public void backButtonClick(View v) {
		
	}

	@Override
	public Boolean showTitleButton() {
		return null;
	}

	@Override
	public String showTitleButtonStr() {
		return null;
	}

	@Override
	public void titleButtonClick(View v) {
		
	}

	@Override
	public Boolean showRightButton() {
		return null;
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
		return null;
	}

}
