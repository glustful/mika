package com.miicaa.base.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ExcuteDataHelper {
	private SQLiteDatabase db = null;
	
	public Boolean openOrCreateDatabase(String dbPath) {
		try {
			db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
			return true;
		} catch (SQLException e) {
			Log.d("DatabaseHelper", "DatabaseHelper openOrCreateDatabase failed: " + e.toString());
		}
		return false;
	}
	
	public Boolean execSQL(String sql) {
		if (isOpen() && checkSql(sql)) {
			try {
				db.execSQL(sql);
				return true;
			} catch (SQLException e) {
				Log.d("DatabaseHelper", "DatabaseHelper execSQL failed: " + e.toString());
			}
		}
		return false;
	}
	
	private Boolean checkArgs(Object[] bindArgs) {
		return (bindArgs != null) && (bindArgs.length > 0);
	}
	
	public Boolean execSQL(String sql, Object... bindArgs) {
		if (isOpen() && checkSql(sql) && checkArgs(bindArgs)) {
			try {
				db.execSQL(sql, bindArgs);
				return true;
			} catch (SQLException e) {
				Log.d("DatabaseHelper", "DatabaseHelper execSQL failed: " + e.toString());
			}
		}
		return false;
	}
	

	// ע�⣺
		//    ����Sqlite������ݿ����������һ�����񣬵��·������е���ݿ�������ܷǳ���
		//    �뽫������ݿ��������һ�������м��д���
		public void onTransaction(OnTransaction onTransaction) {
			if (isOpen() && onTransaction != null) {
				// �ֶ���ʼ����
				db.beginTransaction();
				try {
					onTransaction.transaction();

					// ����������ɹ��������û��Զ��ع����ύ
					db.setTransactionSuccessful();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// �ύ����
					db.endTransaction();
				}
			}
		}

	
	//�ر���ݿ�
	public void close() {
		if (isOpen()) {
			db.close();
		}
	}

	public ArrayList<ContentValues> query(OnEachRow onEachRow, Object cbData, String sql, String... bindArgs) {
		ArrayList<ContentValues> rs = new ArrayList<ContentValues>();
		if (isOpen() && checkSql(sql)) {
			try {
				Cursor c = db.rawQuery(sql, bindArgs);
				c.moveToFirst();
				while (!c.isAfterLast()) {
					ContentValues row = new ContentValues();
					DatabaseUtils.cursorRowToContentValues(c, row);
					rs.add(row);

					// �ص��ⲿ����ÿһ�����
					if (onEachRow != null) {
						onEachRow.eachRow(row, cbData);
					}

					c.moveToNext();
				}
				c.close();
			} catch (SQLException e) {
				Log.d("DatabaseHelper", "DatabaseHelper rawQuery failed: " + e.toString());
			}
		}
		return rs;
	}
	
	private Boolean isOpen() {
		return (db != null) && (db.isOpen());
	}
	private Boolean checkSql(String sql) {
		return (sql != null) && (sql.length() > 0);
	}
}
