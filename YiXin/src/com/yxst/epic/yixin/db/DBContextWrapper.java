package com.yxst.epic.yixin.db;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBContextWrapper extends ContextWrapper {

	private String dbPath;
	
	public DBContextWrapper(Context base, String dbPath) {
		super(base);
		this.dbPath = dbPath;
	}
    
	@Override
	public File getDatabasePath(String name) {
		// TODO Auto-generated method stub
		return super.getDatabasePath(name);
	}
	
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		// TODO Auto-generated method stub
		return super.openOrCreateDatabase(name, mode, factory);
	}
	
	@SuppressLint("NewApi")
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		return super.openOrCreateDatabase(name, mode, factory, errorHandler);
	}
}
