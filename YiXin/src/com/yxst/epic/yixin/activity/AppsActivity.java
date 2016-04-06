package com.yxst.epic.yixin.activity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.miicaa.home.R;
import com.yxst.epic.yixin.adapter.AppListAdapter;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.request.BaseRequest;
import com.yxst.epic.yixin.data.dto.request.GetApplicationListRequest;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.GetApplicationListResponse;
import com.yxst.epic.yixin.data.rest.IMInterface;
import com.yxst.epic.yixin.rest.IMInterfaceProxy;
import com.yxst.epic.yixin.utils.CacheUtils;
import com.yxst.epic.yixin.view.ContactItemFooterView;
import com.yxst.epic.yixin.view.ContactItemFooterView_;

@EActivity(R.layout.activity_apps)
public class AppsActivity extends ActionBarActivity implements
		OnItemClickListener, RestErrorHandler {

	public static final String TAG = "AppsActivity";

	// private GridView mGridView;

	@Extra
	String localUserName;

	@ViewById(android.R.id.list)
	PinnedHeaderListView mListView;

	@Bean
	AppListAdapter mAppListAdapter;

	IMInterface mIMInterfaceProxy;

	private ContactItemFooterView mFooterView;

	@AfterInject
	void afterInject() {
		Log.d(TAG, "afterInject() localUserName:" + localUserName);
		
		mIMInterfaceProxy = IMInterfaceProxy.create();
		mIMInterfaceProxy.setRestErrorHandler(this);
	}

	@AfterViews
	void afterViews() {
		initFooterView();
		// mListView.setAdapter(new AppAdapter(this));
		mListView.setAdapter(mAppListAdapter);
		mListView.setOnItemClickListener(this);

		final ActionBar bar = getSupportActionBar();
		bar.setTitle("应用");

		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
		bar.setDisplayOptions(change, flags);

		doInBackground();
	}

	private void initFooterView() {
		ContactItemFooterView footer = ContactItemFooterView_.build(this);
		mListView.addFooterView(footer);

		mFooterView = footer;
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	@Background
	void doInBackground() {
		onPreExecute();

		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);

		GetApplicationListRequest request = new GetApplicationListRequest();
		request.BaseRequest = baseRequest;

		Log.d(TAG, "doInBackground() request:" + request);

		GetApplicationListResponse response = mIMInterfaceProxy
				.getApplicationList(request);

		onPostExecute(response);
	}

	@UiThread
	void onPreExecute() {
		mFooterView.show();
	}

	@UiThread
	void onPostExecute(GetApplicationListResponse response) {
		Log.d(TAG, "onPostExecute() response:" + response);

		mFooterView.hide();

		if (response == null) {
			return;
		}

		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}

		mAppListAdapter.changeData(response.memberList);
	}

	//
	// @Override
	// public void finish() {
	// super.finish();
	// overridePendingTransition(R.anim.back_left_in, R.anim.back_right_out);
	// }

//	private class AppAdapter extends BaseAdapter {
//
//		private Context context;
//
//		public AppAdapter(Context context) {
//			this.context = context;
//		}
//
//		@Override
//		public int getCount() {
//			return FakeData.APPNAMES.length;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			ContactItemView view;
//
//			if (convertView == null) {
//				view = ContactItemView_.build(context);
//			} else {
//				view = (ContactItemView) convertView;
//			}
//
//			view.mNameTV.setText(FakeData.APPNAMES[position]);
//			view.mIconIV.setImageResource(FakeData.PHOTO[position
//					% FakeData.PHOTO.length]);
//			view.mIconIV.setScaleType(ScaleType.CENTER);
//
//			view.setDividerVisible(position != 0);
//
//			return view;
//		}
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// AppDetailActivity_.intent(this).start();
		Member member = (Member) parent.getItemAtPosition(position);
		ChatActivity_.intent(this).localUserName(localUserName)
				.remoteUserName(member.UserName)
				.remoteDisplayName(member.NickName).start();
	}

	@UiThread
	@Override
	public void onRestClientExceptionThrown(RestClientException arg0) {
		// TODO Auto-generated method stub
		mFooterView.hide();
		Toast.makeText(this, "访问失败", Toast.LENGTH_SHORT).show();
	}

}
