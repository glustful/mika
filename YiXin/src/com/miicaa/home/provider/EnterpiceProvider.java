package com.miicaa.home.provider;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.annotation.Table;
import com.miicaa.common.base.DatabaseHelper;
import com.miicaa.home.data.business.org.EntirpiseInfo;
import com.miicaa.home.data.storage.LocalPath;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class EnterpiceProvider extends ContentProvider{

	private static final String AUTHORITY = "com.miicaa.home.enterp";
	private static final String BASE_PATH = "Enterpice";
	private static  String TABLE_NAME;
	private static  SQLiteDatabase DB;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + BASE_PATH;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + BASE_PATH;
	
	private static final int ENTERPICE_DIR = 0;
	private static final int ENTERPICE_ID = 1;
	
	private static final UriMatcher sURIMatcher;
	static{
		sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, ENTERPICE_DIR);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ENTERPICE_ID);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		int rowsDeleted = 0;
		String id;
		switch (uriType) {
		case ENTERPICE_DIR:
			rowsDeleted = DB.delete(TABLE_NAME, selection, selectionArgs);
			break;
		case ENTERPICE_ID:
			id = uri.getLastPathSegment();
//			if (TextUtils.isEmpty(selection)) {
//				rowsDeleted = DB.delete(TABLE_NAME, PK + "=" + id, null);
//			} else {
//				rowsDeleted = db.delete(TABLENAME, PK + "=" + id + " and "
//						+ selection, selectionArgs);
//			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		switch (sURIMatcher.match(uri)) {
		case ENTERPICE_DIR:
			return CONTENT_TYPE;
		case ENTERPICE_ID:
			return CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		long id = 0;
		String path = "";
		switch (uriType) {
		case ENTERPICE_DIR:
			id = DB.insert(TABLE_NAME, null, values);
			path = BASE_PATH + "/" + id;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(path);
	}

	@Override
	public boolean onCreate() {
		TABLE_NAME = EntirpiseInfo.class.getAnnotation(Table.class).name();
		DB =  SQLiteDatabase.openOrCreateDatabase(LocalPath.intance().configDatabaseFilePath, null);
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ENTERPICE_DIR:
			queryBuilder.setTables(TABLE_NAME);
			break;
		case ENTERPICE_ID:
//			queryBuilder.setTables(TABLE_NAME);
//			queryBuilder.appendWhere(PK + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		Cursor cursor = queryBuilder.query(DB, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		int rowsUpdated = 0;
		String id;
		switch (uriType) {
		case ENTERPICE_DIR:
			rowsUpdated = DB
					.update(TABLE_NAME, values, selection, selectionArgs);
			break;
		case ENTERPICE_ID:
			id = uri.getLastPathSegment();
//			if (TextUtils.isEmpty(selection)) {
//				rowsUpdated = db.update(TABLENAME, values, PK + "=" + id, null);
//			} else {
//				rowsUpdated = db.update(TABLENAME, values, PK + "=" + id
//						+ " and " + selection, selectionArgs);
//			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
