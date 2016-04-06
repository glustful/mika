package com.yxst.epic.yixin.fragment;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.afinal.simplecache.ACache;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.yxst.epic.yixin.activity.AppsActivity_;
import com.yxst.epic.yixin.activity.ContactDetailActivity_;
import com.yxst.epic.yixin.activity.ContactSearchActivity_;
import com.yxst.epic.yixin.activity.ContactSubListActivity_;
import com.yxst.epic.yixin.adapter.ContactListAdapter;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.request.BaseRequest;
import com.yxst.epic.yixin.data.dto.request.GetOrgInfoRequest;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.GetOrgInfoResponse;
import com.yxst.epic.yixin.data.rest.IMInterface;
import com.yxst.epic.yixin.listener.OnMemberCheckedChangedListener;
import com.yxst.epic.yixin.listener.OnMemberClickListener;
import com.yxst.epic.yixin.rest.IMInterfaceProxy;
import com.yxst.epic.yixin.utils.CacheUtils;
import com.yxst.epic.yixin.utils.Utils;
import com.yxst.epic.yixin.view.ContactItemFooterView;
import com.yxst.epic.yixin.view.ContactItemFooterView_;
import com.yxst.epic.yixin.view.ContactItemView;
import com.yxst.epic.yixin.view.ContactItemView_;
import com.yxst.epic.yixin.view.ListHeaderContactSearch;
import com.yxst.epic.yixin.view.ListHeaderContactSearch_;

@EFragment(R.layout.fragment_contact_list)
public class ContactListFragment extends Fragment implements IMainFragment, RestErrorHandler {

	private static final String TAG = "ContactListFragment";

	@FragmentArg
	String localUserName;
	
	@FragmentArg
	boolean isSelectMode;

//	@FragmentArg("selectMembers")
	List<Member> selectMembers;

	// @RestService
	// IMInterface mRestClient;

	IMInterface mIMInterfaceProxy;

	// @Bean
	// MyErrorHandler myErrorHandler;

	@ViewById(android.R.id.list)
	PinnedHeaderListView mListView;

	@Bean
	ContactListAdapter mContactListAdapter;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onActivityCreated()");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume()");
//		getOrgInfoDoInBackground();

		super.onResume();
	}

	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "onAttach()");
		super.onAttach(activity);
	}

	@Override
	public void onStart() {
		Log.d(TAG, "onStart()");
		super.onStart();
	}

	@AfterInject
	void afterInject() {
		Log.d(TAG, "afterInject() localUserName:" + localUserName);
		
		mIMInterfaceProxy = IMInterfaceProxy.create();
		mIMInterfaceProxy.setRestErrorHandler(this);

		// changeData();
	}

	private void changeData(GetOrgInfoResponse response) {
		LinkedHashMap<String, List<Member>> map = new LinkedHashMap<String, List<Member>>();

		map.put("公共通讯录", null);
		map.put("我的常联系人", null);

		if (response != null
				&& response.BaseResponse.Ret == BaseResponse.RET_SUCCESS) {
			List<Member> list = Utils.listFindPath(
					response.OgnizationMemberList, response.UserOgnization);
			Collections.reverse(list);

			map.put("公共通讯录", list);

			map.put("我的常联系人", response.StarMemberList);
		}

		// map.put("公共通讯录", new ArrayList<Member>());
		// map.put("我的常联系人", new ArrayList<Member>());

		mContactListAdapter.setOnMemberCheckedChangedListener(null);
		mContactListAdapter.changeData(map);
		mContactListAdapter.setOnMemberCheckedChangedListener(mOnMemberCheckedChangedListener);
	}

	@AfterViews
	void afterViews() {
		Log.d(TAG, "afterViews()");

		mContactListAdapter.setSelectMembers(selectMembers);
		mContactListAdapter.setLockMembers(lockMembers);
		mContactListAdapter.setIsSelectMode(isSelectMode);
		mContactListAdapter.setOnMemberCheckedChangedListener(mOnMemberCheckedChangedListener);

		// mListView.setMode(Mode.DISABLED);

		mListView.setPinHeaders(false);
		// mListView.getRefreshableView().setPinHeaders(false);
		mListView.setOnItemClickListener(mOnItemClickListener);

		setAdapter(isSelectMode);

		GetOrgInfoResponse response = (GetOrgInfoResponse) ACache.get(
				getActivity()).getAsObject("getOrgInfo");

		changeData(response);

		getOrgInfoDoInBackground();
		// if (isSelectMode) {
		// doInBackground();
		// }
	}

	private void setAdapter(boolean isSelectMode) {
		if (!isSelectMode) {
			initHeaderView();
		}
		initFooterView();

		mListView.setAdapter(mContactListAdapter);
	}

	private ContactItemFooterView mFooterView;

	private void initHeaderView() {
		ListHeaderContactSearch headerView1 = ListHeaderContactSearch_.build(getActivity());
		mListView.addHeaderView(headerView1);
		
		ContactItemView item2 = ContactItemView_.build(getActivity());
		item2.bind(R.drawable.addfriend_icon_qrscan, R.string.addfriend_app);
//		item2.setDividerVisible(false);
		mListView.addHeaderView(item2);
		// mListView.getRefreshableView().addHeaderView(item2);
	}

	private void initFooterView() {
		ContactItemFooterView footer = ContactItemFooterView_
				.build(getActivity());
		mListView.addFooterView(footer);

		mFooterView = footer;
	}

	@Background(id="getOrgInfo")
	void getOrgInfoDoInBackground() {
		onPreExecute();

		BaseRequest baseRequest = CacheUtils.getBaseRequest(getActivity());

		GetOrgInfoRequest request = new GetOrgInfoRequest();
		request.BaseRequest = baseRequest;

		Log.d(TAG, "doInBackground() request:" + request);

		// GetOrgInfoResponse response = mRestClient.getOrgInfo(request);
		GetOrgInfoResponse response = mIMInterfaceProxy.getOrgInfo(request);

		onPostExecute(response);
	}

	@UiThread
	void onPreExecute() {
		Log.d(TAG, "onPreExecute()");
		mFooterView.show();
	}

	@UiThread
	void onPostExecute(GetOrgInfoResponse response) {
		Log.d(TAG, "onPostExecute()");
		Log.d(TAG, "onPostExecute() response:" + response);
		// Utils.longLogD(TAG, "response:" + response);
		
		mFooterView.hide();
		
		if (isCanceledGetOrgInfo || getActivity() == null) {
			return;
		}
		
		if (response == null) {
			return;
		}

		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(getActivity(), response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}

		ACache.get(getActivity()).put("getOrgInfo", response);
		changeData(response);
	}

	private PinnedHeaderListView.OnItemClickListener mOnItemClickListener = new PinnedHeaderListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int section, int position, long id) {
			Log.d(TAG, "onItemClick() section:" + section);
			Log.d(TAG, "onItemClick() position:" + position);

			ContactListAdapter adapter = mContactListAdapter;

			Member member = (Member) adapter.getItem(section, position);

			if (!isSelectMode) {
				if (member.isTypeOrg() || member.isTypeTenant()) {
					ContactSubListActivity_.intent(getActivity())
							.member(member).start();
				} else if (member.isTypeUser()) {
					ContactDetailActivity_
							.intent(getActivity())
							.userName(member.UserName)
							.member((Member) adapter.getItem(section, position))
							.start();
				}
			} else {
				if (mOnMemberClickListener != null) {
					mOnMemberClickListener.onMemberClick(member);
				}
			}
		}

		@Override
		public void onSectionClick(AdapterView<?> adapterView, View view,
				int section, long id) {
			Log.d(TAG, "onSectionClick() section:" + section);
		}

		@Override
		public void onHeaderViewClick(AdapterView<?> adapterView, View view,
				int position) {
			Log.d(TAG, "onHeaderViewClick() position:" + position);
			if (position == 0) {
				ContactSearchActivity_.intent(getActivity()).start();
			} else if (position == 1) {
				AppsActivity_.intent(getActivity()).localUserName(localUserName).start();
			}
		}

		@Override
		public void onFooterViewClick(AdapterView<?> adapterView, View view,
				int position) {
			Log.d(TAG, "onFooterViewClick() position:" + position);
		}

	};

	// Listener

	private OnMemberClickListener mOnMemberClickListener;

	public void setOnMemberClickListener(OnMemberClickListener l) {
		mOnMemberClickListener = l;
	}

	private OnMemberCheckedChangedListener mOnMemberCheckedChangedListener;

	public void setOnMemberCheckedChangedListener(
			OnMemberCheckedChangedListener l) {
		mOnMemberCheckedChangedListener = l;
		if (mContactListAdapter != null) {
			mContactListAdapter.setOnMemberCheckedChangedListener(l);
		}
	}

	@Override
	public void onTagChanged() {
		Log.d(TAG, "onTagChanged()");
		getOrgInfoDoInBackground();
	}

	@UiThread
	@Override
	public void onRestClientExceptionThrown(RestClientException e) {
		mFooterView.hide();
		if (isCanceledGetOrgInfo || getActivity() == null) {
			return;
		}
		Toast.makeText(getActivity(), "访问失败", Toast.LENGTH_SHORT).show();
	}

	public void setSelectMembers(List<Member> members) {
		this.selectMembers = members;
		if (mContactListAdapter != null) {
			mContactListAdapter.setSelectMembers(members);
		}
	}
	
	private List<Member> lockMembers;
	
	public void setLockMembers(List<Member> lockMembers) {
		this.lockMembers = lockMembers;
		if (mContactListAdapter != null) {
			mContactListAdapter.setLockMembers(lockMembers);
		}
	}

	private boolean isCanceledGetOrgInfo = false;
	
	@Override
	public void onDestroy() {
		BackgroundExecutor.cancelAll("getOrgInfo", true);
		isCanceledGetOrgInfo = true;
		
		super.onDestroy();
	}

	public void notifyDataSetChanged() {
		mContactListAdapter.notifyDataSetChanged();
	}
}
