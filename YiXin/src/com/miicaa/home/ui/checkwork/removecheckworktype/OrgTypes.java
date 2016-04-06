package com.miicaa.home.ui.checkwork.removecheckworktype;

import com.miicaa.home.ui.checkwork.CheckWorkActivity.ScreenResult;

public class OrgTypes extends CheckWorkSingleTypes{

	@Override
	public void removeCheckWorkType(ScreenResult screenResult) {
		screenResult.clearOrgCodesTypes();
	}

}
