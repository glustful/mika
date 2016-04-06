package com.yxst.epic.yixin.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.db.MessageDao;

public class ChatImagesLoader extends CursorLoader {

	private static final int ID = MessageDao.Properties.Id.ordinal;
	
	public static final String TAG = "ChatImagesLoader";
	
	private String localUserName;
	private String remoteUserName;
	private Long currentMsgId;
	private int mPosition;

	public ChatImagesLoader(Context context, String localUserName,
			String remoteUserName, Long msgId) {
		super(context);
		this.localUserName = localUserName;
		this.remoteUserName = remoteUserName;
		this.currentMsgId = msgId;
		mPosition = 0;
	}

	@Override
	public Cursor loadInBackground() {
		Cursor cursor = DBManager.getInstance(getContext()).getChatMessagesInThePictures(localUserName,
				remoteUserName);
		if (currentMsgId != null && currentMsgId > 0) {
			while (cursor.moveToNext()) {
				Long msgId = cursor.getLong(ID);
				if (msgId != null && msgId.equals(currentMsgId)) {
					mPosition = cursor.getPosition();
					break;
				}
			}
		}
		return cursor;
	}
	
	public int getPosition(){
		return mPosition;
	}
	
}
