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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;

@EActivity(R.layout.layout_pay_receipt_activity)
public class ReceiptRecordActivity extends Activity {
	public static ReceiptRecordActivity instance;
	@ViewById(R.id.pay_headTitle)
	TextView headTitle;//标题
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.receipt_current_useable)
	TextView currentUseable;
	@ViewById(R.id.receipt_total)
	TextView total;
	
	Context mContext;
	ListView refreshView;
	@ViewById(R.id.pay_receipt_list_view)
	PullToRefreshListView listView;

	HashMap<String, String> paramMap;
	int pageCount = 1;// 翻页获取数据
	ReceiptAdapter matterCell;
	ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();// 所有要解析的数据
	private double mCurrentUseable = 0;
	
	@Click(R.id.pay_cancleButton)
	void cancel(){
		finish();
	}

	@Click(R.id.open_receipt)
	void open(){
		ReceiptOpeningActivity_.intent(mContext)
		.mCurrentUseable(mCurrentUseable)
		.start();
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}
	
	
	@AfterInject
	void initData(){
		this.mContext = this;
		instance = this;
	
		
		/*
		 * 初始化请求参数
		 */
		initParams();
	}

	void initParams() {
		paramMap = new HashMap<String, String>();
		
		paramMap.put("pageNo", pageCount + "");
		

	}

	@AfterViews
	void initUI() {

		
		back.setText("返回");
		headTitle.setText("发票记录");
		matterCell = new ReceiptAdapter(mContext, jsonObjects);
		initViews();
		requestFirst();
	}
	
	void initViews() {

		listView.setOnRefreshListener(new HowWillIrefresh());
		listView.setMode(Mode.BOTH);
		refreshView = listView.getRefreshableView();
		refreshView.setFastScrollEnabled(false);
		refreshView.setFadingEdgeLength(0);
		refreshView.setAdapter(matterCell);

		matterCell.refresh(jsonObjects);
	}

	/*
	 * 第一次启动时
	 */
	void requestFirst() {
		resetList();
		PayUtils.showDialog(mContext);
		requestMatter();
	}

	// 如果遇到刷新则要删除所有list数据
	void resetList() {
		jsonObjects.clear();
		pageCount = 1;// 刷新的时候复位pageCount;
		setPageCount(1);
	}

	

	void setPageCount(int count) {

		paramMap.put("pageNo", count + "");

	}


	public void requestMatter() {// 请求办理页数据
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				PayUtils.closeDialog();

				if (data.getResultState() == ResultState.eSuccess) {
					
					if (listView != null) {
						listView.onRefreshComplete();
						
						initReceipt(data.getJsonObject());
						callBackInRequest(data.getJsonObject()
								.optJSONArray("invoiceList"));

					}

				} else {
					PayUtils.showToast(mContext, ""+data.getMsg(), 1000);
				}

				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}
		.setUrl(mContext.getString(R.string.pay_receipt_url))
		.addParam(paramMap)
		.notifyRequest();
		

	}
	
	protected void initReceipt(JSONObject jsonObject) {
		try{
			total.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(jsonObject.optDouble("invoiceAmount", 0)));
			mCurrentUseable  = jsonObject.optDouble("validInvoiceAmount", 0);
			currentUseable.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(mCurrentUseable));
		}catch(Exception e){
			e.printStackTrace();
		}
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

		matterCell.refresh(jsonObjects);
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
			//
			resetList();

			requestMatter();

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			
			pageCount += 1;
			setPageCount(pageCount);

			requestMatter();

		}

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
}
