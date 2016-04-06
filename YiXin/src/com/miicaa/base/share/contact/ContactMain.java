package com.miicaa.base.share.contact;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.miicaa.base.share.ShareMain;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.contactList.ContactList;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.view.LabelEditView;

public class ContactMain extends ShareMain {
	public static final int REQUEST_CODE = 0X3;
	ArrayList<SamUser> mUsers;
	String mRoundJson;
	LabelEditView mView;
	public ArrayList<SamUser> getmUser() {
		return mUsers;
	}

	public void setmUser(ArrayList<SamUser> mUsers) {
		this.mUsers = mUsers;
	}

	public ContactMain(Context mContext) {
		super(mContext);
		
	}
	@Override
	public void setRootView(View view) {
		super.setRootView(view);
		mView = (LabelEditView) view;
	}
	
	@Override
	public boolean invalide(){
		if(mUsers==null||mUsers.size()<1){
			PayUtils.showToast(mContext, "客户负责人不能为空", 3000);
			return false;
		}
		return true;
		
	}
	
	@Override
	public void start(){
		Intent intent = new Intent(mContext,
				SelectContacter.class);
		Bundle bundle = new Bundle();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> code = new ArrayList<String>();
		if (mUsers!=null && mUsers.size() > 0) {
			for (SamUser s : mUsers) {
				name.add(s.getmName());
				code.add(s.getmCode());
			}
			bundle.putStringArrayList("name", name);
			bundle.putStringArrayList("code", code);
		}
		bundle.putInt("type", ContactList.SINGLESELECT);
		bundle.putString(SelectContacter.how, "round");
		intent.putExtra("bundle", bundle);
		((Activity) this.mContext).startActivityForResult(intent, REQUEST_CODE);
		super.start();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(mUsers==null){
			mUsers = new ArrayList<SamUser>();
		}
		ArrayList<ContactViewShow> copyDatas = data
				.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
		if (copyDatas == null || copyDatas.size() == 0) {
			mView.setContent("");
			mUsers.clear();

			return;
		}
		this.mRoundJson = setCopyData(copyDatas,mUsers,mView);
	}
}
