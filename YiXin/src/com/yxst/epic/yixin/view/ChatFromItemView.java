package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.utils.AllUtils;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.model.Content;
import com.yxst.epic.yixin.model.ContentNormal;
import com.yxst.epic.yixin.model.ContentQun;
import com.yxst.epic.yixin.utils.SmileyParser;

@EViewGroup(R.layout.list_item_chat_from)
public class ChatFromItemView extends ChatItem {

	public static final String TAG = "ChatFromItemView";
	
	@ViewById(R.id.iv_icon)
	ImageView ivIcon;

	@ViewById(R.id.tv_content)
	TextView tvContent;

	public ChatFromItemView(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {
		tvTime.setText(AllUtils.getnormalTime(message.getExtTime()));
		String username = "";
		Content content = Content.createContent(message.getExtRemoteUserName(), message.getContent());
		if (Member.isTypeQun(message.getExtRemoteUserName())) {
			ContentQun contentQun = (ContentQun) content;
			username  = contentQun.member.UserName;
			tvName.setVisibility(View.VISIBLE);
			tvName.setText(contentQun.member != null ? contentQun.member.DisplayName : null);
			tvContent.setText(SmileyParser.getInstance().addSmileySpans(contentQun.realContent));
		} else {
			ContentNormal contentNormal = (ContentNormal) content;
			username = message.getExtRemoteUserName();
			tvName.setVisibility(View.GONE);
			tvName.setText(username);
			tvContent.setText(SmileyParser.getInstance().addSmileySpans(contentNormal.content));
		}
		if(username != null && !"".equals(username)){
		String u = username.substring(0,username.indexOf("@"));
		Long uid = Long.parseLong(u);
		Log.d("TOKENTOUID", uid.toString());
		String usercode = UserInfo.findByIdForUserCode(uid);
		usercode = usercode == null?"t":usercode;
		Tools.setHeadImg(usercode, ivIcon);
		}
	}

//	@Click(R.id.iv_icon)
//	void onClickIcon(View view) {
//		ContactDetailActivity_.intent(getContext())
//				.userName(message.getExtRemoteUserName()).start();
//	}

}
