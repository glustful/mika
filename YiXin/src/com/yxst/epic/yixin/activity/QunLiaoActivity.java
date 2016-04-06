package com.yxst.epic.yixin.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.miicaa.home.R;

@EActivity(R.layout.activity_qunliao)
public class QunLiaoActivity extends ActionBarActivity {

	@AfterViews
	void afterViews() {
		ActionBar bar = getSupportActionBar();
		
		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
		bar.setDisplayOptions(change, flags);
		
		bar.setTitle("群聊");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_qunliao, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			ContactSelectActivity_.intent(this).start();
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
