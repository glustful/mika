package com.miicaa.base.share;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.miicaa.home.R;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.view.LabelEditView;

public class ShareMain {
	public static final int REQUEST_CODE = 0X1;
	public Context mContext;
	public View mRootView;
	

	public ShareMain(Context mContext) {
		super();
		this.mContext = mContext;
		
	}

	public boolean invalide(){
		return true;
	}
	
	public View getRootView() {
		if (mRootView == null) {

		}
		return this.mRootView;
	}

	public void setRootView(View view) {
		this.mRootView = view;
		setOnclick();
	}

	private void setOnclick() {
		this.mRootView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				start();

			}
		});

	}

	/*
	 * 点击事件
	 */
	public void start() {
		
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}

	protected String setCopyData(ArrayList<ContactViewShow> data,ArrayList<SamUser> mUsers,LabelEditView mView) {

		String mRoundJson = "[";
		for (int i = 0; i < data.size(); i++) {
			mUsers.add(new SamUser(data.get(i).getCode(), data.get(i)
					.getName()));
			mRoundJson += "{\"code\":\"" + data.get(i).getCode()
					+ "\",\"name\":\"" + data.get(i).getName() + "\"}";

			if (i < data.size() - 1) {

				mRoundJson += ",";
			}

		}
		if (data.size() > 1) {
			mView.setContent(data.get(0).getName() + "等" + data.size()
					+ "个人");
		} else if (data.size() == 1) {
			mView.setContent(data.get(0).getName());
		} else {
			mView.setContent("");
		}
		mRoundJson += "]";

		return mRoundJson;
	}
}
