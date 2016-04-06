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
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.detail.DetailContentInfo;
import com.miicaa.detail.DetailDiscussAdapter;
import com.miicaa.detail.DetailDiscussAdapter.OnDelDiscussListener;
import com.miicaa.detail.DetailDiscussFragment;
import com.miicaa.detail.OnDiscussButtonClickListener;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.home.MatterCell;
import com.miicaa.home.ui.org.MatterHttp;
import com.miicaa.home.ui.org.MatterHttp.OnMatterResult;
import com.miicaa.utils.AddMoreListView;
import com.miicaa.utils.AddMoreListView.OnRefreshLoadingMoreListener;
import com.miicaa.utils.AllUtils;

public class DiscussFragment extends SuperFragment implements OnDiscussButtonClickListener,OnDelDiscussListener{
	private View rootView;
	Bundle mBundle;
	AddMoreListView list;
	RelativeLayout progressBar;
	TextView progressText;
	Integer pagesize = 20;
	Integer pagenum;
	Html.ImageGetter imageGetter;
	DetailDiscussAdapter dadapter;
	 ArrayList<String> codes = new ArrayList<String>();
	 private boolean isLoading = false;;
	ArrayList<DetailContentInfo> infos = new ArrayList<DetailContentInfo>();
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		mBundle = getArguments();
		 imageGetter = DetailDiscussFragment.setExpressIcon(getActivity(),null);
		 list.setFooterDividersEnabled(true);
		 dadapter = new DetailDiscussAdapter(getActivity(),AllUtils.discuss,imageGetter);
		 dadapter.setOnDiscussButtonClickListener(this);
		 dadapter.setOnDelDiscussListener(this);
		 list.setOnRefreshListener(morelistener);
		 list.setAdapter(dadapter);
		// refreshD(dadapter.getCount());
		 progressBar.setVisibility(View.VISIBLE);
		doBackground();
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
		progressBar = (RelativeLayout)rootView.findViewById(R.id.progressBar);
		progressText = (TextView)rootView.findViewById(R.id.progressText);
		list = (AddMoreListView) rootView.findViewById(R.id.listview);
		return rootView;
	}
	
	public void doBackgrounds(){
		if(isLoading)
			return;
		isLoading = true;
		  String url = "/home/phone/thing/getdiscussion";
		  pagenum = pagenum == null||pagenum == 1 ? 1:pagenum+1;
		  new RequestAdpater() {
				
				@Override
				public void onReponse(ResponseData data) {
					isLoading = false;
					progressBar.setVisibility(View.GONE);
					progressText.setVisibility(View.GONE);
					
					if(data.getResultState() == ResultState.eSuccess){
						jsonToData(data.getJsonArray());
					}else{
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

	protected void jsonToData(JSONArray discussarray) {
		if(discussarray == null || discussarray.length() == 0){
			list.onLoadMoreComplete(true);
			return;	
		}
		
		for(int i = 0;i < discussarray.length(); i++){
			JSONObject discuss = discussarray.optJSONObject(i);
			DetailContentInfo info = new DetailContentInfo(discuss).save();
			infos.add(info);
			}
		
		refreshD(discussarray.length());
		

		
	}
	
	void refreshD(int count){
		if(dadapter != null){
			if(count < 20){
				list.onLoadMoreComplete(true);
				}else{
					list.onLoadMoreComplete(false);
				}
			((ReportDetailActivity)getActivity()).addDiscussNum(infos.size());
			dadapter.refresh(infos);
		}
	}
	
	//分页，上拉加载更多
	AddMoreListView.OnRefreshLoadingMoreListener morelistener = new OnRefreshLoadingMoreListener() {
		
		@Override
		public void onRefresh() {
			
		}
		
		@Override
		public void onLoadMore() {
			doBackground();
		}
	};

	private void refresh(){
		infos.clear();
		pagenum = 1;
		doBackground();
	}

	@Override
	public void deldiscuss() {
		refresh();
		
		if(onDelDiscussListener != null)
			onDelDiscussListener.deldiscuss();
		
	}

	@Override
	public void adddiscuss() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDiscussClick(String dataId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nameClick(int position, String name) {
		((ReportDetailActivity)getActivity()).discussFoot.setName(position,name);
		
	}

	public void sendDiscussDiscuss(int position, String content) {
		MatterHttp.addDiscussDiscuss(new OnMatterResult() {
			
			@Override
			public void onSuccess(String msg, Object obj) {
				if(onDelDiscussListener != null)
					onDelDiscussListener.adddiscuss();
				resetDiscuss();
			}
			
			@Override
			public void onFailure(String msg) {
				// TODO Auto-generated method stub
				
			}
		}, infos.get(position).id,  Tools.getDiscussText(content), Tools.getDiscussHTML(content));
		
	}

	public void sendDiscuss(String content) {
		progressText.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.VISIBLE);
		if(content == null || "".equals(content.trim()))
			return;
		MatterHttp.requestDiscuss(new OnMatterResult() {
			
			@Override
			public void onSuccess(String msg, Object obj) {
				if(onDelDiscussListener != null)
					onDelDiscussListener.adddiscuss();
				resetDiscuss();
			}
			
			@Override
			public void onFailure(String msg) {
				
			}
		}, mBundle.getString("dataId"), MatterCell.WORKREPORTTYPE, Tools.getDiscussText(content), Tools.getDiscussHTML(content));
		
	}
	
	void resetDiscuss(){
		if(isLoading)
			return;
		
		pagenum = null;
		infos.clear();
		dadapter.refresh(infos);
		progressBar.setVisibility(View.VISIBLE);
		doBackgrounds();
	}
	
	OnDelDiscussListener onDelDiscussListener;
	public void setOnDelDiscussListener(OnDelDiscussListener listener){
		this.onDelDiscussListener = listener;
	}

	@Override
	public void doBackground() {
		resetDiscuss();
		
	}
}
