package com.yxst.epic.yixin.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.miicaa.home.R;
import com.yxst.epic.yixin.adapter.ChatImagesAdapter;
import com.yxst.epic.yixin.loader.ChatImagesLoader;
import com.yxst.epic.yixin.view.HackyViewPager;

@EActivity(R.layout.activity_chat_images)
public class ChatImagesActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	protected static final String TAG = "ChatImagesActivity";
	
	@Extra
	String localUserName;

	@Extra
	String remoteUserName;

	@Extra
	String remoteDisplayName;
	
	@Extra
	Long currentMsgId;
	
	@ViewById(R.id.view_pager)
	HackyViewPager mViewPager;
	
	@Bean
	ChatImagesAdapter mChatImagesAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i(TAG, "message id: " + currentMsgId);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		getSupportLoaderManager().destroyLoader(3);
	}
	
	@AfterViews
	void afterViews() {
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setAdapter(mChatImagesAdapter);
		getSupportLoaderManager().initLoader(3, null, this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new ChatImagesLoader(ChatImagesActivity.this, localUserName,
				remoteUserName, currentMsgId);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		mChatImagesAdapter.setCursor(cursor);
		mChatImagesAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(((ChatImagesLoader)arg0).getPosition(), false);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
	}
	
}
