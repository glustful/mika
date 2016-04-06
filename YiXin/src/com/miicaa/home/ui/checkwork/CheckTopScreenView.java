package com.miicaa.home.ui.checkwork;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.BaseAdapter;

import com.miicaa.home.ui.checkwork.CheckWorkActivity.ScreenResult;
import com.miicaa.home.ui.checkwork.removecheckworktype.CheckWorkSingleTypes;
import com.miicaa.home.ui.home.TopScreenView;
import com.miicaa.utils.AllUtils;

public class CheckTopScreenView extends TopScreenView{

	static String TAG = "CheckTopScreenView";
	Context mContext;
//	LinkedHashMap<String, String> typesMap;
	ArrayList<CheckScreenValue> types;
	CheckWorkScreenViewAdapter adapter_;
	
	public CheckTopScreenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		this.mContext = context;
	}

	public CheckTopScreenView(Context context, AttributeSet attrs) {
//		super(context, attrs);
		super(context, attrs, -1);
		init();
		this.mContext = context;
	}

	public CheckTopScreenView(Context context) {
//		super(context);
		super(context,null);
		init();
		this.mContext = context;
	}

	private void init(){
//		typesMap = new LinkedHashMap<String, String>();
	}
	
	@Override
	public void initObjects() {
//		typesMap.putAll(addNewCheckWorkTypes());
		types = new ArrayList<CheckScreenValue>();
		types.addAll(addNewCheckWorkTypes());
	}

	@Override
	public BaseAdapter getAdapter() {
		adapter_ = new CheckWorkScreenViewAdapter(mContext);
		return adapter_;
	}

	@Override
	public void refreshAdapter() {
		adapter_.refresh(types);
	}

	@Override
	public ArrayList<CheckScreenValue> getScreenTypes() {
		return types;
	}

	@Override
	public void removeType(CheckScreenValue type) {
		removeTyep(type);
	}
	
	ScreenResult mScreenResult;
	public CheckTopScreenView create(ScreenResult screenResult){
		this.mScreenResult = screenResult;
		return this;
	}
	
	private ArrayList<CheckScreenValue> addNewCheckWorkTypes(){
		assert this.mScreenResult != null;
		ArrayList<CheckScreenValue> vs = new ArrayList<CheckScreenValue>();
		String beginDate = mScreenResult.nowBeginDate != null ? mScreenResult.nowBeginDate : "";
		String endDate = mScreenResult.nowEndDate != null ? mScreenResult.nowEndDate : "";
		String date = "时间:"+beginDate + "——" + endDate;
		String userNames = AllUtils.listToString(mScreenResult.nowUserNames);
		String orgName = mScreenResult.nowOrgNames != null ? mScreenResult.nowOrgNames : "";
		if(beginDate.length() > 0 && endDate.length() > 0){
			vs.add(new CheckScreenValue( CheckWorkSingleTypes.DATE, date));
		}
//		if(endDate.length() != 0){
//			vs.add(new CheckScreenValue(CheckWorkSingleTypes.END_DATE, endDate));
//		}
		if(userNames != null && userNames.length() != 0){
			vs.add(new CheckScreenValue(CheckWorkSingleTypes.USER, "姓名:"+userNames));
		}
		if(orgName != null && orgName.length() != 0){
			vs.add(new CheckScreenValue(CheckWorkSingleTypes.ORG, "部门:"+orgName));
		}
		return vs;
	}

	@Override
	public void removeType(int position) {
		CheckScreenValue value = (CheckScreenValue)adapter_.getItem(position);
		removeTyep(value);
	}
	
	public  void removeTyep(CheckScreenValue value){
		if(value == null){
			return;
		}
		String className = value.mKey;
		try {
			Log.d(TAG, "remove class name is"+"..."+className);
			Class<?> class_ = Class.forName(className);
			CheckWorkSingleTypes type = (CheckWorkSingleTypes) class_.newInstance();
			type.removeCheckWorkType(mScreenResult);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
//		RemoveScreenTypeListener listener_ = getRemoveTypeListener();
//		if(listener_ != null)
//			listener_.removeType();
		types.remove(value);
		adapter_.refresh(types);
		
	}

	@Override
	public void initContext(Context context) {
		this.mContext = context;
	}

}
