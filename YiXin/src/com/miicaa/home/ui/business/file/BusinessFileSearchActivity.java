package com.miicaa.home.ui.business.file;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.common.base.BusiFileSearchFunction;
import com.miicaa.common.base.BusiFileSearchFunction.OnSearchCallBack;
import com.miicaa.common.base.MatterRequest;
import com.miicaa.common.base.MatterRequest.MatterHomeCallBackListener;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;

public class BusinessFileSearchActivity extends Activity {

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		intance = null;
	}

	public static final int SEARCH_CODE = 1;
	public static final int RENAMEFILE_CODE = 2;
	public static final int NEWFILE_CODE = 3;
	public static final int DELETEFILE_CODE = 4;
	public static final int EDITFILE_CODE = 5;
	public static final int MOVETOFILE_CODE = 6;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode != Activity.RESULT_OK)
			return;
		BusinessFileActivity.getIntance().isRefresh();
		switch (requestCode) {
		
		case BusinessFileActivity.PHOTOCHECK:
			resetList();
			showRefresh();
			setPageCount(1);
			requestMatter(serachMap);
			break;
		case NEWFILE_CODE:
			// initParams();
			resetList();
			showRefresh();
			setPageCount(1);
			requestMatter(serachMap);
			break;
		case RENAMEFILE_CODE:
			String name = data.getStringExtra("name");
			matterCell.updateName(name, 0);
			break;
		case EDITFILE_CODE:
			String nameS = data.getStringExtra("json");
			matterCell.updateName(nameS, 1);
			break;
		case MOVETOFILE_CODE:
			
			isRefresh();
			//jsonObjects.remove(matterCell.selectedPosition);
			//matterCell.refresh(jsonObjects, isManager);
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		
			
		super.onBackPressed();
		
	}

	Context mContext;
	
	ListView refreshView;
	PullToRefreshListView listView;
	TextView empty;
	boolean isManager = false;
	public ProgressDialog progressDialog;
	private static BusinessFileSearchActivity intance;


	int pageCount = 1;// 翻页获取数据
	BusinesssFileCell matterCell;
	ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();// 所有要解析的数据
	BusiFileSearchFunction searchFunction;
	RelativeLayout serchLayout;
	String parentId = "";

	HashMap<String, String> serachMap = new HashMap<String, String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.business_file_search_activity);

		this.mContext = this;
		intance = this;
		initData();
		initUI();

	}
	
	public static BusinessFileSearchActivity getIntance(){
		return intance;
	}

	private void initData() {
		
		progressDialog = new ProgressDialog(this);
		
		progressDialog.setTitle("miicaa");
		progressDialog.setCanceledOnTouchOutside(false);
		
		/*
		 * 初始化请求参数
		 */
		initParams();

		
		
		
		searchFunction = new BusiFileSearchFunction(mContext,
				"/enterprisedoc/phone/efile/list");
		searchFunction.setSearchHint("查找文件名或上传者");
		searchFunction.setParam(serachMap);
		searchFunction.setSearchCallBack(new OnSearchCallBack() {

			@Override
			public void textChange(Boolean isText) {
				
				
			}

			@Override
			public void search(ResponseData data) {
				
				if (data.getResultState() == ResultState.eSuccess
						|| data.getResponseStatus() == 200) {
					listView.onRefreshComplete();
					resetList();
					isManager = data.getMRootData().optBoolean("isManager");
					callBackInRequest(data.getMRootData().optJSONArray(
							"filelist"));
				} else {
					showWhat(data.getCode(), data.getMsg());
				}
			}

			@Override
			public void deltext() {
				
				
				//resetList();
				//matterCell.refresh(jsonObjects);
				
			}

			@Override
			public void addMore(ResponseData data) {
				
				if (data.getResultState() == ResultState.eSuccess
						|| data.getResponseStatus() == 200) {
					isManager = data.getMRootData().optBoolean("isManager");
					callBackInRequest(data.getMRootData().optJSONArray(
							"filelist"));
				} else {
					showWhat(data.getCode(), data.getMsg());
					
				}
			}

			@Override
			public void clearRefresh() {
				
			}
		});

	}

	private void initUI() {
		
		serchLayout = (RelativeLayout) findViewById(R.id.business_file_searchKuang);
		empty = (TextView) findViewById(R.id.business_file_search_list_view_empty);
		empty.setText("");
		initViews();
	}

	void initViews() {
		serchLayout.addView(searchFunction.getSearchView());
		listView = (PullToRefreshListView) findViewById(R.id.business_file_search_list_view);
		listView.setOnRefreshListener(new HowWillIrefresh());
		listView.setMode(Mode.BOTH);
		refreshView = listView.getRefreshableView();
		refreshView
				.setEmptyView(empty);
		refreshView.setFastScrollEnabled(false);
		refreshView.setFadingEdgeLength(0);
		matterCell = new BusinesssFileCell(mContext, jsonObjects,true);
		refreshView.setAdapter(matterCell);

		matterCell.refresh(jsonObjects,false);
	}

	void showWhat(int code, String msg) {
		listView.onRefreshComplete();
		if (code == -1) {
			Toast.makeText(mContext, "没有数据！", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, "网络错误！请稍后再试！" + msg, Toast.LENGTH_SHORT)
					.show();
		}

	}

	void initParams() {
		serachMap.put("pageNo", "1");
		serachMap.put("menu", 1 + "");
		serachMap.put("star", "false");

	}

	

	// 如果遇到刷新则要删除所有list数据
	void resetList() {
		jsonObjects.clear();
		pageCount = 1;// 刷新的时候复位pageCount;
		setPageCount(1);
		
	}

	void showRefresh() {
		progressDialog.setMessage("正在加载，请稍后...");
		progressDialog.show();
	}

	
	public void requestMatter(HashMap<String, String> map) {
	
		String url = "/enterprisedoc/phone/efile/list";
		
		MatterRequest.requestMatterHome(url, map,
				new MatterHomeCallBackListener() {

					@Override
					public void callBack(ResponseData data) {
						progressDialog.dismiss();
						empty.setText("抱歉，找不到符合条件的文件，请换个搜索条件试试吧！");
						if (data.getResultState() == ResultState.eSuccess
								|| data.getResponseStatus() == 200) {
							
							if (listView != null) {
								listView.onRefreshComplete();
								isManager = data.getMRootData().optBoolean("isManager");
								callBackInRequest(data.getMRootData()
										.optJSONArray("filelist"));
							
							}
						
						} else {
							if (listView != null)
								listView.onRefreshComplete();
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
			empty.setText("已显示全部内容!");
		}

		
		
		matterCell.refresh(jsonObjects,isManager);
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
			// showRefresh();
			/*
			 * 重置分页数据
			 */
			resetList();
			
				searchFunction.setPageCount(1);
				searchFunction.search();
			

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			
			pageCount += 1;
			setPageCount(pageCount);
			
			
				searchFunction.search();
			
		}

	}

	void setPageCount(int count) {
		
			serachMap.put("pageNo", count+"");
		

	}

	

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
	
	

	public void requestFolder(JSONObject json) {
		
		String optString = json.optString("id");
		parentId = optString;
		String name = json.optString("name");
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.putAll(serachMap);
		map.remove("name");
		map.put("navi", optString);
		map.put("title", name);
		Intent intent = new Intent();
		intent.putExtra("map", map);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	
	public String getParentId(){
		return parentId;
	}
	
	public void isRefresh() {
		jsonObjects.clear();
	
	
			setPageCount(1);
			requestMatter(serachMap);
			
		
	}
}
