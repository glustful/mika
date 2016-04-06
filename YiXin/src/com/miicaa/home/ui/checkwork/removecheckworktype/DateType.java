package com.miicaa.home.ui.checkwork.removecheckworktype;

import com.miicaa.home.ui.checkwork.CheckWorkActivity.ScreenResult;

public class DateType extends CheckWorkSingleTypes{

	@Override
	public void removeCheckWorkType(ScreenResult screenResult) {
		screenResult.clearBeginDateTypes();
		screenResult.celarEndDateTypes();
		}

}
