package com.miicaa.home.ui.discover.dailywork;

import com.miicaa.home.ui.discover.BaseDiscoverTypes;

public class BaseReportType extends BaseDiscoverTypes{

	@Override
	public void removeType(Object parentType) {
		if(parentType instanceof DailyWorkScreenResult){
			((DailyWorkScreenResult)parentType).clearReportTypes();
		}
	}

}
