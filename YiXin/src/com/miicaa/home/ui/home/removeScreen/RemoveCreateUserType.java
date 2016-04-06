package com.miicaa.home.ui.home.removeScreen;

import com.miicaa.home.ui.menu.ScreenType;

public class RemoveCreateUserType extends RemoveScreenType{

	@Override
	public void removeType(ScreenType type) {
		// TODO Auto-generated method stub
		type.removeCreatorUsers();
	}

}
