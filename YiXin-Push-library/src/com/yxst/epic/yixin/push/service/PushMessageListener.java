package com.yxst.epic.yixin.push.service;

import java.util.ArrayList;

import com.yxst.epic.yixin.push.cli.PushMessage;

public interface PushMessageListener {

	public void processOnlineMessage(PushMessage message);

	public void processOfflineMessage(ArrayList<PushMessage> messages);
}
