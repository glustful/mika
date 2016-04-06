package com.miicaa.home.ui.discover.crm;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.BaseAdapter;

import com.miicaa.home.ui.checkwork.CheckScreenValue;
import com.miicaa.home.ui.checkwork.CheckWorkScreenViewAdapter;
import com.miicaa.home.ui.discover.BaseDiscoverTypes;
import com.miicaa.home.ui.discover.ScreenResult;
import com.miicaa.home.ui.home.TopScreenView;

public class CRMTopScreenView extends TopScreenView{

	static String TAG = "DailyWorkTopScreenView";
	
	Context mContext;
	CheckWorkScreenViewAdapter adapter_;
	ArrayList<CheckScreenValue> screenValueList;
	
	public CRMTopScreenView(Context context) {
		super(context);
//		this.mContext = context;
	}
	
	

	public CRMTopScreenView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
//		this.mContext = context;
	}



	public CRMTopScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		this.mContext = context;
	}



	@Override
	public void initObjects() {
		screenValueList = new ArrayList<CheckScreenValue>();
		screenValueList.addAll(initScreenValueList());
	}

	@Override
	public BaseAdapter getAdapter() {
		adapter_ = new CheckWorkScreenViewAdapter(mContext);
		return adapter_;
	}

	@Override
	public void refreshAdapter() {
		adapter_.refresh(screenValueList);
	}

	@Override
	public ArrayList<CheckScreenValue> getScreenTypes() {
		return screenValueList;
	}

	@Override
	public void removeType(CheckScreenValue type) {
		String clzName = type.mKey;
		try {
			Class<?> claz = Class.forName(clzName);
			BaseDiscoverTypes discoverTypes = (BaseDiscoverTypes)claz.newInstance();
			discoverTypes.removeType(screenResult);
		} catch (ClassNotFoundException e) {
			Log.d(TAG, "removeType ClassNotFoundException :"+e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			Log.d(TAG, "removeType InstantiationException:"+e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.d(TAG, "removeType IllegalAccessException:"+e.getMessage());
			e.printStackTrace();
		}
		screenValueList.remove(type);
		adapter_.refresh(screenValueList);
	}

	@Override
	public void removeType(int position) {
		
	}

	@Override
	public void initContext(Context context) {
		this.mContext = context;
	}
	
	ScreenResult screenResult;
	@Override
	public CRMTopScreenView createView(ScreenResult result){
		
		this.screenResult = result;
		return this;
	}

	private ArrayList<CheckScreenValue> initScreenValueList(){
		ArrayList<CheckScreenValue> values = new ArrayList<CheckScreenValue>();
		String reportName = screenResult.getReportNames();
		if(reportName != null && reportName.length()>0){
			reportName = "报告类型："+reportName;
			values.add(new CheckScreenValue(BaseDiscoverTypes.REPORT, reportName));
		}
		String peopleName = screenResult.getPepoleNames();
		if(peopleName != null && peopleName.length()>0){
			peopleName = "报告人："+peopleName;
			values.add(new CheckScreenValue(BaseDiscoverTypes.PEOPLE, peopleName));
		}
		return values;
	}



	
}
