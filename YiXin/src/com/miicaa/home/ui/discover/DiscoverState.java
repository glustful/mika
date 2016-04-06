package com.miicaa.home.ui.discover;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public abstract class DiscoverState {
	
	public abstract String getTitle() ;
	
	public abstract void setRightButton(Button right) ;

	public abstract ScreenResult getScreenResult() ;

	public abstract DiscoverReponseData getResponseData() ;

	public abstract DiscoverRequest getRequest() ;

	public abstract ScreenMenu getScreenMenu(DiscoverActivity context) ;

	public abstract DiscoverAdapter getAdapter(Context context) ;
	
	public View getNoDataView(View root){
		return null;
	}

	public void noData(LinearLayout root) {
		if(root.getChildCount()>0){
			return;
		}
		root.addView(getNoDataView(root));
		root.setVisibility(View.VISIBLE);
	}
	
	
}
