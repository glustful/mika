package com.yxst.epic.yixin.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.yxst.epic.yixin.db.DBManager;

public class ChatListLoader extends CursorLoader {

	public String localUserName;

	public ChatListLoader(Context context, String localUserName) {
		super(context);

		this.localUserName = localUserName;
	}

	@Override
	public Cursor loadInBackground() {
		Cursor cursor = DBManager.getInstance(getContext()).getChatList(
				localUserName);
		// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
		// System.currentTimeMillis());
		// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
		// DatabaseUtils.dumpCursorToString(cursor));
		// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
		// System.currentTimeMillis());
		return cursor;
	}

}
