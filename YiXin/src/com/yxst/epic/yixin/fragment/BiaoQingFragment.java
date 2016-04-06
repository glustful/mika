package com.yxst.epic.yixin.fragment;

import java.util.Arrays;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.miicaa.home.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.yxst.epic.yixin.view.WrapContentHeightViewPager;

//import com.viewpagerindicator.PageIndicator;

public class BiaoQingFragment extends Fragment {

	protected static final String TAG = "BiaoQingFragment";
	TestFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.simple_circles, container,
				false);

		mAdapter = new TestFragmentAdapter(getActivity());
		mAdapter.setOnItemClickListener(mInnerOnItemClickListener);

		mPager = (WrapContentHeightViewPager) rootView.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		CirclePageIndicator indicator = (CirclePageIndicator) rootView
				.findViewById(R.id.indicator);
		mIndicator = indicator;
		indicator.setViewPager(mPager);
		indicator.setSnap(true);

		return rootView;
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

	public static Fragment newInstance(OnItemClickListener listener) {
		BiaoQingFragment f = new BiaoQingFragment();
		f.setOnItemClickListener(listener);
		return f;
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
			ImageView view = new ImageView(parent.getContext());

			view.setImageResource(smilleyDrawable[position]);
			view.setBackgroundResource(R.drawable.abc_item_background_holo_light);

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
