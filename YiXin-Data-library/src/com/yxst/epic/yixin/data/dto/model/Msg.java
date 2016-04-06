package com.yxst.epic.yixin.data.dto.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yxst.epic.yixin.data.dto.util.Utils;

/**
 * 消息：用于发送与接收<br/>
 * 发送：只使用 FromUserName、ToUserName、MsgType、Content、MediaId、ClientMsgId；<br/>
 * 
 * @author liuxue
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Msg extends BaseMsg {

	private static final long serialVersionUID = -6790642716436712051L;

	/**
	 * 普通消息-文本
	 */
	public static final int MSG_TYPE_NORMAL = 1;

	/**
	 * 普通消息-图片
	 */
	public static final int MSG_TYPE_IMAGE = 2;
	
	/**
	 * 普通消息-语音
	 */
	public static final int MSG_TYPE_VOICE = 3;
	
	/**
	 * 普通消息-提示
	 */
	public static final int MSG_TYPE_TIPS = 51;
	
	/**
	 * 删除聊天会话列表
	 */
	public static final int MSG_TYPE_DELETE_CHAT = 9;

	/**
	 * 应用消息-类型1
	 * <p>
	 * ObjectContent : {@link com.yxst.epic.yixin.data.dto.model.ObjectContentApp101
	 * ObjectContentApp101}
	 * <p>
	 * 
	 * @see com.yxst.epic.yixin.data.dto.model.ObjectContentApp101
	 */
	public static final int MSG_TYPE_APP_101 = 101;
	
	public static final int MSG_TYPE_APP_102 = 102;
	
	public static final int MSG_TYPE_APP_103 = 103;

	/**
	 * 系统消息-更新
	 * <p>
	 * ObjectContent : {@link com.yxst.epic.yixin.data.dto.model.ObjectContentUpdate
	 * ObjectContentUpdate}
	 * <p>
	 * 
	 * @see com.yxst.epic.yixin.data.dto.model.ObjectContentUpdate
	 */
	public static final int MSG_TYPE_UPDATE = 1001;

	public String deviceID;
	
	/**
	 * 消息ID（服务器）
	 */
	@JsonProperty(value = "msgId")
	public String MsgId;

	/**
	 * 消息ID（本地生成）
	 */
	@JsonProperty(value = "clientMsgId")
	public String ClientMsgId;

	/**
	 * 发送人用户名（发送人可以是人、应用、群）
	 */
	@JsonProperty(value = "fromUserName")
	public String FromUserName;

	@JsonProperty(value = "fromDisplayName")
	public String FromDisplayName;

	@JsonProperty(value = "toDisplayName")
	public String ToDisplayName;

	/**
	 * 接收人用户名（接收人可以是人、应用、群）
	 */
	@JsonProperty(value = "toUserName")
	public String ToUserName;

	/**
	 * 消息类型 文本消息、图片消息、视频消息
	 */
	@JsonProperty(value = "msgType")
	public int MsgType;

	/**
	 * 消息内容<br/>
	 * 发送：图片消息不使用Content，使用MediaId<br/>
	 * 接收：消息内容 除简单文本，也可能为复杂格式的消息。如JSON格式。<br/>
	 */
	@JsonProperty(value = "content")
	public String Content;

//	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "msgType")
	@JsonProperty(value = "objectContent", required = false)
//	public ObjectContent ObjectContent;
	private Object ObjectContent;

	@JsonIgnore(true)
	public void setObjectContent(int msgType, String objectContentStr) {
		
//		Map<String, Object> fromValue = new HashMap<String, Object>();
//		fromValue.put("msgType", msgType);
//		fromValue.put("objectContent",
//				readValue(objectContentStr, Object.class));
//
//		Msg msg = convertValue(fromValue, Msg.class);
////		Msg msg = readValue(writeValueAsString(fromValue), Msg.class);
//		this.ObjectContent = msg.ObjectContent;
		
		this.MsgType = msgType;
		this.ObjectContent = Utils.readValue(objectContentStr, Object.class);
	}
	
	@JsonIgnore(true)
	public void setObjectContent(int msgType, ObjectContent objectContent) {
		this.MsgType = msgType;
		this.ObjectContent = Utils.convertValue(objectContent, Object.class);
	}

	@JsonIgnore(true)
	public String getObjectContentAsString() {
		return Utils.writeValueAsString(ObjectContent);
	}

	@JsonIgnore(true)
	public ObjectContent getObjectContentAsObjectContent() {
		Object fromValue = this.ObjectContent;
		Class<? extends ObjectContent> toValueType = null;
		
		switch (this.MsgType) {
		case MSG_TYPE_UPDATE:
			toValueType = ObjectContentUpdate.class;
			break;
		case MSG_TYPE_IMAGE:
			toValueType = ObjectContentImage.class;
			break;
		case MSG_TYPE_VOICE:
			toValueType = ObjectContentVoice.class;
			break;
		case MSG_TYPE_APP_101:
			toValueType = ObjectContentApp101.class;
			break;
		case MSG_TYPE_APP_102:
			toValueType = ObjectContentApp102.class;
			break;
		case MSG_TYPE_APP_103:
			toValueType = ObjectContentApp103.class;
			break;
		default:
			break;
		}
		
		return Utils.convertValue(fromValue, toValueType);
	}
	
	/**
	 * 资源ID<br/>
	 * 图片消息、视频消息等上传服务器成功后，返回的资源ID
	 */
	@JsonProperty(value = "mediaId")
	public String MediaId;

	@JsonProperty(value = "status")
	public int Status;

	/**
	 * 创建时间（服务器）
	 */
	@JsonProperty(value = "createTime")
	public Long CreateTime;

	/**
	 * 消息点击后跳转的URL
	 */
	@JsonProperty(value = "url")
	public String Url;

	/**
	 * 应用消息 的 类型 非应用消息为0
	 */
	@JsonProperty(value = "appMsgType")
	public String AppMsgType;

	@JsonProperty(value = "expire")
	public Long Expire;

	public ArrayList<String> activeSessions = new ArrayList<String>(0);

	public String toUserKey;

	@Override
	public String toString() {
		return Utils.writeValueAsString(this);
	}

	public static Msg readValue(String msg) {
		return Utils.readValue(msg, Msg.class);
	}
}
