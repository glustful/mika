package com.yxst.epic.yixin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class WrapContentHeightGridView extends GridView {

	public WrapContentHeightGridView(Context context) {
		super(context);
	}

	public WrapContentHeightGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WrapContentHeightGridView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 设置不滚动
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
