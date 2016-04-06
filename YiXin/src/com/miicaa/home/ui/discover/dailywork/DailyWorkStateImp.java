package com.miicaa.home.ui.discover.dailywork;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.discover.DiscoverActivity;
import com.miicaa.home.ui.discover.DiscoverAdapter;
import com.miicaa.home.ui.discover.DiscoverReponseData;
import com.miicaa.home.ui.discover.DiscoverRequest;
import com.miicaa.home.ui.discover.DiscoverState;
import com.miicaa.home.ui.discover.ScreenMenu;
import com.miicaa.home.ui.discover.ScreenMenuListener;
import com.miicaa.home.ui.discover.ScreenResult;
import com.miicaa.home.ui.menu.SelectPersonInfo;

public class DailyWorkStateImp extends DiscoverState {

	DailyWorkScreenResult mScreenResult;
	DailyWorkRequest mRequest;
	DailyWorkResponseData mResponseData;
	DailyWorkScreenMenu mScreenMenu;
	DailyWorkTopScreenView mTopScreenView;
	DailyWorkAdapter mAdapter;
	
	@Override
	public String getTitle() {
		
		return "全公司-工作报告";
	}

	@Override
	public ScreenResult getScreenResult() {
		if(mScreenResult == null){
			mScreenResult = new DailyWorkScreenResult();
		}
		return mScreenResult;
	}

	@Override
	public DiscoverReponseData getResponseData() {
		if(mResponseData == null){
			mResponseData = new DailyWorkResponseData();
		}
		return mResponseData;
	}

	@Override
	public DiscoverRequest getRequest() {
		if(mRequest == null)
		mRequest = new DailyWorkRequest("/home/phone/workReport/getAllCompanyReport");
		return mRequest;
	}

	@Override
	public ScreenMenu getScreenMenu(final DiscoverActivity context) {
		if(mScreenMenu == null){
			mScreenMenu = DailyWorkScreenMenu_.build(context);
			mScreenMenu.setOnScreenMenuListener(new ScreenMenuListener() {
				
				@Override
				public void onOrigaintorButtonClick(View v) {
					Intent i = new Intent(context, SelectContacter.class);
					Bundle bunlde = new Bundle();
					ArrayList<String> names = new ArrayList<String>();
					ArrayList<String> codes = new ArrayList<String>();
					for(SelectPersonInfo info : mScreenResult.tmpPeopleList){
						names.add(info.mName);
						codes.add(info.mCode);
					}
					bunlde.putStringArrayList("name", names);
					bunlde.putStringArrayList("code", codes);
					bunlde.putString("how", SelectContacter.ROUND);
					i.putExtra("bundle", bunlde);
					context.startActivityForResult(i, DiscoverActivity.SELECT_ORIGITOR);
				}
				
				@Override
				public void onCompleteButtonClick(View v) {
					context.toggleMenu();
					mScreenResult.convertToType();
					context.topScreenView.openOrCreateView();
					mRequest.resetPage();
					mRequest.changeOriginators(mScreenResult.getPeopleCodes());
					mRequest.changeReportTypes(mScreenResult.getReportCodes());
					context.showDialog();
					mRequest.executeRequest(mResponseData);
				}
			});
		}
		
		return mScreenMenu;
	}

	@Override
	public void setRightButton(Button right) {
		right.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public DiscoverAdapter getAdapter(Context context) {
		if(mAdapter == null){
			mAdapter = new DailyWorkAdapter(context);
		}
		return mAdapter;
	}
	
	

}
