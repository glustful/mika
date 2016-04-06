package com.miicaa.home.ui.home.removeScreen;

import com.miicaa.home.ui.menu.ScreenType;

public class RemoveReportTypes extends RemoveScreenType{

	@Override
	public void removeType(ScreenType type) {
		type.removeReports();
	}

}
