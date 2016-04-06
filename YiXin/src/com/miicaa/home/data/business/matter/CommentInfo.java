package com.miicaa.home.data.business.matter;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;

import com.miicaa.home.data.storage.CacheDatabase;
import com.miicaa.home.data.storage.LocalDatabase;
import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by apple on 13-12-31.
 */
public class CommentInfo {
    private static LocalDatabase db;
    private String id;
    private String dataId;
    private String content;
    private Date createTime;
    private Date endTime;
    private String userCode;
    private String userName;
    private String status;
    private String dataType;

    public static void init() {
        db = CacheDatabase.instance();

        if (!db.isTableExsit(CommentInfoSql.tb_name_comment_info)) {
            db.execCmd(CommentInfoSql.createTable());

            initTest();
        }
    }

    public static void initTest() {
        showAll();
    }

    public static void showAll() {
        ArrayList<ContentValues> rows = db.queryCmd(CommentInfoSql.showAll());
        SqlCmd.showRows(rows);
    }

    public static ArrayList<CommentInfo> findByDataId(String dataId)
    {
        SqlCmd cmd = new SqlCmd(CommentInfoSql.tb_name_comment_info);
        SqlCmd sqlCmd = cmd.select("*").where("%s = ?", CommentInfoSql.col_name_data_id).ps(dataId);
        ArrayList<ContentValues> rows = db.queryCmd(sqlCmd);
        ArrayList<CommentInfo> list = new ArrayList<CommentInfo>();
        if (rows != null )
        {
            for(int i =0; i < rows.size();i++)
            {
                list.add(CommentInfoSql.fromRow(rows.get(i)));
            }
        }
        return list;
    }



    public CommentInfo() {
    }

    public CommentInfo(String id, String dataId, String content, Date createTime, Date endTime, String userCode, String userName, String status, String dataType) {
        this.id = id;
        this.dataId = dataId;
        this.content = content;
        this.createTime = createTime;
        this.endTime = endTime;
        this.userCode = userCode;
        this.userName = userName;
        this.status = status;
        this.dataType = dataType;
    }

    public static LocalDatabase getDb() {
        return db;
    }

    public static void setDb(LocalDatabase db) {
        CommentInfo.db = db;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
