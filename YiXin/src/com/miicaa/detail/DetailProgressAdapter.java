package com.miicaa.detail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.detail.DetailProgressDetailAdapter.OnImgAddBackListener;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.utils.AddMoreListView;
import com.miicaa.utils.AddMoreListView.OnRefreshLoadingMoreListener;
import com.miicaa.utils.ViewHolder;

public class DetailProgressAdapter extends BaseAdapter {

	static String TAG = "DetailProgressAdapter";
	
	final static int NONEPROGRESS = 0;
	final static int PROGRESS = 1;
	
	 Context context;
	 String[] items;
	 String clientCode;
	 String progressId;
	 Boolean showDelete = false;
	 Boolean isAll = true;
	 Resources resources;
//	 ProgressListInfo pInfo;
	 ArrayList<ProgressListInfo> infos = new ArrayList<ProgressListInfo>();
	 OnBacktoAddListViewListener listener;
	 LayoutInflater inflater;
	
	 public DetailProgressAdapter(Context context) {
		// TODO Auto-generated constructor stub
		 this.context = context;
		 resources = context.getResources();
		 items = new String[]{"复制","删除"};
		 inflater = LayoutInflater.from(context);
	}
	 
	 public void setShowDelete(Boolean h){
		 showDelete = h;
		 
	 }
	
	 public void refresh(ArrayList<ProgressListInfo> infos,Boolean isAll){
		 this.infos.clear();
		 this.infos.addAll(infos);
		 this.isAll = isAll;
		 this.notifyDataSetChanged();
//		 if(listener != null){
//			 this.listener.back();
//		 }
	 }
	 
	 
	 
	@Override
	public int getItemViewType(int position) {
		return position == 0 ? NONEPROGRESS : PROGRESS;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getCount() {
		return infos != null ?infos.size()+1:1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertview, ViewGroup viewgroup) {
		int type = getItemViewType(position);
		switch (type) {
		case NONEPROGRESS:
			if(convertview == null)
			convertview = inflater.inflate(R.layout.normal_text_view, null);
			TextView textView = ViewHolder.get(convertview, R.id.textView);
			String str = isAll ? resources.getString(R.string.all_no_progress) : resources.getString(R.string.no_progress);
			textView.setHint(str);
			textView.setVisibility(infos.size() == 0 ? View.VISIBLE : View.GONE);
			WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) 
					textView.getLayoutParams();
			lp.setMargins(width/4, 0, 0, 0);
			textView.setLayoutParams(lp);
			break;
		case PROGRESS:
			final ProgressListInfo info = infos.get(position-1);
			
			if(convertview == null){
				convertview = LayoutInflater.from(context).inflate(R.layout.matter_do_progress_content, null);
				
			}
			ImageView headimg = ViewHolder.get(convertview, R.id.head_img);
			TextView headname = ViewHolder.get(convertview, R.id.head_name);
			RatingBar rating = ViewHolder.get(convertview, R.id.star);
			Button continueBtn = ViewHolder.get(convertview, R.id.continueProgress);
			TextView nodoProgressTxtView = ViewHolder.get(convertview, R.id.noProgressTextView);
			AddMoreListView listView = ViewHolder.get(convertview,R.id.list);
			Tools.setHeadImg(info.usercode,headimg );
			headname.setText(info.username);
			if(info.star != null && info.star >0 )
			{
				rating.setVisibility(View.VISIBLE);
			rating.setRating(info.star);
			}else{
				rating.setVisibility(View.GONE);
			}
			if("1".equals(info.btnflag) || "3".equals(info.btnflag))
				continueBtn.setVisibility((rating.getVisibility() == View.VISIBLE) ? 
						View.GONE : View.VISIBLE);
			else
				continueBtn.setVisibility(View.GONE);
			continueBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ContinueProgressActivity_.intent(context).dataId(info.detailist.get(position-1).dataid)
					.userCode(info.usercode)
					.userName(info.username)
					.startForResult(0);;
					
				}
			});
			
			
			if(info.detailist == null || info.detailist.size() == 0){
				listView.setVisibility(View.GONE);
				nodoProgressTxtView.setVisibility(View.VISIBLE);
			}
			else{
				listView.setVisibility(View.VISIBLE);
				nodoProgressTxtView.setVisibility(View.GONE);
			}
			
			listView.setMore(info.havemore);
			addList(listView,info,position-1);
			break;
		default:
			break;
		}
		
		return convertview;
	}
	
	
	void addList(final AddMoreListView list,final ProgressListInfo info,final int position){
		final DetailProgressDetailAdapter adapter = new DetailProgressDetailAdapter(context);
		
		list.setAdapter(adapter);
		adapter.setOnImgAddBackListener(new OnImgAddBackListener() {
			
			@Override
			public void imgAddBack() {
				// TODO Auto-generated method stub
				list.setOpenView(true);
//				listener.back();
			}
		});
		adapter.showDelete(showDelete);
		
		list.setOnRefreshListener(new OnRefreshLoadingMoreListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				loadMore(list,adapter, info,position);
				
//				list.setOpenView(true);
			}
		});
		adapter.refresh(info.detailist);
		if(info.detailist != null)
		Log.d(TAG, "infoDetailList"+"......"+info.detailist.size());
		list.setOpenView(true);
		
	}
//
//	@Override
//	public void onRefresh() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onLoadMore() {
//		// TODO Auto-generated method stub
//		
//	}
	
	//分页。从服务器一次取出五条数据
	@SuppressWarnings("serial")
	void loadMore(final AddMoreListView list,final DetailProgressDetailAdapter adapter,final ProgressListInfo info,int position){
		String url = "/home/phone/thing/getmoreprogress";
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				// TODO Auto-generated method stub
				if(data.getResultState() == ResultState.eSuccess){
					
					JSONObject detail = data.getJsonObject();
					if(detail == null){
						return;
					}
					Boolean hasMore = detail.optBoolean("hasMore");
					JSONArray ja = detail.optJSONArray("progresses");
					jsonToDataForMore(ja, info);
					adapter.refresh(info.detailist);
					list.setOpenView(true);
					if(listener != null){
					listener.back();
					}
					if(hasMore){
					list.onLoadMoreComplete(false);
					}else{
						list.bugToProgressAdapter();
					}
					
				}
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("dataId",info.detailist.get(position).dataid)
		.addParam("userCode",info.usercode)
		.addParam("beginInt",String.valueOf(info.detailist.size()))
		.notifyRequest();
		
		
	}
	
	void jsonToDataForMore(JSONArray data,ProgressListInfo info){
		if(data != null && data.length()>0){
			for(int i = 0; i <data.length(); i++){
				JSONObject jo = data.optJSONObject(i);
		try {
			DetailList dl = new DetailList();
			dl.save(jo);
			info.detailist.add(dl);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		}
	}
	
	
	
		public interface OnBacktoAddListViewListener{
			void back();
			
		}
		public void setOnBacktoAddListViewListener(OnBacktoAddListViewListener listener){
			this.listener = listener;
		}
	
	
	

}
