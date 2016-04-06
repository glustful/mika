package com.miicaa.detail;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.text.Html;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.utils.AddMoreListView;
import com.miicaa.utils.AddMoreListView.OnRefreshLoadingMoreListener;
@EFragment(R.layout.matter_do_normal)
public class DetailTrendFragment extends Fragment{
	Integer pagesize = 20;
	Integer pagenum;
	Html.ImageGetter imageGetter;
	public static DetailTrendFragment instance;
	DeatilTrendsAdapter tadapter;
	ArrayList<String> codes = new ArrayList<String>();
	ArrayList<DetailTrendsInfo> tinfos = new ArrayList<DetailTrendsInfo>();
	@ViewById(R.id.listview)
	AddMoreListView listView;
	@FragmentArg
	String dataId;
	@FragmentArg
	String type;
	@FragmentArg
	String dataType;
	
	@AfterInject
	void afterInject(){
		instance = this;
		if(tinfos.size() == 0){
			doBackound();
			}
	}
	
	@AfterViews
	void afterView(){
		imageGetter = DetailDiscussFragment.setExpressIcon(getActivity(),null);
		 tadapter = new DeatilTrendsAdapter(getActivity());
		 listView.setFooterDividersEnabled(true);
		 listView.setAdapter(tadapter);
         refreshT(tadapter.getCount());
		 listView.setOnRefreshListener(morelistener);
	}
	
	public static DetailTrendFragment getInstance(){
		return instance;
	}
	
	@Background
	void doBackound(){
		
		String url = "/home/phone/thing/getactivitylst";
		
		pagenum = pagenum == null ? 1:pagenum+1;
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				// TODO Auto-generated method stub
				
				if(data.getResultState() == ResultState.eSuccess){

					jsonToData(data.getJsonArray());
				}
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}.setUrl(url)
		.addParam("dataId",dataId)
		.addParam("pageNo",pagenum.toString())
		.addParam("pageSize",pagesize.toString())
		.notifyRequest();
	}
	
	@UiThread
	void jsonToData(JSONArray discussarray){
//		listView.onRefreshComplete();
//		listView.onLoadMoreComplete(false);
		if(discussarray == null || discussarray.length() == 0){
			listView.onLoadMoreComplete(true);
//			listView.onRefreshComplete();
			return;	
		}
		listView.onLoadMoreComplete(false);
	
		for(int i = 0;i<discussarray.length();i++){
			JSONObject trend = discussarray.optJSONObject(i);
			DetailTrendsInfo info = new DetailTrendsInfo(trend).save();
			tinfos.add(info);
		}
		refreshT(discussarray.length());
	
}
	
	void refreshT(int position){
		if(tadapter != null){
			tadapter.refresh(tinfos);
			AddMoreListView.setListViewHeightBasedOnChildren(listView);
			}
		if(position < 20){
			listView.onLoadMoreComplete(true);
			}else{
				listView.onLoadMoreComplete(false);
			}
	}
	
	//分页，上拉加载更多
	AddMoreListView.OnRefreshLoadingMoreListener morelistener = new OnRefreshLoadingMoreListener() {
		
		@Override
		public void onRefresh() {
			
		}
		
		@Override
		public void onLoadMore() {
			doBackound();
		}
	};
	
	
	
	public interface OnTrendsChangeListener{
		void onTrendsCountChange(int count);
	}
		

void resetDiscuss(){
	pagenum = null;
	tinfos.removeAll(tinfos);
	doBackound();
}
}
	
