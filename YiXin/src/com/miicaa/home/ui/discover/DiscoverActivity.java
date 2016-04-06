package com.miicaa.home.ui.discover;

import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.base.request.response.BaseResopnseData.OnResponseListener;
import com.miicaa.home.BaseKeyVaule;
import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;
import com.miicaa.home.ui.SearchFunctionView;
import com.miicaa.home.ui.SearchFunctionView.OnSearchFounctionViewListener;
import com.miicaa.home.ui.SearchFunctionView_;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.discover.dailywork.DailyWorkTopScreenView;
import com.miicaa.home.ui.home.TopScreenView;
import com.miicaa.home.ui.home.TopScreenView.RemoveScreenTypeListener;
import com.miicaa.home.ui.menu.BaseScreenView.OnBaseScreenClickLinstener;
import com.miicaa.home.ui.menu.SelectPersonInfo;
import com.miicaa.utils.AllUtils;

@EActivity
public class DiscoverActivity extends MainActionBarActivity{
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1!=Activity.RESULT_OK)
			return;
		switch(arg0){
		case 100:
			mRequest.resetPage();
			mRequest.executeRequest();
			break;
		}
	}

	private String TAG = "DiscoverActivity";
	private DiscoverState mState;
	
	public final static int SELECT_ORIGITOR = 0x17;
	
	private DiscoverReponseData responseData;
	private DiscoverRequest mRequest;
	private Toast mToast;
	private DiscoverAdapter mDiscoverAdapter;
    private ProgressDialog mProgressDialog;
	
	
	@Extra
	int discoverType;

//	@ViewById(R.id.listView)
	PullToRefreshListView pullToRefreshListView;
	public TopScreenView topScreenView;
	
	MenuDrawer menuDrawer;
	ListView listView;
	LinearLayout noData;
	SearchFunctionView searchFunctionView;
	Button rightButton;
	Button titleButton;
	ScreenMenu screenMenuView;
	ScreenResult mScreenResult;
	
	
	
   @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mState = DiscoverStateFactory.build(discoverType);
		mDiscoverAdapter = mState.getAdapter(this);
		mScreenResult = mState.getScreenResult();
		searchFunctionView = SearchFunctionView_.build(this);
		searchFunctionView.setSearHint("查找标题");
		searchFunctionView.setOnSearchFunctionViewListener(new OnSearchFounctionViewListener() {
			
			@Override
			public void onSearchClick(CharSequence text) {
				mRequest.changeSearchParam(text.toString());
				mProgressDialog.setMessage("正在搜索,请稍后...");
				mProgressDialog.show();
				mRequest.executeRequest(responseData);
			}
			
			@Override
			public void onScreenClick() {
				mScreenResult.convertToTmpType();
				screenMenuView.refreshViewChage(mScreenResult.tmpReportTypeList, mScreenResult.getPepoleNames());
				menuDrawer.toggleMenu();
			}
			
			@Override
			public void onDelSearchTextClick(CharSequence text) {
				mRequest.changeSearchParam(text.toString());
				mRequest.executeRequest(responseData);
			}
		});
		screenMenuView = mState.getScreenMenu(this);
		screenMenuView.refreshScreenViewGroup(new OnBaseScreenClickLinstener() {
			
			@Override
			public <T extends BaseKeyVaule> void sreenCancle(T type) {
				mScreenResult.removeReportType(type);
			}
			
			@Override
			public <T extends BaseKeyVaule> void screenClick(T type) {
				mScreenResult.addReportType(type);
			}
		});
		
		menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.RIGHT);
		menuDrawer.setContentView(R.layout.activity_discover,0);
		menuDrawer.setMenuView(screenMenuView);
		menuDrawer.getMenuContainer().setBackgroundColor(Color.TRANSPARENT);
		menuDrawer.setDropShadow(R.drawable.menu_draw_bg_shape);
		menuDrawer.setDropShadowSize((int)getResources().getDimension(R.dimen.shadow_width));
		menuDrawer.setMaxAnimationDuration(3000);  
		menuDrawer.setHardwareLayerEnabled(false);
		menuDrawer.setMenuSize((int)getResources().getDimension(R.dimen.slidingmenu_offset));
		menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		menuDrawer.setOffsetMenuEnabled(true);
		noData = (LinearLayout) menuDrawer.findViewById(R.id.noData);
		topScreenView = (DailyWorkTopScreenView) menuDrawer.findViewById(R.id.topScreenView);
		topScreenView.createView(mScreenResult)
		.openOrCreateView();
		topScreenView.setRemoveScreenTypeListener(new RemoveScreenTypeListener() {
			
			@Override
			public void removeType() {
				mRequest.changeOriginators(mScreenResult.getPeopleCodes());
				mRequest.changeReportTypes(mScreenResult.getReportCodes());
				mRequest.resetPage();
				mRequest.executeRequest(responseData);
			}
			
			@Override
			public Object initObjects() {
				return null;
			}
		});
		pullToRefreshListView = (PullToRefreshListView) menuDrawer.findViewById(R.id.listView);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("miicaa");
		afterView();
	}

	
//	@AfterViews
	void afterView(){
		/*得到请求和回调所需要的类*/
		setDiscoverRequestInfo();
		responseData.setOnResponseListener(new OnResponseListener() {
			
			@Override
			public void onResponseError(String errMsg, String errCode) {
				if(mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				pullToRefreshListView.onRefreshComplete();
				mToast.setText("网络连接错误:"+errMsg);
				mToast.show();
			}
			
			@Override
			public void onReponseComplete() {
				Log.d(TAG, "onReponseComplete isFirstPage:"+mRequest.isFirstPage());
				responseData.discoverList.clear();
				if(mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				if(mRequest.isFirstPage()){
					if(mRequest.getSearchText().equals("")&&responseData.discoverList.size()<1){
						mState.noData(noData);
					}
					mDiscoverAdapter.refresh(responseData.discoverList);
				}
				else
				mDiscoverAdapter.add(responseData.discoverList);
				
				pullToRefreshListView.onRefreshComplete();
			}
			
			@Override
			public void onProgress(float count) {
				
			}
			
			@Override
			public void onNoneData() {
				if(mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				pullToRefreshListView.onRefreshComplete();
				String searchText = mRequest.getSearchText();
				if(mRequest.isFirstPage()){
				if(searchText != null && searchText.length() > 0 ){
					mToast.setText("抱歉，找不到符合条件的工作报告，请换个搜索条件试试吧");
				} else{
					mToast.setText("没有数据");
				}
				mDiscoverAdapter.refresh(responseData.discoverList);
			   }else{
					mToast.setText("没有更多数据");
				}
				mToast.show();
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				
			}
		});
		pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		pullToRefreshListView.setOnRefreshListener(new OnDiscoverRefreshListener());
		listView = pullToRefreshListView.getRefreshableView();
		listView.addHeaderView(searchFunctionView);
		listView.setAdapter(mDiscoverAdapter);
		mProgressDialog.setMessage("正在加载，请稍后...");
		mProgressDialog.show();
		mRequest.executeRequest(responseData);
	}
	
	@Override
	public String showBackButtonStr() {
		return null;
	}

	@Override
	public Boolean showBackButton() {
		return true;
	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public Boolean showTitleButton() {
		return true;
	}

	@Override
	public String showTitleButtonStr() {
		return "发现";
	}

	@Override
	public void titleButtonClick(View v) {
		
	}

	@Override
	public Boolean showRightButton() {
		return true;
	}

	@Override
	public String showRightButtonStr() {
		return null;
	}

	@Override
	public void rightButtonClick(View v) {
		
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}

	@Override
	protected void activityYMove() {
		AllUtils.hiddenSoftBorad(this);
		super.activityYMove();
	}
	
	
	class OnDiscoverRefreshListener implements PullToRefreshBase.OnRefreshListener2<ListView>{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			mRequest.resetPage();
			mRequest.executeRequest(responseData);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			mRequest.addPageCount();
			mRequest.executeRequest(responseData);
		}
		
	}
	
	public void  setDiscoverRequestInfo(){
		Button title = (Button) getCenterButton();
		title.setText(mState.getTitle());
		mState.setRightButton((Button)getRightButton());
		responseData = mState.getResponseData();
		mRequest = mState.getRequest();
	}
	
	@OnActivityResult(SELECT_ORIGITOR)
	void onSelectOrigitorResult(int resultCode,Intent data){
		if(resultCode == RESULT_OK){
		ArrayList<ContactViewShow> origitors = data.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
		mScreenResult.tmpPeopleList.clear();
		for(ContactViewShow c : origitors){
			mScreenResult.addPeopeList(new SelectPersonInfo(c.name, c.code));
		}
		screenMenuView.setorigiatorText(mScreenResult.getPepoleNames());
		}
	}
	
	public void toggleMenu(){
		menuDrawer.toggleMenu();
	}
	
	public void showDialog(){
		mProgressDialog.setMessage("正在筛选,请稍后");
		mProgressDialog.show();
	}

}