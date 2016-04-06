package com.miicaa.home.data.storage;

import com.miicaa.common.base.OnTransaction;
import com.miicaa.home.data.business.account.LoginInfo;
import com.miicaa.home.data.business.org.GroupInfo;
import com.miicaa.home.data.business.org.GroupUserInfo;
import com.miicaa.home.data.business.org.OrgInfo;
import com.miicaa.home.data.business.org.UnitInfo;
import com.miicaa.home.data.business.org.UnitUserInfo;
import com.miicaa.home.data.business.org.UserInfo;

/**
 * 全局配置数据库对象
 * 用于处理应用程序全局配置数据库相关的操作
 * <p/>
 * Created by yayowd on 13-12-17.
 */
public class ConfigDatabase extends LocalDatabase {
	private static ConfigDatabase _instance = null;

	public static ConfigDatabase instance() {
		synchronized (ConfigDatabase.class) {
			if (_instance == null) {
				_instance = new ConfigDatabase();
				_instance.init();
			}
		}
		return _instance;
	}

	@Override
	protected Boolean init() {
		// 打开配置数据库
		if (super.open(LocalPath.intance().configDatabaseFilePath)) {
			onTransaction(new OnTransaction() {
				@Override
				public void transaction() {
					// 组织机构初始化
					OrgInfo.init();
					UserInfo.init();
					GroupInfo.init();
					GroupUserInfo.init();
					UnitInfo.init();
					UnitUserInfo.init();
					// 登录信息初始化
					LoginInfo.init();
				}
			});

			return true;
		}
		return false;
	}
}