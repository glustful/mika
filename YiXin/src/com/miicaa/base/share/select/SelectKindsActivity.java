package com.miicaa.base.share.select;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.base.share.select.SelectKindsAdapter.OnCheckChangeListener;
import com.miicaa.common.base.SearchFunction;
import com.miicaa.common.base.SearchFunction.OnSearchCallBack;
import com.miicaa.home.HiddenSoftActivity;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.calendar.FramCalendarFragment.ProgressState;
import com.miicaa.home.ui.org.LableGroup;
import com.miicaa.home.ui.pay.PayUtils;

@EActivity(R.layout.select_kinds)
public class SelectKindsActivity extends HiddenSoftActivity {
	KindSearchFunction searchFunction;
	@ViewById(R.id.pay_headTitle)
	TextView title;
	@ViewById(R.id.pay_commitButton)
	Button commit;
	@ViewById(R.id.view_group)
	LableGroup mTopView;
	@ViewById(R.id.listView)
	ListView mListView;
	ProgressState state;
	Context mContext;
	HashMap<String,String> paramMap = new HashMap<String, String>();
	ArrayList<Kind> mKinds = new ArrayList<Kind>();
	
	@Bean
	SelectKindsAdapter mAdapter;
	@Extra
	ArrayList<Kind> mSelectedKinds;
	@Click(R.id.pay_cancleButton)
	void cancel(){
		this.finish();
		
	}
	@Click(R.id.pay_commitButton)
	void commit(){
		if(mSelectedKinds.size()<1){
			PayUtils.showToast(mContext, "请选择客户类型后再提交", 3000);
			return;
		}
		Intent i = new Intent();
		i.putExtra("selected", mSelectedKinds);
		setResult(RESULT_OK, i);
		finish();
	}
	@AfterInject
	void initData(){
		this.mContext = this;
		if(mSelectedKinds==null){
			mSelectedKinds = new ArrayList<Kind>();
		}
		this.mAdapter.setListener(new OnCheckChangeListener() {
			
			@Override
			public void onCheckChanged(Kind name, boolean isCheck) {
				if(isCheck){
					setViewGroup(name,false);
				}else{
					removeViewGroup(name);
				}
				
			}
		});
	}
	@AfterViews
	void initUI(){
		title.setText("选择客户类型");
		commit.setVisibility(View.VISIBLE);
		commit.setText("提交");
		View headView = LayoutInflater.from(this).inflate(R.layout.matter_home_view, null);
		mListView.addHeaderView(headView);
		initSearch(headView);
		mListView.setAdapter(mAdapter);
		requestKinds();
	}
	
	
	
	private void requestKinds() {
		PayUtils.showDialog(mContext);
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				if(data.getResultState()==ResultState.eSuccess){
					PayUtils.closeDialog();
					convertToObject(data.getJsonArray());
				}else{
					PayUtils.showToast(mContext, data.getMsg(), 3000);
				}
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}.setUrl(getString(R.string.crm_custom_kind_url))
		.notifyRequest()
		;
		
	}
	protected void convertToObject(JSONArray jsonArray) {
		if(jsonArray==null||jsonArray.length()<1)
			return;
		mKinds.clear();
		for(int i=0;i<jsonArray.length();i++){
			JSONObject obj = jsonArray.optJSONObject(i);
			Kind itme = new Kind(obj.optString("id"),obj.optString("orgCode"), obj.optString("name"), false);
			if(mSelectedKinds!=null||mSelectedKinds.size()>0){
				for(Kind k:mSelectedKinds){
					if(k.getId().equals(itme.getId())){
						itme.setCheck(true);
						setViewGroup(itme, true);
						break;
					}
				}
			}
			mKinds.add(itme);
		}
		mAdapter.refresh(mKinds);
	}
	private void initSearch(View headView) {
		
		 searchFunction = new KindSearchFunction(this, null,headView,true);
		    searchFunction.setParam(paramMap);
		    searchFunction.setHint("查找类别");
		    searchFunction.search();
			searchFunction.setSearchCallBack(new OnSearchCallBack() {
				
				@Override
				public void textChange(Boolean isText) {
					
				}
				
				@Override
				public void search(ResponseData data) {

				}
				
				@Override
				public void deltext() {
					mAdapter.refresh(mKinds);
				}

				@Override
				public void addMore(ResponseData data) {
					
				}

				@Override
				public void clearRefresh() {
					mAdapter.refresh(mKinds);
				}
			});
		
	}
	
	public void setViewGroup(Kind element,boolean isInit) {
		
		if(!isInit&&mSelectedKinds.contains(element))
			return;
		
		LinearLayout linearLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 5, 0, 5);
		linearLayout.setLayoutParams(params);
		linearLayout.setMinimumHeight(40);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		RelativeLayout sRelativeLayout = new RelativeLayout(mContext);
		ViewGroup.LayoutParams rParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		sRelativeLayout.setBackgroundColor(mContext.getResources().getColor(
				R.color.labelbg));
		sRelativeLayout.setLayoutParams(rParams);
		
		TextView textView = new TextView(mContext);
		RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		tParams.setMargins(5, 5, 5, 0);
		tParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		textView.setLayoutParams(tParams);
		textView.setText(element.getName());
		sRelativeLayout.addView(textView);
		RelativeLayout relativeLayout = new RelativeLayout(mContext);
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(20,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		linearLayout.addView(sRelativeLayout);
		linearLayout.addView(relativeLayout);
		linearLayout.setTag(element);
		linearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeViewGroup(v);
			}
		});
		
		mTopView.addView(linearLayout);
		((View)mTopView.getParent()).setPadding(10, 10,10, 10);
		if(!isInit)
		mSelectedKinds.add(element);

	}

	public void removeViewGroup(Kind element) {
		View view = null;
		int index = 0;
		for (int i=0;i<mTopView.getChildCount();i++) {
			if (mTopView.getChildAt(i).getTag()==element){
				index = i;
				view = mTopView.getChildAt(i);
				break;
			}
				
		}
		
		if (index >=0) {
			
			mSelectedKinds.remove(element);
			mTopView.removeView(view);
		}
		if(mTopView.getChildCount()==0){
			((View)mTopView.getParent()).setPadding(0, 0,0, 0);
		}
	}

	public void removeViewGroup(View view) {

		if (view != null) {
			int index = mTopView.indexOfChild(view);
			if (index == -1)
				return;
			if(view.getTag() instanceof Kind){
				((Kind)view.getTag()).setCheck(false);
			mSelectedKinds.remove(view.getTag());
			mTopView.removeView(view);
			mAdapter.notifyDataSetInvalidated();
			}
		}

	}
	



class KindSearchFunction extends  SearchFunction{

	public KindSearchFunction(Context context, String url, boolean isShow) {
		super(context, url, isShow);
		// TODO Auto-generated constructor stub
	}

	public KindSearchFunction(Context context, String url, View view,
			boolean isShow) {
		super(context, url, view, isShow);
		// TODO Auto-generated constructor stub
	}
	@Override
	public  void search(){
		ArrayList<Kind> tmp = new ArrayList<Kind>();
		for(Kind k:mKinds){
			if(k.getName().contains(getSearchText())){
				tmp.add(k);
			}
		}
		 if (progressDialog.isShowing()){
             progressDialog.dismiss();
		 }
		mAdapter.refresh(tmp);
	}
}
}
