package com.miicaa.home.data;

import android.util.Log;

import com.miicaa.home.data.business.BusinessAspect;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.storage.CacheDatabase;
import com.miicaa.home.data.storage.ConfigDatabase;
import com.miicaa.home.data.storage.LocalPath;

/**
 * 数据中心对象
 * 用于公共变量的定义和对其它数据中心对象的封装
 * <p/>
 * Created by yayowd on 13-12-6.
 */
public class DataCenter {
	public static boolean INIT_TEST = false;
	private static DataCenter _instance = null;

	public static DataCenter intance() {
		synchronized (DataCenter.class) {
			if (_instance == null) {
				_instance = new DataCenter();
				_instance.init();
			}
		}
		return _instance;
	}

	// 请特别注意：初始化顺序
	protected void init() {
		// 初始化全局数据中心对象
		LocalPath.intance();

		// 初始化配置库
		ConfigDatabase.instance();

		RequestAdpater.responseAspect = new BusinessAspect();
	}

	public void setUser(String orgCode, String userCode) {
		Log.d("DataCenter", "orgcode and usercode :"+"....."+orgCode+"....."+userCode);
		// 初始化用户相关本地路径
		LocalPath.intance().setUser(orgCode, userCode);

		// 初始化用户数据中心对象
		CacheDatabase.instance().open();
	}
}
