package com.yxst.epic.yixin.activity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.request.AddOrRemoveContactRequest;
import com.yxst.epic.yixin.data.dto.request.BaseRequest;
import com.yxst.epic.yixin.data.dto.request.GetMemberRequest;
import com.yxst.epic.yixin.data.dto.response.AddOrRemoveContactResponse;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.GetMemberResponse;
import com.yxst.epic.yixin.data.rest.IMInterface;
import com.yxst.epic.yixin.rest.IMInterfaceProxy;
import com.yxst.epic.yixin.utils.CacheUtils;

@EActivity(R.layout.activity_contact_detail_2)
public class ContactDetailActivity extends ActionBarActivity implements
		RestErrorHandler {

	private static final String TAG = "ContactDetailActivity";

	@Extra
	String userName;

	@Extra("Member")
	Member member;

	@ViewById
	ImageView ivAvatar;

	@ViewById
	Button btnSendMsg;

	@ViewById
	TextView tvName;

	@ViewById
	TextView tvNickName;

	@ViewById
	ImageView ivStar;

	/**
	 * 个性签名
	 */
	@ViewById
	TextView tvSignature;
	/**
	 * 手机
	 */
	@ViewById
	TextView tvMobile;
	/**
	 * 邮箱
	 */
	@ViewById
	TextView tvEmail;

	@AfterViews
	void afterViews() {
		final ActionBar bar = getSupportActionBar();
		bar.setTitle("详细资料");

		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
		bar.setDisplayOptions(change, flags);

		bind(member);
		
		getMemberDoInBackground();
	}

	void bind(Member member) {
		this.member = member;
		if (member != null) {
			ivStar.setVisibility(member.StarFriend == 0 ? View.GONE
					: View.VISIBLE);
			tvName.setText(member.NickName);
			tvNickName.setText("昵称：" + member.NickName);
			tvMobile.setText(member.Mobile);
			tvEmail.setText(member.Email);
			tvSignature.setText(member.Signature);
		}

		supportInvalidateOptionsMenu();
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	@Click(R.id.btnSendMsg)
	void onClickBtnSendMsg(View view) {
		ChatActivity_.intent(this)
				.localUserName(MyApplication.getInstance().getLocalUserName())
				.remoteUserName(member.UserName)
				.remoteDisplayName(member.NickName).start();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu()");
		// Inflate the menu; this adds items to the action bar if it is present.
		if (member != null) {
			getMenuInflater().inflate(R.menu.activity_contact_detail, menu);
			MenuItem item = menu.findItem(R.id.action_star_friend);
			item.setTitle(member.StarFriend == 0 ? R.string.starFriend_set
					: R.string.starFriend_cancel);
		}
		return true;
	}

	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// Log.d(TAG, "onPrepareOptionsMenu()");
	// MenuItem item = menu.findItem(R.id.action_star_friend);
	// item.setTitle(member.StarFriend == 0 ? R.string.starFriend_set
	// : R.string.starFriend_cancel);
	// return super.onPrepareOptionsMenu(menu);
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_star_friend) {
			addOrRemoveContactDoInBackground();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Click(R.id.layoutMobile)
	void onClickMobile(View v) {
		if (member != null && !TextUtils.isEmpty(member.Mobile)) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + member.Mobile));
			startActivity(intent);
		}
	}

	private IMInterface mIMInterfaceProxy;

	@AfterInject
	void afterInject() {
		mIMInterfaceProxy = IMInterfaceProxy.create();
		mIMInterfaceProxy.setRestErrorHandler(this);
	}

	@Background
	void addOrRemoveContactDoInBackground() {
		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		AddOrRemoveContactRequest request = new AddOrRemoveContactRequest();
		request.BaseRequest = baseRequest;
		request.Uid = member.Uid;
		request.StarFriend = member.StarFriend == 0 ? 1 : 0;

		Log.d(TAG, "doInBackground() request:" + request);

		AddOrRemoveContactResponse response = mIMInterfaceProxy
				.addOrRemoveContact(request);

		addOrRemoveContactOnPostExecute(response);
	}

	@UiThread
	void addOrRemoveContactOnPostExecute(AddOrRemoveContactResponse response) {
		Log.d(TAG, "onPostExecute() response:" + response);
		if (response == null) {
			return;
		}

		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (member.StarFriend == 0) {
			member.StarFriend = 1;
			Toast.makeText(this, "已标为常联系人", Toast.LENGTH_SHORT).show();
		} else {
			member.StarFriend = 0;
			Toast.makeText(this, "已取消常联系人", Toast.LENGTH_SHORT).show();
		}

		bind(member);
	}

	@Background
	void getMemberDoInBackground() {
		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		GetMemberRequest request = new GetMemberRequest();
		request.BaseRequest = baseRequest;
		request.UserName = userName;
		
		Log.d(TAG, "getMemberDoInBackground() request:" + request);
		
		GetMemberResponse response = mIMInterfaceProxy.getMember(request);
		
		getMemberOnPostExecute(response);
	}
	
	@UiThread
	void getMemberOnPostExecute(GetMemberResponse response) {
		Log.d(TAG, "getMemberOnPostExecute() response:" + response);
		if (response == null) {
			return;
		}
		
		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		bind(response.Member);
	}
	
	@UiThread
	@Override
	public void onRestClientExceptionThrown(RestClientException e) {
		Toast.makeText(this, "访问失败", Toast.LENGTH_SHORT).show();
	}

}
