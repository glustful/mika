package com.miicaa.home.ui.announcement;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.common.base.MatterRequest;
import com.miicaa.common.base.MatterRequest.MatterHomeCallBackListener;
import com.miicaa.common.base.SearchFunction;
import com.miicaa.common.base.SearchFunction.OnSearchCallBack;
import com.miicaa.home.HiddenSoftActivity;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.pay.PayUtils;

@EActivity(R.layout.announcement_main_view)
public class AnnouncementActivity extends HiddenSoftActivity {
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode!=Activity.RESULT_OK)
			return;
		searchFunction.setSearchText("");
		requestFirst();
	}
	
	ListView refreshView;
	ProgressState state;
	SearchFunction searchFunction;

	ImageButton screenView;
	RelativeLayout serchLayout;
	Context mContext;

	@AfterInject
	void afterInject() {
		this.mContext = this;
		state = ProgressState.eManual;

		/*
		 * 初始化请求参数
		 */

		initParams();

	}

	@ViewById(R.id.title)
	TextView title;
	@ViewById(R.id.calendar_yet_list_view)
	PullToRefreshListView listView;
	@ViewById(R.id.newtask)
	Button mNewTask;
	@ViewById(R.id.calendar_yet_back)
	Button back;

	@Click(R.id.calendar_yet_back)
	void backToPre() {

		finish();
	}

	@AfterViews
	void initData() {

		mNewTask.setVisibility(View.INVISIBLE);
		title.setText("公告");
		back.setText("发现");
		adapter = new AnnounceAdapter(mContext, jsonObjects);
		initViews();
		requestFirst();
	}

	HashMap<String, String> paramMap;
	int pageCount = 1;// 翻页获取数据
	AnnounceAdapter adapter;
	ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();// 所有要解析的数据

	void initViews() {

		listView.setOnRefreshListener(new HowWillIrefresh());
		listView.setMode(Mode.BOTH);
		refreshView = listView.getRefreshableView();
		View headView = LayoutInflater.from(mContext).inflate(
				R.layout.matter_home_view, null);
		refreshView.addHeaderView(headView);
		initSearch(headView);
		refreshView.setFastScrollEnabled(false);
		refreshView.setFadingEdgeLength(0);
		refreshView.setAdapter(adapter);
		/*
		 * 再次回到办理页由于fragmentadapter会自动销毁页面
		 */
		adapter.refresh(jsonObjects);
	}

	private void initSearch(View headView) {
		searchFunction = new SearchFunction(mContext,
				mContext.getString(R.string.announcement_list_url), headView, true);
		searchFunction.setParam(paramMap);
		searchFunction.setHint("请输入公告的标题");
		searchFunction.setSearchCallBack(new OnSearchCallBack() {

			@Override
			public void textChange(Boolean isText) {
				System.out.println("istext="+isText);
				if (isText) {

					state = ProgressState.eSearch;
				} else {

					state = ProgressState.eManual;
				}
			}

			@Override
			public void search(ResponseData data) {
				
				if (data.getResultState() == ResultState.eSuccess) {
					listView.onRefreshComplete();
					resetList();
					callBackInRequest(data.getJsonArray());
				} else {
					PayUtils.showToast(mContext, data.getMsg(), 3000);
				}
			}

			@Override
			public void deltext() {
				
				state = ProgressState.eManual;
				showRefresh();
				resetList();
				requestMatter(paramMap);
			}

			@Override
			public void addMore(ResponseData data) {
				
				if (data.getResultState() == ResultState.eSuccess) {
					callBackInRequest(data.getJsonArray());
				} else {
					PayUtils.showToast(mContext, data.getMsg(), 3000);
				}
			}

			@Override
			public void clearRefresh() {
				
				state = ProgressState.eManual;
				requestFirst();
			}
		});

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void requestMatter(HashMap<String, String> map) {// 请求办理页数据
		String url = getString(R.string.announcement_list_url);

		MatterRequest.requestMatterHome(url, map,
				new MatterHomeCallBackListener() {

					@Override
					public void callBack(ResponseData data) {
						PayUtils.closeDialog();
						if (listView != null) {
							listView.onRefreshComplete();

							if (data.getResultState() == ResultState.eSuccess) {

								callBackInRequest(data.getJsonArray());

							} else {
								PayUtils.showToast(mContext, data.getMsg(),
										3000);
							}

						}
					}
				});

	}

	void requestOut() {
		resetList();
		showRefresh();
		state = ProgressState.eManual;
		requestMatter(paramMap);
	}

	// 数据解析
	void callBackInRequest(JSONArray workList) {

		ArrayList<JSONObject> jsObject = new ArrayList<JSONObject>();

		if (workList != null && workList.length() > 0) {

			for (int j = 0; j < workList.length(); j++) {
				jsObject.add(workList.optJSONObject(j));
			}
			jsonObjects.addAll(jsObject);

		} else {
			PayUtils.showToast(mContext, "数据已加载完成", 3000);
		}

		adapter.refresh(jsonObjects);
		listView.onRefreshComplete();
	}

	class HowWillIrefresh implements
			PullToRefreshBase.OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			String label = DateUtils.formatDateTime(mContext,
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);

			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

			/*
			 * 重置分页数据
			 */
			resetList();
			if (state == ProgressState.eManual) {
				requestMatter(paramMap);
			} else if (state == ProgressState.eSearch) {
				searchFunction.setPageCount(1);
				searchFunction.search();
			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

			
			if (state == ProgressState.eManual) {
				pageCount += 1;
				setPageCount(pageCount);
				requestMatter(paramMap);
			} else if (state == ProgressState.eSearch) {
				searchFunction.search();
			}
		}

	}

	void initParams() {
		paramMap = new HashMap<String, String>();

		paramMap.put("pageCount", "20");
		paramMap.put("pageNum", pageCount + "");
		paramMap.put("readType", "all");
		paramMap.put("searchText", "");
	}

	void setPageCount(int count) {

		if (state == ProgressState.eManual) {
			paramMap.put("pageNum", count + "");
		}

	}

	// 如果遇到刷新则要删除所有list数据
	void resetList() {
		jsonObjects.clear();
		pageCount = 1;// 刷新的时候复位pageCount;
		setPageCount(1);
	}

	void showRefresh() {
		PayUtils.showDialog(mContext);
	}

	/*
	 * 第一次启动时
	 */
	void requestFirst() {
		resetList();
		showRefresh();
		requestMatter(paramMap);
	}

	enum ProgressState {
		eSearch, eScreen, eManual
	}

	void resetManual() {
		state = ProgressState.eManual;
		searchFunction.clearSearch();
	}

}
