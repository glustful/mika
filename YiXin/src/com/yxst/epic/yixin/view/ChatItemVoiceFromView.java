package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.org.UserInfo;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.model.ObjectContent;
import com.yxst.epic.yixin.data.dto.model.ObjectContentVoice;
import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.model.Content;
import com.yxst.epic.yixin.model.ContentQun;
import com.yxst.epic.yixin.utils.MessageUtils;
import com.yxst.epic.yixin.utils.Utils;
import com.yxst.epic.yixin.utils.VoiceUtils;
import com.yxst.epic.yixin.utils.VoiceUtils.DefaultVoiceLoadCallBack;

@EViewGroup(R.layout.list_item_chatfrom_voice)
public class ChatItemVoiceFromView extends ChatItem {

	private static final String TAG = "ChatItemVoiceFromView";

	@ViewById
	TextView tvName;
	
	@ViewById
	ImageView ivIcon;
	
//	@ViewById
//	LinearLayout layoutContent;
	
	@ViewById
	ImageView ivVoice;
	
	@ViewById
	TextView tvVoiceLenght;
	
	@ViewById
	ProgressBar statusProgress;

	@ViewById
	ImageView statusError;

	@ViewById
	ImageView ivNew;

	@DrawableRes(R.drawable.chatfrom_voice_playing)
	Drawable chatfrom_voice;

	@DrawableRes(R.drawable.chatfrom_voice)
	Drawable chatfrom_voice_playing;

	public ChatItemVoiceFromView(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {
		this.setTag(message.getClientMsgId());

		statusProgress.setVisibility(View.GONE);
		statusError.setVisibility(View.GONE);
		ivNew.setVisibility(View.GONE);
		ivVoice.setImageDrawable(chatfrom_voice);
		tvVoiceLenght.setText(null);

		String username = "";
		// tvName.setText("需要服务器中转加入用户名");
		Content content = Content.createContent(message.getExtRemoteUserName(),
				message.getContent());
		if (Member.isTypeQun(message.getExtRemoteUserName())) {
			ContentQun contentQun = (ContentQun) content;
			username = contentQun.member.UserName;
			tvName.setVisibility(View.VISIBLE);
			tvName.setText(contentQun.member != null ? contentQun.member.DisplayName
					: null);
		} else {
			tvName.setVisibility(View.GONE);
			username = message.getExtRemoteUserName();
			tvName.setText(message.getExtRemoteUserName());
		}
		if(username != null && !"".equals(username)){
			String u = username.substring(0,username.indexOf("@"));
			Long uid = Long.parseLong(u);
			Log.d("TOKENTOUID", uid.toString());
			String usercode = UserInfo.findByIdForUserCode(uid);
			usercode = usercode == null?"t":usercode;
			Tools.setHeadImg(usercode, ivIcon);
			}

		ObjectContentVoice objectContent = ObjectContent.readValue(
				message.getObjectContent(), ObjectContentVoice.class);
		if (objectContent != null) {
			tvVoiceLenght.setText((int) (objectContent.voiceLength / 1000)
					+ "\"");
			ivNew.setVisibility(objectContent.isListened ? View.GONE
					: View.VISIBLE);

			boolean isPlaying = voiceUtils.isPlaying()
					&& objectContent.filePath != null
					&& objectContent.filePath.equals(voiceUtils.getPath());
			if (isPlaying) {
				ivVoice.setImageDrawable(chatfrom_voice_playing);
				MyVoiceLoadCallback callBack = new MyVoiceLoadCallback();
				voiceUtils.setVoiceLoadCallBack(callBack);
			}
		}

		Integer extStatus = message.getExtStatus();
		if (extStatus != null) {
			switch (extStatus) {
			case DBMessage.STATUS_MEDIA_DOWNLOAD_PENDING:
			case DBMessage.STATUS_MEDIA_DOWNLOAD_START:
			case DBMessage.STATUS_MEDIA_DOWNLOAD_LOADING:
				statusProgress.setVisibility(View.VISIBLE);
				break;
			case DBMessage.STATUS_MEDIA_DOWNLOAD_FAILURE:
			case DBMessage.STATUS_MEDIA_DOWNLOAD_CANCEL:
				statusError.setVisibility(View.VISIBLE);
				break;
			case DBMessage.STATUS_MEDIA_DOWNLOAD_SUCCESS:
				break;
			default:
				break;
			}
		}
	}

	@Click(R.id.layoutContent)
	void onClickLayoutContent() {
		Log.d(TAG, "onClickLayoutContent()");

		Integer extStatus = message.getExtStatus();
		Log.d(TAG, "onClickLayoutContent() extstatus:"+extStatus);
		if (extStatus != null) {
			switch (extStatus) {
			case DBMessage.STATUS_MEDIA_DOWNLOAD_PENDING:
			case DBMessage.STATUS_MEDIA_DOWNLOAD_START:
			case DBMessage.STATUS_MEDIA_DOWNLOAD_LOADING:
				Utils.showToastShort(getContext(), "正在下载，请稍后查看！");
				break;
			case DBMessage.STATUS_MEDIA_DOWNLOAD_FAILURE:
			case DBMessage.STATUS_MEDIA_DOWNLOAD_CANCEL:
				Utils.showToastShort(getContext(), "下载失败，请重试！");
				break;
			case DBMessage.STATUS_MEDIA_DOWNLOAD_SUCCESS:
				ObjectContentVoice objectContent = ObjectContent.readValue(
						message.getObjectContent(), ObjectContentVoice.class);

				Log.d(TAG, "voiceLength:" + objectContent.voiceLength);
				Log.d(TAG, "filePath:" + objectContent.filePath);

				objectContent.isListened = true;
				message.setObjectContent(Utils
						.writeValueAsString(objectContent));

				DBManager.getInstance(getContext()).update(message);

				ivNew.setVisibility(View.GONE);

				MyVoiceLoadCallback callBack = new MyVoiceLoadCallback();
				voiceUtils.play(objectContent.filePath, callBack);

				break;
			default:
				break;
			}
		}

	}

	private class MyVoiceLoadCallback extends DefaultVoiceLoadCallBack {

		@Override
		public void onPreLoad(String uri) {
			super.onPreLoad(uri);

			ivVoice.setImageDrawable(chatfrom_voice_playing);
		}

		@Override
		public void onError() {
			Log.d(TAG, "onDestroy()");
			super.onError();
		}

		@Override
		public void onDestroy() {
			Log.d(TAG, "onDestroy()");
			super.onDestroy();

			ivVoice.setImageDrawable(chatfrom_voice);
		}

		@Override
		public void onLoadFailed(String uri) {
			Log.d(TAG, "onLoadFailed()");
			super.onLoadFailed(uri);
			ivVoice.setImageDrawable(chatfrom_voice);
		}

		@Override
		public void onCompletion() {
			Log.d(TAG, "onCompletion()");
			super.onCompletion();
			ivVoice.setImageDrawable(chatfrom_voice);
		}
	}

	private VoiceUtils voiceUtils;

	public void setVoiceUtils(VoiceUtils voiceUtils) {
		this.voiceUtils = voiceUtils;
	}
	
	@Click(R.id.statusError)
	void statusErrorClick(View v){
		MessageUtils.receiveMediaMsg(getContext(), message);
	}
	
	
}
