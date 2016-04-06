package com.miicaa.home.ui.home;

import android.content.Context;
import android.content.SharedPreferences;

public class GuideBgUtil {
	public final static int GUIDE_TYPE = 0x102;
	private static String GUIDE_CODE = "code";
	private static String KEY = "key";
	
	public static int getGuideType(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(GUIDE_CODE, Context.MODE_PRIVATE);
		int perfenceValue = sharedPreferences.getInt(KEY, 0);
		if(perfenceValue != GUIDE_TYPE){
			SharedPreferences.Editor perEditor = sharedPreferences.edit();
			perEditor.putInt(KEY, GUIDE_TYPE);
			perEditor.commit();
		}
		return perfenceValue;
	}
}
