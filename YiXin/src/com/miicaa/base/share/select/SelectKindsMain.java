package com.miicaa.base.share.select;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.miicaa.base.share.ShareMain;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.view.LabelEditView;

/*
 * 选择客户类型公共类
 * 默认布局用
 * com.miicaa.home.view.LabelEditView
 */
public class SelectKindsMain extends ShareMain{
	public static final int REQUEST_CODE = 0X2;
	private ArrayList<Kind> mKinds;
	public ArrayList<Kind> getmKinds() {
		return mKinds;
	}
	public void setmKinds(ArrayList<Kind> mKinds) {
		this.mKinds = mKinds;
	}


	private LabelEditView mView;
	

	public SelectKindsMain(Context mContext) {
		
		super(mContext);
		
	}
	@Override
	public void setRootView(View view) {
		
		super.setRootView(view);
		this.mView = (LabelEditView) view;
	}
	
	/*
	 * 点击事件
	 */
	@Override
	public void start() {
		SelectKindsActivity_.intent(mContext)
		.mSelectedKinds(mKinds)
		.startForResult(REQUEST_CODE);
		super.start();
	}

	@Override
	public boolean invalide(){
		if(mKinds==null||mKinds.size()<1){
			PayUtils.showToast(mContext, "客户类型不能为空", 3000);
			return false;
		}
		return true;
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(mKinds==null){
			mKinds = new ArrayList<Kind>();
		}else{
		mKinds.clear();
		}
		ArrayList<Kind> tmp = (ArrayList<Kind>) data.getSerializableExtra("selected");
		mKinds.addAll(tmp);
		if (mKinds.size() > 1) {
			mView.setContent(mKinds.get(0).getName() + "等" + mKinds.size()
					+ "个客户类型");
		} else if (mKinds.size() == 1) {
			mView.setContent(mKinds.get(0).getName());
		} else {
			mView.setContent("");
		}
	}

}
