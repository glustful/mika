package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;

import android.content.Context;

import com.yxst.epic.yixin.db.Message;

@EViewGroup
public class ChatItemNullView extends ChatItem {

	public ChatItemNullView(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {
//		super.bind(message);
	}
	
	@Override
	public void bindLastMessage(Long lastExtTime) {
//		super.bindLastMessage(lastExtTime);
	}
}
