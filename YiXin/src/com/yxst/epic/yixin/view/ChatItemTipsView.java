package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.widget.TextView;

import com.miicaa.home.R;
import com.yxst.epic.yixin.db.Message;

@EViewGroup(R.layout.list_item_chat_tips)
public class ChatItemTipsView extends ChatItem {

	@ViewById
	TextView tvContent;

	public ChatItemTipsView(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {
		tvContent.setText(message.getContent());
	}
}
