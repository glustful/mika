package com.yxst.epic.yixin.push.cli;

import java.util.ArrayList;

public interface Listener {
	void onOpen();

	void onClose();

	void onOnlineMessage(PushMessage message);

	void onOfflineMessage(ArrayList<PushMessage> messages);

	void onError(Throwable e, String message);
}
