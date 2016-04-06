package com.yxst.epic.yixin.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.yxst.epic.yixin.fragment.IMainFragment;

public class TabsAdapter extends FragmentPagerAdapter implements
		TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
	
	private static final String TAG = "TabsAdapter";
	
	private final Context mContext;
	private final TabHost mTabHost;
	private final ViewPager mViewPager;
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

	static final class TabInfo {
		private final String tag;
		private final Class<?> clss;
		private final Bundle args;

		TabInfo(String _tag, Class<?> _class, Bundle _args) {
			tag = _tag;
			clss = _class;
			args = _args;
		}
	}

	static class DummyTabFactory implements TabHost.TabContentFactory {
		private final Context mContext;

		public DummyTabFactory(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	ActionBarActivity mActivity;
	FragmentManager mFragmentManager;
	
	public TabsAdapter(FragmentActivity activity, TabHost tabHost,
			ViewPager pager) {
		super(activity.getSupportFragmentManager());
		mFragmentManager = activity.getSupportFragmentManager();
		mActivity = (ActionBarActivity) activity;
		mContext = activity;
		mTabHost = tabHost;
		mViewPager = pager;
		mTabHost.setOnTabChangedListener(this);
		mViewPager.setAdapter(this);
		mViewPager.setOnPageChangeListener(this);
	}

	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
		tabSpec.setContent(new DummyTabFactory(mContext));
		String tag = tabSpec.getTag();

		TabInfo info = new TabInfo(tag, clss, args);
		mTabs.add(info);
		mTabHost.addTab(tabSpec);
		mViewPager.setOffscreenPageLimit(mTabs.size());
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	private Map<Integer, String> mMap = new HashMap<Integer, String>();
	
	@Override
	public Fragment getItem(int position) {
//		Log.d(TAG, "getItem() position:" + position);
		TabInfo info = mTabs.get(position);
		return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
//		return super.instantiateItem(container, position);
		
		Object object = super.instantiateItem(container, position);
//		Log.d(TAG, "instantiateItem() position:" + position + ",object:" + object);
//		Log.d(TAG, "instantiateItem() etTag():" + ((Fragment) object).getTag());
		mMap.put(position, ((Fragment) object).getTag());
		return object;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		Log.d(TAG, "destroyItem() position:" + position);
		super.destroyItem(container, position, object);
	}
	
	@Override
	public int getItemPosition(Object object) {
//		Log.d(TAG, "getItemPosition() position:" + super.getItemPosition(object));
		return super.getItemPosition(object);
	}
	
	@Override
	public void onTabChanged(String tabId) {
//		Log.d(TAG, "onTabChanged()");
		int position = mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(position, false);
		
		ActionBar bar = mActivity.getSupportActionBar();
		TabInfo info = mTabs.get(position);
		bar.setTitle(info.tag);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		Log.d(TAG, "onPageSelected() position:" + position);
		// Unfortunately when TabHost changes the current tab, it kindly
		// also takes care of putting focus on it when not in touch mode.
		// The jerk.
		// This hack tries to prevent this from pulling focus out of our
		// ViewPager.
		TabWidget widget = mTabHost.getTabWidget();
		int oldFocusability = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		mTabHost.setCurrentTab(position);
		widget.setDescendantFocusability(oldFocusability);
		
		Fragment f = mFragmentManager.findFragmentByTag(mMap.get(position));
		if (f instanceof IMainFragment) {
			IMainFragment mainFragment = (IMainFragment) f;
			mainFragment.onTagChanged();
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}
}