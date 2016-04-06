package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.widget.TextView;

import com.miicaa.home.R;
import com.yxst.epic.yixin.data.dto.model.ObjectContent;
import com.yxst.epic.yixin.data.dto.model.ObjectContentApp103;
import com.yxst.epic.yixin.db.Message;

@EViewGroup(R.layout.list_item_chat_app_103)
public class ChatItemApp103View extends ChatItem {

	@ViewById
	TextView tvContent;
	
	public ChatItemApp103View(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {
		ObjectContentApp103 oc = ObjectContent.readValue(message.getObjectContent(), ObjectContentApp103.class);
		if (oc != null) {
			tvContent.setText(oc.content);
		} else {
			tvContent.setText(message.getContent());
		}
	}

}
