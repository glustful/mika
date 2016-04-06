package com.yxst.epic.yixin.service;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.activity.UpdateEnableActivity_;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.data.dto.model.ObjectContentUpdate;
import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.DaoSession;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.db.MessageDao;
import com.yxst.epic.yixin.model.Content;
import com.yxst.epic.yixin.model.ContentNormal;
import com.yxst.epic.yixin.model.ContentQun;
import com.yxst.epic.yixin.push.cli.PushMessage;
import com.yxst.epic.yixin.push.service.PushMessageListener;
import com.yxst.epic.yixin.utils.Utils;

public class MiicaaPushMessageListener implements PushMessageListener {

	private static final String TAG = "MiicaaPushMessageListener";
	
	private Context context;

	VoiceMessageFilter mVoiceMessageFilter;
	VoiceMessageListener mVoiceMessageListener;
	
	public MiicaaPushMessageListener(Context context) {
		this.context = context;
		mVoiceMessageFilter = new VoiceMessageFilter(context);
		mVoiceMessageListener = new VoiceMessageListener(context);
	}

	@Override
	public void processOnlineMessage(PushMessage pushMessage) {
		Log.d(TAG, "processOnlineMessage():"+pushMessage);
		
		Message message = DBManager.getInstance(context).onOnlineMessage(
				pushMessage);

		if (message != null) {

			if (isMsgUpdate(DBMessage.retriveMsgFromMessage(message))) {
				return;
			}

			boolean acceptVoiceMsg = mVoiceMessageFilter.acceptOnlineMessage(pushMessage);
			if (acceptVoiceMsg) {
				mVoiceMessageListener.processOnlineMessage(pushMessage);
			}
			
//			if (Utils.isSouldNotification(context)) {
//				Content content = Content.createContent(message.getExtRemoteUserName(), message.getContent());
//				String contentStr = null;
//				if (Member.isTypeQun(message.getExtRemoteUserName())) {
//					ContentQun contentQun = (ContentQun) content;
//					contentStr = contentQun.realContent;
//				} else {
//					ContentNormal contentNormal = (ContentNormal) content;
//					contentStr = contentNormal.content;
//				}
//				
////				Utils.showNotification(context,
////						"来自[" + message.getExtRemoteDisplayName() + "]的消息",
////						contentStr);
//			} else {
//				Utils.playRingtone(context);
//			}
//			notification(context);
		}
	}

	@Override
	public void processOfflineMessage(ArrayList<PushMessage> messages) {
		Log.d(TAG, "processOfflineMessage()");
		
		ArrayList<Message> ids = DBManager.getInstance(context)
				.onOfflineMessage(messages);

		notification(context);

		handleMsgUpdate(ids);
		
		boolean acceptVoiceMsg = mVoiceMessageFilter.acceptOfflineMessage(messages);
		if (acceptVoiceMsg) {
			mVoiceMessageListener.processOfflineMessage(messages);
		}
	}

	private boolean isMsgUpdate(Msg msg) {
		if (msg.MsgType == Msg.MSG_TYPE_UPDATE) {
//			ObjectContentUpdate oc = Msg.readValue(msg.getObjectContent(),
//					ObjectContentUpdate.class);
			ObjectContentUpdate oc = (ObjectContentUpdate) msg.getObjectContentAsObjectContent();
//			ObjectContentUpdate oc = (ObjectContentUpdate) msg.ObjectContent;
			if (oc != null && oc.versionCode > Utils.getVersionCode(context)) {
				Utils.playRingtone(context);
				UpdateEnableActivity_.intent(context)
						.flags(Intent.FLAG_ACTIVITY_NEW_TASK).msg(msg).start();
			}
			return true;
		}
		return false;
	}
	
//	private void notification() {
//		long unreadCount = DBManager.getInstance(context).getChatListUnreadCount(
//				MyApplication.getInstance().getLocalUserName());
//		if (unreadCount > 0) {
//			if (Utils.isSouldNotification(context)) {
//				Utils.showNotification(context, "未读消息", unreadCount + "条未读消息");
//			} else {
//				Utils.playRingtone(context);
//			}
//		} 
//	}
	
	public static void notification(Context context) {
//		if (Utils.isSouldNotification(context)) {
			String localUserName = MyApplication.getInstance().getLocalUserName();
			Log.d(TAG, "localUserName:"+localUserName);
			Cursor cursor = DBManager.getInstance(context).getAllTypeChatList(localUserName);
			int count = cursor.getCount();
			DaoSession daoSession = MyApplication.getDaoSession(context);
			MessageDao messageDao = daoSession.getMessageDao();
			
			if (count > 0) {
				int contactCount = 0;
				int messageCount = 0;
				cursor.moveToFirst();
	            while (!cursor.isAfterLast()) {
	            	int currentContactUnreadCount = cursor.getInt(1);
	            	if(currentContactUnreadCount > 0) {
	            		contactCount += 1;
	            	}
	            	messageCount += currentContactUnreadCount;
	            	
	            	cursor.moveToNext();
	            }
	            cursor.moveToFirst();
				Message message = messageDao.readEntity(cursor, DBManager.GET_CHATLIST_COLUMNS_OFFSET);
				Log.d(TAG, "notification:"+message.getExtLocalUserName()+message.getExtRemoteUserName()+message.getExtRemoteDisplayName());
				if(Utils.isSouldNotification(context)){
	            if (contactCount == 1) {
//	            	cursor.moveToFirst();
//					Message message = messageDao.readEntity(cursor, DBManager.GET_CHATLIST_COLUMNS_OFFSET);
					
					Content content = Content.createContent(message.getExtRemoteUserName(), message.getContent());
					String contentStr = null;
					if (Member.isTypeQun(message.getExtRemoteUserName())) {
						ContentQun contentQun = (ContentQun) content;
						contentStr = contentQun.realContent;
					} else {
						ContentNormal contentNormal = (ContentNormal) content;
						contentStr = contentNormal.content;
					}
					
					String title = message.getExtRemoteDisplayName();
					if (messageCount > 1){
						title += "(" + messageCount + "条新消息)";
					}
					Utils.showNotification(context, title, contentStr);
	            } else if (contactCount > 1) {
	            	Utils.showNotification(context, contactCount + "个联系人", messageCount + "条新消息");
	            }
				} else if(messageCount > 0 && !message.getFromUserName().equals(localUserName)){
					Utils.playRingtone(context);
				}
			} 
			cursor.close();
	}
	
	private void handleMsgUpdate(ArrayList<Message> ids) {
		for (Iterator<Message> it = ids.iterator(); it.hasNext();) {
			Message message = it.next();
			isMsgUpdate(DBMessage.retriveMsgFromMessage(message));
		}
	}
}
