package com.yxst.epic.yixin.activity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.yxst.epic.yixin.adapter.ContactSearchAdapter;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.request.BaseRequest;
import com.yxst.epic.yixin.data.dto.request.SearchRequest;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.SearchResponse;
import com.yxst.epic.yixin.data.rest.IMInterface;
import com.yxst.epic.yixin.rest.IMInterfaceProxy;
import com.yxst.epic.yixin.utils.CacheUtils;
import com.yxst.epic.yixin.view.ContactItemFooterView;
import com.yxst.epic.yixin.view.ContactItemFooterView_;

@EActivity(R.layout.activity_contact_search)
public class ContactSearchActivity extends ActionBarActivity implements
		OnScrollListener, RestErrorHandler {

	private static final String TAG = "ContactSearchActivity";

	@ViewById
	EditText etSearch;

	@ViewById
	ImageButton btnSearch;

	@ViewById(android.R.id.list)
	ListView mListView;

	@Bean
	ContactSearchAdapter mAdapter;

	private ContactItemFooterView mFooterView;

	IMInterface mIMInterfaceProxy;

	@AfterInject
	void afterInject() {
		mIMInterfaceProxy = IMInterfaceProxy.create();
		mIMInterfaceProxy.setRestErrorHandler(this);
	}

	@AfterViews
	void afterViews() {
		final ActionBar bar = getSupportActionBar();
		bar.setTitle("搜索联系人");

		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
		bar.setDisplayOptions(change, flags);

		mListView.setOnScrollListener(this);

		ContactItemFooterView footerView = ContactItemFooterView_.build(this);
		mFooterView = footerView;

		setAdapter();
		mFooterView.hide();
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	private void setAdapter() {
		mListView.addFooterView(mFooterView);
		mListView.setAdapter(mAdapter);
	}

	private String searchKey;

	@Click(R.id.btnSearch)
	void onClickBtnSearch(View v) {
		searchKey = etSearch.getText().toString();
		if (!TextUtils.isEmpty(searchKey)) {
			mAdapter.clear();
			setAdapter();
			mFooterView.show();
			doInBackground(searchKey, 0);
		}
	}

	int lastVisibleItem = 0;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		Log.d(TAG, "onScrollStateChanged()");
		Log.d(TAG, "onScrollStateChanged() lastVisibleItem:" + lastVisibleItem);
		Log.d(TAG,
				"onScrollStateChanged() mAdapter.getCount():"
						+ mAdapter.getCount());
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleItem == mAdapter.getCount()) {
			doInBackground(searchKey, mAdapter.getCount());
		}
	}

	private int Count;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.d(TAG, "onScroll()");
		Log.d(TAG, "onScroll() totalItemCount:" + totalItemCount);
		Log.d(TAG, "onScroll() Count:" + Count);
		// 计算最后可见条目的索引
		lastVisibleItem = firstVisibleItem + visibleItemCount - 1;

		// 所有的条目已经和最大条数相等，则移除底部的View
		// if (totalItemCount == Count + 1) {
		// mListView.removeFooterView(mFooterView);
		// }
	}

	@Background
	void doInBackground(String searchKey, int offset) {
		onPreExecute();

		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		SearchRequest request = new SearchRequest();
		request.BaseRequest = baseRequest;
		// request.SearchKey = "";
		request.SearchKey = searchKey;
		request.Offset = offset;
		request.Limit = 20;
		request.SearchType = SearchRequest.SEARCH_TYPE_USER;

		Log.d(TAG, "doInBackground() request:" + request);

		SearchResponse response = mIMInterfaceProxy.search(request);

		onPostExecute(response);
	}

	@UiThread
	void onPreExecute() {
		mFooterView.setVisibility(View.VISIBLE);
	}

	@UiThread
	void onPostExecute(SearchResponse response) {
		Log.d(TAG, "onPostExecute() response:" + response);
		if (response == null) {
			return;
		}

		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}

		Count = response.Count;
		mAdapter.addMembers(response.MemberList);

		if (mAdapter.getCount() == Count) {
			mListView.removeFooterView(mFooterView);
		}

		if (Count > 0 && mAdapter.getCount() == Count) {
			mListView.removeFooterView(mFooterView);
			Toast.makeText(this, "数据全部加载完成，没有更多数据！", Toast.LENGTH_SHORT).show();
		}
	}

	@UiThread
	@Override
	public void onRestClientExceptionThrown(RestClientException e) {
		Toast.makeText(this, "访问失败", Toast.LENGTH_SHORT).show();
		if (mAdapter.getCount() == 0) {
			mListView.removeFooterView(mFooterView);
		}
	}

	@ItemClick(android.R.id.list)
	void onItemClick(Member member) {
		ContactDetailActivity_.intent(this).member(member)
				.userName(member.UserName).start();
	}
}
