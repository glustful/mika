package com.yxst.epic.yixin.db;

import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.push.cli.PushMessage;

/**
 * @author liuxue
 *	
 * MessageExt-Message Extention
 *
 */
public class DBMessage /*extends Message implements Parcelable*/ {

	public static final int INOUT_IN = 1;
	public static final int INOUT_OUT = 0;
	public static final int READ_TRUE = 1;
	public static final int READ_FALSE = 0;
	
	public static final int STATUS_MEDIA_UPLOAD_PENDING = 100;
	public static final int STATUS_MEDIA_UPLOAD_START = 101;
	public static final int STATUS_MEDIA_UPLOAD_CANCEL = 102;
	public static final int STATUS_MEDIA_UPLOAD_SUCCESS = 103;
	public static final int STATUS_MEDIA_UPLOAD_FAILURE = 104;
	public static final int STATUS_MEDIA_UPLOAD_LOADING = 105;
	
	public static final int STATUS_MEDIA_DOWNLOAD_PENDING = 150;
	public static final int STATUS_MEDIA_DOWNLOAD_START = 151;
	public static final int STATUS_MEDIA_DOWNLOAD_CANCEL = 152;
	public static final int STATUS_MEDIA_DOWNLOAD_SUCCESS = 153;
	public static final int STATUS_MEDIA_DOWNLOAD_FAILURE = 154;
	public static final int STATUS_MEDIA_DOWNLOAD_LOADING = 155;
	
	public static final int STATUS_PENDING = 0;
	public static final int STATUS_SENDING = 2;
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_ERROR = -1;

	public static Message retriveMessageFromPushMessage(PushMessage pushMessage, int inOut) {
		Msg msg = Msg.readValue(pushMessage.getMsg());
		
		Message message = retriveMessageFromMsg(msg, inOut);
		
		message.setMid(pushMessage.getMid());
		message.setGid(pushMessage.getGid());
		
		return message;
	}
	
	public static Message retriveMessageFromMsg(Msg msg, int inOut, int status) {
		Message message = retriveMessageFromMsg(msg, inOut);
		
		message.setExtStatus(status);
		
		return message;
	}
	
	public static Message retriveMessageFromMsg(Msg msg, int inOut) {
		Message message = new Message();
		
		retriveMessageFromMsg(message, msg, inOut);
		
		return message;
	}
	
	private static void retriveMessageFromMsg(Message message, Msg msg, int inOut) {
		message.setExtInOut(inOut);
		message.setExtTime(System.currentTimeMillis());

		if (DBMessage.INOUT_IN == inOut) {
			message.setExtLocalUserName(msg.ToUserName);
			message.setExtRemoteUserName(msg.FromUserName);
			message.setExtRemoteDisplayName(msg.FromDisplayName);
//			message.setExtRemoteDisplayName(!TextUtils.isEmpty(msg.FromDisplayName) ? msg.FromDisplayName : msg.FromUserName);

			message.setExtRead(DBMessage.READ_FALSE);
		} else if (DBMessage.INOUT_OUT == inOut) {
			message.setExtLocalUserName(msg.FromUserName);
			message.setExtRemoteUserName(msg.ToUserName);
			message.setExtRemoteDisplayName(msg.ToDisplayName);
//			message.setExtRemoteDisplayName(!TextUtils.isEmpty(msg.FromDisplayName) ? msg.FromDisplayName : msg.FromUserName);
		}

		message.setMsgId(msg.MsgId);
		message.setClientMsgId(msg.ClientMsgId);
		message.setMsgType(msg.MsgType);
		message.setCreateTime(msg.CreateTime);
		message.setContent(msg.Content);
		message.setObjectContent(msg.getObjectContentAsString());
		message.setMediaId(msg.MediaId);
		message.setFromUserName(msg.FromUserName);
		message.setToUserName(msg.ToUserName);
		message.setUrl(msg.Url);
	}
	
	public static Msg retriveMsgFromMessage(Message message) {
		Msg msg = new Msg();
		
		msg.FromUserName = message.getFromUserName();
		msg.ToUserName = message.getToUserName();
		msg.Content = message.getContent();
		msg.ClientMsgId = message.getClientMsgId();
		msg.CreateTime = message.getExtTime();
		msg.MediaId = message.getMediaId();
		msg.MsgId = message.getMsgId();
		msg.MsgType = message.getMsgType();
//		msg.ObjectContent = message.getObjectContent();
		msg.setObjectContent(message.getMsgType(), message.getObjectContent());
		msg.Url = message.getUrl();
		
		int inOut = message.getExtInOut();
		if (INOUT_IN == inOut) {
			msg.FromDisplayName = message.getExtRemoteDisplayName();
		} else if (INOUT_OUT == inOut) {
			msg.ToDisplayName = message.getExtRemoteDisplayName();
		}
			
		return msg;
	}

}
