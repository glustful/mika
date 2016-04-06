package com.miicaa.home.ui.pay;

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

@EActivity(R.layout.layout_pay_bill_activity)
public class BillRecordActivity extends Activity {
	
	 /**
     * 套餐
     */
    public static final String GOOD_TYPE_SET = "00";
    
    /**
     * 商品
     */
    public static final String GOOD_TYPE_GOOD = "10";
	
	@ViewById(R.id.pay_headTitle)
	TextView headTitle;// 标题
	@ViewById(R.id.pay_cancleButton)
	Button back;
	
	ListView refreshView;
	@ViewById(R.id.pay_bill_list_view)
	PullToRefreshListView listView;
	Context mContext;
	HashMap<String, String> paramMap;
	int pageCount = 1;// 翻页获取数据
	ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();// 所有要解析的数据
	BillRecordAdapter mAdapter;

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
		headTitle.setText("订单记录");
		
	
		mAdapter = new BillRecordAdapter(mContext, jsonObjects);
		initViews();
	}

	void initViews() {

		listView.setOnRefreshListener(new HowWillIrefresh());
		listView.setMode(Mode.BOTH);
		refreshView = listView.getRefreshableView();
		refreshView.setFastScrollEnabled(false);
		refreshView.setFadingEdgeLength(0);
		//refreshView.setDividerHeight(10);
		 refreshView.setAdapter(mAdapter);

		 mAdapter.refresh(jsonObjects);
		 PayUtils.showDialog(mContext);
		 requestMatter();
	}

	void initParams() {
		paramMap = new HashMap<String, String>();
		paramMap.put("pageNo", pageCount + "");

	}

	void resetList() {
		jsonObjects.clear();
		pageCount = 1;
		paramMap.put("pageNo", String.valueOf(1));
	}

	public void requestMatter() {// 请求办理页数据
		String url = mContext.getString(R.string.pay_order_url);
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

	@Override
	public void finish() {
		super.finish();

		PayMainActivity_.intent(mContext)
		.flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
		.start();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
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
			paramMap.put("pageNo", String.valueOf(pageCount));
			requestMatter();

		}

	}
}
