package com.yxst.epic.yixin.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.miicaa.home.R;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.fragment.ContactSubListFragment;
import com.yxst.epic.yixin.fragment.ContactSubListFragment_;
import com.yxst.epic.yixin.listener.OnMemberClickListener;

@EActivity(R.layout.activity_contact_sub_list)
public class ContactSubListActivity extends ActionBarActivity implements
		OnMemberClickListener {

	@Extra("Member")
	Member member;

	@AfterViews
	void afterViews() {
		final ActionBar bar = getSupportActionBar();
		bar.setTitle(member.NickName);

		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
		bar.setDisplayOptions(change, flags);

		ContactSubListFragment fragment = ContactSubListFragment_.builder()
				.member(member).build();
		fragment.setOnMemberClickListener(this);
		addFragment(fragment);
	}

	@Override
	public boolean onSupportNavigateUp() {
		if (!popBackStack()) {
			finish();
		}
		// return super.onSupportNavigateUp();
		return true;
	}

	private void addFragment(Fragment newFragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.layout_content, newFragment).commit();
	}

	private void addFragmentToStack(Fragment newFragment) {
		// Add the fragment to the activity, pushing this transaction
		// on to the back stack.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.layout_content, newFragment, null);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
	}

	private boolean popBackStack() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
			return true;
			// return fm.popBackStackImmediate();
		}
		return false;
	}

	private boolean popBackStackToHome() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack(fm.getBackStackEntryAt(0).getId(),
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
			return true;
		}
		return false;
	}

	@Override
	public void onMemberClick(Member member) {
		if (member.isTypeOrg()) {

			ContactSubListFragment fragment = ContactSubListFragment_.builder()
					.isSelectMode(false).member(member).build();
			fragment.setOnMemberClickListener(this);
			addFragmentToStack(fragment);
		} else if (member.isTypeUser()) {
			ContactDetailActivity_.intent(this).userName(member.UserName)
					.member(member).start();
		}
	}

}
