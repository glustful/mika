package com.miicaa.home.ui.business.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.CustomPopup;
import com.miicaa.common.base.CustomPopup.OnCustomDismissListener;
import com.miicaa.common.base.MatterRequest;
import com.miicaa.common.base.MatterRequest.MatterHomeCallBackListener;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.photoGrid.PhotoGridContentActivity;
import com.miicaa.utils.fileselect.FileListActivity_;

public class BusinessFileActivity extends Activity {

	public static final int SEARCH_CODE = 1;
	public static final int RENAMEFILE_CODE = 2;
	public static final int NEWFILE_CODE = 3;
	public static final int DELETEFILE_CODE = 4;
	public static final int EDITFILE_CODE = 5;
	public static final int MOVETOFILE_CODE = 6;
	public static final int PHOTOCHECK = 7;
	private static BusinessFileActivity instance;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {
		case PHOTOCHECK:
			resetList();
			showRefresh();
			setPageCount(1);
			requestMatter(paramMap, true);
			break;
		case SEARCH_CODE:

			HashMap<String, String> map = (HashMap<String, String>) data
					.getSerializableExtra("map");
			map.put("pageNo", "1");
			requestFolder(map);
			break;
		case NEWFILE_CODE:
			// initParams();
			resetList();
			showRefresh();
			setPageCount(1);
			requestMatter(paramMap, true);
			break;
		case RENAMEFILE_CODE:
			String name = data.getStringExtra("name");
			matterCell.updateName(name, 0);
			break;
		case EDITFILE_CODE:
			//String nameS = data.getStringExtra("json");
			//if(!matterCell.updateName(nameS, 1)){
				resetList();
				
				setPageCount(1);
				requestMatter(paramMap, true);
			//}
			
			break;
		case MOVETOFILE_CODE:
			//mStack.peek().remove(matterCell.selectedPosition);
			HashMap<String, String> mp = new HashMap<String, String>();
			mp.put("pageNo", "1");
			mp.put("navi", data.getStringExtra("id"));
			mp.put("title", data.getStringExtra("name"));

			requestFolder(mp);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (isRight() && mParams.size() > 1) {
			listView.onRefreshComplete();
			
			mFolderName.pop();
			mParams.pop();
			paramMap.clear();
			paramMap.putAll(mParams.peek());
			try {
				parentId = paramMap.get("navi");
				if (parentId == null)
					parentId = "all";
			} catch (Exception e) {
				parentId = "all";
			}
			try {
				pageCount = Integer.parseInt(paramMap.get("pageNo"));
			} catch (Exception e) {
				pageCount = 1;
			}
			String name = mFolderName.peek();
			name = name.length()>10?name.substring(0, 10)+"...":name;
			mTitle.setText(name);
			if (mFolderName.size() == 1) {
				kinds.setVisibility(View.VISIBLE);
			}
			jsonObjects.clear();
			refreshView.setTag(1);
			
			matterCell.refresh(jsonObjects, isManager);
			resetList();
			showRefresh();
			requestMatter(paramMap, false);
			
		} else {

			super.onBackPressed();
		}
	}

	Context mContext;
	FrameLayout rootLayout;
	View rootLinear;
	ListView refreshView;
	PullToRefreshListView listView;
	public ProgressDialog progressDialog;
	String parentId = "all";
	String tmpParentId = "";
	String tmpParentName = "";
	HashMap<String, String> paramMap;
	boolean isRefresh = false;
	int pageCount = 1;// 翻页获取数据
	BusinesssFileCell matterCell;
	boolean isManager = false;
	ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();// 所有要解析的数据

	ImageButton kinds;
	RelativeLayout headLayout;

	CustomPopup.Builder mBuilder;
	ArrayList<PopupItem> items;

	Stack<String> mFolderName;
	Stack<HashMap<String, String>> mParams;
	TextView mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.business_file_main_activity);

		this.mContext = this;
		instance = this;
		initUI();
		initData();

	}

	public static BusinessFileActivity getIntance() {
		if (instance == null) {
			instance = new BusinessFileActivity();
		}
		return instance;
	}

	private void initData() {
		// codeMap = new HashMap<String, String>();
		progressDialog = new ProgressDialog(this);

		progressDialog.setTitle("miicaa");
		progressDialog.setCanceledOnTouchOutside(false);
		
		mFolderName = new Stack<String>();

		mParams = new Stack<HashMap<String, String>>();
		/*
		 * 初始化请求参数
		 */
		initParams();

		requestFirst();
		items = new ArrayList<PopupItem>();
		PopupItem item = new PopupItem("上传图片", "pic");
		items.add(item);
		item = new PopupItem("上传文件", "file");
		items.add(item);
		item = new PopupItem("搜索", "search");
		items.add(item);

		item = new PopupItem("取消", "cancel");
		items.add(item);

	}

	private void initUI() {
		headLayout = (RelativeLayout) findViewById(R.id.business_file_headlayout);
		kinds = (ImageButton) findViewById(R.id.business_file_kinds);

		rootLayout = (FrameLayout) findViewById(R.id.business_file_main_root);
		rootLinear = findViewById(R.id.business_file_main_linear);
		mTitle = (TextView) findViewById(R.id.business_file_headTitle);
		initViews();
	}

	void initViews() {

		listView = (PullToRefreshListView) findViewById(R.id.business_file_list_view);
		listView.setOnRefreshListener(new HowWillIrefresh());
		listView.setMode(Mode.BOTH);
		refreshView = listView.getRefreshableView();
		//refreshView
				//.setEmptyView(findViewById(R.id.business_file_list_view_empty));
		refreshView.setFastScrollEnabled(false);
		refreshView.setFadingEdgeLength(0);
		matterCell = new BusinesssFileCell(mContext, jsonObjects, false);
		refreshView.setAdapter(matterCell);

		matterCell.refresh(jsonObjects, false);
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

		paramMap = new HashMap<String, String>();
		cleanParam();

		paramMap.put("pageNo", pageCount + "");

		paramPush(paramMap);

	}

	/*
	 * 第一次启动时
	 */
	void requestFirst() {

		resetList();
		showRefresh();
		requestMatter(paramMap, false);
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

	void setPageCount(int count) {
		pageCount = count;
		paramMap.put("pageNo", count + "");

	}

	public void requestMatter(final HashMap<String, String> map,
			boolean isCurrent) {
		if (isCurrent){
			
			mParams.peek().clear();
			mParams.peek().putAll(map);
		}

		final HashMap<String, String> mClock = new HashMap<String, String>();
		mClock.putAll(map);
		
		refreshView.setTag(mClock);

		String url = "/enterprisedoc/phone/efile/list";

		MatterRequest.requestMatterHome(url, map,
				new MatterHomeCallBackListener() {

					@Override
					public void callBack(ResponseData data) {

						
						if (!mClock.equals(refreshView.getTag()))
							return;
						isManager = false;
						progressDialog.dismiss();
						
						// TODO Auto-generated method stub
						if (data.getResultState() == ResultState.eSuccess
								|| data.getResponseStatus() == 200) {

							if (listView != null) {
								listView.onRefreshComplete();
								isManager = data.getMRootData().optBoolean(
										"isManager");
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
		ArrayList<JSONObject> copy = new ArrayList<JSONObject>();
		if (workList != null && workList.length() > 0) {

			for (int j = 0; j < workList.length(); j++) {
				jsObject.add(workList.optJSONObject(j));
			}
			jsonObjects.addAll(jsObject);
			
		

		} else {
			Toast.makeText(mContext, "已显示全部内容！", Toast.LENGTH_SHORT).show();
		}
		refreshView.setTag(1);
		
		matterCell.refresh(jsonObjects, isManager);
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

			requestMatter(paramMap, true);

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

			pageCount += 1;
			setPageCount(pageCount);

			requestMatter(paramMap, true);

		}

	}

	public void bnClick(View view) {
		switch (view.getId()) {
		case R.id.business_file_cancleButton:
			onBackPressed();
		
			break;
		case R.id.business_file_headTitle:
			if(kinds.getVisibility()==View.VISIBLE){
				showKinds();
				
			}
			break;
		case R.id.business_file_kinds:
			showKinds();
			
			break;
		case R.id.business_file_uploadButton:
			if (isManager && items.size() < 5) {
				PopupItem itme = new PopupItem("新建文件夹", "new");
				items.add(3, itme);
			}
			BottomScreenPopup.builder(mContext)
					.setDrawable(R.drawable.white_color_selector)
					
					.setMargin(false).setItems(items)
					.setOnMessageListener(new OnMessageListener() {

						@Override
						public void onClick(PopupItem msg) {
							if (msg.mCode.equals("pic")) {
								uploadPic(parentId, "");
							} else if (msg.mCode.equals("file")) {
								uploadFile(parentId, "");
							} else if (msg.mCode.equals("search")) {
								Intent intent = new Intent(mContext,
										BusinessFileSearchActivity.class);
								((Activity) mContext).startActivityForResult(
										intent, SEARCH_CODE);
								((Activity) mContext)
										.overridePendingTransition(
												R.anim.my_slide_in_right,
												R.anim.my_slide_out_left);
							} else if (msg.mCode.equals("new")) {
								Intent intent = new Intent(mContext,
										BusinessFileNORActivity.class);
								intent.putExtra("type", "new");
								intent.putExtra("parentId", parentId);
								((Activity) mContext).startActivityForResult(
										intent, NEWFILE_CODE);
								((Activity) mContext)
										.overridePendingTransition(
												R.anim.my_slide_in_right,
												R.anim.my_slide_out_left);
							}

						}
					}).show();

			// testUpload();
			break;
		case R.id.business_file_pop_all:
			initParams();

			dismissPop();
			requestFirst();

			break;
		case R.id.business_file_pop_doc:
			initParamsDoc();
			requestFirst();
			break;
		case R.id.business_file_pop_pic:
			initParamsPic();
			requestFirst();
			break;
		case R.id.business_file_pop_video:
			initParamsVideo();
			requestFirst();
			break;
		case R.id.business_file_pop_star:
			initParamsStar();
			requestFirst();
			break;
		}
	}
	
	private void showKinds(){
		int height = listView.getHeight();//rootLinear.getHeight();
		kinds.setImageResource(R.drawable.an_up_flag_file);
		final int[] location = new int[2];
		headLayout.getLocationInWindow(location);

		mBuilder = CustomPopup.builder(mContext)
				.setContentView(R.layout.business_file_kinds)
				.setOnDismissListener(new OnCustomDismissListener() {

					@Override
					public void onDimiss() {
						kinds.setImageResource(R.drawable.an_down_flag_file);
					}
				});
		if (height > 0) {
			//height -= (location[1] + headLayout.getHeight());
			mBuilder.setHeight(height);
		}
		mBuilder.show(headLayout, Gravity.NO_GRAVITY, 0, location[1]
				+ headLayout.getHeight());
	}

	public void uploadPic(String id, String name) {
		tmpParentId = id;
		tmpParentName = name;
		Intent intent = new Intent(mContext, PhotoGridContentActivity.class);
		intent.putExtra("upload", true);
		((Activity) mContext).startActivity(intent);
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}

	public void uploadFile(String id, String name) {
		tmpParentId = id;
		tmpParentName = name;
		Intent intent = new Intent(mContext, FileListActivity_.class);
		intent.putExtra("upload", true);
		((Activity) mContext).startActivity(intent);
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}

	private void initParamsStar() {
		dismissPop();
		cleanParam();
		paramMap.put("menu", "5");
		paramMap.put("pageNo", "1");
		pageCount = 1;
		paramMap.put("type", "");
		paramMap.put("star", "true");
		paramPush(paramMap);
	}

	private void initParamsVideo() {
		dismissPop();
		cleanParam();
		paramMap.put("menu", "4");
		paramMap.put("pageNo", "1");
		pageCount = 1;
		paramMap.put("type", "20");
		paramMap.put("star", "false");
		paramPush(paramMap);
	}

	private void initParamsPic() {
		dismissPop();
		cleanParam();
		paramMap.put("menu", "3");
		paramMap.put("pageNo", "1");
		pageCount = 1;
		paramMap.put("type", "10");
		paramMap.put("star", "false");
		paramPush(paramMap);
	}

	private void initParamsDoc() {
		dismissPop();
		cleanParam();
		paramMap.put("menu", "2");
		paramMap.put("pageNo", "1");
		pageCount = 1;
		paramMap.put("type", "00");
		paramMap.put("star", "false");
		paramPush(paramMap);
	}

	private void paramPush(HashMap<String, String> map) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.putAll(map);
		mParams.push(param);
	}

	private void cleanParam() {
		
		mParams.clear();
		paramMap.clear();
		mFolderName.clear();
		mFolderName.push("企业文件");
	}

	private void dismissPop() {
		if (mBuilder != null) {
			mBuilder.dismiss();
			mBuilder = null;
		}
	}

	public String getParentId() {
		return parentId.equals("all") ? "" : parentId;
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}

	public void requestFolder(JSONObject json) {
		listView.onRefreshComplete();
		String optString = json.optString("id");
		parentId = optString;
		String name = json.optString("name");
		mFolderName.push(name);
		name = name.length()>10?name.substring(0, 10)+"...":name;
		mTitle.setText(name);
		kinds.setVisibility(View.GONE);
		resetList();
		showRefresh();
		HashMap<String, String> map = new HashMap<String, String>();
		
		matterCell.refresh(jsonObjects, isManager);
		map.putAll(paramMap);

		map.put("navi", optString);
		mParams.push(map);
		paramMap.clear();
		paramMap.putAll(map);
		requestMatter(paramMap, false);
	}

	public void requestFolder(HashMap<String, String> json) {
		listView.onRefreshComplete();
		if (json == null)
			return;
		
		parentId = json.get("navi");
		if(parentId.equals("all")||parentId.equals("")){
			kinds.setVisibility(View.VISIBLE);
			mTitle.setText("企业文件");
			initParams();
			requestFirst();
			return;
		}
		String name = json.get("title");
		name = name.equals("")?"全部文件":name;
		mFolderName.push(name);
		mParams.push(json);
		name = name.length()>10?name.substring(0, 10)+"...":name;
		mTitle.setText(name);
		kinds.setVisibility(View.GONE);
		resetList();
		showRefresh();
		
		matterCell.refresh(jsonObjects, false);

		paramMap.clear();
		paramMap.putAll(json);
		requestMatter(paramMap, false);
	}

	public void isRefresh() {
	
		jsonObjects.clear();
		
		if (tmpParentName.equals("")) {
			setPageCount(1);
			requestMatter(paramMap, true);
		} else {
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("navi", tmpParentId);
			param.put("title", tmpParentName);
			param.put("pageNo", "1");
			requestFolder(param);
			tmpParentId = "";
			tmpParentName = "";
		}
	}

	public String getTmpParentId() {

		return tmpParentId.equals("all") ? "" : tmpParentId;
	}

	public void setTmpParentId(String stringExtra, String stringExtra2, JSONObject jsonObject) {
		this.tmpParentId = stringExtra;
		this.tmpParentName = stringExtra2;
		
	}
	
	private boolean isRight(){
		if(mParams.size()==mFolderName.size()){
			
				return true;
		}
		return false;
	}
	
}
