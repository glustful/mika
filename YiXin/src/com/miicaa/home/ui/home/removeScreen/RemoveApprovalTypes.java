package com.miicaa.home.ui.home.removeScreen;

import com.miicaa.home.ui.menu.ScreenType;

public class RemoveApprovalTypes extends RemoveScreenType{

	@Override
	public void removeType(ScreenType type) {
		type.removeApprovalCodes();
	}

}
