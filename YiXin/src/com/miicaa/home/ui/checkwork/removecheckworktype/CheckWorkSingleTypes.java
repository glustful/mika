package com.miicaa.home.ui.checkwork.removecheckworktype;

import com.miicaa.home.ui.checkwork.CheckWorkActivity.ScreenResult;

public abstract class CheckWorkSingleTypes {

	 static String ROOT = "com.miicaa.home.ui.checkwork.removecheckworktype.";
	 public final static String DATE = DateType.class.getName();
	 public final static String END_DATE = EndDateTypes.class.getName();
	 public final static String ORG = OrgTypes.class.getName();
	 public final static String USER = UserTypes.class.getName();
	
	 
	public abstract void removeCheckWorkType(ScreenResult screenResult);
	
}
