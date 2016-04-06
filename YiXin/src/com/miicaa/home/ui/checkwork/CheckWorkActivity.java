
package com.miicaa.home.ui.checkwork;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.common.base.PopupItem;
import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.checkwork.CheckWorkScreenLayout.OnCheckWorkScreenCotentClickListener;
import com.miicaa.home.ui.checkwork.HeadBottomPopupView.OnBottomPopWindowItemClickListener;
import com.miicaa.home.ui.checkwork.removecheckworktype.CheckWorkSingleTypes;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.home.TopScreenView.RemoveScreenTypeListener;
import com.miicaa.home.ui.person.PersonDepartEdit;
import com.miicaa.utils.AllUtils;

@EActivity
public class CheckWorkActivity extends MainActionBarActivity{

	static String TAG = "CheckWorkActivity";
	
	
	public final static String NAME = "name";
	public final static String LATE = "late";
	public final static String LEAVED = "leaved";
	public final static String NOSIGNIN = "noSignIn";
	public final static String NOSIGNOUT = "nosignOut";
	
	
	
//	@ViewById(R.id.titleBtn)
//	Button titleButton;
//	
	@SystemService
	LayoutInflater inflater;
	
//	@ViewById(R.id.mainListView)
	PullToRefreshListView pullToRefreshListView;
	ListView listView;
	
	String topTitle;
	MenuDrawer menuDrawer;
	CheckWorkScreenLayout checkWorkScreenLayout;
	ScreenResult screenResult;
	Button titleButton;
	String myUserCode;
	Long nowDate;
	
	MyCheckWorkAdapter myCheckWorkAdapter;
    TongjiCheckWorkAdapter tongjiCheckWorkAdapter;
	AllCheckWorkAdapter allCheckWorkAdapter;
	RefreshInfo refreshInfo;
	CheckTopScreenView resultView;
	ProgressBar progressBar;
	Toast mToast;
	int listViewdiverHeight;
	
	final static String MY_CHECKWORK = "myAttend";
	final static String ALL_CHECKWORK = "allAttend";
	final static String TONGJI_CHECKWORK = "statisticAttend";
		
	
	final static int START_FORNAME = 0 ;
	final static int START_FORORG = 1;
    public final static int START_FORSIGN = 7;
	
	final static int RESULT_FORNAME = 2;
	final static int RESULT_FORNONE = 3;
	final static int RESULT_FORALL = 4;
	final static int RESULT_REFRESH = 5;
	final static int RESULT_FORORG = 6;
	final static int RESULT_CLEARNOW = 7;
	
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		initAdapters();
		titleButton = (Button)getCenterButton();
		setButtonRightDrawable(titleButton, R.drawable.checkwork_title_flag_selector);
		refreshInfo = new RefreshInfo();
		screenResult = new ScreenResult();
		checkWorkScreenLayout = CheckWorkScreenLayout_.build(this)
				.setState(CheckWorkScreenLayout.MY_CHECKWORK);
		checkWorkScreenLayout.setBackgroundColor(getResources().getColor(R.color.sliding_menu_bgcolor));
		menuDrawer = MenuDrawer.attach(this,MenuDrawer.Type.OVERLAY, Position.RIGHT);
		menuDrawer.setContentView(R.layout.activity_check_work,0);
		menuDrawer.getMenuContainer().setBackgroundColor(Color.TRANSPARENT);
		menuDrawer.setMenuView(checkWorkScreenLayout);
		menuDrawer.setDropShadow(R.drawable.menu_draw_bg_shape);
		menuDrawer.setDropShadowSize((int)getResources().getDimension(R.dimen.shadow_width));
		menuDrawer.setMaxAnimationDuration(3000);  
		menuDrawer.setHardwareLayerEnabled(false);
		menuDrawer.setMenuSize((int)getResources().getDimension(R.dimen.slidingmenu_offset));
		menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		menuDrawer.setOffsetMenuEnabled(true);
		menuDrawer.setTop(200);
		progressBar = (ProgressBar)menuDrawer.findViewById(R.id.progressBar);
		resultView = (CheckTopScreenView)menuDrawer.findViewById(R.id.resultScreenView);
		resultView.create(screenResult)
		.openOrCreateView();
		resultView.setRemoveScreenTypeListener(new RemoveScreenTypeListener() {
			
			@Override
			public void removeType() {
				requestData(false);
			}

			@Override
			public Object initObjects() {
				return screenResult;
			}
		});
		pullToRefreshListView = (PullToRefreshListView)menuDrawer.findViewById(R.id.mainListView);
		pullToRefreshListView.setMode(Mode.BOTH);
		pullToRefreshListView.setOnRefreshListener(new PullToRefreshTwo());
		listView = pullToRefreshListView.getRefreshableView();
		listView.setAdapter(myCheckWorkAdapter);
		listViewdiverHeight = listView.getDividerHeight();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listItemClick(parent, view, position, id);
			}
		});
		registerForContextMenu(listView);
		checkWorkScreenLayout.setOnCheckWorkScreenCotentClickListener(new OnCheckWorkScreenCotentClickListener() {
			
			@Override
			public void orgButtonClick() {
				Intent intent = new Intent(CheckWorkActivity.this,PersonDepartEdit.class);
				intent.putExtra("rightType", "20");
				Bundle bundle = new Bundle();
				bundle.putString("type", "unit");
				bundle.putString("url", "/home/phone/personcenter/getunit");
				bundle.putString("code", "");
				
				intent.putExtra("bundle", bundle);
				startActivityForResult(intent, START_FORORG);
			}
			
			@SuppressWarnings("deprecation")
			@Override
			public void onCompleteClick() {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(((screenResult.nowBeginDate != null && screenResult.nowBeginDate.length() > 0)
						&&(screenResult.nowEndDate == null || screenResult.nowEndDate.length() == 0))
						||(((screenResult.nowBeginDate == null || screenResult.nowBeginDate.length() == 0)
								&&(screenResult.nowEndDate != null && screenResult.nowEndDate.length() > 0)))){
					mToast.setText("您必须输入开始时间和结束时间才能进行筛选");
					mToast.show();
					return;
				} else
					try {
						if( sdf.parse(screenResult.nowBeginDate).getTime()>
						sdf.parse(screenResult.nowEndDate).getTime()){
							mToast.setText("结束时间必须大于开始时间");
							mToast.show();
							return;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				refreshInfo.isRefresh = true;
				updateMenuContent(RESULT_FORNONE);
				menuDrawer.toggleMenu();
				if(refreshInfo.requestStatus_.equals(MY_CHECKWORK)){
					CheckWorkTongjiContents tongjiContent = myCheckWorkAdapter.getTongjiContents();
					if(screenResult.nowBeginDate != null && screenResult.nowEndDate != null ){
						String date = "";
				if(screenResult.nowBeginDate.length() == 0 || screenResult.nowEndDate.length() == 0)
					 date = "本月";
				else
					date =  screenResult.nowBeginDate + "\t" +"——"+screenResult.nowEndDate;
					if(tongjiContent != null){
					tongjiContent.timeType = new CheckScreenValue(CheckWorkSingleTypes.DATE, date);
					Log.d(TAG, "my checkwork screen time:"+tongjiContent.timeType.mValue);
					myCheckWorkAdapter.tongjiAdapterRefresh(tongjiContent);
					}
					}
				}else{
				resultView.create(screenResult)
				.openOrCreateView();
				}
				progressBar.setVisibility(View.VISIBLE);
				requestData(false);
			}
			
			@Override
			public void nameButtonClick() {
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("name", screenResult.nowUserNames);
				bundle.putStringArrayList("code", screenResult.nowUserCodes);
				bundle.putString("how", SelectContacter.ROUND);
				Intent intent = new Intent(CheckWorkActivity.this,SelectContacter.class);
				intent.putExtra("bundle", bundle);
				startActivityForResult(intent, START_FORNAME);
			}
			
			@Override
			public void endDateClick(String date) {
				screenResult.nowEndDate = date != null ? date : "";
			}
			
			@Override
			public void beginDateClick(String date) {
				screenResult.nowBeginDate = date != null ? date : "";
			}

			@Override
			public void onClearClick() {
				updateMenuContent(RESULT_CLEARNOW, false);
				
			}
		});
		progressBar.setVisibility(View.VISIBLE);
		requestData(true);
	}
	
	private void initAdapters(){
		myCheckWorkAdapter = new MyCheckWorkAdapter(this);
		allCheckWorkAdapter = new AllCheckWorkAdapter(this);
		tongjiCheckWorkAdapter = new TongjiCheckWorkAdapter(this);
	}
	
	
	private void clickCenterItems(PopupItem item){
		refreshInfo.refreshRequest(item.mCode);
		pullToRefreshListView.onRefreshComplete();
		menuDrawer.closeMenu();
		progressBar.setVisibility(View.VISIBLE);
		updateMenuContent(RESULT_REFRESH, false);
		if(item.mCode.equals(MY_CHECKWORK)){
			checkWorkScreenLayout.setState(CheckWorkScreenLayout.MY_CHECKWORK);
			listView.setAdapter(myCheckWorkAdapter);
//			if(myCheckWorkAdapter.getCount() <= 2){
			listView.setDividerHeight(listViewdiverHeight);
				requestData(false);
//			}else{
//			myCheckWorkAdapter.notifyDataSetChanged();
//			}
		}else if(item.mCode.equals(ALL_CHECKWORK)){
			checkWorkScreenLayout.setState(CheckWorkScreenLayout.ALL_CHECKWORK);
			listView.setAdapter(allCheckWorkAdapter);
//			if(allCheckWorkAdapter.getCount() <= 0){
			listView.setDividerHeight(listViewdiverHeight);
				requestData(false);
//			}else{
//			allCheckWorkAdapter.notifyDataSetChanged();
//			}
		}else if(item.mCode.equals(TONGJI_CHECKWORK)){
			checkWorkScreenLayout.setState(CheckWorkScreenLayout.TONGJI_CHECKWORK);
			listView.setAdapter(tongjiCheckWorkAdapter);
			listView.setDividerHeight(0);
//			if(tongjiCheckWorkAdapter.getCount() <= 1){
				requestData(false);
//			}else{
//			tongjiCheckWorkAdapter.notifyDataSetChanged();
//			}
		}
	}
	
	private void setButtonRightDrawable(Button button,int resId){
		Context mContext = CheckWorkActivity.this;
		Drawable rightDrawable= mContext.getResources().getDrawable(resId);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
				rightDrawable.getMinimumHeight());
		button.setCompoundDrawables(null, null, rightDrawable, null);
	}
	
	private void updateMenuContent(final int what){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, String> map = new HashMap<String, String>();
				String names = null;
				String orgs = null;
				switch (what) {
				case RESULT_FORNONE:
					screenResult.convertToNowTypes();
					handler.obtainMessage(what).sendToTarget();
					break;
				case RESULT_FORALL:
					screenResult.resetToTrueTypes();
					 names = AllUtils.listToString(screenResult.nowUserNames);
					 map.put("name", names);
					handler.obtainMessage(what,map).sendToTarget();
					break;
				case RESULT_REFRESH:
					screenResult.clearTyeps();
					names = AllUtils.listToString(screenResult.nowUserNames);
					map.put("name", names);
					handler.obtainMessage(what, map).sendToTarget();
				default:
					break;
				}
			}
		}).start();
		
	}
	
	private void updateMenuContent(int what,Boolean isSync){
		if(isSync){
			updateMenuContent(what);
		}else{
			HashMap<String, String> map = new HashMap<String, String>();
			String names = null;
			String orgs = null;
			String beginDate = null;
			String endDate = null;
			switch (what) {
			case RESULT_FORALL:
				screenResult.resetToTrueTypes();
				 names = AllUtils.listToString(screenResult.nowUserNames);
				 orgs = screenResult.nowOrgNames == null ? "" : screenResult.nowOrgNames;
				 beginDate = screenResult.nowBeginDate == null ? "" :screenResult.nowBeginDate;
				 endDate = screenResult.nowEndDate == null ? "" : screenResult.nowEndDate;
				 checkWorkScreenLayout.setAllViewText(names, orgs, 
							beginDate, 
							endDate);
				break;
			case RESULT_REFRESH:
				screenResult.clearTyeps();
				orgs = screenResult.nowOrgNames == null ? "" : screenResult.nowOrgNames;
				 beginDate = screenResult.nowBeginDate == null ? "" :screenResult.nowBeginDate;
				 endDate = screenResult.nowEndDate == null ? "" : screenResult.nowEndDate;
				 checkWorkScreenLayout.setAllViewText(names, orgs, 
							beginDate, 
							endDate);
				 resultView.create(screenResult)
				 .openOrCreateView();
				names = AllUtils.listToString(screenResult.nowUserNames);
				break;
			case RESULT_CLEARNOW:
				screenResult.clearNowTypes();
				orgs = screenResult.nowOrgNames == null ? "" : screenResult.nowOrgNames;
				 beginDate = screenResult.nowBeginDate == null ? "" :screenResult.nowBeginDate;
				 endDate = screenResult.nowEndDate == null ? "" : screenResult.nowEndDate;
				 checkWorkScreenLayout.setAllViewText(names, orgs, 
							beginDate, 
							endDate);
				checkWorkScreenLayout.setAllViewText(names, orgs, 
						beginDate, 
						endDate);
				break;
			default:
				break;
			}
		}
	}
	
	
	Handler handler = new MyDefalutHandler();
	@SuppressLint("HandlerLeak")
	class MyDefalutHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String names = null;
			String orgs = null;
			String beginDate = null;
			String endDate = null;
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>)msg.obj;
			switch (msg.what) {
			case RESULT_FORNAME:
				names = map.get("name");
				checkWorkScreenLayout.setUserNames(names);
				break;
			case RESULT_FORORG:
				orgs = map.get("name") != null ? map.get("name") : "";
				checkWorkScreenLayout.setOrgNames(orgs);
				break;
			case RESULT_FORALL:
				 names = map.get("name");
				 orgs = screenResult.nowOrgNames != null ? screenResult.nowOrgNames : "";
				 beginDate = screenResult.nowBeginDate == null ? "" :screenResult.nowBeginDate;
				 endDate = screenResult.nowEndDate == null ? "" : screenResult.nowEndDate;
				 checkWorkScreenLayout.setAllViewText(names, orgs, 
							beginDate, 
							endDate);
				 break;
			case RESULT_REFRESH:
				 names = map.get("name");
				 orgs = screenResult.nowOrgNames != null ? screenResult.nowOrgNames : "";
				 beginDate = screenResult.nowBeginDate == null ? "" :screenResult.nowBeginDate;
				 endDate = screenResult.nowEndDate == null ? "" : screenResult.nowEndDate;
				checkWorkScreenLayout.setAllViewText(names, orgs, 
						beginDate, 
						endDate);
				resultView.create(screenResult)
				.openOrCreateView();
				break;
			default:
				break;
			}
		}
		
	}
	
	@OnActivityResult(START_FORNAME)
	void onNameResult(int resultCode,Intent data){
		if(resultCode == RESULT_OK){
			final ArrayList<ContactViewShow> contacts = data.
					getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
			new Thread(new Runnable() {
				@Override
				public void run() {
					screenResult.nowUserNames.clear();
					screenResult.nowUserCodes.clear();
					for(int i = 0 ; i < contacts.size();i++){
						ContactViewShow contact = contacts.get(i);
						screenResult.nowUserNames.add(contact.name);
						screenResult.nowUserCodes.add(contact.code);
					}
					HashMap<String, String> map = new HashMap<String, String>();
					String str = AllUtils.listToString(screenResult.nowUserNames);
					map.put("name", str);
					handler.obtainMessage(RESULT_FORNAME,map).sendToTarget();;
				}
			}).start();
		}
	}
	
	@OnActivityResult(START_FORORG)
	void onOrgResult(int resultCode,final Intent data){
		if(resultCode != RESULT_OK){
			return;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String orgCode = data.getStringExtra("code");
				String orgName = data.getStringExtra("result");
				screenResult.nowOrgCodes = orgCode;
				screenResult.nowOrgNames = orgName;
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name",screenResult.nowOrgNames);
				handler.obtainMessage(RESULT_FORORG, map).sendToTarget();;
			}
		}).start();
	}
	
	@OnActivityResult(START_FORSIGN)
	void onSignResult(int resultCode,Intent data){
		if(resultCode != RESULT_OK){
			return;
		}
		progressBar.setVisibility(View.VISIBLE);
		updateMenuContent(RESULT_REFRESH, false);
		requestData(false);
	}
	
	class PullToRefreshTwo implements PullToRefreshBase.OnRefreshListener2<ListView>{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			refreshInfo.refreshRequest();
			requestData(false);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			refreshInfo.addMoreRequest();
			requestData(false);
		}
		
	}
	
	/*刷新控制的参数*/
	class RefreshInfo{
		int pageCount;
		String requestStatus_;
		Boolean isRefresh;
		
		public RefreshInfo(){
			pageCount = 1;
			requestStatus_ = MY_CHECKWORK;
			isRefresh = true;
		}
		
		public void refreshRequest(){
			pageCount = 1;
			isRefresh = true;
		}
		
		public void refreshRequest(String status_){
			refreshRequest();
			changeRequestStatus(status_);
		}
		
		public void addMoreRequest(){
			pageCount ++;
			isRefresh = false;
		}
		
		public void changeRequestStatus(String status_){
			requestStatus_ = status_;
		}
		
	}
	
	public class ScreenResult{
		String trueBeginDate;
		String nowBeginDate;
		String trueEndDate;
		String nowEndDate;
		String trueOrgCodes;
		String nowOrgCodes;
		String trueOrgNames;
		String nowOrgNames;
		
		ArrayList<String> trueUserCodes;
		ArrayList<String> nowUserCodes;
		ArrayList<String> trueUserNames;
		ArrayList<String> nowUserNames;
//		ArrayList<String> trueOrgCodes;
//		ArrayList<String> nowOrgCodes;
//		ArrayList<String> trueOrgNames;
//		ArrayList<String> nowOrgNames;
		
		public ScreenResult(){
			trueUserCodes = new ArrayList<String>();
			nowUserCodes = new ArrayList<String>();
			trueUserNames = new ArrayList<String>();
			nowUserNames = new ArrayList<String>();
//			trueOrgCodes = new ArrayList<String>();
//			nowOrgCodes = new ArrayList<String>();
//			trueOrgNames = new ArrayList<String>();
//			nowOrgNames = new ArrayList<String>();
		}
		
		public void convertToNowTypes(){
			trueBeginDate = nowBeginDate;
			trueEndDate = nowEndDate;
			trueUserCodes.clear();
			trueUserCodes.addAll(nowUserCodes);
			trueUserNames.clear();
			trueUserNames.addAll(nowUserNames);
			trueOrgCodes = nowOrgCodes;
//			trueOrgCodes.addAll(nowOrgCodes);
//			trueOrgNames.clear();
			trueOrgNames = nowOrgNames;
		}
		
		public void resetToTrueTypes(){
			nowBeginDate =  trueBeginDate;
			nowEndDate =  trueEndDate;
			nowUserCodes.clear();
			nowUserCodes.addAll(trueUserCodes);
			nowUserNames.clear();
			nowUserNames.addAll(trueUserNames);
			nowOrgCodes = trueOrgCodes;
//			nowOrgCodes.addAll(trueOrgCodes);
			nowOrgNames = trueOrgNames;
//			nowOrgNames.addAll(trueOrgNames);
		}
		
		public void clearTyeps(){
			trueBeginDate = "";
			nowBeginDate = "";
		    trueEndDate = "";
		    nowEndDate = "";
		    trueUserCodes.clear();
			nowUserCodes.clear();
			trueUserNames.clear();
			nowUserNames.clear();
		    trueOrgCodes = "";
		    nowOrgCodes = "";
		    trueOrgNames = "";
		    nowOrgNames = "";
		}
		
		public void clearBeginDateTypes(){
			trueBeginDate = "";
			nowBeginDate = "";
		}
		
		public void celarEndDateTypes(){
			trueEndDate = "";
			nowEndDate = "";
		}
		
		public void clearUserCodesTypes(){
			trueUserNames.clear();
			nowUserNames.clear();
			trueUserCodes.clear();
			nowUserCodes.clear();
		}
		
		public void clearOrgCodesTypes(){
			trueOrgCodes = "";
			nowOrgCodes = "";
			trueOrgNames = "";
			nowOrgNames = "";
		}
		
		public void clearNowTypes(){
			nowBeginDate = "";
		    nowEndDate = "";
			nowUserCodes.clear();
			nowUserNames.clear();
		    nowOrgCodes = "";
		    nowOrgNames = "";
		}
	}

	@Override
	public Boolean showTitleButton() {
		return true;
	}

	@Override
	public String showTitleButtonStr() {
		return "我的考勤";
	}

	@Override
	public void titleButtonClick(final View v) {
		v.setSelected(!v.isSelected());
		ArrayList<PopupItem> items = new ArrayList<PopupItem>();
		items.add(new PopupItem("我的考勤",MY_CHECKWORK));
		items.add(new PopupItem("全部考勤", ALL_CHECKWORK));
		items.add(new PopupItem("考勤统计", TONGJI_CHECKWORK));
		HeadBottomPopupView.build(CheckWorkActivity.this)
		.setItems(items)
		.setOnBottomPopWindowClickListener(new OnBottomPopWindowItemClickListener() {
			
			@Override
			public void itemClick(PopupItem item) {
				clickCenterItems(item);
				titleButton.setText(item.mContent);
			}

			@Override
			public void popDismiss() {
				v.setSelected(!v.isSelected());
			}
		}).showBottom(titleButton);
		
	}

	@Override
	public Boolean showRightButton() {
		return true;
	}

	@Override
	public String showRightButtonStr() {
		return "筛选";
	}

	@Override
	public void rightButtonClick(View v) {
		updateMenuContent(RESULT_FORALL);
		menuDrawer.toggleMenu();
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
	
	@Override
	public String showBackButtonStr() {
		return "发现";
	}

	@Override
	public Boolean showBackButton() {
		return true;
	}

	@Override
	public void backButtonClick(View v) {
		finish();    
	}
	
//	@OnItemClick(R.id.listView)
	void listItemClick(AdapterView<?> parent, View view, int position,long id){
		CheckWorkDetailContent content = null;
		if(refreshInfo.requestStatus_.equals(MY_CHECKWORK)){
			 content = (CheckWorkDetailContent)
					parent.getAdapter().getItem(position);
			 CheckWorkPersonDetailActivity_.intent(CheckWorkActivity.this)
			 .detailContent(content)
			 .myUserCode(myUserCode)
			 .nowDate(nowDate)
			 .startForResult(START_FORSIGN);
		}else if(refreshInfo.requestStatus_.equals(ALL_CHECKWORK)){
			content = (CheckWorkDetailContent)parent.getAdapter().getItem(position);
			CheckWorkPersonDetailActivity_.intent(CheckWorkActivity.this)
			 .detailContent(content)
			 .myUserCode(myUserCode)
			 .nowDate(nowDate)
			 .startForResult(START_FORSIGN);
		}
		
	}
	
 OnItemClickListener onItemClickListener = new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
};	

	
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
////		super.onCreateContextMenu(menu, v, menuInfo);
//		menu.setHeaderTitle("miicaa");
//		
//	}

	private void requestData(Boolean isDefault){
		String beginDate = screenResult.nowBeginDate != null ? screenResult.nowBeginDate : "";
		String endDate = screenResult.nowEndDate != null ? screenResult.nowEndDate : "";
		String userName = AllUtils.listToString(screenResult.nowUserCodes);
		String unitName = screenResult.nowOrgCodes != null ? screenResult.nowOrgCodes : "";
//		String unitName = AllUtils.listToString(screenResult.nowOrgCodes);
		userName = userName == null ? "" : userName;
		unitName = unitName == null ? "" : unitName;
		String url = "/attendance/phone/attend/index";
		String def = isDefault ? "true" : "false";
		if(beginDate.length() == 0 && endDate.length() == 0 
				&&userName.length() == 0 && unitName.length() == 0
				){
			def = "true";
		}
		Log.d(TAG, "requestData param 's"+"...beginDate:"+beginDate+"...endDate:"+endDate+"...userName:"
				+"..."+userName+"...unitName:"+unitName+"...def:"+def);
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				if(data.getResultState() == ResultState.eSuccess){
					JSONObject jsonObject = data.getJsonObject();
					myUserCode = jsonObject.optString("userCode");
					Log.d(TAG, "onReponse(ResponseData data) data result:"+jsonObject);
					if(refreshInfo.requestStatus_.equals(MY_CHECKWORK)){
						saveMyCheckWorkContent(jsonObject);
					}else if(refreshInfo.requestStatus_.equals(ALL_CHECKWORK)){
						saveAllCheckWorkContent(jsonObject);
					}else if(refreshInfo.requestStatus_.equals(TONGJI_CHECKWORK)){
						saveTongjiCheckWorkContent(jsonObject);
					}
				}else{
					Log.d(TAG, "requestData falied :"+data.getMsg()+data.getCode());
					Toast.makeText(CheckWorkActivity.this,
							"请求失败:"+data.getMsg(),Toast.LENGTH_SHORT)
							.show();
				}
				pullToRefreshListView.onRefreshComplete();
				progressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("getDefault",def)
		.addParam("requestPage",refreshInfo.requestStatus_)
		.addParam("userCodes",userName)
		.addParam("unitCodes",unitName)
		.addParam("beginDate",beginDate)
		.addParam("endDate",endDate)
		.addParam("orderByClause","")
		.addParam("pageNo",String.valueOf(refreshInfo.pageCount))
		.addParam("pageLength","30")
		.notifyRequest();
	}
	
	
	@Background
	 void saveMyCheckWorkContent(JSONObject jsonObject){
		 nowDate = jsonObject.optLong("now");
		JSONObject statisticsStatus = jsonObject.optJSONObject("statisticsStatus");
		JSONArray attendanceList = jsonObject.optJSONArray("attendanceList");
		JSONObject todayAttendance = jsonObject.optJSONObject("todayAttendance");
		HashMap<String, String> todayContentMap  = new HashMap<String, String>();
		CheckWorkTongjiContents tongjiMap = null;
		ArrayList<CheckWorkDetailContent> detailContents = new ArrayList<CheckWorkDetailContent>();
		if(todayAttendance != null)
	       todayContentMap = getTodayContentMap(todayAttendance);
		   String workTimeBegin = jsonObject.optString("workTimeBegin");
		   String remindIn = jsonObject.optString("remindIn");
		   if(workTimeBegin != null)
			   todayContentMap.put(MyCheckWorkAdapter.TODAYWORKTIMEBEGIN, workTimeBegin);
		   if(remindIn != null)
			   todayContentMap.put(MyCheckWorkAdapter.TODAYSIGNINREMIND, remindIn);
		   
		   String remindOut = jsonObject.optString("remindOut");
		   if(remindOut != null)
			   todayContentMap.put(MyCheckWorkAdapter.TODAYSIGNOUTREMIND, remindOut);
		   
	       String workTimeEnd = jsonObject.optString("workTimeEnd");
	       if(workTimeEnd != null)
	    	   todayContentMap.put(MyCheckWorkAdapter.TODAYWORKTIMEEND, workTimeEnd);
	       
//		if(statisticsStatus != null )
		   tongjiMap = getTongjiMap(statisticsStatus);
		
		if(myCheckWorkAdapter.getTongjiContents() != null && tongjiMap != null){
			tongjiMap.timeType = myCheckWorkAdapter.getTongjiContents().timeType;
		}
		
		if(attendanceList != null)
		   detailContents = getCheckWorkDetailContents(attendanceList);
		
		refreshMyAdapter(todayContentMap,tongjiMap, detailContents,nowDate);
	}
	
	@Background
	 void saveAllCheckWorkContent(JSONObject jsonObject){
		 nowDate = jsonObject.optLong("now");
		JSONArray attendanceList = jsonObject.optJSONArray("attendanceList");
		ArrayList<CheckWorkDetailContent> detailContents = new ArrayList<CheckWorkDetailContent>();
		if(attendanceList != null)
			detailContents = getCheckWorkDetailContents(attendanceList);
		
		refreshAllAdapter(detailContents,nowDate);
	}
	
	@Background
	 void saveTongjiCheckWorkContent(JSONObject jsonObject){
		JSONArray statisticsStatus = jsonObject.optJSONArray("statisticsStatusList");
		ArrayList<CheckWorkTongjiContents> tongjiList = new ArrayList<CheckWorkTongjiContents>();
		if(statisticsStatus != null){
		for(int i = 0; i < statisticsStatus.length(); i++){
			JSONObject status = statisticsStatus.optJSONObject(i);
			CheckWorkTongjiContents tongjiContent = getTongjiMap(status);
			tongjiList.add(tongjiContent);		
		}
		}
		refreshTongjiAdapter(tongjiList);
	}
	
	
	@UiThread
	void refreshMyAdapter(HashMap<String, String> todayContentMap,CheckWorkTongjiContents tongjiMap,
			ArrayList<CheckWorkDetailContent> detailContents,Long nowDateStr){
		if(refreshInfo.isRefresh){
		myCheckWorkAdapter.refresh(todayContentMap,tongjiMap, detailContents,nowDateStr);
		}else{
			myCheckWorkAdapter.addMore(todayContentMap,tongjiMap, detailContents);
		}
	}
	
	@UiThread
	void refreshAllAdapter(ArrayList<CheckWorkDetailContent> detailContents,Long nowDateStr){
		if(refreshInfo.isRefresh){
			allCheckWorkAdapter.refesh(detailContents,nowDateStr);
		}else{
			allCheckWorkAdapter.addMore(detailContents);
		}
	}
	
	@UiThread
	void refreshTongjiAdapter(ArrayList<CheckWorkTongjiContents> tongjiList){
		if(refreshInfo.isRefresh){
			tongjiCheckWorkAdapter.refresh(tongjiList);
		}else{
			tongjiCheckWorkAdapter.addMore(tongjiList);
		}
	}
	
	private HashMap<String, String> getTodayContentMap(JSONObject jsonObject){
		HashMap<String, String> todayContentMap = new HashMap<String, String>();
		Long signInTime  = jsonObject.optLong("signInTime");
		Long signOutTime = jsonObject.optLong("signOutTime");
		
		String signInTimeStr = AllUtils.getTime(signInTime);
		String signOutTimeStr = AllUtils.getTime(signOutTime);
		Log.d(TAG, "getTodayContentMap():"+"signInTime:"+signInTime+
				"signOutTime:"+signOutTime);
		String signInRemind = null;
		String signOutRemind = null;
		if(signInTimeStr != null)
			todayContentMap.put(MyCheckWorkAdapter.TODAYSIGINDATE, signInTimeStr);
		if(signOutTimeStr != null)
			todayContentMap.put(MyCheckWorkAdapter.TODAYSIGNOUTDATE, signOutTimeStr);
		return todayContentMap;
	}
	
	private CheckWorkTongjiContents getTongjiMap(JSONObject statisticsStatus){
		CheckWorkTongjiContents statisticsContent = new CheckWorkTongjiContents() {
			
			@Override
			public void OnDateButtonClick(CheckScreenValue timeValue) {
				
			}
		};
		if(statisticsStatus == null){
			return statisticsContent;
		}
		String lateCount = statisticsStatus.optString("lateCount") == null ? "0" : 
			statisticsStatus.optString("lateCount"); 
		String leavedCount = statisticsStatus.optString("leaveEarlyCount") == null ? "0" : 
			statisticsStatus.optString("leaveEarlyCount"); 
		String noSignInCount = statisticsStatus.optString("noSignInCount") == null ? "0" : 
			statisticsStatus.optString("noSignInCount"); 
		String noSignOutCount = statisticsStatus.optString("noSignOutCount") == null ? "0" : 
			statisticsStatus.optString("noSignOutCount"); 
		String name = statisticsStatus.optString("userName");
		statisticsContent.save(lateCount, leavedCount, noSignInCount, noSignOutCount)
		.addName(name);
		return statisticsContent;
	}
	
	private ArrayList<CheckWorkDetailContent> getCheckWorkDetailContents(JSONArray attendanceList){
		ArrayList<CheckWorkDetailContent> checkWorkDetailContents = new 
				ArrayList<CheckWorkDetailContent>();
		for(int i = 0 ; i < attendanceList.length(); i ++){
			Log.d(TAG, "getCheckWorkDetailContents attend:"+attendanceList.optJSONObject(i));
			JSONObject jsonObject = attendanceList.optJSONObject(i);
			String name = jsonObject.optString("userName");
			String dateStr = jsonObject.optString("attendDateStr");
			Long signInTime = jsonObject.optLong("signInTime");
			String signInTimeStr = AllUtils.getTime(signInTime);
			Long signOutTime = jsonObject.optLong("signOutTime");
			String signOutTimeStr = AllUtils.getTime(signOutTime);
			String signInRemarksStr = jsonObject.optString("signInRemarks");
			String signOutRemarksStr = jsonObject.optString("signOutRemarks");
			String signId = jsonObject.optString("id");
			String signInStatusStr = jsonObject.optString("signInStatusStr");
			String signOutStatusStr = jsonObject.optString("signOutStatusStr");
			String userCode = jsonObject.optString("userCode");
			Long date = jsonObject.optLong("attendDate");
			String signInWhere = "";
			String signOutWhere = "";
			JSONArray signInAarray = jsonObject.optJSONArray("signInSiteStr");
			if(signInAarray != null){
			 signInWhere = signInAarray.optString(0);
			}
			JSONArray signOutArray = jsonObject.optJSONArray("signOutSiteStr");
			if(signOutArray != null){
			 signOutWhere = jsonObject.optJSONArray("signOutSiteStr").optString(0);
			}
			String signInCoordinate = jsonObject.optString("signInCoordinate");
			String signOutCoordinate = jsonObject.optString("signOutCoordinate");
			
			
			String signInLongitude = null;
			String signInLatgitude = null;
			String signOutLongitude = null;
			String signOutLatgitude = null;
			if(signInCoordinate != null && signInCoordinate.length() > 0){
				signInLatgitude = signInCoordinate.substring(signInCoordinate.indexOf(",")+1,signInCoordinate.length());
				signInLongitude = signInCoordinate.substring(0,signInCoordinate.indexOf(","));
			}
			if(signOutCoordinate != null && signOutCoordinate.length() > 0){
				signOutLatgitude = signOutCoordinate.substring(signOutCoordinate.indexOf(",")+1,signOutCoordinate.length());
				signOutLongitude = signOutCoordinate.substring(0,signOutCoordinate.indexOf(","));
			}
			
			ArrayList<String> signInFileIdList = new ArrayList<String>();
			ArrayList<String> signOutFileIdList = new ArrayList<String>();
			JSONArray signInFileArray = jsonObject.optJSONArray("signInPhotoList");
			JSONArray signOutFileArray = jsonObject.optJSONArray("signOutPhotoList");
			if(signInFileArray != null){
			for(int s = 0; s < signInFileArray.length(); s++){
			     JSONObject jsonFile = signInFileArray.optJSONObject(s);
			     String fileId = jsonFile.optString("fileId");
			     signInFileIdList.add(fileId);
			}
			}
			
			if(signOutFileArray != null){
				for(int s = 0; s < signOutFileArray.length(); s++){
				     JSONObject jsonFile = signOutFileArray.optJSONObject(s);
				     String fileId = jsonFile.optString("fileId");
				     signOutFileIdList.add(fileId);
				}
			}
			
			CheckWorkDetailContent detailContent = new CheckWorkDetailContent().build(name, dateStr, 
					signInTimeStr, signInWhere, signOutTimeStr, signOutWhere)
					.addBeizhu(signInRemarksStr, signOutRemarksStr)
					.addId(signId)
					.addDataUserCode(userCode)
					.addDate(date)
					.addsignInFileIdList(signInFileIdList)
					.addsignOutFileIdList(signOutFileIdList)
					.addStatusStr(signInStatusStr, signOutStatusStr)
					.addItude(signInLongitude, signInLatgitude, signOutLongitude, signOutLatgitude);
			checkWorkDetailContents.add(detailContent);
			
			
		}
		return checkWorkDetailContents;
	}
}
 