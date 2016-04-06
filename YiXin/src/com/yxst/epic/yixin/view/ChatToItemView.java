package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.yxst.epic.yixin.activity.ContactDetailActivity_;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.service.MsgService;
import com.yxst.epic.yixin.utils.SmileyParser;

@EViewGroup(R.layout.list_item_chat_to)
public class ChatToItemView extends ChatItem {

	public static final String TAG = "ChatToItemView";
	
//	@ViewById(R.id.tv_time)
//	TextView tvTime;

	@ViewById(R.id.iv_icon)
	ImageView ivIcon;

	@ViewById(R.id.tv_content)
	TextView tvContent;

	@ViewById
	TextView tvStatus;

	@ViewById
	View statusSending;

	@ViewById
	View statusError;

	public ChatToItemView(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {

		tvTime.setText(DateUtils.getRelativeDateTimeString(getContext(),
				message.getExtTime(), System.currentTimeMillis(),
				DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE));

		tvContent.setText(SmileyParser.getInstance().addSmileySpans(
				message.getContent()));

		tvStatus.setText(null);
		statusSending.setVisibility(View.GONE);
		statusError.setVisibility(View.GONE);
		
		String username = message.getExtLocalUserName();
		
//		Log.d("uiduid", username+"username");
//		if(username != null && !"".equals(username)){
//			AccountInfo.instance().getLastUserInfo().getCode();
//		String u = username.substring(0,username.indexOf("@"));
//		Long uid = Long.parseLong(u);
//		Log.d("uiduid", uid+"uid");
		
//		String usercode = UserInfo.findByIdForUserCode(uid);
//		UserInfo uInfo = UserInfo.findById(uid);
//		Log.d("uiduid",AccountInfo.instance().getLastUserInfo().getId()+"lastId");
//		Log.d("uiduid", uInfo.getName()+"userInfo");
//		Log.d("uiduid", usercode+"usercode");
//		usercode = usercode.substring(1, usercode.length());
		String usercode = AccountInfo.instance().getLastUserInfo().getCode();
		Tools.setHeadImg(usercode, ivIcon);

//		}

		if (message.getExtStatus() != null) {
			if (DBMessage.STATUS_PENDING == message.getExtStatus()) {
				statusSending.setVisibility(View.VISIBLE);
			} else if (DBMessage.STATUS_SENDING == message.getExtStatus()) {
				// tvStatus.setText("正在发送");
				statusSending.setVisibility(View.VISIBLE);
			} else if (DBMessage.STATUS_SUCCESS == message.getExtStatus()) {
				// tvStatus.setText("发送成功");
			} else if (DBMessage.STATUS_ERROR == message.getExtStatus()) {
				// tvStatus.setText("发送失败");
				statusError.setVisibility(View.VISIBLE);
			}
		}
	}

	@Click(R.id.iv_icon)
	void onClickIcon() {
		ContactDetailActivity_.intent(getContext())
				.userName(message.getExtLocalUserName()).start();
	}

	@Click(R.id.statusError)
	void onClickStatusError() {
		MsgService.getMsgWriter(getContext()).sendMessage(message);
	}
}
