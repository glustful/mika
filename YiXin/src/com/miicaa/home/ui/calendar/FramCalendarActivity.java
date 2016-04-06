package com.miicaa.home.ui.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.miicaa.home.R;

public class FramCalendarActivity extends FragmentActivity {
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	RelativeLayout loadingLayout;
	String type = "00";
	String parentId = "";
	boolean isSubTask = false;
	String dataType;
	String arrangeType;
	
	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		
		setContentView(R.layout.calendar_yet);
		type = getIntent().getStringExtra("type");
			if(type==null||type.equals("")){
				type = "00";
			}
		
		parentId = getIntent().getStringExtra("parentId");
		dataType = getIntent().getStringExtra("dataType");
		arrangeType = getIntent().getStringExtra("arrangeType");
		if(parentId!=null&&!parentId.equals("")){
			isSubTask = true;
		}
		initFragment();
	}

	private void initFragment() {
		
		Bundle type = new Bundle();
		type.putString("viewType", this.type);
		type.putBoolean("isSubTask", isSubTask);
		type.putString("parentId", parentId);
		type.putString("dataType", dataType);
		type.putString("arrangeType", arrangeType);
		Fragment f = Fragment.instantiate(this, FramCalendarFragment_.class.getName(), type);
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();  
        ft.replace(R.id.calendar_layout_yet, f);  
       
        ft.commit();  
		
	}
	    
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}

	
}
