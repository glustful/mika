package com.miicaa.home.ui.menu;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.menu.FragmentToPlan.SelectInf;
import com.miicaa.home.ui.menu.FragmentToScreen.OnSrcCodeChange;
@EFragment(R.layout.sliding_menu_fragment)
public class SlidingFragment extends Fragment {
	ArrayList<Fragment> menuFragments;
	ArrayList<ImageView> screenTabs;
    FragmentToPlan plan;
    FragmentToScreen screen;
	View planRootView;
	View screenRootView;
	SelectListener selectListener;
@ViewById(R.id.button_to_plan)
ImageView planView;
@ViewById(R.id.button_to_screen)
ImageView screenView;
@ViewById(R.id.sliding_menu_add)
LinearLayout menuAddLayout;
@AfterInject
void afterinject(){
	typeApproval();
}


@SuppressWarnings("serial")
//@UiThread
void typeApproval(){
	
	String url = "/home/phone/thing/getapproveTemp";
	new RequestAdpater() {
		
		@Override
		public void onReponse(ResponseData data) {
			// TODO Auto-generated method stub
			if(data.getResultState() == ResultState.eSuccess){
			    JSONArray apprArray = data.getJsonArray();
			    if(apprArray == null || apprArray.length() == 0){
			    	return;
			    }
			    jsonToData(apprArray);
			    
			}
		}
		
		@Override
		public void onProgress(ProgressMessage msg) {
			// TODO Auto-generated method stub
			
		}
	}.setUrl(url)
	.notifyRequest();
}


@AfterViews
void initAllViews(){
	menuFragments = new ArrayList<Fragment>();
	screenTabs = new ArrayList<ImageView>();
//	screenTabs.add(planView);
//	screenTabs.add(screenView);
	resetNinit();
	planView.setOnClickListener(planClickListener);
	screenView.setOnClickListener(screenClickListener);
	screenRootView.setVisibility(View.GONE);	
}

public void resetNinit(){
	menuAddLayout.removeAllViews();
	plan = new FragmentToPlan(menuAddLayout.getContext());
	plan.setSelectInf(new SelectInf() {
		
		@Override
		public void selectWhat(int position) {
			// TODO Auto-generated method stub
			selectListener.selectPlan(position);
		
	}
	});
	planRootView = plan.getPlanView();
	screen = new FragmentToScreen(menuAddLayout.getContext());
	screen.setOnSrcCodeChange(new OnSrcCodeChange() {
		
		@Override
		public void setSrcCode(ScreenType type) {
			// TODO Auto-generated method stub
			selectListener.selectScreen(type);
		}
	});
	screenRootView = screen.getScreenView();
	menuAddLayout.addView(planRootView);
	menuAddLayout.addView(screenRootView);
}

void jsonToData(JSONArray apparray){
	screen.clearApprScreen();
	for (int i = 0 ; i < apparray.length(); i ++){
    	ScreenApproveType type = new ScreenApproveType(apparray.optJSONObject(i)).save();
    	screen.addApprScreen(type);
    }
	screen.refreshApprType();
}


//回调选择的筛选条件
public void setSelect(SelectListener selectListener){
	this.selectListener = selectListener;
}
class slidingMenuAdapter extends FragmentPagerAdapter{

	public slidingMenuAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		menuFragments.get(arg0);
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuFragments.size();
	}
	
	
}
//切换。我这里傻逼了，我早早的就该使用tabhost
	OnClickListener planClickListener = new OnClickListener() {
		
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			planRootView.setVisibility(View.VISIBLE);
			screenRootView.setVisibility(View.GONE);
			planView.setBackgroundDrawable(getActivity().getResources().getDrawable(
					R.drawable.screen_select));
			screenView.setBackgroundDrawable(getActivity().getResources().getDrawable(
					R.drawable.screen_nomarl));
		}
	};
	
	OnClickListener screenClickListener = new OnClickListener() {
		
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			typeApproval();
			planRootView.setVisibility(View.GONE);
		    screenRootView.setVisibility(View.VISIBLE);
		    planView.setBackgroundDrawable(getActivity().getResources().getDrawable(
		    		R.drawable.screen_nomarl));
		    screenView.setBackgroundDrawable(getActivity().getResources().getDrawable(
		    		R.drawable.screen_select));
		}
	};
	
	public void screenWhat(){
		screen.covertTmp();
		screen.oldWhat();
	}


	@Override
	public void onStart() {
		screen.onRestart();
		super.onStart();
	}
	
	
	
}



