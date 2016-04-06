package com.miicaa.home.ui.enterprise;

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
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.common.base.MatterRequest;
import com.miicaa.common.base.MatterRequest.MatterHomeCallBackListener;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.pay.PayUtils;

@EActivity(R.layout.layout_pay_bill_activity)
public class MyServiceActivity extends Activity {
	
	 
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resetList();
		requestMatter();
	}



	@ViewById(R.id.pay_headTitle)
	TextView headTitle;// 标题
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.pay_commitButton)
	Button commit;
	
	ListView refreshView;
	@ViewById(R.id.pay_bill_list_view)
	PullToRefreshListView listView;
	Context mContext;
	HashMap<String, String> paramMap;
	int pageCount = 1;// 翻页获取数据
	int pageLength = 10;
	ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();// 所有要解析的数据
	MyServiceAdapter mAdapter;

	@Click(R.id.pay_cancleButton)
	void cancel() {
		finish();
	}

	@AfterInject
	void initData() {
		this.mContext = this;
	
		initParams();
	}

	@AfterViews
	void initUI() {

		back = (Button) findViewById(R.id.pay_cancleButton);
		back.setText("返回");
		headTitle.setText("我的服务");
		
	
		mAdapter = new MyServiceAdapter(mContext, jsonObjects);
		initViews();
	}

	void initViews() {

		listView.setOnRefreshListener(new HowWillIrefresh());
		listView.setMode(Mode.BOTH);
		refreshView = listView.getRefreshableView();
		refreshView.setFastScrollEnabled(false);
		refreshView.setFadingEdgeLength(0);
		
		refreshView.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
		refreshView.setDividerHeight(30);
		listView.setPadding(30, 30, 30, 30);
		 refreshView.setAdapter(mAdapter);
		 

		 mAdapter.refresh(jsonObjects);
		 PayUtils.showDialog(mContext);
		
	}

	void initParams() {
		paramMap = new HashMap<String, String>();
		paramMap.put("pageNum", pageCount + "");
		paramMap.put("pageLength", pageLength+"");
	}

	void resetList() {
		jsonObjects.clear();
		pageCount = 1;
		paramMap.put("pageNum", String.valueOf(1));
	}

	public void requestMatter() {// 请求办理页数据
		String url = mContext.getString(R.string.enterprice_myservice_url);
		MatterRequest.requestMatterHome(url, paramMap,
				new MatterHomeCallBackListener() {

					@Override
					public void callBack(ResponseData data) {
						PayUtils.closeDialog();
						if (data.getResultState() == ResultState.eSuccess) {
							if (listView != null) {
								listView.onRefreshComplete();
								callBackInRequest(data.getJsonArray());
								
							}
							
						} else {
							Toast.makeText(mContext, "网络错误:" + data.getMsg(),
									Toast.LENGTH_SHORT).show();
						}

					}
				});

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
			PayUtils.showToast(mContext, "数据已加载完成", 1000);
		}

		
		mAdapter.refresh(jsonObjects);
		listView.onRefreshComplete();
	}

	

	class HowWillIrefresh implements
			PullToRefreshBase.OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			
			String label = DateUtils.formatDateTime(mContext,
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);

			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			
			resetList();

			requestMatter();

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			
			pageCount += 1;
			paramMap.put("pageNum", String.valueOf(pageCount));
			requestMatter();

		}

	}
}
