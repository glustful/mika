package com.miicaa.home.ui.report;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.miicaa.detail.DeatilTrendsAdapter;
import com.miicaa.detail.DetailDiscussFragment;
import com.miicaa.detail.DetailTrendsInfo;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.utils.AddMoreListView;
import com.miicaa.utils.AddMoreListView.OnRefreshLoadingMoreListener;

public class DynamicFragment extends SuperFragment {
	private View rootView;
	Bundle mBundle;
	AddMoreListView listView;
	RelativeLayout progressBar;
	Integer pagesize = 20;
	Integer pagenum;
	Html.ImageGetter imageGetter;
	DeatilTrendsAdapter tadapter;
	ArrayList<String> codes = new ArrayList<String>();
	ArrayList<DetailTrendsInfo> tinfos = new ArrayList<DetailTrendsInfo>();
	private boolean isLoading = false;;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		mBundle = getArguments();
		imageGetter = DetailDiscussFragment.setExpressIcon(getActivity(),null);
		 tadapter = new DeatilTrendsAdapter(getActivity());
		 listView.setFooterDividersEnabled(true);
		 listView.setAdapter(tadapter);
       // refreshT(tadapter.getCount());
		 listView.setOnRefreshListener(morelistener);
		 progressBar.setVisibility(View.VISIBLE);
		 doBackgrounds();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.report_discuss_fragment_view, null);
            
        }
		progressBar = (RelativeLayout) rootView.findViewById(R.id.progressBar);
		listView = (AddMoreListView) rootView.findViewById(R.id.listview);
		return rootView;
	}
	
	

	public void doBackgrounds(){
		if(isLoading)
			return;
		String url = "/home/phone/thing/getactivitylst";
		isLoading  = true;
		pagenum = pagenum == null ? 1:pagenum+1;
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.GONE);
				isLoading = false;
				if(data.getResultState() == ResultState.eSuccess){
					
					jsonToData(data.getJsonArray());
				}
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}.setUrl(url)
		.addParam("dataId",mBundle.getString("dataId"))
		.addParam("pageNo",pagenum.toString())
		.addParam("pageSize",pagesize.toString())
		.notifyRequest();
	}

void jsonToData(JSONArray discussarray){

	if(discussarray == null || discussarray.length() == 0){
		listView.onLoadMoreComplete(true);

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

int mCount = 0;
void refreshT(int position){
	mCount = tinfos.size();
	if(onDaynamicChangeListener != null)
		onDaynamicChangeListener.onCountChange(mCount);
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
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		doBackground();
	}
};


public interface OnDaynamicChangeListener{
	void onCountChange(int count);
}

OnDaynamicChangeListener onDaynamicChangeListener;
public void setOnDaynmicChangeListener(OnDaynamicChangeListener listener){
	onDaynamicChangeListener = listener;
}



void resetDiscuss(){
	if(isLoading)
		return;
	progressBar.setVisibility(View.VISIBLE);
pagenum = null;
tinfos.clear();
tadapter.refresh(tinfos);
doBackgrounds();
}

@Override
public void doBackground() {
	
	
	resetDiscuss();
	
}


}
