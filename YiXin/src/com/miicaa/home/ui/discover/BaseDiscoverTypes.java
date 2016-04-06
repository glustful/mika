package com.miicaa.home.ui.discover;

import com.miicaa.home.ui.discover.dailywork.BasePeopleType;
import com.miicaa.home.ui.discover.dailywork.BaseReportType;

public abstract class BaseDiscoverTypes {

	public static String REPORT = BaseReportType.class.getName();
	public static String PEOPLE = BasePeopleType.class.getName();
	
	public abstract void removeType(Object parentType);
}
