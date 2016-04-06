package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.readystatesoftware.viewbadger.BadgeView;

@EViewGroup(R.layout.tab_indicator)
public class TabIndicator extends LinearLayout {

	@ViewById(R.id.iv_icon)
	ImageView mIconIV;
	
	@ViewById(R.id.tv_title)
	TextView mTitleTV;
	
	@ViewById(R.id.v_badge)
	BadgeView mBadgeView;

	@AfterViews
	void afterViews() {
		mBadgeView.setVisibility(View.GONE);
	}
	
	public TabIndicator(Context context) {
		super(context);
	}

	public TabIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
//		initFromAttributeSet(context, attrs);
	}

//	private void initFromAttributeSet(Context c, AttributeSet attrs) {
//		TypedArray a = c
//				.obtainStyledAttributes(attrs, R.styleable.TabIndicator);
//		mIconIV.setImageDrawable(a.getDrawable(R.styleable.TabIndicator_tbIcon));
//		mTitleTV.setText(a.getString(R.styleable.TabIndicator_tbTitle));
//		a.recycle();
//	}

	public void setupTabIndicator(int iconResId, int titleResId) {
		Resources res = getResources();
		setupTabIndicator(res.getDrawable(iconResId), res.getString(titleResId));
	}

	public void setupTabIndicator(Drawable icon, String title) {
		mIconIV.setImageDrawable(icon);
		mTitleTV.setText(title);
	}
	
	public void setBadgeVisibility(boolean visible) {
		mBadgeView.setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	public void setBadge(long num) {
		mBadgeView.setVisibility(num == 0 ? View.GONE : View.VISIBLE);
		mBadgeView.setText(String.valueOf(num));
		
		if (num > 99) {
			mBadgeView.setText("N");
		}
	}
}
