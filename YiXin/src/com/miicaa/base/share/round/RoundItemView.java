package com.miicaa.base.share.round;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.miicaa.home.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

@EViewGroup(R.layout.select_round_item)
public class RoundItemView extends LinearLayout {

	@ViewById
	RadioButton label;
	@ViewById
	TextView content;
	public RoundItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RoundItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RoundItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setChecked(boolean b) {
		label.setChecked(b);
		
	}

	public void clean() {
		content.setText("");
		
	}

}
