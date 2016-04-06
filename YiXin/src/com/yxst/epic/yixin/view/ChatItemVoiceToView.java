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
import com.miicaa.home.data.business.account.AccountInfo;
import com.yxst.epic.yixin.data.dto.model.ObjectContent;
import com.yxst.epic.yixin.data.dto.model.ObjectContentVoice;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.utils.MessageUtils;
import com.yxst.epic.yixin.utils.VoiceUtils;
import com.yxst.epic.yixin.utils.VoiceUtils.DefaultVoiceLoadCallBack;

@EViewGroup(R.layout.list_item_chatto_voice)
public class ChatItemVoiceToView extends ChatItem {

	private static final String TAG = "ChatItemVoiceToView";
	
//	@ViewById
//	LinearLayout layoutContent;

	@ViewById
	ImageView ivVoice;
	
	@ViewById
	TextView tvVoiceLenght;

	@ViewById
	ProgressBar statusSending;
	
	@ViewById
	ImageView ivStatusError;

	@DrawableRes(R.drawable.chatto_voice_playing)
	Drawable chatto_voice;

	@DrawableRes(R.drawable.chatto_voice)
	Drawable chatto_voice_playing;

	public ChatItemVoiceToView(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {
		this.setTag(message.getClientMsgId());

		statusSending.setVisibility(View.GONE);
		ivStatusError.setVisibility(View.GONE);
		ivVoice.setImageDrawable(chatto_voice);
		tvVoiceLenght.setText(null);

		ObjectContentVoice objectContent = ObjectContent.readValue(
				message.getObjectContent(), ObjectContentVoice.class);
		if (objectContent != null) {
			tvVoiceLenght.setText((int) (objectContent.voiceLength / 1000)
					+ "\"");
			boolean isPlaying = voiceUtils.isPlaying()
					&& objectContent.filePath != null
					&& objectContent.filePath.equals(voiceUtils.getPath());
			if (isPlaying) {
				ivVoice.setImageDrawable(chatto_voice_playing);
				MyVoiceLoadCallback callBack = new MyVoiceLoadCallback();
				voiceUtils.setVoiceLoadCallBack(callBack);
			}
//			String userName = message.getExtLocalUserName();
//			userName = userName.substring(0,userName.indexOf("@"));
//			Long uid = Long.parseLong(userName);
//			Log.d("TOKENTOUID", uid.toString());
//			String usercode = UserInfo.findByIdForUserCode(uid);
//			usercode = usercode == null?"t":usercode;
//			Tools.setHeadImg(usercode, ivIcon);
			String userCode = AccountInfo.instance().getLastUserInfo().getCode();
			Tools.setHeadImg(userCode, ivIcon);
		}

		Integer extStatus = message.getExtStatus();
		if (extStatus != null) {
			switch (extStatus) {
			case DBMessage.STATUS_PENDING:
			case DBMessage.STATUS_SENDING:
				statusSending.setVisibility(View.VISIBLE);
				break;
			case DBMessage.STATUS_SUCCESS:
				break;
			case DBMessage.STATUS_ERROR:
				ivStatusError.setVisibility(View.VISIBLE);
				break;
			case DBMessage.STATUS_MEDIA_UPLOAD_PENDING:
			case DBMessage.STATUS_MEDIA_UPLOAD_START:
			case DBMessage.STATUS_MEDIA_UPLOAD_LOADING:
			case DBMessage.STATUS_MEDIA_UPLOAD_SUCCESS:
				statusSending.setVisibility(View.VISIBLE);
				break;
			case DBMessage.STATUS_MEDIA_UPLOAD_FAILURE:
				ivStatusError.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}

		// bindUploadInfo(getContext());
	}

	@Click(R.id.layoutContent)
	void onClickLayoutContent() {
		Log.d(TAG, "onClickLayoutContent()");

		// Integer extStatus = message.getExtStatus();
		// if (extStatus != null) {
		// switch (extStatus) {
		// case DBMessage.STATUS_MEDIA_DOWNLOAD_PENDING:
		// case DBMessage.STATUS_MEDIA_DOWNLOAD_START:
		// case DBMessage.STATUS_MEDIA_DOWNLOAD_LOADING:
		// Utils.showToastShort(getContext(), "�������أ����Ժ�鿴��");
		// break;
		// case DBMessage.STATUS_MEDIA_DOWNLOAD_FAILURE:
		// case DBMessage.STATUS_MEDIA_DOWNLOAD_CANCEL:
		// Utils.showToastShort(getContext(), "����ʧ�ܣ��������ԣ�");
		// break;
		// case DBMessage.STATUS_MEDIA_DOWNLOAD_SUCCESS:
		// break;
		// default:
		// break;
		// }
		// }

		ObjectContentVoice objectContent = ObjectContent.readValue(
				message.getObjectContent(), ObjectContentVoice.class);

		MyVoiceLoadCallback callBack = new MyVoiceLoadCallback();

		this.voiceUtils.play(objectContent.filePath, callBack);
	}

	private class MyVoiceLoadCallback extends DefaultVoiceLoadCallBack {

		@Override
		public void onPreLoad(String uri) {
			super.onPreLoad(uri);

			ivVoice.setImageDrawable(chatto_voice_playing);
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

			ivVoice.setImageDrawable(chatto_voice);
		}

		@Override
		public void onLoadFailed(String uri) {
			Log.d(TAG, "onLoadFailed()");
			super.onLoadFailed(uri);
			ivVoice.setImageDrawable(chatto_voice);
		}

		@Override
		public void onCompletion() {
			Log.d(TAG, "onCompletion()");
			super.onCompletion();
			ivVoice.setImageDrawable(chatto_voice);
		}
	}

	private VoiceUtils voiceUtils;

	public void setVoiceUtils(VoiceUtils voiceUtils) {
		this.voiceUtils = voiceUtils;
	}

	@Click(R.id.ivStatusError)
	void onClickIvStatusError() {
		// Integer extStatus = message.getExtStatus();
		// if (extStatus != null) {
		// if (extStatus == DBMessage.STATUS_MEDIA_UPLOAD_FAILURE) {
		// MessageUtils.sendMediaMsg(getContext(), message);
		// } else if (extStatus == DBMessage.STATUS_ERROR) {
		// MsgService.getMsgWriter(getContext()).sendMessage(message);
		// }
		// }
		MessageUtils.sendMediaMsg(getContext(), message);
	}

	@Override
	public void refresh() {

	}
}
