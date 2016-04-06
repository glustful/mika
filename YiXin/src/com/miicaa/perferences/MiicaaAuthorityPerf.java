package com.miicaa.perferences;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

import com.miicaa.utils.AllUtils;

@SharedPref
public interface MiicaaAuthorityPerf {

	@DefaultInt(AllUtils.NORMAL_User)
	int photoAuthority();
	
	@DefaultInt(AllUtils.NORMAL_User)
	int subTaskAuth();
	
	@DefaultInt(AllUtils.NORMAL_User)
	int approveProcessAuth();
}
