package com.miicaa.home.data.storage;

import com.miicaa.common.base.Debugger;

/**
 * 用户缓存数据库对象
 * 用于处理用户缓存数据库相关的操作
 * <p/>
 * Created by yayowd on 13-12-17.
 */
public class CacheDatabase extends LocalDatabase {
	private static CacheDatabase _instance = null;

	public static CacheDatabase instance() {
		synchronized (CacheDatabase.class) {
			if (_instance == null) {
				_instance = new CacheDatabase();
				_instance.init();
			}
		}
		return _instance;
	}

	public Boolean open() {
		if (LocalPath.intance().userCacheDatabaseFilePath == null) {
			Debugger.d("DataCenter", "Please set userinfo to DataCenter before open CacheDatabase.");
			return false;
		}

		return super.open(LocalPath.intance().userCacheDatabaseFilePath);
	}
}