package com.miicaa.home.data.business.matter;

import java.io.Serializable;

/**
 * Created by apple on 13-12-31.
 */
public class AccessoryInfo implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -8191397351393756098L;
//    private static LocalDatabase db;
    private String id;
    private String title;
    private String ext;
    private long size;
    private String infoId;
    private String fileId;
    private String userCode;
    private String userName;
    private Long upLoadTime;

    public static void init() {
//        db = CacheDatabase.instance();
//
//        if (!db.isTableExsit(AccessoryInfoSql.tb_name_unit_info)) {
//            db.execCmd(AccessoryInfoSql.createTable());
//
//            initTest();
//        }
    }

    public static void initTest() {
        //new AccessoryInfo(1111, "关于审批的规范1.doc", "doc",2004,2004,120).save();
        //new AccessoryInfo(2222, "关于审批的规范2.doc", "doc",2004,2004,120).save();
        //new AccessoryInfo(2223, "关于审批的规范3.doc", "doc",2004,2004,120).save();
        //new AccessoryInfo(2224, "关于审批的规范4.doc", "doc",2004,2004,120).save();
        //new AccessoryInfo(2225, "关于审批的规范5.doc", "doc",2004,2004,120).save();
//        showAll();
    }

//    public static ArrayList<AccessoryInfo> findByDataId(String dataId)
//    {
//        SqlCmd cmd = new SqlCmd(AccessoryInfoSql.tb_name_unit_info);
//        SqlCmd sqlCmd = cmd.select("*").where("%s = ?", AccessoryInfoSql.col_name_info_id).ps(dataId);
//        ArrayList<ContentValues> rows = db.queryCmd(sqlCmd);
//        ArrayList<AccessoryInfo> list = new ArrayList<AccessoryInfo>();
//        if (rows != null )
//        {
//            for(int i =0; i < rows.size();i++)
//            {
//                list.add(AccessoryInfoSql.fromRow(rows.get(i)));
//            }
//        }
//        return list;
//    }

//    public static void showAll() {
//        ArrayList<ContentValues> rows = db.queryCmd(AccessoryInfoSql.showAll());
//        SqlCmd.showRows(rows);
//    }

    public AccessoryInfo() {
    }

    public AccessoryInfo(String id, String title, String ext, long size, String infoId, String fileId) {
        this.id = id;
        this.title = title;
        this.ext = ext;
        this.size = size;
        this.infoId = infoId;
        this.fileId = fileId;
    }

//    public Boolean exist() {
//        return db.hasRow(AccessoryInfoSql.exist(this));
//    }
//
//    public Boolean save() {
//        if (exist()) {
//            return db.execCmd(AccessoryInfoSql.update(this));
//        } else {
//            return db.execCmd(AccessoryInfoSql.insertInto(this));
//        }
//    }
//
//    public static LocalDatabase getDb() {
//        return db;
//    }
//
//    public static void setDb(LocalDatabase db) {
//        AccessoryInfo.db = db;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public void setUserCode(String code){
    	this.userCode = code;
    }
    public String getUserCode(){
    	return this.userCode;
    }
    
    public void setUserName(String userName){
    	this.userName = userName;
    }
    
    public String getUserName(){
    	return this.userName;
    	}
    
    public void setUpLoadTime(Long upLoadTime){
    	this.upLoadTime = upLoadTime;
    }
    public Long getUpLoadTime(){
    	return this.upLoadTime;
    }
}

