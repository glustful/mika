package com.miicaa.home.ui.discover;

import com.miicaa.base.request.MRequest;

public class DiscoverRequest extends MRequest{
	
	public DiscoverRequest(String url){
		super(url);
	}

	public void changeSearchParam(String title){
	}
	
	public void changeReportTypes(String types){
	}
	
	public void changeOriginators(String originats){
	}
	
	public void addPageCount(){
	}
	
	public void resetPage(){
		
	}
	
	public Boolean isFirstPage(){
		return null;
	}
	
	public String getSearchText(){
		return null;
	}
}
