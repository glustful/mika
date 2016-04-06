package com.miicaa.detail;



import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class DetailProgressListView extends ListView{

	public DetailProgressListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public DetailProgressListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DetailProgressListView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}


	
}
