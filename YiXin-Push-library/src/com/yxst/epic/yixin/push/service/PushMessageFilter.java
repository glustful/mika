package com.yxst.epic.yixin.push.service;

import java.util.ArrayList;

import com.yxst.epic.yixin.push.cli.PushMessage;

public interface PushMessageFilter {

	public boolean acceptOnlineMessage(PushMessage message);

	public boolean acceptOfflineMessage(ArrayList<PushMessage> messages);
	public ArrayList<PushMessage> getAcceptOfflineMessage(ArrayList<PushMessage> messages);
}
