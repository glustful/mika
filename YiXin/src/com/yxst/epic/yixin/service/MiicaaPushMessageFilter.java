package com.yxst.epic.yixin.service;

import java.util.ArrayList;

import android.util.Log;

import com.yxst.epic.yixin.data.dto.model.BaseMsg;
import com.yxst.epic.yixin.push.cli.PushMessage;
import com.yxst.epic.yixin.push.service.PushMessageFilter;

public class MiicaaPushMessageFilter implements PushMessageFilter {

	private static final String TAG = "MiicaaMessageFilter";

	private static final int BASE_MSG_TYPE_CODE_YOUXIN = 0;
	
	@Override
	public boolean acceptOnlineMessage(PushMessage message) {
		Log.d(TAG, "acceptOnlineMessage()");

//		if (message != null) {
//			BaseMsg baseMsg = BaseMsg.readValue(message.getMsg());
//			if (baseMsg != null) {
//				if (baseMsg.BaseMsgTypeCode == BASE_MSG_TYPE_CODE_YOUXIN) {
//					return true;
//				}
//			}
//		}
//
//		return false;
		return true;
	}

	@Override
	public boolean acceptOfflineMessage(ArrayList<PushMessage> messages) {
		Log.d(TAG, "acceptOfflineMessage()");

		ArrayList<PushMessage> list = this.getAcceptOfflineMessage(messages);
		if (list != null && list.size() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public ArrayList<PushMessage> getAcceptOfflineMessage(
			ArrayList<PushMessage> messages) {
		
		ArrayList<PushMessage> list = new ArrayList<PushMessage>();
		
		for (PushMessage message : messages) {
			if (this.acceptOnlineMessage(message)) {
				list.add(message);
			}
		}
		
		return list;
	}

}
