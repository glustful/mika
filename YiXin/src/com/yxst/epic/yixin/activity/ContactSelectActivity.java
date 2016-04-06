package com.yxst.epic.yixin.activity;

import java.io.Serializable;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.miicaa.home.R;
import com.yxst.epic.yixin.adapter.ContactSelectAdapter;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.request.BaseRequest;
import com.yxst.epic.yixin.data.dto.request.CreateQunRequest;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.CreateQunResponse;
import com.yxst.epic.yixin.data.rest.IMInterface;
import com.yxst.epic.yixin.fragment.ContactListFragment;
import com.yxst.epic.yixin.fragment.ContactListFragment_;
import com.yxst.epic.yixin.fragment.ContactSubListFragment;
import com.yxst.epic.yixin.fragment.ContactSubListFragment_;
import com.yxst.epic.yixin.listener.OnMemberCheckedChangedListener;
import com.yxst.epic.yixin.listener.OnMemberClickListener;
import com.yxst.epic.yixin.listener.OnMemberDeleteListener;
import com.yxst.epic.yixin.rest.IMInterfaceProxy;
import com.yxst.epic.yixin.utils.CacheUtils;
import com.yxst.epic.yixin.utils.Utils;

@EActivity(R.layout.activity_contact_select)
public class ContactSelectActivity extends ActionBarActivity implements
		OnMemberClickListener, OnMemberCheckedChangedListener, RestErrorHandler, OnMemberDeleteListener {

	private static final String TAG = "SelectContactActivity";

	@Extra
	boolean isPickMode;

	@Extra
	List<Member> lockMembers;
	
	@Extra
	String localUserName;

	@ViewById(android.R.id.list)
	HorizontalListView mHorizontalListView;

	@ViewById
	TextView tvTip;

//	@FragmentById(R.id.fragment_contact_select_top)
//	ContactSelectTopFragment mContactSelectTopFragment;
	
	@ViewById(R.id.layout_content)
	FrameLayout mContentLayout;

	@Bean
	ContactSelectAdapter mContactSelectAdapter;

	@SystemService
	LayoutInflater mLayoutInflater;

	Button mCustomView;

	IMInterface mRestClient;

	// @Bean
	// MyErrorHandler myErrorHandler;

	// @InstanceState
	// int mStackLevel = 1;

	@AfterInject
	void afterInject() {
		mRestClient = IMInterfaceProxy.create();
		mRestClient.setRestErrorHandler(this);
	}

	@AfterViews
	void afterViews() {
		initActionBar();

		mContactSelectAdapter.setOnMemberDeleteListener(this);
		mHorizontalListView.setAdapter(mContactSelectAdapter);

		Log.d(TAG, "afterViews()");
		ContactListFragment fragment = ContactListFragment_.builder()
				.localUserName(localUserName).isSelectMode(true).build();
		fragment.setSelectMembers(mContactSelectAdapter.getMembers());
		fragment.setLockMembers(lockMembers);
		fragment.setOnMemberClickListener(this);
		fragment.setOnMemberCheckedChangedListener(this);
		addFragment(fragment);
	}

	private void initActionBar() {
		mCustomView = (Button) mLayoutInflater.inflate(
				R.layout.abc_view_contact_select, null);
		mCustomView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isPickMode) {
					Intent data = new Intent();
					data.putExtra("members",
							(Serializable) mContactSelectAdapter.getMembers());
					setResult(RESULT_OK, data);
					finish();
				} else {
					doInBackground(mContactSelectAdapter.getMembers());
				}
			}
		});

		mCustomView.setEnabled(false);
		mCustomView.setText("确定(0)");

		final ActionBar bar = getSupportActionBar();

		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		flags = flags ^ ActionBar.DISPLAY_SHOW_CUSTOM;
		int change = bar.getDisplayOptions() ^ flags;
		bar.setDisplayOptions(change, flags);

		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = lp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.RIGHT;
		bar.setCustomView(mCustomView, lp);
	}

	@Override
	public boolean onSupportNavigateUp() {
		if (!popBackStack()) {
			finish();
		}
		// return super.onSupportNavigateUp();
		return true;
	}

	private void addFragment(Fragment newFragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.layout_content, newFragment).commit();
	}

	private void addFragmentToStack(Fragment newFragment) {
		// Add the fragment to the activity, pushing this transaction
		// on to the back stack.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.layout_content, newFragment, null);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
	}

	private boolean popBackStack() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
			return true;
			// return fm.popBackStackImmediate();
		}
		return false;
	}

	@Override
	public void onMemberClick(Member member) {
		if (member.isTypeOrg() || member.isTypeTenant()) {
			ContactSubListFragment fragment = ContactSubListFragment_.builder()
					.isSelectMode(true).member(member).build();
			fragment.setSelectMembers(mContactSelectAdapter.getMembers());
			fragment.setLockMembers(lockMembers);
			fragment.setOnMemberClickListener(this);
			fragment.setOnMemberCheckedChangedListener(this);
			addFragmentToStack(fragment);
		} else if (member.isTypeUser()) {
			ContactDetailActivity_.intent(this).userName(member.UserName)
					.member(member).start();
		}
	}

	@Override
	public void onMemberCheckedChanged(Member member, boolean isChecked) {
		
		if (isChecked) {
			mContactSelectAdapter.addMember(member);
		} else {
			mContactSelectAdapter.removeMember(member);
		}
		
		updateBtnSubmit();
		updateFragment();
	}

	@Override
	public void onMemberDelete(Member member) {
		mContactSelectAdapter.removeMember(member);
		
		updateBtnSubmit();
		updateFragment();
	}
	
	private void updateFragment() {
		FragmentManager fm = getSupportFragmentManager();
		Fragment f = fm.findFragmentById(R.id.layout_content);
		
		if (f instanceof ContactListFragment) {
			((ContactListFragment) f).notifyDataSetChanged();
		} else if (f instanceof ContactSubListFragment) {
			((ContactSubListFragment) f).notifyDataSetChanged();
		}
	}
	
	private void updateBtnSubmit() {
		int size = mContactSelectAdapter.getMembers().size();
		mCustomView.setEnabled(size != 0);
		mCustomView.setText("确定(" + size + ")");

		tvTip.setVisibility(size == 0 ? View.VISIBLE : View.INVISIBLE);
	}
	
	@Background
	void doInBackground(List<Member> list) {
		onPreExecute();

		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);

		CreateQunRequest request = new CreateQunRequest();
		request.BaseRequest = baseRequest;
		request.Topic = "我的群";
		request.MemberCount = list.size();
		request.MemberList = list;

		Log.d(TAG, "doInBackground() request:" + request);
		Log.d(TAG,
				"doInBackground() request:" + Utils.writeValueAsString(request));

		CreateQunResponse response = mRestClient.createQun(request);

		onPostExecute(response);
	}

	private ProgressDialog mProgressDialog;

	@UiThread
	void onPreExecute() {
		mProgressDialog = ProgressDialog.show(this, "提示", "正在加载...");
	}

	@UiThread
	void onPostExecute(CreateQunResponse response) {
		Log.d(TAG, "onPostExecute() response:" + response);
		mProgressDialog.dismiss();
		if (response == null) {
			return;
		}

		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}

		ChatActivity_.intent(this).localUserName(localUserName)
				.remoteUserName(response.ChatRoomName)
				.remoteDisplayName(response.Topic).start();

		finish();
	}

	@Override
	public void onRestClientExceptionThrown(RestClientException e) {
		onRestClientExceptionThrownUI(e);
	}

	@UiThread
	void onRestClientExceptionThrownUI(RestClientException e) {
		mProgressDialog.dismiss();
		Toast.makeText(this, "访问失败", Toast.LENGTH_SHORT).show();
	}
	
	@ItemClick(android.R.id.list)
	void onItemClickListener(Member member) {
		
	}
}
