package com.yxst.epic.yixin.service;

import java.util.ArrayList;

import android.content.Context;

import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.push.cli.PushMessage;
import com.yxst.epic.yixin.push.service.PushMessageFilter;

public class VoiceMessageFilter implements PushMessageFilter {

	private static final int BASE_MSG_TYPE_CODE_YOUXIN = 0;

	public VoiceMessageFilter(Context context) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean acceptOnlineMessage(PushMessage message) {
		if (message != null) {
			Msg msg = Msg.readValue(message.getMsg());
			if (msg != null) {
				if (msg.BaseMsgTypeCode == BASE_MSG_TYPE_CODE_YOUXIN
						&& msg.MsgType == Msg.MSG_TYPE_VOICE) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean acceptOfflineMessage(ArrayList<PushMessage> messages) {
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
