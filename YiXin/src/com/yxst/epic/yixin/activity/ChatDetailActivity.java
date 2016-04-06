package com.yxst.epic.yixin.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.yxst.epic.yixin.adapter.ChatDetailAdapter;
import com.yxst.epic.yixin.adapter.ChatDetailAdapter.OnDelMemberListenr;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.request.AddQunMemberRequest;
import com.yxst.epic.yixin.data.dto.request.BaseRequest;
import com.yxst.epic.yixin.data.dto.request.CreateQunRequest;
import com.yxst.epic.yixin.data.dto.request.DelQunMemberRequest;
import com.yxst.epic.yixin.data.dto.request.GetMemberRequest;
import com.yxst.epic.yixin.data.dto.request.GetQunMembersRequest;
import com.yxst.epic.yixin.data.dto.request.UpdateQunTopicRequest;
import com.yxst.epic.yixin.data.dto.response.AddQunMemberResponse;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.CreateQunResponse;
import com.yxst.epic.yixin.data.dto.response.DelQunMemberResponse;
import com.yxst.epic.yixin.data.dto.response.GetMemberResponse;
import com.yxst.epic.yixin.data.dto.response.GetQunMembersResponse;
import com.yxst.epic.yixin.data.dto.response.UpdateQunTopicResponse;
import com.yxst.epic.yixin.data.rest.IMInterface;
import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.fragment.ReTopicQunDialogFragment;
import com.yxst.epic.yixin.fragment.ReTopicQunDialogFragment_;
import com.yxst.epic.yixin.rest.IMInterfaceProxy;
import com.yxst.epic.yixin.utils.CacheUtils;
import com.yxst.epic.yixin.view.WrapContentHeightGridView;

@EActivity(R.layout.activity_chat_detail)
public class ChatDetailActivity extends ActionBarActivity implements
		RestErrorHandler, OnDelMemberListenr {

	private static final String TAG = "ChatDetailActivity";

	@Extra
	String localUserName;

	@Extra
	Member localMember;

	@Extra
	String remoteUserName;

	@Extra
	String remoteDisplayName;
	
	@SystemService
	LayoutInflater inflater;
	
	//@ViewById(R.id.chat_base_id_title)
	//TextView gtitle;
	
	@Click(R.id.chat_base_id_back)
	void click(){
		finish();
	}
	
	
	//TextView title;

	private IMInterface mIMInterfaceProxy;

	@ViewById(R.id.grid)
	WrapContentHeightGridView mGridView;

	@ViewById
	View trQun;

	@ViewById
	TextView tvQun;

	@ViewById
	Button btnQuitQun;
	
	@Bean
	ChatDetailAdapter mAdapter;

	@AfterInject
	void afterInject() {
		mIMInterfaceProxy = IMInterfaceProxy.create();
		mIMInterfaceProxy.setRestErrorHandler(this);
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//title =(TextView) inflater.inflate(R.layout.action_title, null);
		super.onCreate(savedInstanceState);
		if(getActionBar() != null)
			getActionBar().hide();
	}



	@AfterViews
	void afterViews() {
		//final ActionBar bar = getSupportActionBar();
		//bar.setTitle("返回");
		//title.setText(remoteDisplayName);
		//gtitle.setText(remoteDisplayName);
		//int flags = ActionBar.DISPLAY_HOME_AS_UP;
		// flags =  flags ^ ActionBar.DISPLAY_SHOW_CUSTOM;
	//	int change = bar.getDisplayOptions() ^ flags;
	//	bar.setDisplayOptions(change, flags);
		//ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//lp.gravity = lp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
			//	| Gravity.CENTER;
		//bar.setCustomView(title, lp);

		mGridView.setOnItemClickListener(mOnItemClickListener);
		mGridView.setAdapter(mAdapter);
		mAdapter.setLocalUserName(localUserName);
		mAdapter.setOnDelMemberListenr(this);
		
		
		if (Member.isTypeQun(remoteUserName)) {
			trQun.setVisibility(View.VISIBLE);
			tvQun.setText(remoteDisplayName);
			btnQuitQun.setVisibility(View.VISIBLE);
			getQunMembersDoInBackground();
		} else if (Member.isTypeUser(remoteUserName)) {
			trQun.setVisibility(View.GONE);
			btnQuitQun.setVisibility(View.GONE);
			getMemberDoInBackground();
		} else {
			trQun.setVisibility(View.GONE);
			btnQuitQun.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	@Background
	void getQunMembersDoInBackground() {
		onPreExecute();

		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		GetQunMembersRequest request = new GetQunMembersRequest();
		request.BaseRequest = baseRequest;
		request.userName = remoteUserName;

		Log.d(TAG, "getQunMembersDoInBackground() request:" + request);

		GetQunMembersResponse response = mIMInterfaceProxy
				.getQunMembers(request);
		
		getQunMembersOnPostExecute(response);
	}

	private void checkPermission(GetQunMembersResponse response) {
		// 返回不正常我不能乱提示
		if( response == null || 
				response.BaseResponse.Ret 
				!= 
				BaseResponse.RET_SUCCESS ){
			return;
		}
		
		boolean hasMovedOut = true;
		for(Member member:response.memberList){
			if( localUserName.equals(member.UserName) ){
				hasMovedOut = false;
				break;
			}
		}
		
		if( hasMovedOut ){
			// 你走！我没你这个儿子！
			Toast.makeText(ChatDetailActivity.this
					, "您已不是群成员，无权操作！"
					, Toast.LENGTH_SHORT).show();
			finish();
		}
		
		return;
	}



	@UiThread
	void getQunMembersOnPostExecute(GetQunMembersResponse response) {
		Log.d(TAG, "getQunMembersOnPostExecute() response:" + response);
		
		// check permission
		checkPermission(response);

		dismissProgressDialog();

		if (response == null) {
			return;
		}

		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}

		mAdapter.setShowAdd(true);
		mAdapter.setShowMinus(localUserName
				.equals(response.quninfo.creatorUserName));

		changeData(response.memberList);
		
		remoteDisplayName = response.quninfo.name;
		tvQun.setText(response.quninfo.name);
	}

	private void changeData(List<Member> memberList) {
		Collections.sort(memberList, new Comparator<Member>() {
			@Override
			public int compare(Member lhs, Member rhs) {
				if (lhs.UserName.equals(localUserName)) {
					return -1;
				} else if (rhs.UserName.equals(localUserName)) {
					return 1;
				}
				return 0;
			}
		});
		mAdapter.changeData(memberList);
	}

	private ProgressDialog mProgressDialog;

	@UiThread
	void onPreExecute() {
		mProgressDialog = ProgressDialog.show(this, "提示", "加载数据");
	}

	@Background
	void getMemberDoInBackground() {
		onPreExecute();

		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		GetMemberRequest request = new GetMemberRequest();
		request.BaseRequest = baseRequest;
		request.UserName = remoteUserName;

		Log.d(TAG, "getMemberDoInBackground() request:" + request);

		GetMemberResponse response = mIMInterfaceProxy.getMember(request);

		getMemberOnPostExecute(response);
	}

	@UiThread
	void getMemberOnPostExecute(GetMemberResponse response) {

		Log.d(TAG, "getMemberOnPostExecute() response:" + response);

		dismissProgressDialog();

		if (response == null) {
			return;
		}

		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, "连接错误"+response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			
			return;
		}

		mAdapter.setShowAdd(true);

		List<Member> memberList = new ArrayList<Member>();
		//memberList.add(localMember);
		memberList.add(response.Member);
		mAdapter.changeData(memberList);
	}

	private void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@UiThread
	@Override
	public void onRestClientExceptionThrown(RestClientException e) {
		dismissProgressDialog();
		Toast.makeText(this, "访问失败", Toast.LENGTH_SHORT).show();
	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (!mAdapter.isPositionAdd(position)
					&& !mAdapter.isPositionMinus(position)) {
//				Member member = (Member) parent.getItemAtPosition(position);
//				ContactDetailActivity_.intent(ChatDetailActivity.this)
//						.userName(member.UserName).member(member).start();
			} else if (mAdapter.isPositionAdd(position)) {
//				ContactSelectActivity_.intent(ChatDetailActivity.this)
//						.isPickMode(true).lockMembers(mAdapter.getMembers())
//						.startForResult(REQUESTCODE_PICK);
				addNewMember(mAdapter.getMembers());
				
			} else if (mAdapter.isPositionMinus(position)) {
				mAdapter.setDelMode(!mAdapter.isDelMode());
			}
		}
	};
	
	
	@Background
	void addNewMember(List<Member> members){
		ArrayList<String> extraUids = new ArrayList<String>();
		for(Member member : members){
			Log.d(TAG, "memBerUid + ';';';';';';';';"+member.Uid);
			if(member.Uid != null)
			extraUids.add(member.Uid);
		}
		if(!extraUids.contains(String.valueOf(AccountInfo.instance().getLastUserInfo().getId())))
		extraUids.add(String.valueOf(AccountInfo.instance().getLastUserInfo().getId()));
		toNewMember(extraUids);
	}
	
	@UiThread
	void toNewMember(ArrayList<String> extraUids){
		Intent i = new Intent(this,SelectContacter.class);
		Bundle  b = new Bundle();
		b.putString(SelectContacter.how, SelectContacter.ADDQUN);
		b.putStringArrayList("uids", extraUids);
		i.putExtra("bundle", b);
		startActivityForResult(i,REQUESTCODE_PICK);
	}
	

	public static final int REQUESTCODE_PICK = 1;

	@OnActivityResult(REQUESTCODE_PICK)
	void onActivityResult(int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			@SuppressWarnings("unchecked")
			List<Member> members = (List<Member>) data
					.getSerializableExtra("members");
			Log.d(TAG, "members:" + members);
			if (Member.isTypeQun(remoteUserName)) {
				addQunMemberDoInBackground(members);
			} else if (Member.isTypeUser(remoteUserName)) {
				createQunDoInBackground(members);
			}
			break;

		default:
			break;
		}
	}

	@Background
	void createQunDoInBackground(List<Member> members) {
		onPreExecute();

		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		CreateQunRequest request = new CreateQunRequest();
		request.BaseRequest = baseRequest;
		request.addMembers(mAdapter.getMembers());
		request.addMembers(members);
		request.MemberCount = request.MemberList == null ? 0
				: request.MemberList.size();
		request.Topic = "我的群";

		Log.d(TAG, "createQunDoInBackground() request:" + request);

		CreateQunResponse response = mIMInterfaceProxy.createQun(request);

		createQunOnPostExecute(response);
	}

	@UiThread
	void createQunOnPostExecute(CreateQunResponse response) {
		Log.d(TAG, "createQunOnPostExecute() response:" + response);
		dismissProgressDialog();
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

	@Background
	void addQunMemberDoInBackground(List<Member> members) {
		onPreExecute();

		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		AddQunMemberRequest request = new AddQunMemberRequest();
		request.BaseRequest = baseRequest;
		request.ChatRoomName = remoteUserName;
		request.memberList = members;

		Log.d(TAG, "addQunMemberDoInBackground() request:" + request);

		AddQunMemberResponse response = mIMInterfaceProxy.addQunMember(request);

		addQunMemberOnPostExecute(response);
	}

	@UiThread
	void addQunMemberOnPostExecute(AddQunMemberResponse response) {
		dismissProgressDialog();
		Log.d(TAG, "addQunMemberOnPostExecute() response:" + response);
		if (response == null) {
			return;
		}

		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}

		changeData(response.memberList);
	}

	@Click(R.id.trQun)
	void onClickTrQun(View v) {
		ReTopicQunDialogFragment f = ReTopicQunDialogFragment_.builder()
				.topic(remoteDisplayName).build();
		f.setOnTopicChangeListener(mOnTopicChangeListener);
		f.show(getSupportFragmentManager(),
				ReTopicQunDialogFragment.class.getName());
	}

	@Click(R.id.btnQuitQun)
	void onClickBtnQuitQun() {
		doInBackgroundQuitQun(localMember);
	}
	
	@Background
	void doInBackgroundQuitQun(Member member) {
		onPreExecute();
		
		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		DelQunMemberRequest request = new DelQunMemberRequest();
		request.BaseRequest = baseRequest;
		request.ChatRoomName = remoteUserName;
		request.addMember(member);

		Log.d(TAG, "doInBackgroundQuitQun() request:" + request);

		DelQunMemberResponse response = mIMInterfaceProxy.delQunMember(request);
		
		onPostExecuteQuitQun(response);
	}
	
	@UiThread
	void onPostExecuteQuitQun(DelQunMemberResponse response) {
		Log.d(TAG, "onPostExecuteQuitQun() response:" + response);

		dismissProgressDialog();

		if (response == null) {
			return;
		}
		
		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		DBManager.getInstance(this).deleteChat(localUserName, remoteUserName);
		
//		Intent data = new Intent();
//		data.putExtra("isQuitQunSuccess", true);
		setResult(RESULT_OK);
		finish();
	}
	
	private ReTopicQunDialogFragment.OnTopicChangeListener mOnTopicChangeListener = new ReTopicQunDialogFragment.OnTopicChangeListener() {

		@Override
		public void onTopicChange(String topic) {
			
			if(topic==null||topic.equals("")){
				Toast.makeText(ChatDetailActivity.this, "群聊名称不能为空", Toast.LENGTH_LONG).show();
				return;
			}
			if(topic.equals(remoteDisplayName)){
				return;
			}
			updateQunTopicDoInBackground(topic);
		}
	};

	@Background
	void updateQunTopicDoInBackground(String topic) {
		onPreExecute();

		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		UpdateQunTopicRequest request = new UpdateQunTopicRequest();
		request.BaseRequest = baseRequest;
		request.ChatRoomName = remoteUserName;
		request.topic = topic;

		Log.d(TAG, "updateQunTopicDoInBackground() request:" + request);

		UpdateQunTopicResponse response = mIMInterfaceProxy
				.updateQunTopic(request);

		updateQunTopicOnPostExecute(response, topic);
	}

	@UiThread
	void updateQunTopicOnPostExecute(UpdateQunTopicResponse response,
			String topic) {
		Log.d(TAG, "updateQunTopicOnPostExecute() response:" + response);

		dismissProgressDialog();

		if (response == null) {
			return;
		}

		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}
		remoteDisplayName = topic;
		tvQun.setText(topic);

		// DBManager.getInstance(this).updateQunTopic(remoteUserName, topic);
	}

	@Override
	public void onDelMember(Member member) {
		delMemberDoInBackground(member);
	}

	@Background
	void delMemberDoInBackground(Member member) {
		onPreExecute();

		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		DelQunMemberRequest request = new DelQunMemberRequest();
		request.BaseRequest = baseRequest;
		request.ChatRoomName = remoteUserName;
		request.addMember(member);

		Log.d(TAG, "delMemberDoInBackground() request:" + request);

		DelQunMemberResponse response = mIMInterfaceProxy.delQunMember(request);

		delQunMemberOnPostExecute(response);
	}

	@UiThread
	void delQunMemberOnPostExecute(DelQunMemberResponse response) {
		Log.d(TAG, "delQunMemberOnPostExecute() response:" + response);

		dismissProgressDialog();

		if (response == null) {
			return;
		}

		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			Toast.makeText(this, response.BaseResponse.ErrMsg,
					Toast.LENGTH_SHORT).show();
			return;
		}

		changeData(response.memberList);
	}
}
