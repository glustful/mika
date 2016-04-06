package com.yxst.epic.yixin.adapter;

import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.miicaa.home.R;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.db.DaoSession;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.db.MessageDao;
import com.yxst.epic.yixin.view.ChatListItem;
import com.yxst.epic.yixin.view.ChatListItem_;

@EBean
public class ChatListAdapter extends CursorAdapter {

	private static String TAG = "ChatListAdapter";
	
	OnNotZeroListener listener;
	private DaoSession daoSession;
	private MessageDao messageDao;

	public ChatListAdapter(Context context) {
		super(context, null, true);
		daoSession = MyApplication.getDaoSession(context);
		messageDao = daoSession.getMessageDao();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ChatListItem itemView = ChatListItem_.build(context);
		itemView.setBackgroundResource(R.drawable.white_color_selector);
		return itemView;
	}
	
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int unreadCount = cursor.getInt(1);
		Message message = messageDao.readEntity(cursor, DBManager.GET_CHATLIST_COLUMNS_OFFSET);
		ChatListItem itemView = (ChatListItem) view;
		itemView.bind(message, unreadCount);
	}
	
	
	


	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
//		Log.d(TAG, "notifyDataSetChanged() count:"+getCount());
//		if(listener != null){
//			listener.notZero(getCount() == 0 ? true : false);
//		}
	}

	public Message readEntity(Cursor cursor) {
		return messageDao.readEntity(cursor,
				DBManager.GET_CHATLIST_COLUMNS_OFFSET);
	}
	
	
	public interface OnNotZeroListener{
		void notZero(Boolean isZero);
	}
	
	public void setOnNotZeroListener(OnNotZeroListener listener){
		this.listener = listener;
	}
}
