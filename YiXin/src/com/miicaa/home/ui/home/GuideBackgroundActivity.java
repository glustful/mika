package com.miicaa.home.ui.home;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.EntirpiseInfo;
import com.miicaa.home.ui.enterprise.EnterpriceMainActivity_;
import com.miicaa.home.ui.enterprise.EnterpriseLocation;

@EActivity(R.layout.activity_guide_background)
public class GuideBackgroundActivity extends Activity{

	
	@Click(R.id.ceneterView)
	void centerButtonClick(View v){
		EnterpriceMainActivity_.intent(this)
		.start();
		finish();
	}
	@Click(R.id.leaveBtn)
	void leavedButtonClick(View v){
		EntirpiseInfo entirpiseInfo = new EntirpiseInfo();
		entirpiseInfo.userCode = AccountInfo.instance().getLastUserInfo().getCode();
		entirpiseInfo.userEmail = AccountInfo.instance().getLastUserInfo().getEmail();
		entirpiseInfo.locationType = EnterpriseLocation.YunnanReadedCancel;
		entirpiseInfo.gonggaoCount = EnterpriseLocation.getInstance().getGonggaoCount();
		EntirpiseInfo.saveOrUpdateLcation(this, entirpiseInfo);
		finish();
	}
	
}
