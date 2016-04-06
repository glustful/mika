package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.miicaa.home.R;
import com.yxst.epic.yixin.activity.ContactDetailActivity_;
import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.data.dto.model.ObjectContentApp101;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;

@EViewGroup(R.layout.list_item_chat_app_101)
public class ChatItemApp101View extends ChatItem {

//	@ViewById(R.id.tv_time)
//	TextView tvTime;

	@ViewById
	TextView tvTitle;

	@ViewById
	TextView tvContent;

	public ChatItemApp101View(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {

//		ObjectContentApp101 oc = Msg.readValue(message.getObjectContent(),
//				ObjectContentApp101.class);
		Msg msg = DBMessage.retriveMsgFromMessage(message);
		ObjectContentApp101 oc = (ObjectContentApp101) msg.getObjectContentAsObjectContent();
//		ObjectContentApp101 oc = (ObjectContentApp101) msg.ObjectContent;

		tvTitle.setText(null);
		tvContent.setText(null);

		if (oc.data != null) {
			tvTitle.setText(oc.data.title);
			tvContent.setText(oc.data.content);
		}
	}

	@Click(R.id.iv_icon)
	void onClickIcon(View view) {
		ContactDetailActivity_.intent(getContext())
				.userName(message.getExtRemoteUserName()).start();
	}

	public static final String TAG = "ChatFromItemView";

}
