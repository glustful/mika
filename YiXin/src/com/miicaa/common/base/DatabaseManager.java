package com.miicaa.common.base;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager {
	private String TAG = this.toString();
	private SQLiteDatabase db = null;
	private Context context = null;

	public class DatabaseOpenHelper extends SQLiteOpenHelper {
		DatabaseOpenHelper(Context context, String dbName) {
			super(context, dbName, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO 创建数据库后，对数据库的操作
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO 更改数据库版本的操作
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			// TODO 每次成功打开数据库后首先被执行

			Log.d(this.toString(), "创建或打开数据库成功");
		}
	}

	public DatabaseManager(Context context) {
		this.context = context;
	}

	public boolean openOrCreateDB(String dbPath, String dbName) {
		boolean rc = false;

		if (/*null == dbPath || dbPath.length() <= 0 || */null == dbName || dbName.length() <= 0)
			return rc;

		if (null != context) {
			db = (new DatabaseOpenHelper(context, dbName)).getWritableDatabase();
			if (null != db) {
				rc = true;
			}
		}
		return rc;
	}

	public boolean dropDB(String dbPath, String dbName) {
		boolean rc = false;
		if (/*null == dbPath || dbPath.length() <= 0 || */null == dbName || dbName.length() <= 0)
			return rc;

		if (null != context) {
			rc = context.deleteDatabase(dbName);
		}
		return rc;
	}

	public boolean createTable(String sqlString) {
		boolean rc = false;

		if (null == db || null == sqlString || sqlString.length() <= 0)
			return rc;

		try {
			db.execSQL(sqlString);
			rc = true;
		} catch (SQLException e) {
			Log.d(TAG, "创建table的sql语句有错." + e.getMessage());
		}
		return rc;
	}

	public boolean dropTable(String sqlString) {
		boolean rc = false;

		if (null == db || null == sqlString || sqlString.length() <= 0)
			return rc;
		try {
			db.execSQL(sqlString);
			rc = true;
		} catch (SQLException e) {
			Log.d(TAG, "销毁table的sql语句有错.");
		}

		return rc;
	}

	//插入一条数据,使用不定参数的方式
	public boolean insert(String sqlString, Object... parameters) {
		boolean rc = false;

		if (null == db || null == sqlString || sqlString.length() <= 0 || null == parameters || parameters.length <= 0)
			return rc;

		String localSqlString = sqlString;

		int cnt = 0;
		int pos = 0;
		while (pos >= 0) {
			pos = localSqlString.indexOf("?", pos + 1);
			if (pos > 0)
				cnt++;
		}

		if (cnt == parameters.length) {
			String strValue = null;
			cnt = 0;
			pos = 0;
			while (pos >= 0) {
				pos = localSqlString.indexOf("?", pos);
				if (pos > 0) {
					Object object = parameters[cnt];
					if (object instanceof String) {
						strValue = "'" + (String) object + "'";
					} else {
						strValue = String.valueOf(object);
					}

					localSqlString = localSqlString.replaceFirst("\\?", strValue);

					pos++;
					cnt++;
				}
			}

			try {
				db.execSQL(localSqlString);
				rc = true;
			} catch (SQLException e) {
				Log.d(TAG, "插入数据出现异常.sql:" + localSqlString);
			}
		} else {
			Log.d(TAG, "参数个数不正确");
		}

		return rc;
	}

	public boolean update(String sqlString, Object... parameters) {
		boolean rc = false;

		if (null == db || null == sqlString || sqlString.length() <= 0 || null == parameters || parameters.length <= 0)
			return rc;

		try {
			db.execSQL(sqlString, parameters);
			rc = true;
		} catch (SQLException e) {
			Log.d(TAG, "更新数据出现异常.");
		}

		return rc;
	}

	public ArrayList<ContentValues> query(String sqlString, String... parameters) {
		ArrayList<ContentValues> result = new ArrayList<ContentValues>();

		if (null == db || null == sqlString || sqlString.length() <= 0 || null == parameters || parameters.length <= 0)
			return result;

		try {
			Cursor c = db.rawQuery(sqlString, parameters); // 第二个参数只能为String,可能会有问题
			c.moveToFirst();
			while (!c.isAfterLast()) {
				ContentValues rowValues = new ContentValues();
				DatabaseUtils.cursorRowToContentValues(c, rowValues);

				result.add(rowValues);
				c.moveToNext();
			}
			c.close();
		} catch (SQLException e) {
			Log.d(TAG, "查询数据出现异常.");
		}

		return result;
	}

	public boolean delete(String sqlString, Object... parameters) {
		boolean rc = false;

		if (null == db || null == sqlString || sqlString.length() <= 0 || null == parameters || parameters.length <= 0)
			try {
				db.execSQL(sqlString, parameters);
				rc = true;
			} catch (SQLException e) {
				Log.d(TAG, "删除数据出现异常.");
			}

		return rc;
	}
}
