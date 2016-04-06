package com.miicaa.perferences;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface GuideBgPerf {
	
	@DefaultBoolean(false)
	boolean showBg();
	@DefaultBoolean(false)
	boolean showBageView();
	
}
