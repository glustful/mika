package com.miicaa.detail;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.detail.DetailProgressAdapter.OnBacktoAddListViewListener;
import com.miicaa.detail.ProgressFootView.OnProgressClickListener;
import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.MatterInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.org.ArrangementPlan_;
import com.miicaa.utils.AddMoreListView;

@SuppressLint("ValidFragment")
@EFragment(R.layout.matter_do_progress)
public class DetailProgressFragment extends Fragment{
	private static String TAG = "DetailProgressFragment";
	
	View rootView;
	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("oncreateview");
		if(rootView==null){
			rootView = inflater.inflate(R.layout.matter_do_progress, null);
		}
		 ViewGroup parent = (ViewGroup) rootView.getParent();  
	        if (parent != null) {  
	            parent.removeView(rootView);  
	        }   
		return rootView;
	}
*/
	public static DetailProgressFragment instance ;
	final static String PUBLIC_ARRANGE = "1";
	public final static String SECRET_ARRANGE = "0";
	public final static String REPORT_TYPE = "9";
	final static int gridColums = 3;
	final static int gridRows = 2;
	Boolean isProgress = true;
	Boolean isDiscuss = true;
	Boolean isShow;
//	ExpressPageAdpater gridAdapter;
	ArrayList<GridView> gridViews;
	DetailProgressAdapter adapter;
	ProgressGridAdapter gridadapter;
	ArrayList<ProgressListInfo> pInfos ;
	ArrayList<PrgressDoPeople> doinfos;
	ArrayList<ProgressListInfo> showInfos;
	 
	 Context context;
	 OnTabCountListener tabListener;
	ArrayList<ProgressListInfo> wInfos;
	ProgressTongjiInfo tongji;
	Boolean isShowGrid = false;//显示人员
	Boolean isShowList = false;
	@ViewById(R.id.grid)
	MyGridView gridview;
//	@ViewById(R.id.gridpager)
//	ViewPager gridPager;
	@ViewById(R.id.head_img)
	ImageView headImg;
	@ViewById(R.id.head_name)
	TextView headName;	
	@ViewById(R.id.matterNodo)
	TextView noDoView;
	@ViewById(R.id.matterDoing)
	TextView doingView;
	@ViewById(R.id.matterComplete)
	TextView completeView;
//	@ViewById(R.id.progressFootview)
//	ProgressFootView_ footview;
	@ViewById(R.id.nodoLayout)
	ImageView nodo;
	@ViewById(R.id.todoLayout)
	ImageView todo;
	@ViewById(R.id.doneLayout)
	ImageView done;
	@FragmentArg
	String dataId;
	@FragmentArg
	String operateGroup;
	@FragmentArg
	MatterInfo mInfo;
	@ViewById(R.id.progressList)
	DetailProgressListView listview;
     @SuppressLint("ValidFragment")
//	public DetailProgressFragment(OnTabCountListener listener) {
//		// TODO Auto-generated constructor stub
//    	 this.tabListener = listener;
//	}
	@AfterInject
	void afterInject(){
		instance = this;
//		gridViews = new ArrayList<GridView>();
//		gridAdapter = new ExpressPageAdpater(gridViews);
		pInfos = new ArrayList<ProgressListInfo>();
		wInfos = new ArrayList<ProgressListInfo>();
		doinfos   = new ArrayList<DetailProgressFragment.PrgressDoPeople>();
		showInfos = new ArrayList<ProgressListInfo>();
		
		doBackground();
	}
	
	public static DetailProgressFragment getInstance(){
		return instance;
	}
	@AfterViews
	void init(){
		
		adapter = new DetailProgressAdapter(getActivity());
		gridadapter = new ProgressGridAdapter(getActivity());
		listview.setAdapter(adapter);
		adapter.setOnBacktoAddListViewListener(new OnBacktoAddListViewListener() {
			
			@Override
			public void back() {
				AddMoreListView.setListViewHeightBasedOnChildren(listview);
			}
		});
		gridview.setAdapter(gridadapter);
		gridview.setOnItemClickListener(gridClick);
		refreshlist(pInfos,true);
		if(tongji != null){
		numToDo(tongji);
		}
		this.context = getActivity();
		
		if(isShow != null){
			adapter.setShowDelete(isShow);
		}
//		if(mInfo != null)
//		writeProgress(mInfo.getArrangeType());
		
	}
	@SuppressWarnings("serial")
//	@Background
	 void doBackground(){
		
		  String url = "/home/phone/thing/getprogressnew";
		  new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				// TODO Auto-generated method stub
				if(data.getResultState() == ResultState.eSuccess){
					jsonToCache(data);
				}else{
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("dataId",dataId)
		.notifyRequest();
	}
	
	@Background
	void jsonToCache(ResponseData data){
		pInfos.clear();
		Log.d(TAG, "progress json:"+data.getData());
		JSONObject dataobject = data.getJsonObject();
		
		DetailProgressInfo info = new DetailProgressInfo(dataobject.optJSONArray("progressList"),
				dataobject.optJSONObject("tongji")).save();
		if(info.progressList != null){
		pInfos.addAll(info.progressList);
		}
		
		tongji = info.tongjiInfo;
		if(adapter != null){
		refreshlist(pInfos,true);
		numToDo(tongji);
		}
		
	}
	@UiThread
	void numToDo(ProgressTongjiInfo info){
		SpannableStringBuilder buidler = new SpannableStringBuilder();
		String content = info.notdonum+"人未填写";
		buidler.append(content);
		int end = String.valueOf(info.notdonum).length();
		buidler.setSpan(new ForegroundColorSpan(Color.RED), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		noDoView.setText(buidler);
		
		buidler.clear();
		content = info.todonum+"人已填写";
		buidler.append(content);
		end = String.valueOf(info.notdonum).length();
		if(getActivity() == null){
			Log.d("DetailProgressFragment", "getActivity is null !");
		}
		buidler.setSpan(new ForegroundColorSpan(Color.parseColor("#5CA9B9")), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		doingView.setText(buidler);
		
		buidler.clear();
		content = info.donenum+"人已完成";
		buidler.append(content);
		end = String.valueOf(info.notdonum).length();
		buidler.setSpan(new ForegroundColorSpan(Color.parseColor("#97CCE6")), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		completeView.setText(buidler);
	}
	
	@Click(R.id.matterNodo)
	void nodoClick(){
		if(tongji == null || tongji.notdonum == 0){
			return;
		}
		if(!nodo.isShown()){
		nodoList();
		}
		showGrid(nodo);
		todo.setVisibility(View.GONE);
		done.setVisibility(View.GONE);
		resetShow();
		
	}
	
	@Click(R.id.matterDoing)
	void todoClick(){
		if(tongji == null || tongji.todonum == 0){
			return;
		}
		if(!todo.isShown()){
		todoList();
		}
		showGrid(todo);
		nodo.setVisibility(View.GONE);
		done.setVisibility(View.GONE);
		resetShow();
	}
	@Click(R.id.matterComplete)
	void doneClick(){
		if(tongji == null || tongji.donenum == 0){
			return;
		}
		if(!done.isShown()){
		doneList();
		}
		showGrid(done);
		todo.setVisibility(View.GONE);
		nodo.setVisibility(View.GONE);
		resetShow();
	}
	
	
    @UiThread
	void showGrid(ImageView v){
//    	showInfos.clear();
		if(v.isShown())
		{
			v.setVisibility(View.GONE);
		}else{
			v.setVisibility(View.VISIBLE);
		}
	}
	@Background
	void nodoList(){
		doinfos.clear();
		if(tongji.notdos == null || tongji.notdos.size()<1){
			refreshGrid(doinfos);
			return;
		}
//		ArrayList<PrgressDoPeople> doinfos = new ArrayList<DetailProgressFragment.PrgressDoPeople>();
//		for(int i = 0 ; i < pInfos.size();i++){
			for(ProgressTongjiInfo.DoStatus tj : tongji.notdos){
					PrgressDoPeople info = new PrgressDoPeople(tj.username,
							tj.usercode,tj.star);
					doinfos.add(info);
			}
		refreshGrid(doinfos);

	}
	
	@Background
	void todoList(){
		doinfos.clear();
		if(tongji.todos == null || tongji.todos.size()<1){
			refreshGrid(doinfos);
			return;
		}
			for(ProgressTongjiInfo.DoStatus tj : tongji.todos){
					PrgressDoPeople doinfo = new PrgressDoPeople(tj.username,
							tj.usercode,tj.star);
					doinfos.add(doinfo);
				}	
		refreshGrid(doinfos);

	}
	
	@UiThread
	void doneList(){
		doinfos.clear();
		
//		for(int i = 0 ; i < pInfos.size();i++){
		if(tongji.dones == null || tongji.dones.size()<1){
			refreshGrid(doinfos);
			return;
		}
			for(ProgressTongjiInfo.DoStatus tj : tongji.dones){
					PrgressDoPeople info = new PrgressDoPeople(tj.username,
							tj.usercode,tj.star);
					doinfos.add(info);
					continue;
				}	
		refreshGrid(doinfos);

	}
	
	@UiThread
	void refreshlist(ArrayList<ProgressListInfo> infos,Boolean isAll){
		adapter.refresh(infos,isAll);
		AddMoreListView.setListViewHeightBasedOnChildren(listview);
	}

	
	@UiThread
	void showPeople(int position,View v){
		for(int i = 0 ; i< pInfos.size();i++){
			if(doinfos.get(position).usercode.equals(pInfos.get(i).usercode)){
				 showInfos.add(pInfos.get(i));
				 break;
			}else{
				
			}
		}
//		adapter.refresh(showInfos,false);
		refreshlist(showInfos, false);
	}
	@UiThread
	void noshowPeople(int position,View v){
		for(int i = 0 ; i< showInfos.size();i++){
			if(doinfos.get(position).usercode.equals(showInfos.get(i).usercode)){
				 showInfos.remove(i);
				 break;
			}
		}
		Log.d("DetailProgressFragment", "noshowPeople showInfos size is:"+showInfos.size());
		refreshlist(showInfos, false);
	}
	
	@UiThread
	void resetShow(){
		if(!nodo.isShown()&&!todo.isShown()&&!done.isShown()){
			gridview.setVisibility(View.GONE);
//			adapter.refresh(pInfos);//回复总体情况
			refreshlist(pInfos, true);
			gridShowMap.clear();
		}else{
			gridview.setVisibility(View.VISIBLE);
		}
	}
	
	OnProgressClickListener listener = new OnProgressClickListener() {
		
		@Override
		public void remindClick() {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void progressClick() {
			// TODO Auto-generated method stub
			DiscussProgressActivity_.intent(getActivity()).dataid(dataId).start();
		}
		
		@Override
		public void planClick() {
			// TODO Auto-generated method stub
//			Intent intent = new Intent(getActivity(), ArrangementPlan.class);
//			Bundle bundle = new Bundle();
//			intent.putExtra("bundle", bundle);
//			openActivityToPlan(intent);
			ArrangementPlan_.intent(context).clientcode(mInfo.getClientName())
			.dataid(dataId)
			.todoid(mInfo.getTodoId())
			.start();
		}
		
	};
	
	
	HashMap<String, Boolean> gridShowMap = new HashMap<String, Boolean>();
   android.widget.AdapterView.OnItemClickListener gridClick = new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String tag = gridadapter.getItem(position);
		if(!gridShowMap.containsKey(tag) || !gridShowMap.get(tag)){
	view.setBackgroundColor(Color.parseColor("#F1F6FA"));
	showPeople(position, view);
	gridShowMap.put(tag, true);
	
}else{
	gridShowMap.remove(tag);
	view.setBackgroundColor(Color.parseColor("#ffffff"));
	noshowPeople(position, view);
	if(gridShowMap.size() == 0)
//		adapter.refresh(pInfos);
		refreshlist(pInfos,false);
	else
	{
	resetShow();
	}
	
}
	}
};
	
	@UiThread
	void refreshGrid(ArrayList<PrgressDoPeople> infos){
		gridadapter.refresh(infos,gridShowMap);
	}
	
	
	public class PrgressDoPeople{
		String username;
		String usercode;
		Integer star;
		public PrgressDoPeople(String username,String usercode,Integer star){
			this.username = username;
			this.usercode = usercode;
			this.star = star;
		}
	}
	
	
	public void putMatterInfo(MatterInfo mInfo){
		this.mInfo = mInfo;
//		writeProgress(mInfo.getArrangeType());
	}
	//判断什么时候填写jin
	
	@UiThread
	public void setProgressCountListener(OnTabCountListener tabListener){
		this.tabListener = tabListener;
	}
	
	public void showDelete(Boolean h){
		isShow = h ;
	}
	

	
}
