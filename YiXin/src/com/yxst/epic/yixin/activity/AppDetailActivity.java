package com.yxst.epic.yixin.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.miicaa.home.R;

@EActivity(R.layout.activity_app_detail)
public class AppDetailActivity extends ActionBarActivity {

	@Extra
	String userName;
	
	@Extra
	String displayName;
	
	@Extra
	String nickName;
	
	@ViewById
	TextView tvDisplayName;

	@ViewById
	TextView tvNickName;

	@AfterViews
	void afterViews() {
		final ActionBar bar = getSupportActionBar();
		bar.setTitle("应用详情");

		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
		bar.setDisplayOptions(change, flags);
		
		tvDisplayName.setText(displayName);
		tvNickName.setText(nickName);
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	public void onSendMsgBtnClick(View v) {
		// ChatActivity.actionChat(this, FakeData.getChatList().get(2));
	}
}
