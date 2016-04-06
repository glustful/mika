package com.miicaa.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class PaomadengTextView extends TextView{

	public PaomadengTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public PaomadengTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PaomadengTextView(Context context) {
		super(context);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
	

	
}
