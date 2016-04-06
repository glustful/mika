package com.yxst.epic.yixin.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.yxst.epic.yixin.db.DBManager;

public class ChatLoader extends CursorLoader {

	private String localUserName;
	private String remoteUserName;

	public ChatLoader(Context context, String localUserName,
			String remoteUserName) {
		super(context);
		this.localUserName = localUserName;
		this.remoteUserName = remoteUserName;
	}

	@Override
	public Cursor loadInBackground() {
		return DBManager.getInstance(getContext()).getChatMessages(localUserName,
				remoteUserName);
	}
}
