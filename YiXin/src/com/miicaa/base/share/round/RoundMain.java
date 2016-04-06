package com.miicaa.base.share.round;

import java.util.ArrayList;
import java.util.HashMap;

import com.miicaa.base.share.ShareMain;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.view.LabelEditView;

import android.content.Context;
import android.content.Intent;
import android.view.View;

/*
 * 查看范围公共类
 * 默认布局用
 * com.miicaa.home.view.LabelEditView
 */
public class RoundMain extends ShareMain{
	public static final int REQUEST_CODE = -2;
	
	/*
	 * map键值对 rightType选择类型 roundjson类型对应的json字符串，没有为空 title显示标题内容
	 */
	private HashMap<String, String> mParams;
	private ArrayList<SamUser> mRoundUser;// 选择人员时初始化值
	private ArrayList<RoundKinds> mKinds;// 显示类别对应关系
	private LabelEditView mView;
	public ArrayList<RoundKinds> getmKinds() {
		return mKinds;
	}

	public void setmKinds(ArrayList<RoundKinds> mKinds) {
		this.mKinds = mKinds;
	}

	public RoundMain(Context mContext, ArrayList<RoundKinds> mKinds) {
		super(mContext);
		
		this.mKinds = mKinds;
		this.mParams = new HashMap<String, String>();
		this.mRoundUser = new ArrayList<SamUser>();
		mParams.put("rightType", "00");
		mParams.put("json", "[]");
		mParams.put("name", "");
	}

	

	/*
	 * 点击事件
	 */
	@Override
	public void start() {
		mParams.put("name", mView.getText());
		SelectRoundActivity_.intent(mContext).mParam(mParams).mKinds(mKinds)
				.mUsers(mRoundUser)

				.startForResult(REQUEST_CODE);
		super.start();
	}

	public HashMap<String, String> getmParams() {
		return mParams;
	}

	public void setmParams(HashMap<String, String> mParams) {
		this.mParams = mParams;
	}

	public ArrayList<SamUser> getmRoundUser() {
		return mRoundUser;
	}

	public void setmRoundUser(ArrayList<SamUser> mRoundUser) {
		this.mRoundUser = mRoundUser;
	}

	

	@Override
	public void setRootView(View view) {
		
		super.setRootView(view);
		this.mView = (LabelEditView) view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		RoundKinds mKinds = (RoundKinds) data.getSerializableExtra("selected");
		mRoundUser.clear();
		mParams.put("json", "[]");
		mParams.put("rightType", mKinds.code);
		mView.setContent(mKinds.content);
		if (mKinds == RoundKinds.UNIT || mKinds == RoundKinds.GROUP) {
			mView.setContent(data.getStringExtra("result"));
			mParams.put("json", data.getStringExtra("json"));
		}
		if (mKinds == RoundKinds.PEOPLE) {
			ArrayList<ContactViewShow> copyDatas = data
					.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
			if (copyDatas == null || copyDatas.size() == 0) {
				mView.setContent("");
				mRoundUser.clear();

				return;
			}
			mParams.put("json", setCopyData(copyDatas,mRoundUser,mView));
		}

	}

	

}
