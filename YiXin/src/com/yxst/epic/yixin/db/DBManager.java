package com.yxst.epic.yixin.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.afinal.simplecache.ACache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.push.cli.PushMessage;
import com.yxst.epic.yixin.utils.RefreshUtils;
import com.yxst.epic.yixin.utils.Utils;

import de.greenrobot.dao.query.QueryBuilder;

public class DBManager {
	RefreshUtils mRefreshUtils;

	private static final String TAG = "DBManager";

	private static DBManager sInstance;

	private Context context;
	private DaoSession daoSession;
	private MessageDao messageDao;
	private MemberDao memberDao;

	public static synchronized DBManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DBManager(context);
		}
		return sInstance;
	}

	DBManager(Context context) {
		this.context = context;
		mRefreshUtils = new RefreshUtils();
		this.daoSession = MyApplication.getDaoSession(context);
		this.messageDao = this.daoSession.getMessageDao();
		this.memberDao = this.daoSession.getMemberDao();
	}

	public Message onOnlineMessage(PushMessage pushMessage) {
		Log.d(TAG, "onOnlineMessage()");

		Log.d(TAG, "onOfflineMessage() isMessageIn(pushMessage):" + isMessageIn(pushMessage));
		Message message = DBMessage.retriveMessageFromPushMessage(pushMessage,
				isMessageIn(pushMessage) ? DBMessage.INOUT_IN : DBMessage.INOUT_OUT);

		if (isMsgDeleteChat(message)) {
			DBManager.getInstance(context).deleteChat(message.getToUserName(), message.getFromUserName());
			return message;
		}
		long id = insertMessageDelyNotifycation(message);
		String localUserName = MyApplication.getInstance().getLocalUserName();
		updateMid(localUserName, pushMessage);
		if (id != 0) {
			return message;
		}
		
		return null;
	}

	public ArrayList<Message> onOfflineMessage(ArrayList<PushMessage> messages) {
		Log.d(TAG, "onOfflineMessage()");
		
		Log.v("jpg", "start!");
		long startTime = System.currentTimeMillis();

		String localUserName = MyApplication.getInstance().getLocalUserName();
		ArrayList<Message> ids = new ArrayList<Message>();

		for (Iterator<PushMessage> it = messages.iterator(); it.hasNext();) {
			PushMessage pushMessage = it.next();
			updateMid(localUserName, pushMessage);

//			Message message = DBMessage.retriveMessageFromPushMessage(
//					pushMessage, DBMessage.INOUT_IN);
			Log.d(TAG, "onOfflineMessage() isMessageIn(pushMessage):" + isMessageIn(pushMessage));
			Message message = DBMessage.retriveMessageFromPushMessage(
					pushMessage, isMessageIn(pushMessage) ? DBMessage.INOUT_IN : DBMessage.INOUT_OUT);

			if (!isDuplicateOfflineMsg(pushMessage)) {
				long id = insertMessageOffline(message);
				
				if (id != 0) {
					ids.add(message);
				}
			}
			if (isMsgDeleteChat(message)) {
				DBManager.getInstance(context).deleteChat(message.getToUserName(), message.getFromUserName());
			}
			
			mRefreshUtils.INeedNotify(context, MessageContentProvider.CONTENT_URI);
		}

		Log.v("jpg", "over! it cust = "
				+((System.currentTimeMillis() - startTime))/1000);
		
		if (ids.size() != 0) {
			context.getContentResolver().notifyChange(
					MessageContentProvider.CONTENT_URI, null);
		}

		Log.d(TAG, "onOfflineMessage() ids:" + ids);

		return ids;
	}

	private boolean isDuplicateOfflineMsg(PushMessage pushMessage) {
		Msg msg = Msg.readValue(pushMessage.getMsg());
		if (msg != null) {
			String localUserName = MyApplication.getInstance().getLocalUserName();
			String deviceID = Utils.getAndroidDeviceId(context);
			return msg.FromUserName.equals(localUserName) && msg.deviceID.equals(deviceID);
		}
		return false;
	}
	
	private boolean isMessageIn(PushMessage pushMessage) {
		Msg msg = Msg.readValue(pushMessage.getMsg());
		if (msg != null) {
			String localUserName = MyApplication.getInstance().getLocalUserName();
			if (msg.FromUserName != null) {
				return !msg.FromUserName.equals(localUserName);
			}
		}
		return true;
	}
	
	private long insertMessageOffline(Message message) {

		QueryBuilder<Message> qb = messageDao.queryBuilder();
		qb.where(MessageDao.Properties.ExtInOut.eq(DBMessage.INOUT_IN),
				MessageDao.Properties.Mid.ge(message.getMid()));
		
		if( qb.count() == 0 ){
			messageDao.insertInTx(message);
			return 1;
		}

//		long cachedMid = getLastMid(MyApplication.getInstance().getLocalUserName());
//		boolean isMessageMidGtCacheMid = message.getMid() > cachedMid;
//		
//		if (isMessageMidGtCacheMid) {
//			messageDao.insertInTx(message);
//			return 1;
//		}

		return 0;
	}

	public long insertMessageDelyNotifycation(Message message) {
		long id = messageDao.insert(message);

		mRefreshUtils.INeedNotify(context, MessageContentProvider.CONTENT_URI);
		
//		context.getContentResolver().notifyChange(
//				MessageContentProvider.CONTENT_URI, null);

		return id;
	}
	
	public long insertMessage(Message message) {
		long id = messageDao.insert(message);

		context.getContentResolver().notifyChange(
				MessageContentProvider.CONTENT_URI, null);

		return id;
	}

	public static final int GET_CHATLIST_COLUMNS_OFFSET = 2;

	public Cursor getChatList(String localUserName) {
		Log.d(TAG, "getChatList()");

		String[] columns = new String[messageDao.getAllColumns().length
				+ GET_CHATLIST_COLUMNS_OFFSET];
		columns[0] = "count("
				+ MessageDao.Properties.ExtRemoteUserName.columnName + ")";
		columns[1] = "sum(" + MessageDao.Properties.ExtRead.columnName + "==0)";
		System.arraycopy(messageDao.getAllColumns(), 0, columns,
				GET_CHATLIST_COLUMNS_OFFSET, messageDao.getAllColumns().length);

		String textColumn = MessageDao.Properties.ExtTime.columnName;
		String groupBy = MessageDao.Properties.ExtRemoteUserName.columnName;
		String orderBy = textColumn + " DESC";

		String selection = MessageDao.Properties.ExtLocalUserName.columnName
				+ "=?";
		selection += " AND " + MessageDao.Properties.MsgType.columnName + "!="
				+ Msg.MSG_TYPE_UPDATE;
		// selection += " LEFT JOIN " +
		// MessageDao.Properties.ExtRemoteUserName.columnName + "=member." +
		// MemberDao.Properties.UserName.columnName;
		String[] selectionArgs = { localUserName };

		Cursor cursor = messageDao.getDatabase().query(
				messageDao.getTablename(), columns, selection, selectionArgs,
				groupBy, null, orderBy);

		cursor.setNotificationUri(context.getContentResolver(),
				MessageContentProvider.CONTENT_URI);

		// getChatList2(localUserName);

		return cursor;
	}

	public Cursor getChatList2(String localUserName) {
		String[] selectionArgs = { localUserName };

		String sql = "SELECT count("
				+ MessageDao.Properties.ExtRemoteUserName.columnName + "),"
				+ "sum(" + MessageDao.Properties.ExtRead.columnName + "==0),*";

		sql += "FROM " + messageDao.getTablename() + " AS message LEFT JOIN "
				+ memberDao.getTablename() + " AS member";

		sql += " ON message."
				+ MessageDao.Properties.ExtRemoteUserName.columnName
				+ "=member." + MemberDao.Properties.UserName.columnName
				+ " AND message."
				+ MessageDao.Properties.ExtLocalUserName.columnName + "=?";

		sql += " GROUP BY "
				+ MessageDao.Properties.ExtRemoteUserName.columnName;
		sql += " ORDER BY " + MessageDao.Properties.ExtTime.columnName
				+ " DESC";

		Cursor cursor = messageDao.getDatabase().rawQuery(sql, selectionArgs);

		cursor.setNotificationUri(context.getContentResolver(),
				MessageContentProvider.CONTENT_URI);

		Log.d(TAG,
				"DatabaseUtils.dumpCursorToString(cursor)"
						+ DatabaseUtils.dumpCursorToString(cursor));

		return cursor;
	}

	public Cursor getChatMessages(String localUserName, String remoteUserName) {
		String textColumn = MessageDao.Properties.ExtTime.columnName;
		String orderBy = textColumn + " ASC";

		String selection = MessageDao.Properties.ExtLocalUserName.columnName
				+ "=? AND "
				+ MessageDao.Properties.ExtRemoteUserName.columnName + "=?";
		String[] selectionArgs = { localUserName, remoteUserName };

		Cursor cursor = messageDao.getDatabase().query(
				messageDao.getTablename(), messageDao.getAllColumns(),
				selection, selectionArgs, null, null, orderBy);

		cursor.setNotificationUri(context.getContentResolver(),
				MessageContentProvider.CONTENT_URI);

		return cursor;
	}
	
	public Cursor getChatMessagesInThePictures(String localUserName, String remoteUserName) {
		String textColumn = MessageDao.Properties.ExtTime.columnName;
		String orderBy = textColumn + " ASC";

		String selection = MessageDao.Properties.ExtLocalUserName.columnName
				+ "=? AND "
				+ MessageDao.Properties.ExtRemoteUserName.columnName + "=?" 
				+ " AND " + MessageDao.Properties.MsgType.columnName + "=" + Msg.MSG_TYPE_IMAGE;
		String[] selectionArgs = { localUserName, remoteUserName };
		
		Cursor cursor = messageDao.getDatabase().query(
				messageDao.getTablename(), messageDao.getAllColumns(),
				selection, selectionArgs, null, null, orderBy);

		return cursor;
	}
	
	private void updateMid(String localUserName,PushMessage pushMessage){
		String key = "Mid_" + localUserName;
		long lastMid = getLastMid(localUserName);
		if(lastMid < pushMessage.getMid()){
			ACache.get(context).put(key, String.valueOf(pushMessage.getMid()));
		}
	}

	public int updateRead(String localUserName, String remoteUserName) {

		ContentValues values = new ContentValues();
		values.put(MessageDao.Properties.ExtRead.columnName,
				DBMessage.READ_TRUE);

		String whereClause = MessageDao.Properties.ExtLocalUserName.columnName
				+ "=? AND "
				+ MessageDao.Properties.ExtRemoteUserName.columnName
				+ "=? AND " + MessageDao.Properties.ExtInOut.columnName
				+ "=? AND " + MessageDao.Properties.ExtRead.columnName + "=?";
		String[] whereArgs = new String[] { localUserName, remoteUserName,
				String.valueOf(DBMessage.INOUT_IN),
				String.valueOf(DBMessage.READ_FALSE) };

		int num = messageDao.getDatabase().update(messageDao.getTablename(),
				values, whereClause, whereArgs);

		context.getContentResolver().notifyChange(
				MessageContentProvider.CONTENT_URI, null);

		return num;
	}

	public Message load(long id) {
		return messageDao.load(id);
	}

	public Message loadByClientMsgID(String clientMsgID) {
		QueryBuilder<Message> qb = messageDao.queryBuilder();
		qb.where(MessageDao.Properties.ClientMsgId.eq(clientMsgID));
		return qb.unique();
	}

	public Message loadByMid(Long mid) {
		QueryBuilder<Message> qb = messageDao.queryBuilder();
		qb.where(MessageDao.Properties.Mid.eq(mid));
		qb.limit(1);

		return qb.unique();
	}

	public long getChatListUnreadCount(String localUserName) {
		QueryBuilder<Message> qb = messageDao.queryBuilder();
		qb.where(MessageDao.Properties.ExtInOut.eq(DBMessage.INOUT_IN),
				MessageDao.Properties.ExtLocalUserName.eq(localUserName),
				MessageDao.Properties.ExtRead.eq(DBMessage.READ_FALSE),
				MessageDao.Properties.MsgType.notEq(Msg.MSG_TYPE_UPDATE));
		return qb.count();
	}

	public void update(Message message) {
		messageDao.update(message);

		context.getContentResolver().notifyChange(
				MessageContentProvider.CONTENT_URI, null);
	}
	
	public void update(Message message, boolean notifyChange) {
		messageDao.update(message);
		if (notifyChange) {
			context.getContentResolver().notifyChange(
					MessageContentProvider.CONTENT_URI, null);
		}
	}

	public int deleteChat(String localUserName, String remoteUserName) {

		ContentValues values = new ContentValues();
		values.put(MessageDao.Properties.ExtRead.columnName,
				DBMessage.READ_TRUE);

		String whereClause = MessageDao.Properties.ExtLocalUserName.columnName
				+ "=? AND "
				+ MessageDao.Properties.ExtRemoteUserName.columnName + "=?";
		String[] whereArgs = new String[] { localUserName, remoteUserName };

		int num = messageDao.getDatabase().delete(messageDao.getTablename(),
				whereClause, whereArgs);

		context.getContentResolver().notifyChange(
				MessageContentProvider.CONTENT_URI, null);

		return num;
	}

	public void deleteMessage(Message message) {

		messageDao.delete(message);
		
		context.getContentResolver().notifyChange(
				MessageContentProvider.CONTENT_URI, null);
	}

	public String getChatDisplayName(String localUserName, String remoteUserName) {
		QueryBuilder<Message> qb = messageDao.queryBuilder();
		qb.where(MessageDao.Properties.ExtLocalUserName.eq(localUserName),
				MessageDao.Properties.ExtRemoteUserName.eq(remoteUserName));
		qb.orderCustom(MessageDao.Properties.ExtTime, "DESC");
		qb.limit(1);
		Log.d(TAG, "message =======:"+qb);
		Message message = qb.unique();
		if (message != null) {
			return message.getExtRemoteDisplayName();
		}
		return null;
	}

	public long getLastMid(String localUserName) {
//		Log.d(TAG, "getLastMid()");
//		Log.d(TAG, "getLastMid() localUserName:" + localUserName);
//		QueryBuilder<Message> qb = messageDao.queryBuilder();
//		qb.where(MessageDao.Properties.ExtLocalUserName.eq(localUserName));
//
//		Log.d(TAG, "getLastMid() qb.count():" + qb.count());
//
//		qb.orderCustom(MessageDao.Properties.Mid, "DESC");
//		qb.limit(1);
//		Message message = qb.unique();
//		Log.d(TAG, "getLastMid() message:" + message);
//		if (message != null) {
//			return message.getMid() == null ? 0 : message.getMid();
//		}
//
//		return 0;
		String key = "Mid_"+ localUserName;
		String midStr = ACache.get(context).getAsString(key);
		try{
		return Long.parseLong(midStr);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public Message getMessageByMid(/*String localUserName, */long mid) {
		Log.d(TAG, "getLastMid()");
//		Log.d(TAG, "getLastMid() localUserName:" + localUserName);
		Log.d(TAG, "getLastMid() mid:" + mid);
		QueryBuilder<Message> qb = messageDao.queryBuilder();
//		qb.where(MessageDao.Properties.ExtLocalUserName.eq(localUserName)
//				, MessageDao.Properties.Mid.eq(mid));
		qb.where(MessageDao.Properties.Mid.eq(mid));
		qb.limit(1);
		return qb.unique();
	}

	public List<Message> getMessagesPendingAndSending(String localUserName) {
		QueryBuilder<Message> qb = messageDao.queryBuilder();
		qb.where(MessageDao.Properties.ExtLocalUserName.eq(localUserName), qb
				.or(MessageDao.Properties.ExtStatus
						.eq(DBMessage.STATUS_PENDING),
						MessageDao.Properties.ExtStatus
								.eq(DBMessage.STATUS_SENDING)));
		qb.orderCustom(MessageDao.Properties.ExtTime, "DESC");
		return qb.list();
	}
	
	public Cursor getAllTypeChatList(String localUserName) {
		Log.d(TAG, "getAllChatList()");

		String[] columns = new String[messageDao.getAllColumns().length
				+ GET_CHATLIST_COLUMNS_OFFSET];
		columns[0] = "count("
				+ MessageDao.Properties.ExtRemoteUserName.columnName + ")";
		columns[1] = "sum(" + MessageDao.Properties.ExtRead.columnName + "==0)";
		System.arraycopy(messageDao.getAllColumns(), 0, columns,
				GET_CHATLIST_COLUMNS_OFFSET, messageDao.getAllColumns().length);

		String textColumn = MessageDao.Properties.ExtTime.columnName;
		String groupBy = MessageDao.Properties.ExtRemoteUserName.columnName;
		String orderBy = textColumn + " DESC";

		String selection = MessageDao.Properties.ExtLocalUserName.columnName
				+ " like ?";
		selection += " AND " + MessageDao.Properties.MsgType.columnName + "!="
				+ Msg.MSG_TYPE_UPDATE;
//		selection += " AND " + MessageDao.Properties.MsgType.columnName + "!="
//				+ Msg.MSG_TYPE_TIPS;
//		selection += " AND " + MessageDao.Properties.ExtRemoteUserName.columnName + " NOT LIKE '%@app'";
		// selection += " LEFT JOIN " +
		// MessageDao.Properties.ExtRemoteUserName.columnName + "=member." +
		// MemberDao.Properties.UserName.columnName;
		String[] selectionArgs = { localUserName };

		Cursor cursor = messageDao.getDatabase().query(
				messageDao.getTablename(), columns, selection, selectionArgs,
				groupBy, null, orderBy);

		cursor.setNotificationUri(context.getContentResolver(),
				MessageContentProvider.CONTENT_URI);

		// getChatList2(localUserName);

		return cursor;
	}
	
	public List<Message> getMessagesVoiceNotDownloadSuccess(String localUserName) {
		QueryBuilder<Message> qb = messageDao.queryBuilder();
		qb.where(MessageDao.Properties.ExtLocalUserName.like(localUserName)
				, MessageDao.Properties.MsgType.eq(Msg.MSG_TYPE_VOICE)
				, MessageDao.Properties.ExtInOut.eq(DBMessage.INOUT_IN)
				, MessageDao.Properties.ExtStatus.notEq(DBMessage.STATUS_MEDIA_DOWNLOAD_SUCCESS));
		qb.orderCustom(MessageDao.Properties.ExtTime, "DESC");
		return qb.list();
	}
	
	private boolean isMsgDeleteChat(Message message) {
		if (message.getMsgType() == Msg.MSG_TYPE_DELETE_CHAT) {
			return true;
		}		return false;
	}

}
