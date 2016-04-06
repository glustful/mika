package com.miicaa.home.view;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.pay.PayUtils;

@EViewGroup(R.layout.descript_layout)
public class DescriptView extends LinearLayout{

	@ViewById
	EditText desc;
	@ViewById
	TextView totalcount;
	@AfterViews
	void initUI(){
		desc.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				
				if(desc.getLineCount()>5){
				
					v.getParent().requestDisallowInterceptTouchEvent(true);
					
				
				}
				return false;
			}
		});
	}
	@AfterTextChange(R.id.desc)
	void textChange(Editable et){
		totalcount.setText(String.valueOf(et.length()));
	}
	
	public DescriptView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public DescriptView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DescriptView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public boolean invalide() {
		if(this.desc.getText().toString().trim().equals("")){
			PayUtils.showToast(getContext(), "备注信息不能为空", 3000);
			return false;
		}
		return true;
	}

}
