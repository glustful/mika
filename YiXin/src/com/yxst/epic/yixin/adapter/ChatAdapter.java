package com.yxst.epic.yixin.adapter;

import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.lidroid.xutils.BitmapUtils;
import com.miicaa.home.R;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.DaoSession;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.db.MessageDao;
import com.yxst.epic.yixin.listener.OnOperationClickListener;
import com.yxst.epic.yixin.utils.MessageUtils;
import com.yxst.epic.yixin.utils.VoiceUtils;
import com.yxst.epic.yixin.utils.XXBitmapUtils;
import com.yxst.epic.yixin.view.ChatFromItemView;
import com.yxst.epic.yixin.view.ChatFromItemView_;
import com.yxst.epic.yixin.view.ChatItem;
import com.yxst.epic.yixin.view.ChatItemApp101View;
import com.yxst.epic.yixin.view.ChatItemApp101View_;
import com.yxst.epic.yixin.view.ChatItemApp102View;
import com.yxst.epic.yixin.view.ChatItemApp102View_;
import com.yxst.epic.yixin.view.ChatItemApp103View;
import com.yxst.epic.yixin.view.ChatItemApp103View_;
import com.yxst.epic.yixin.view.ChatItemImgFromView;
import com.yxst.epic.yixin.view.ChatItemImgFromView_;
import com.yxst.epic.yixin.view.ChatItemImgToView;
import com.yxst.epic.yixin.view.ChatItemImgToView_;
import com.yxst.epic.yixin.view.ChatItemNullView_;
import com.yxst.epic.yixin.view.ChatItemTipsView;
import com.yxst.epic.yixin.view.ChatItemTipsView_;
import com.yxst.epic.yixin.view.ChatItemVoiceFromView;
import com.yxst.epic.yixin.view.ChatItemVoiceFromView_;
import com.yxst.epic.yixin.view.ChatItemVoiceToView;
import com.yxst.epic.yixin.view.ChatItemVoiceToView_;
import com.yxst.epic.yixin.view.ChatToItemView;
import com.yxst.epic.yixin.view.ChatToItemView_;

@EBean
public class ChatAdapter extends CursorAdapter {

	public static final String TAG = "ChatAdapter";
	
	private Context context;
	
	private DaoSession daoSession;
	private MessageDao messageDao;
	
	private BitmapUtils bitmapIconUtils;
	private BitmapUtils bitmapUtils;
	private VoiceUtils voiceUtils;
	
	public ChatAdapter(Context context) {
		super(context, null, true);
		this.context = context;
		
		daoSession = MyApplication.getDaoSession(context);
		messageDao = daoSession.getMessageDao();
		
		bitmapUtils = new XXBitmapUtils(context);
//		bitmapUtils.configDefaultLoadingImage(R.drawable.card_photofail);
		bitmapUtils.configDefaultLoadingImage(new ColorDrawable(Color.parseColor("#33000000")));
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.card_photofail);
		
		bitmapIconUtils = new XXBitmapUtils(context);
		bitmapIconUtils.configDefaultLoadingImage(R.drawable.ic_default_avata_mini);
		bitmapIconUtils.configDefaultLoadFailedImage(R.drawable.ic_default_avata_mini);
		
		voiceUtils = new VoiceUtils(context);
	}
	
	public void stopPlayVoice(boolean stop){
		if(voiceUtils != null)
			voiceUtils.stopPlay(stop);
	}
	
	public void onDestroy() {
		voiceUtils.destroy();
	}
	
	public void onPause() {
//		bitmapIconUtils.flushCache();
//		bitmapIconUtils.clearMemoryCache();
		bitmapIconUtils.pause();
	}
	
	public void onResume() {
		bitmapIconUtils.resume();
	}
	
	private OnOperationClickListener mOnOperationClickListener;
	
	public void setOnOperationClickListener(OnOperationClickListener l) {
		mOnOperationClickListener = l;
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		int inOut = cursor.getInt(MessageDao.Properties.ExtInOut.ordinal);
		int msgType = cursor.getInt(MessageDao.Properties.MsgType.ordinal);
		
		return createChatItemView(context, inOut, msgType);
	}

	public ChatItem createChatItemView(Context context, int inOut, int msgType) {
		
		if (DBMessage.INOUT_IN == inOut) {
			if (msgType == Msg.MSG_TYPE_NORMAL) {
				ChatFromItemView view = ChatFromItemView_.build(context);
				return view;
			} else if (msgType == Msg.MSG_TYPE_TIPS) {
				ChatItemTipsView view = ChatItemTipsView_.build(context);
				return view;
			} else if(msgType == Msg.MSG_TYPE_IMAGE) {
				ChatItemImgFromView view = ChatItemImgFromView_.build(context);
				view.setBitmapUtils(bitmapUtils);
				return view;
			} else if (msgType == Msg.MSG_TYPE_VOICE) {
				ChatItemVoiceFromView view = ChatItemVoiceFromView_.build(context);
				view.setVoiceUtils(voiceUtils);
				return view;
			} else if (msgType == Msg.MSG_TYPE_APP_101) {
				ChatItemApp101View view = ChatItemApp101View_.build(context);
				return view;
			} else if (msgType == Msg.MSG_TYPE_APP_102) {
				ChatItemApp102View view = ChatItemApp102View_.build(context);
				view.setOnOperationClickListener(mOnOperationClickListener);
				return view;
			} else if (msgType == Msg.MSG_TYPE_APP_103) {
				ChatItemApp103View view = ChatItemApp103View_.build(context);
				return view;
			} 
			
		} else if (DBMessage.INOUT_OUT == inOut) {
			if (msgType == Msg.MSG_TYPE_NORMAL) {
				ChatToItemView view = ChatToItemView_.build(context);
				return view;
			} else if(msgType == Msg.MSG_TYPE_IMAGE) {
				ChatItemImgToView view = ChatItemImgToView_.build(context);
				view.setBitmapUtils(bitmapUtils);
				return view;
			} else if (msgType == Msg.MSG_TYPE_VOICE) {
				ChatItemVoiceToView view = ChatItemVoiceToView_.build(context);
				view.setVoiceUtils(voiceUtils);
				return view;
			}
			
		}
		
		return ChatItemNullView_.build(context);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Message message = messageDao.readEntity(cursor, 0);
		
		ChatItem itemView = (ChatItem) view;
		itemView.setIconBitmapUtils(bitmapIconUtils);
		itemView.bind(message, getLastMessageExtTime(cursor));
//		itemView.bindLastMessage(getLastMessageExtTime(cursor));
	}

	private Long getLastMessageExtTime(Cursor cursor) {
//		if (cursor.move(-1)) {
//			return cursor.getLong(MessageDao.Properties.ExtTime.ordinal);
//		}
//		return null;
		
		if (cursor.move(-1)) {
			long mid = cursor.getLong(MessageDao.Properties.Mid.ordinal);
			long extTime = cursor.getLong(MessageDao.Properties.ExtTime.ordinal);
			return MessageUtils.getMessageTime(mid, extTime);
		}
		return null;
	}
	
	public static final int VIEW_TYPE_COUNT = 11;
	
	public static final int VIEW_TYPE_NULL = 0;
	public static final int VIEW_TYPE_OUT_NORMAL = 1;
	public static final int VIEW_TYPE_IN_NORMAL = 2;
	public static final int VIEW_TYPE_IN_TIPS = 3;
	public static final int VIEW_TYPE_IN_IMAGE = 4;
	public static final int VIEW_TYPE_OUT_IMAGE = 5;
	public static final int VIEW_TYPE_OUT_VOICE = 6;
	public static final int VIEW_TYPE_IN_VOICE = 7;
	public static final int VIEW_TYPE_IN_APP_101 = 8;
	public static final int VIEW_TYPE_IN_APP_102 = 9;
	public static final int VIEW_TYPE_IN_APP_103 = 10;
	
	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}
	
	@Override
	public int getItemViewType(int position) {
		Cursor cursor = getCursor();
		cursor.moveToPosition(position);
		int inOut = cursor.getInt(MessageDao.Properties.ExtInOut.ordinal);
		int msgType = cursor.getInt(MessageDao.Properties.MsgType.ordinal);
		
		return createChatItemViewType(context, inOut, msgType);
//		return IGNORE_ITEM_VIEW_TYPE;
	}

	public int createChatItemViewType(Context context, int inOut, int msgType) {
		
		if (DBMessage.INOUT_IN == inOut) {
			if (msgType == Msg.MSG_TYPE_NORMAL) {
				return VIEW_TYPE_IN_NORMAL;
			} else if (msgType == Msg.MSG_TYPE_TIPS) {
				return VIEW_TYPE_IN_TIPS;
			} else if(msgType == Msg.MSG_TYPE_IMAGE) {
				return VIEW_TYPE_IN_IMAGE;
			} else if(msgType == Msg.MSG_TYPE_VOICE) {
				return VIEW_TYPE_IN_VOICE;
			} else if (msgType == Msg.MSG_TYPE_APP_101) {
				return VIEW_TYPE_IN_APP_101;
			} else if (msgType == Msg.MSG_TYPE_APP_102) {
				return VIEW_TYPE_IN_APP_102;
			} else if (msgType == Msg.MSG_TYPE_APP_103) {
				return VIEW_TYPE_IN_APP_103;
			}
			
		} else if (DBMessage.INOUT_OUT == inOut) {
			if (msgType == Msg.MSG_TYPE_NORMAL) {
				return VIEW_TYPE_OUT_NORMAL;
			} else if(msgType == Msg.MSG_TYPE_IMAGE) {
				return VIEW_TYPE_OUT_IMAGE;
			} else if(msgType == Msg.MSG_TYPE_VOICE) {
				return VIEW_TYPE_OUT_VOICE;
			}
			
		}
		
		return VIEW_TYPE_NULL;
	}
	
	public Message readEntity(Cursor cursor) {
		return messageDao.readEntity(cursor, 0);
	}

	public BitmapUtils getBitmapUtils() {
		return bitmapUtils;
	}

	public void setBitmapUtils(BitmapUtils bitmapUtils) {
		this.bitmapUtils = bitmapUtils;
	}

}
