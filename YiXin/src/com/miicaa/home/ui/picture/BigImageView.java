package com.miicaa.home.ui.picture;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BigImageView extends ImageView{

	public BigImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public BigImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public BigImageView(Context context) {
		super(context);
		initView();
	}
	
	private void initView(){
		new PhotoViewAttacher(this);
	}

}
