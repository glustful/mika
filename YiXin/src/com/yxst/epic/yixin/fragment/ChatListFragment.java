package com.yxst.epic.yixin.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.miicaa.home.R;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.activity.ChatActivity_;
import com.yxst.epic.yixin.adapter.ChatListAdapter;
import com.yxst.epic.yixin.adapter.ChatListAdapter.OnNotZeroListener;
import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.loader.ChatListLoader;
import com.yxst.epic.yixin.preference.CachePrefs_;

@EFragment(R.layout.fragment_chat_list)
public class ChatListFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor>,IMainFragment {

	private static final String TAG = "ChatListFragment";
	public static ChatListFragment instance;
	
	@FragmentArg
	String localUserName;

	@ViewById(android.R.id.list)
	ListView mListView;
	
	@ViewById(R.id.tishi)
	TextView tishi;
	
	@ViewById(R.id.fram_talk_add)
	ImageButton addQunButton;

	@Bean
	ChatListAdapter mChatListAdapter;
	
	@Pref
	CachePrefs_ mCachePrefs;

	@org.androidannotations.annotations.AfterInject
	void AfterInject(){
		Log.e("TAGTAG", MyApplication.getInstance().getLocalUserName().toString());
	instance = this;
	}
	
    public static ChatListFragment getInstance(){
		
		return instance;
	}
	
	@AfterViews
	void afterViews() {
		registerForContextMenu(mListView);
		 mChatListAdapter.setOnNotZeroListener(new OnNotZeroListener() {
				
				@Override
				public void notZero(Boolean isZero) {
					Log.d(TAG, "notZero isZero"+isZero);
						tishi.setVisibility(isZero ? View.VISIBLE :View.GONE);
				}
			});
	}
	
	
	
	
	@Override
	public void onResume() {
		localUserName = mCachePrefs.userName().get();
		Log.d(TAG, "localUserName:"+localUserName);
		if(getLoaderManager().getLoader(0) != null && getLoaderManager().getLoader(0).isStarted()){
			getLoaderManager().restartLoader(0, null, this);
		}else{
			getLoaderManager().initLoader(0, null, this);
		}
		mListView.setAdapter(mChatListAdapter);
		super.onResume();
	}

	public void refresh(){
		localUserName = mCachePrefs.userName().get();
		getLoaderManager().restartLoader(0, null, this);
       
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new ChatListLoader(getActivity(), localUserName);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG, "DatabaseUtils.dumpCursorToString(data):" + DatabaseUtils.dumpCursorToString(data));
		mChatListAdapter.changeCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mChatListAdapter.changeCursor(null);
	}

	@ItemClick(android.R.id.list)
	void onItemClick(Cursor cursor) {
		Message message = mChatListAdapter.readEntity(cursor);
		Log.d(TAG, "message.getExtRemoteUserName()"+message.getExtRemoteUserName());
		ChatActivity_.intent(getActivity())
				.localUserName(message.getExtLocalUserName())
				.remoteUserName(message.getExtRemoteUserName())
				.remoteDisplayName(message.getExtRemoteDisplayName()).start();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.activity_chat_list_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Message message = mChatListAdapter.readEntity((Cursor) mChatListAdapter
				.getItem(menuInfo.position));

		if (R.id.delete_chat == item.getItemId()) {
			String remoteUserName = message.getExtRemoteUserName();
			DBManager.getInstance(getActivity()).deleteChat(localUserName, remoteUserName);
			return true;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onTagChanged() {
		Log.d(TAG, "onTagChanged()");
	};
	
	
	Boolean isZero(Cursor data){
		return data == null || data.getCount() == 0;
	}
}
