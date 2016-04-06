package com.miicaa.home.ui.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.common.base.MatterRequest;
import com.miicaa.common.base.MatterRequest.MatterHomeCallBackListener;
import com.miicaa.common.base.SearchFunction;
import com.miicaa.common.base.SearchFunction.OnSearchCallBack;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.home.MatterCell;
import com.miicaa.home.ui.home.MatterType;
import com.miicaa.home.ui.matter.MatterEditor;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.AuthorityUtils.AuthorityState;
import com.yxst.epic.yixin.MyApplication;

@EFragment(R.layout.calendar_fram_yet_fragment)
public class FramCalendarFragment extends Fragment{
	
	private static String TAG = "FramCalendarFragment";
	
	@Override
	public void onResume() {
		
		super.onResume();
		requestFirst();
	}

	ListView refreshView;
	public static FramCalendarFragment instance;
	public ProgressDialog progressDialog;
	HashMap<String,String> codeMap;//筛选条件参数
	ProgressState state;
	SearchFunction searchFunction;
	
	
	ImageButton screenView;
	RelativeLayout serchLayout;
	@AfterInject
	void afterInject(){
		instance = this;
		
		codeMap = new HashMap<String, String>();
		progressDialog = new ProgressDialog(getActivity());
		state = ProgressState.eManual;
		progressDialog.setTitle("miicaa");
		progressDialog.setCanceledOnTouchOutside(false);
		/*
		 * 初始化请求参数
		 */
		    
		Bundle args = getArguments();
		isSubTask = args.getBoolean("isSubTask");
		parentId = args.getString("parentId");
		String type = args.getString("viewType");
		
		initParams();
		if(type != null&&!type.equals("")){
			setType(type);
		}
	}
    public static FramCalendarFragment getInstance(){
    	return instance;
    }
    
    boolean isSubTask = false;
    String parentId = "";
    @ViewById(R.id.title)
    TextView title;
	@ViewById(R.id.calendar_yet_list_view)
	PullToRefreshListView listView;
	@ViewById(R.id.newtask)
	Button mNewTask;
	@Click(R.id.calendar_yet_back)
	void backToPre(){
		this.getActivity().setResult(Activity.RESULT_OK);
		this.getActivity().finish();
	}
	@Click(R.id.newtask)
	void newTask(){
		Intent intent = new Intent(getActivity(), MatterEditor.class);
        Bundle bundle = new Bundle();
        bundle.putString("dataType",getArguments().getString("dataType"));
        bundle.putString("matterType",getArguments().getString("arrangeType"));
        bundle.putString("editType","01");
        bundle.putString("id","");
        bundle.putString("parentId", parentId);
        intent.putExtra("bundle",bundle);
        startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.my_slide_in_right,R.anim.my_slide_out_left);
	}
	@AfterViews
	void initData(){
	   if(isSubTask){
		  // mNewTask.setVisibility(View.INVISIBLE);
		   title.setText("子任务");
	   }else{
		  // mNewTask.setVisibility(View.INVISIBLE);
		   title.setText("未计划的");
	   }
		matterCell = new MatterCell(getActivity(), jsonObjects, MatterType.eDo);
		initViews();
		resetList();
		showRefresh();
	}
	
	HashMap<String, String> paramMap;
	int pageCount = 1;//翻页获取数据
	MatterCell matterCell ;
	ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();//所有要解析的数据
	
	
	void initViews(){
		//serchLayout.addView(searchFunction.getSearchView());
//		listView = (PullToRefreshListView)rootView.findViewById(R.id.matter_list_view);
		
		listView.setOnRefreshListener(new HowWillIrefresh());
		listView.setMode(Mode.BOTH);
		refreshView = listView.getRefreshableView();
		View headView = LayoutInflater.from(getActivity()).inflate(R.layout.matter_home_view, null);
		refreshView.addHeaderView(headView);
		initSearch(headView);
		refreshView.setFastScrollEnabled(false);
		refreshView.setFadingEdgeLength(0);
		refreshView.setAdapter(matterCell);
		/*
		 * 再次回到办理页由于fragmentadapter会自动销毁页面
		 */
		matterCell.refresh(jsonObjects);
	}
	
	
	private void initSearch(View headView) {
		String url = "/home/phone/thing/getallwork";
		if(isSubTask){
			url = getString(R.string.get_subtask_url);
		}
		 searchFunction = new SearchFunction(getActivity(), url,headView,true);
		    searchFunction.setParam(paramMap);
		    searchFunction.setHint("查找标题");
			searchFunction.setSearchCallBack(new OnSearchCallBack() {
				
				@Override
				public void textChange(Boolean isText) {
					// TODO Auto-generated method stub
					if(isText){
						
						state = ProgressState.eSearch;
					}else{
						
						state = ProgressState.eManual;
					}
				}
				
				@Override
				public void search(ResponseData data) {
					// TODO Auto-generated method stub
//					pageCount = se
					Log.d(TAG, "on search ok:"+data.getData());
					if(data.getResultState() == ResultState.eSuccess){
						listView.onRefreshComplete();
						resetList();
						callBackInRequest(data.getJsonObject().optJSONArray("subtaskList"));
					}else{
						showWhat(data.getCode(),data.getMsg());
					}
				}
				
				@Override
				public void deltext() {
					// TODO Auto-generated method stub
					state = ProgressState.eManual;
					showRefresh();
					resetList();
					requestMatter(paramMap);
				}

				@Override
				public void addMore(ResponseData data) {
					// TODO Auto-generated method stub
					if(data.getResultState() == ResultState.eSuccess){
						callBackInRequest(data.getJsonObject().optJSONArray("workList"));
					}else{
						showWhat(data.getCode(), data.getMsg());
//						Toast.makeText(getActivity(), ""+data.getMsg(), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void clearRefresh() {
					// TODO Auto-generated method stub
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
	
	public void requestMatter(HashMap<String,String> map){//请求办理页数据
		String url = "/home/phone/thing/getallwork";
		if(isSubTask){
			url = getString(R.string.get_subtask_url);
		}
		 MatterRequest.requestMatterHome(url,map,new MatterHomeCallBackListener() {
			
			@Override
			public void callBack(ResponseData data) {
				progressDialog.dismiss();
				if(listView != null){
					listView.onRefreshComplete();
					System.out.println(data.getMRootData());
				// TODO Auto-generated method stub
				if(data.getResultState() == ResultState.eSuccess){
					
					if(isSubTask){
						Log.d(TAG, "requestMatter isSubTask:"+isSubTask);
						Log.d(TAG,"data.getJsonObject()"+data.getJsonObject().isNull("canCreate"));
						if(AllUtils.PAYFOR_USER == 
								MyApplication.getInstance().getAuthority(AuthorityState.eSubTask)
								&&!data.getJsonObject().isNull("canCreate")&&data.getJsonObject().optBoolean("canCreate", false)){
							mNewTask.setVisibility(View.VISIBLE);
						}else{
							mNewTask.setVisibility(View.INVISIBLE);
						}
						callBackInRequest(data.getJsonObject().optJSONArray("subtaskList"));
					}else{
						callBackInRequest(data.getJsonObject().optJSONArray("workList"));
					}
					}
				}else{
					isSubTask = false;
					Toast.makeText(getActivity(), "网络错误:"+data.getMsg(), Toast.LENGTH_SHORT).show();
				}
				
			}
		}); 
		
	}
	
	void showWhat(int code,String msg){
		Log.d(TAG, "responseError code:"+code+"message:"+msg);
		if(code == -1){
			
			Toast.makeText(getActivity(), "没有数据！", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getActivity(), "网络错误！请稍后再试！"+msg, Toast.LENGTH_SHORT).show();
			}
		
	}
	
	/*
	 * 刷新啊，什么的就用这个来获取数据吧
	 */
	void requestOut(){
		resetList();
		showRefresh();
		state = ProgressState.eManual;
		requestMatter(paramMap);
	}
	
	//数据解析
	void callBackInRequest(JSONArray workList){
		
		
			ArrayList<JSONObject> jsObject = new ArrayList<JSONObject>();

            if (workList != null && workList.length() > 0) {
            	
                for (int j = 0; j < workList.length(); j++) {
                    jsObject.add(workList.optJSONObject(j));
                }
                jsonObjects.addAll(jsObject);
//                matterCell.refresh(jsonObjects);
    }else {
    	Log.d(TAG, "no data");
		Toast.makeText(getActivity(), "没有数据！", Toast.LENGTH_SHORT).show();
	}

    	
//    Toast.makeText(getActivity(),"没有数据啦！",1).show();
    //TODO:失败了要弹通知
		
            matterCell.refresh(jsonObjects);
            listView.onRefreshComplete();
	}
	
	class HowWillIrefresh implements PullToRefreshBase.OnRefreshListener2<ListView>{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			String label = DateUtils.formatDateTime(getActivity(),
                    System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);

            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//           showRefresh();
            /*
             * 重置分页数据
             */
            resetList();
            if(state == ProgressState.eManual){
			requestMatter(paramMap);
            }else if(state == ProgressState.eSearch){
            	searchFunction.setPageCount(1);
            	searchFunction.search();
            }else if(state == ProgressState.eScreen){
            	requestMatter(codeMap);
            }
			
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
				// TODO Auto-generated method stub
			if(isSubTask)
				return;
			pageCount += 1;
			setPageCount(pageCount);
			if(state == ProgressState.eManual){
				requestMatter(paramMap);
			}else if(state == ProgressState.eSearch){
				searchFunction.search();
			}else if(state == ProgressState.eScreen){
				requestMatter(codeMap);
			}
		}

		
		
	}
	
	void initParams(){
		paramMap  = new HashMap<String, String>();
		
        if(isSubTask){
        	paramMap.put("arrangeId", parentId);
        	return;
        }
        paramMap.put("pageSize", "20");
        paramMap.put("pageNo",pageCount+"");
        paramMap.put("type", "todo");
        paramMap.put("srcCode", "");
        paramMap.put("viewType", "");
       
        
	}
	
	void setPageCount(int count){
		if(isSubTask)
			return;
		if(state == ProgressState.eManual){
		paramMap.put("pageNo", count + "");
		}else if(state == ProgressState.eScreen){
			codeMap.put("pageNo", count + "");
		}
		
	}
	
	public void setType(String typeCode){
		if(isSubTask)
			return;
		paramMap.put("viewType", typeCode);
		
	}
	

	//如果遇到刷新则要删除所有list数据
	void resetList(){
		jsonObjects.clear();
		pageCount = 1;//刷新的时候复位pageCount;
		setPageCount(1);
	}
	
	void serScreenCode(HashMap<String, String> map){
		/*
		 * 将分页设为第一页
		 */
		resetList();
		int count = 0;
		for(Map.Entry<String, String> m: map.entrySet()){
			if(!"".equals(m.getValue())){
				break;
			}
			count++;
		}
		if(count == map.size()){
			state = ProgressState.eManual;
			requestMatter(paramMap);
			
		}
		/*
		 * 改变成筛选状态
		 */
		else{
		state = ProgressState.eScreen;
		codeMap.clear();
		codeMap.putAll(paramMap);
		codeMap.putAll(map);
		codeMap.put("pageSize", "20");
        codeMap.put("pageNo",pageCount+"");
		progressDialog.setMessage("正在筛选，请稍后...");
        progressDialog.show();
		requestMatter(codeMap);
		}
		
	}
	
	
	void showRefresh(){
		 progressDialog.setMessage("正在刷新，请稍后...");
         progressDialog.show();
	}
	
	/*
	 * 第一次启动时
	 */
	void requestFirst(){
		resetList();
		showRefresh();
		requestMatter(paramMap);
	}
	
	public enum ProgressState{
		eSearch,
		eScreen,
		eManual
	}
	
	
	void resetManual(){
		state = ProgressState.eManual;
		searchFunction.clearSearch();
	}
	
	
	
	
	
}
