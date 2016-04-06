package com.miicaa.home.data.business.org;

import java.util.List;

import android.content.Context;

import com.amap.api.mapcore2d.db;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.miicaa.home.data.storage.LocalPath;
import com.miicaa.utils.AllUtils;

public class EntirpiseInfo {
	public static EntirpiseInfo instance_;
	
	@Id
	String id;
	public int locationType;
	public int gonggaoCount;
	public String userCode;
	public String userEmail;
//	public String versionCode;
//	
//	public EntirpiseInfo(){
//		versionCode = "2";
//	}
	
//	public void init(Context context){
//		dbUtils = DbUtils.create(context,
//				LocalPath.intance().configBasePath,
//				LocalPath.USER_CACHE_DB_FILE_NAME);
//		try {
//			dbUtils.createTableIfNotExist(EntirpiseInfo.class);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//	}
	
//	public EntirpiseInfo(Context context){
//		dbUtils = DbUtils.create(context,
//				LocalPath.intance().configBasePath,
//				LocalPath.USER_CACHE_DB_FILE_NAME);
//	}
	
//	public static EntirpiseInfo getInstance(Context context){
//		if(instance_ == null)
//			instance_ = new EntirpiseInfo(context);
//		return instance_;
//	}
//	
	public static EntirpiseInfo findByUserCode(Context context,String userCode){
		DbUtils dbUtils = DbUtils.create(context,
				LocalPath.intance().configBasePath,
				LocalPath.USER_CACHE_DB_FILE_NAME);
		try {
			EntirpiseInfo info = dbUtils.findFirst(Selector.from(EntirpiseInfo.class).
					where("userCode", "=", userCode));
			return info;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static EntirpiseInfo findByCodeMail(Context context,String userCode ,String eMail){
		DbUtils dbUtils = DbUtils.create(context,
				LocalPath.intance().configBasePath,
				LocalPath.USER_CACHE_DB_FILE_NAME);
		try {
			return dbUtils.findFirst(Selector.from(EntirpiseInfo.class)
					.where("userEmail", "=", eMail)
					.and(WhereBuilder.b("userCode", "=", userCode)));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	private static  List<EntirpiseInfo> findByCodeNMail(DbUtils dbUtils,EntirpiseInfo info){
		if(dbUtils == null)
			return null;
		try {
			return dbUtils.findAll(Selector.from(EntirpiseInfo.class)
					.where("userEmail", "=", info.userEmail)
					.and(WhereBuilder.b("userCode", "=", info.userCode)));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
				
	}
	
	public static void saveOrUpdate(Context context,EntirpiseInfo info){
		DbUtils dbUtils = DbUtils.create(context,
				LocalPath.intance().configBasePath,
				LocalPath.USER_CACHE_DB_FILE_NAME);
		List<EntirpiseInfo> infoList = findByCodeNMail(dbUtils, info);
		try{
		if(infoList == null || infoList.size() <= 0){
			dbUtils.save(info);
		}else{
			String[] columNames = {"locationType","gonggaoCount"};
			WhereBuilder builder = WhereBuilder.b("userEmail", "=", info.userEmail)
					.and("userCode", "=", info.userCode);
			dbUtils.update(info,builder,columNames);
		}
		}catch(DbException e){
			e.printStackTrace();
		}
	}
	
	public static void saveOrUpdateGonggao(Context context,EntirpiseInfo info){
		DbUtils dbUtils = DbUtils.create(context,
				LocalPath.intance().configBasePath,
				LocalPath.USER_CACHE_DB_FILE_NAME);
		List<EntirpiseInfo> infoList = findByCodeNMail(dbUtils, info);
		try{
		if(infoList == null || infoList.size() <= 0){
			dbUtils.save(info);
		}else{
			String[] columNames = {"gonggaoCount"};
			WhereBuilder builder = WhereBuilder.b("userEmail", "=", info.userEmail)
					.and("userCode", "=", info.userCode);
			dbUtils.update(info,builder,columNames);
		}
		}catch(DbException e){
			e.printStackTrace();
		}
	}
	
	public static void saveOrUpdateLcation(Context context,EntirpiseInfo info){
		DbUtils dbUtils = DbUtils.create(context,
				LocalPath.intance().configBasePath,
				LocalPath.USER_CACHE_DB_FILE_NAME);
		List<EntirpiseInfo> infoList = findByCodeNMail(dbUtils, info);
		try{
		if(infoList == null || infoList.size() <= 0){
			dbUtils.save(info);
		}else{
			String[] columNames = {"locationType"};
			WhereBuilder builder = WhereBuilder.b("userEmail", "=", info.userEmail)
					.and("userCode", "=", info.userCode);
			dbUtils.update(info,builder,columNames);
		}
		}catch(DbException e){
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "locationType"+locationType
				+"gonggaoCount"+gonggaoCount
				+"userCode"+userCode
				+"userEmail"+userEmail;
	}
	
	
	
	
	
}
