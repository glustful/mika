package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.miicaa.home.R;

@EViewGroup(R.layout.view_rcd_warning)
public class RcdWarningView extends LinearLayout {

	public RcdWarningView(Context context) {
		super(context);
	}

	public RcdWarningView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setVisible(boolean visible) {
		this.setVisibility(visible ? View.VISIBLE : View.GONE);
	}
}
