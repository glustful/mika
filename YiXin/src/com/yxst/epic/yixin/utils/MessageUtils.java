package com.yxst.epic.yixin.utils;

import java.io.File;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.data.dto.model.ObjectContent;
import com.yxst.epic.yixin.data.dto.model.ObjectContentImage;
import com.yxst.epic.yixin.data.dto.model.ObjectContentVoice;
import com.yxst.epic.yixin.data.dto.response.ResponseUpload;
import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.service.MsgService;
import com.yxst.epic.yixin.view.ChatItem;
import com.yxst.epic.yixin.view.ChatItemImgToView;
import com.yxst.epic.yixin.view.ChatItemVoiceToView;

public class MessageUtils {

	private static final String TAG = "MessageUtils";

	public static void sendMediaMsg(Context context, Message message) {
		Long messageId = message.getId();
		message.setExtStatus(DBMessage.STATUS_MEDIA_UPLOAD_PENDING);
		if (messageId == null || messageId <= 0) {
			messageId = DBManager.getInstance(context).insertMessage(message);
		} else {
			DBManager.getInstance(context).update(message);
		}

		String filePath = null;
		String url = Utils.URL_FILE_XX;
		if (Msg.MSG_TYPE_IMAGE == message.getMsgType()) {
			url = Utils.URL_FILE_IMAGE;
			ObjectContentImage objectContent = ObjectContent.readValue(
					message.getObjectContent(), ObjectContentImage.class);
			if (objectContent != null) {
				filePath = objectContent.filePath;
			}
		} else if (Msg.MSG_TYPE_VOICE == message.getMsgType()) {
			url = Utils.URL_FILE_VOICE;
			ObjectContentVoice objectContent = ObjectContent.readValue(
					message.getObjectContent(), ObjectContentVoice.class);
			if (objectContent != null) {
				filePath = objectContent.filePath;
			}
		}

		UploadRequestCallBack callback = new UploadRequestCallBack(context,
				messageId, message);
		MyApplication
				.getInstance()
				.getUploadManager()
				.addNewUpload(message.getClientMsgId(), url, filePath,
						filePath, callback);
	}

	public static class UploadRequestCallBack extends RequestCallBack<String> {

		private Context context;
		private long messageId;
		private Message message;

		public UploadRequestCallBack(Context context, long messageId,
				Message message) {
			this.context = context;
			this.messageId = messageId;
			this.message = message;
		}

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			refreshListItem();

			ResponseUpload responseUpload = Utils.readValue(
					responseInfo.result, ResponseUpload.class);

			Msg msg = DBMessage.retriveMsgFromMessage(message);
			if (msg.MsgType == Msg.MSG_TYPE_IMAGE) {
				ObjectContentImage objectContent = (ObjectContentImage) msg
						.getObjectContentAsObjectContent();
				if (objectContent == null) {
					objectContent = new ObjectContentImage();
				}
				objectContent.responseUpload = responseUpload;
				msg.setObjectContent(Msg.MSG_TYPE_IMAGE, objectContent);
			} else if (msg.MsgType == Msg.MSG_TYPE_VOICE) {
				ObjectContentVoice objectContent = (ObjectContentVoice) msg
						.getObjectContentAsObjectContent();
				if (objectContent == null) {
					objectContent = new ObjectContentVoice();
				}
				objectContent.responseUpload = responseUpload;
				msg.setObjectContent(Msg.MSG_TYPE_VOICE, objectContent);
			}

			if (responseUpload != null) {
				msg.MediaId = responseUpload.fileUrl;
			}

			message.setObjectContent(msg.getObjectContentAsString());
			message.setExtStatus(DBMessage.STATUS_MEDIA_UPLOAD_SUCCESS);
			DBManager.getInstance(context).update(message);

			MsgService.getMsgWriter(context).sendMessage(message);
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			refreshListItem();
			message.setExtStatus(DBMessage.STATUS_MEDIA_UPLOAD_FAILURE);
			DBManager.getInstance(context).update(message);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			// message.setExtStatus(DBMessage.STATUS_MEDIA_UPLOAD_LOADING);
			// DBManager.getInstance(context).update(message);
			Log.d(TAG, "onLoading() total:" + total);
			Log.d(TAG, "onLoading() current:" + current);
			Log.d(TAG, "onLoading() isUploading:" + isUploading);
			refreshListItem();
		}

		@Override
		public void onStart() {
			refreshListItem();
			message.setExtStatus(DBMessage.STATUS_MEDIA_UPLOAD_START);
			DBManager.getInstance(context).update(message);
		}

		@Override
		public void onCancelled() {
			refreshListItem();
			message.setExtStatus(DBMessage.STATUS_MEDIA_UPLOAD_CANCEL);
			DBManager.getInstance(context).update(message);
		}

		@SuppressWarnings("unchecked")
		private void refreshListItem() {
			if (userTag == null)
				return;
			WeakReference<View> tag = (WeakReference<View>) userTag;
			View view = tag.get();
			// if (view != null) {
			// view.refresh();
			// }
			if (view instanceof ChatItemImgToView) {
				ChatItemImgToView v = (ChatItemImgToView) view;
				v.refresh();
			} else if (view instanceof ChatItemVoiceToView) {
				// ChatItemVoiceToView v = (ChatItemVoiceToView) view;
				// v.refresh();
			}
		}
	}
	
	public static void receiveMediaMsg(Context context, Message message) {
		Log.d(TAG, "sendMediaMsg()"+message);
		
		if (message == null || message.getId() == null) {
			return;
		}
		
		Long messageId = message.getId();
		
		String url = null;
		String filePath = null;
		Log.d(TAG, "msgType:"+message.getMsgType()+"is equal:"+(Msg.MSG_TYPE_IMAGE == message.getMsgType()));
		if (Msg.MSG_TYPE_IMAGE == message.getMsgType()) {
			ObjectContentImage objectContent = ObjectContent.readValue(
					message.getObjectContent(), ObjectContentImage.class);
			if (objectContent != null) {
				ResponseUpload responseUpload = objectContent.responseUpload;
				if (responseUpload != null) {
					if (!TextUtils.isEmpty(responseUpload.fileUrl)) {
						url = "http://" + responseUpload.fileUrl;
					}
				}
				
				File dirFile = Utils
						.ensureIMSubDir(
								context,
								message.getExtInOut() == DBMessage.INOUT_IN ? Utils.FILE_PATH_SUB_DIR_SOUND_IN
										: Utils.FILE_PATH_SUB_DIR_IMAGE_OUT);
				File file = new File(dirFile, System.currentTimeMillis() + '.' + objectContent.fileExtention);
				filePath = file.getAbsolutePath();
			}
		} else if (Msg.MSG_TYPE_VOICE == message.getMsgType()) {
			ObjectContentVoice objectContent = ObjectContent.readValue(
					message.getObjectContent(), ObjectContentVoice.class);
			if (objectContent != null) {
				ResponseUpload responseUpload = objectContent.responseUpload;
				if (responseUpload != null) {
					if (!TextUtils.isEmpty(responseUpload.fileUrl)) {
						url = "http://" + responseUpload.fileUrl;
					}
				}
				
				File dirFile = Utils
						.ensureIMSubDir(
								context,
								message.getExtInOut() == DBMessage.INOUT_IN ? Utils.FILE_PATH_SUB_DIR_SOUND_IN
										: Utils.FILE_PATH_SUB_DIR_SOUND_OUT);
				File file = new File(dirFile, System.currentTimeMillis() + ".amr");
				filePath = file.getAbsolutePath();
			}
		}
		
		Log.d(TAG, "sendMediaMsg() url:" + url);
		Log.d(TAG, "sendMediaMsg() filePath:" + filePath);

		DownloadRequestCallBack callback = new DownloadRequestCallBack(context,
				messageId, message);
		try {
			MyApplication
					.getInstance()
					.getDownloadManager()
					.addNewDownload(url, filePath, filePath, false, false, callback);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class DownloadRequestCallBack extends RequestCallBack<File> {

		private Context context;
		private long messageId;
		private Message message;

		public DownloadRequestCallBack(Context context, long messageId,
				Message message) {
			this.context = context;
			this.messageId = messageId;
			this.message = message;
		}

		@Override
		public void onSuccess(ResponseInfo<File> responseInfo) {
			refreshListItem();

			File file = responseInfo.result;

			Msg msg = DBMessage.retriveMsgFromMessage(message);
			if (msg.MsgType == Msg.MSG_TYPE_IMAGE) {
				ObjectContentImage objectContent = (ObjectContentImage) msg
						.getObjectContentAsObjectContent();
				if (objectContent == null) {
					objectContent = new ObjectContentImage();
				}
				objectContent.filePath = file.getPath();
				msg.setObjectContent(Msg.MSG_TYPE_IMAGE, objectContent);
			} else if (msg.MsgType == Msg.MSG_TYPE_VOICE) {
				ObjectContentVoice objectContent = (ObjectContentVoice) msg
						.getObjectContentAsObjectContent();
				if (objectContent == null) {
					objectContent = new ObjectContentVoice();
				}
				objectContent.filePath = file.getPath();
				msg.setObjectContent(Msg.MSG_TYPE_VOICE, objectContent);
			}

			message.setObjectContent(msg.getObjectContentAsString());
			message.setExtStatus(DBMessage.STATUS_MEDIA_DOWNLOAD_SUCCESS);
			DBManager.getInstance(context).update(message);
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			Log.d(TAG, "downloading error:"+error+"message:"+msg);
			refreshListItem();
			message.setExtStatus(DBMessage.STATUS_MEDIA_DOWNLOAD_FAILURE);
			DBManager.getInstance(context).update(message);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			// message.setExtStatus(DBMessage.STATUS_MEDIA_UPLOAD_LOADING);
			// DBManager.getInstance(context).update(message);
			Log.d(TAG, "onLoading() total:" + total);
			Log.d(TAG, "onLoading() current:" + current);
			Log.d(TAG, "onLoading() isUploading:" + isUploading);
			refreshListItem();
		}

		@Override
		public void onStart() {
			refreshListItem();
			message.setExtStatus(DBMessage.STATUS_MEDIA_DOWNLOAD_START);
			DBManager.getInstance(context).update(message);
		}

		@Override
		public void onCancelled() {
			refreshListItem();
			message.setExtStatus(DBMessage.STATUS_MEDIA_DOWNLOAD_CANCEL);
			DBManager.getInstance(context).update(message);
		}

		@SuppressWarnings("unchecked")
		private void refreshListItem() {
			if (userTag == null)
				return;
			WeakReference<View> tag = (WeakReference<View>) userTag;
			View view = tag.get();
			// if (view != null) {
			// view.refresh();
			// }
			if (view instanceof ChatItem) {
				ChatItem v = (ChatItem) view;
				v.refresh();
			}
		}
	}
	
	public static boolean isDeviceSyncMessage(String localUserName, Message message) {
		if (localUserName != null && localUserName.equals(message.getFromUserName())) {
			if (message.getMid() != null && message.getMid() > 0) {
				return true;
			}
		}
		return false;
	}
	
	public static String getFileUrl(Message message) {
		if (message != null) {
			ObjectContentImage objectContent = ObjectContent.readValue(
					message.getObjectContent(), ObjectContentImage.class);
			return getFileUrl(objectContent);
		}
		return null;
	}
	
	public static String getFileUrl(ObjectContentImage objectContent) {
//		String fileUrl = message.getMediaId();
		if (objectContent != null) {
			ResponseUpload responseUpload = objectContent.responseUpload;
			if (responseUpload != null) {
				return responseUpload.fileUrl;
			}
		}
		return null;
	}
	
	public static Long getMessageTime(Message message) {
		return getMessageTime(message.getMid(), message.getExtTime());
	}
	
	public static Long getMessageTime(Long mid, Long extTime) {
		if (mid != null && mid != 0) {
			return mid / 1000;
		}
		return extTime;
	}
}
