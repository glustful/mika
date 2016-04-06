package com.miicaa.base.share.contact;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.miicaa.base.share.ShareMain;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.view.LabelEditView;

public class AddContactMain extends ShareMain {

	public AddContactMain(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
	}
	public static final int REQUEST_CODE = 0X5;
	ArrayList<Contact> mUsers;
	String mRoundJson;
	LabelEditView mView;
	public ArrayList<Contact> getmUser() {
		return mUsers;
	}

	public void setmUser(ArrayList<Contact> mUsers) {
		this.mUsers = mUsers;
	}

	@Override
	public void setRootView(View view) {
		super.setRootView(view);
		mView = (LabelEditView) view;
	}
	@Override
	public void start(){
		ShowContactActivity_.intent(mContext)
		.startForResult(REQUEST_CODE);
		super.start();
	}
	@Override
	public boolean invalide(){
		if(mUsers==null||mUsers.size()<1){
			PayUtils.showToast(mContext, "联系人不能为空", 3000);
			return false;
		}
		return true;
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 mUsers = (ArrayList<Contact>) data.getSerializableExtra("data");
		if (mUsers == null || mUsers.size() == 0) {
			mView.setContent("");
			
			return;
		}
		setCopyData(mUsers,mView);
	}
	
	
	public void setCopyData(ArrayList<Contact> data,LabelEditView mView){
		if (data.size() > 1) {
			mView.setContent(data.get(0).getmName() + "等" + data.size()
					+ "个联系人");
		} else if (data.size() == 1) {
			mView.setContent(data.get(0).getmName());
		} else {
			mView.setContent("");
		}
	}

}
