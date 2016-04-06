package com.yxst.epic.yixin.service;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.activity.UpdateEnableActivity_;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.data.dto.model.ObjectContentUpdate;
import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.model.Content;
import com.yxst.epic.yixin.model.ContentNormal;
import com.yxst.epic.yixin.model.ContentQun;
import com.yxst.epic.yixin.push.cli.PushMessage;
import com.yxst.epic.yixin.push.service.PushMessageListener;
import com.yxst.epic.yixin.utils.Utils;

public class YiXinPushMessageListener implements PushMessageListener {

	private static final String TAG = "YouXinPushMessageListener";
	
	private Context context;

	VoiceMessageFilter mVoiceMessageFilter;
	VoiceMessageListener mVoiceMessageListener;
	
	public YiXinPushMessageListener(Context context) {
		this.context = context;
		mVoiceMessageFilter = new VoiceMessageFilter(context);
		mVoiceMessageListener = new VoiceMessageListener(context);
	}

	@Override
	public void processOnlineMessage(PushMessage pushMessage) {
		Log.d(TAG, "processOnlineMessage()");
		
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
			
			if (Utils.isSouldNotification(context)) {
				Content content = Content.createContent(message.getExtRemoteUserName(), message.getContent());
				String contentStr = null;
				if (Member.isTypeQun(message.getExtRemoteUserName())) {
					ContentQun contentQun = (ContentQun) content;
					contentStr = contentQun.realContent;
				} else {
					ContentNormal contentNormal = (ContentNormal) content;
					contentStr = contentNormal.content;
				}
				
				Utils.showNotification(context,
						"来自[" + message.getExtRemoteDisplayName() + "]的消息",
						contentStr);
			} else {
				Utils.playRingtone(context);
			}
		}
	}

	@Override
	public void processOfflineMessage(ArrayList<PushMessage> messages) {
		Log.d(TAG, "processOfflineMessage()");
		
		ArrayList<Message> ids = DBManager.getInstance(context)
				.onOfflineMessage(messages);

		notification();

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
	
	private void notification() {
		long unreadCount = DBManager.getInstance(context).getChatListUnreadCount(
				MyApplication.getInstance().getLocalUserName());
		if (unreadCount > 0) {
			if (Utils.isSouldNotification(context)) {
				Utils.showNotification(context, "未读消息", unreadCount + "条未读消息");
			} else {
				Utils.playRingtone(context);
			}
		} 
	}
	
	private void handleMsgUpdate(ArrayList<Message> ids) {
		for (Iterator<Message> it = ids.iterator(); it.hasNext();) {
			Message message = it.next();
			isMsgUpdate(DBMessage.retriveMsgFromMessage(message));
		}
	}
}
