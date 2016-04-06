package com.miicaa.detail;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.miicaa.home.R;

@EViewGroup(R.layout.matter_detail_tab_view)
public class MatterDetailTabView extends RelativeLayout{
	
	@ViewById(R.id.button)
	Button tabButton;

	public MatterDetailTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MatterDetailTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MatterDetailTabView(Context context) {
		super(context);
	}
	
	public void setIndicator(String content){
		if(content != null)
			tabButton.setText(content);
	}
	
	
}
