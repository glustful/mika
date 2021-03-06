package com.yxst.epic.yixin.view;

import java.util.Arrays;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.miicaa.home.R;
import com.viewpagerindicator.CirclePageIndicator;

@EViewGroup(R.layout.simple_circles)
public class BiaoQingView extends RelativeLayout {

	protected static final String TAG = "BiaoQingFragment";
	
	public BiaoQingView(Context context) {
		super(context);
	}
	public BiaoQingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public BiaoQingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	TestFragmentAdapter mAdapter;
	
	@ViewById(R.id.pager)
	ViewPager mPager;
	
	@ViewById(R.id.indicator)
	CirclePageIndicator mIndicator;

	@AfterViews
	protected void init() {
		mAdapter = new TestFragmentAdapter(getContext());
		mAdapter.setOnItemClickListener(mInnerOnItemClickListener);
		mPager.setAdapter(mAdapter);
		
		mIndicator.setViewPager(mPager);
		mIndicator.setSnap(true);
	}
	
	private static class TestFragmentAdapter extends PagerAdapter {

		public static final int PAGE_SIZE = 5 * 7;

		private Context context;
		private OnItemClickListener listener;

		private int[] smilleyDrawable;
		private String[] smileyCode;

		public TestFragmentAdapter(Context context) {
			this.context = context;

			TypedArray ta = context.getResources().obtainTypedArray(
					R.array.smiley_drawable);
			int N = ta.length();
			smilleyDrawable = new int[N];

			for (int i = 0; i < N; i++) {
				smilleyDrawable[i] = ta.getResourceId(i, -1);
				
			}

			ta.recycle();

			smileyCode = context.getResources().getStringArray(
					R.array.smiley_code);
		}

		public void setOnItemClickListener(OnItemClickListener listener) {
			this.listener = listener;
		}

		@Override
		public int getCount() {
			return (smileyCode.length + PAGE_SIZE - 1) / PAGE_SIZE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			GridView view = new GridView(context);
			view.setNumColumns(7);

			int start = position * PAGE_SIZE;
			int end = Math.min(start + PAGE_SIZE, smileyCode.length);

			view.setAdapter(new MyGridAdapter(Arrays.copyOfRange(smileyCode,
					start, end), Arrays
					.copyOfRange(smilleyDrawable, start, end)));
			Log.d(TAG, "instantiateItem() listener:" + listener);
			view.setOnItemClickListener(listener);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			container.addView(view, params);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
			object = null;
		}
	}

	private static class MyGridAdapter extends BaseAdapter {
		private int[] smilleyDrawable;
		private String[] smileyCode;

		public MyGridAdapter(String[] smileyCode, int[] smilleyDrawable) {
			this.smileyCode = smileyCode;
			this.smilleyDrawable = smilleyDrawable;
		}

		@Override
		public int getCount() {
			return smileyCode.length;
		}

		@Override
		public Object getItem(int position) {
			return smileyCode[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			BiaoqingItemView view;
			
			if (convertView == null) {
				view = BiaoqingItemView_.build(parent.getContext());
			} else {
				view = (BiaoqingItemView) convertView;
			}

			view.bind(smilleyDrawable[position]);

			return view;
		}

	}

	private OnItemClickListener mInnerOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d(TAG, "onItemClick() mOnItemClickListener:"
					+ mOnItemClickListener);
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(parent, view, position, id);
			}
		}
	};

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}
}
