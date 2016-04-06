package com.yxst.epic.yixin.service;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.push.cli.PushMessage;
import com.yxst.epic.yixin.push.service.PushMessageListener;
import com.yxst.epic.yixin.utils.MessageUtils;

public class VoiceMessageListener implements PushMessageListener {

	private static final String TAG = "VoiceMessageListener";

	private Context context;

	public VoiceMessageListener(Context context) {
		this.context = context;
	}

	@Override
	public void processOnlineMessage(PushMessage message) {
		Log.d(TAG, "processOnlineMessage()");
		Message m = DBManager.getInstance(context).getMessageByMid(
				message.getMid());
		MessageUtils.receiveMediaMsg(context, m);
	}

	@Override
	public void processOfflineMessage(ArrayList<PushMessage> messages) {
		Log.d(TAG, "processOfflineMessage()"+messages.size());
		for(int i = 0; i < messages.size(); i++){
			processOnlineMessage(messages.get(i));
		}
//		for (PushMessage pushMessage : messages) {
//			processOnlineMessage(pushMessage);
//		}
	}

}
