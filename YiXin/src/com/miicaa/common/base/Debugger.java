package com.miicaa.common.base;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;



/**
 * 自定义调试器
 * 用于控制调试信息的输出方式
 * <p/>
 * Created by yayowd on 13-12-10.
 */
public class Debugger {
	private static final Boolean DEV_MODEN = false;
	private static Context _appContext = null;

	public static void init(Context context) {
		_appContext = context;
	}

	public static void d(String tag, String msg) {
		Log.d(tag, msg);
		if (DEV_MODEN) {
			Toast.makeText(_appContext, msg, Toast.LENGTH_LONG).show();
		}
	}

	public static void d(String tag, String format, Object... args) {
		d(tag, String.format(format, args));
	}
}
