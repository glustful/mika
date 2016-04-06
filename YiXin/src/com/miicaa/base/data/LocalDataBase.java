package com.miicaa.base.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.util.Log;

public class LocalDataBase {

	/**
	 * ������ݿ����
	 * ���ڳ��󱾵���ݿ��ͨ�ò���
	 * <p/>
	 * Created by yayowd on 13-12-17.
	 */
	public class LocalDatabase {
		private ExcuteDataHelper dbHelper = null;

		protected LocalDatabase() {
			dbHelper = new ExcuteDataHelper();
		}

		protected Boolean init() {
			return true;
		}

		public Boolean open(String path) {
			return dbHelper.openOrCreateDatabase(path);
		}

		public void close() {
			dbHelper.close();
		}

		public void onTransaction(OnTransaction onTransaction) {
			dbHelper.onTransaction(onTransaction);
		}

		public Boolean execSQL(String sql) {
			Log.d("SQL_COMMAND", sql);
			return dbHelper.execSQL(sql);
		}

		public Boolean execSQL(String sql, Object... bindArgs) {
			Log.d("SQL_COMMAND", sql);
			return dbHelper.execSQL(sql, bindArgs);
		}

		public ArrayList<ContentValues> query(OnEachRow onEachRow, Object cbData, String sql, String... bindArgs) {
	        Log.d("SQL_COMMAND", sql);
	        return dbHelper.query(onEachRow, cbData, sql, bindArgs);
	    }

		public Boolean execCmd(SqlCmd cmd) {
			Log.d("performance", "execCmd: 1: %d %s");
			Boolean res = false;
			if (cmd.hasParams()) {
				res = execSQL(cmd.getSql(), cmd.getObjectParams());
			} else {
				res = execSQL(cmd.getSql());
			}
			Log.d("performance", "execCmd: 2: %d %s");
			return res;
		}

		public void execCmds(ArrayList<SqlCmd> cmds) {
			for (SqlCmd cmd : cmds) {
				execCmd(cmd);
			}
		}

		public ArrayList<ContentValues> queryCmd(SqlCmd cmd) {
			return queryCmd(cmd, null, null);//û�лص�
		}

		public ArrayList<ContentValues> queryCmd(SqlCmd cmd, OnEachRow onEachRow, Object cbData) {
			Log.d("performance", "querCmd: 1: %d %s");
			ArrayList<ContentValues> res = query(onEachRow, cbData, cmd.getSql(), cmd.getStringParams());
			Log.d("performance", "querCmd: 2: %d %s");
			return res;
		}

		public ContentValues firstRow(SqlCmd cmd) {
			ArrayList<ContentValues> rows = queryCmd(cmd);
			if (rows.size() > 0) {
				return rows.get(0);
			}
			return null;
		}

		public Boolean hasRow(String tbName, String where, Object... params) {
			return hasRow(new SqlCmd(tbName).hasRow(where).ps(params));
		}

		public Boolean hasRow(SqlCmd cmd) {
			ContentValues row = firstRow(cmd);
			if (row != null) {
				return row.getAsInteger("count") > 0;
			}
			return false;
		}

		public Boolean isTableExsit(String tbName) {
			return hasRow(SqlCmd.isTableExist(tbName));
		}
	}
}
