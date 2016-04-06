package com.miicaa.home.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.UserInfoSql;
import com.miicaa.home.data.storage.ConfigDatabase;
import com.miicaa.home.data.storage.SqlCmd;

class ContactLoader extends CursorLoader{

	public ContactLoader(Context context) {
		super(context);
	}

	@Override
	public Cursor loadInBackground() {
		SqlCmd sqlCmd = UserInfoSql.usersInOrg(AccountInfo.instance().getLastOrgInfo());
		return ConfigDatabase.instance().queryCursor(sqlCmd.getSql(), sqlCmd.getStringParams());
	}
	
	
	
}