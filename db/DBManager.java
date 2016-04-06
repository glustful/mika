package com.yxst.epic.yixin.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.push.cli.PushMessage;

import de.greenrobot.dao.query.QueryBuilder;

public class DBManager {

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
		this.daoSession = MyApplication.getDaoSession(context);
		this.messageDao = this.daoSession.getMessageDao();
		this.memberDao = this.daoSession.getMemberDao();
	}

	public Message onOnlineMessage(PushMessage pushMessage) {
		Log.d(TAG, "onOnlineMessage()");

		Message message = DBMessage.retriveMessageFromPushMessage(pushMessage,
				DBMessage.INOUT_IN);

		long id = insertMessage(message);

		if (id != 0) {
			return message;
		}

		return null;
	}

	public ArrayList<Message> onOfflineMessage(ArrayList<PushMessage> messages) {
		Log.d(TAG, "onOfflineMessage()");

		ArrayList<Message> ids = new ArrayList<Message>();

		for (Iterator<PushMessage> it = messages.iterator(); it.hasNext();) {
			PushMessage pushMessage = it.next();

			Message message = DBMessage.retriveMessageFromPushMessage(
					pushMessage, DBMessage.INOUT_IN);

			long id = insertMessageOffline(message);

			if (id != 0) {
				ids.add(message);
			}
		}

		if (ids.size() != 0) {
			context.getContentResolver().notifyChange(
					MessageContentProvider.CONTENT_URI, null);
		}

		Log.d(TAG, "onOfflineMessage() ids:" + ids);

		return ids;
	}

	private long insertMessageOffline(Message message) {

		QueryBuilder<Message> qb = messageDao.queryBuilder();
		qb.where(MessageDao.Properties.ExtInOut.eq(DBMessage.INOUT_IN),
				MessageDao.Properties.Mid.ge(message.getMid()));

		if (qb.count() == 0) {
			long id = messageDao.insert(message);
			return id;
		}

		return 0;
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
		Message message = qb.unique();
		if (message != null) {
			return message.getExtRemoteDisplayName();
		}
		return null;
	}

	public long getLastMid(String localUserName) {
		Log.d(TAG, "getLastMid()");
		Log.d(TAG, "getLastMid() localUserName:" + localUserName);
		QueryBuilder<Message> qb = messageDao.queryBuilder();
		qb.where(MessageDao.Properties.ExtLocalUserName.eq(localUserName));

		Log.d(TAG, "getLastMid() qb.count():" + qb.count());

		qb.orderCustom(MessageDao.Properties.Mid, "DESC");
		qb.limit(1);
		Message message = qb.unique();
		Log.d(TAG, "getLastMid() message:" + message);
		if (message != null) {
			return message.getMid() == null ? 0 : message.getMid();
		}

		return 0;
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
}
