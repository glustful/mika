package com.miicaa.home.ui.discover.dailywork;

import android.util.Log;

import com.miicaa.home.ui.discover.DiscoverRequest;

public class DailyWorkRequest extends DiscoverRequest{

	private static String TAG  = "DailyWorkRequest";
	
	/*按人员筛选*/
	private static String originators = "originators";
	private static String reportTypes = "reportTypes";
	private static String searchTitle = "title";
	private static String pageNum = "pageNum";
	
	private int page;
	
	@Override
	public void changeSearchParam(String title){
		if(title != null)
			hashParam.put(searchTitle, title);
	}
	
	@Override
	public void changeReportTypes(String types){
		Log.d(TAG, "changeReportTypes :"+types);
		if(types != null)
			hashParam.put(reportTypes, types);
	}
	
	@Override
	public void changeOriginators(String originats){
		Log.d(TAG, "changeOriginators :"+originats);
		if(originats != null)
			hashParam.put(originators, originats);
	}
	
	@Override
	public void addPageCount(){
		page++;
		hashParam.put(pageNum, String.valueOf(page));
	}
	
	@Override
	public String getSearchText(){
		return hashParam.get(searchTitle);
	}
	
	
	
	@Override
	public void resetPage() {
		page = 1;
		hashParam.put(pageNum, String.valueOf(page));
	}
	
	

	@Override
	public Boolean isFirstPage() {
		return  page == 1 ? true : false;
	}

	public DailyWorkRequest(String url){
		super(url);
		page = 1;
		hashParam.put(pageNum, String.valueOf(page));
		hashParam.put(reportTypes, "");
		hashParam.put(searchTitle, "");
		hashParam.put(originators, "");
		hashParam.put("unitsStr", "");
	}
	
}
