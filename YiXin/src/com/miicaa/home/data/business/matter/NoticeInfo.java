package com.miicaa.home.data.business.matter;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;

import com.miicaa.home.data.storage.CacheDatabase;
import com.miicaa.home.data.storage.LocalDatabase;
import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by apple on 14-1-2.
 */
public class NoticeInfo {
    private static LocalDatabase db;
    private long id;
    private String title;
    private String content;
    private String sendName;
    private Date sendTime;

    public static void init() {
        db = CacheDatabase.instance();

        if (!db.isTableExsit(NoticeInfoSql.tb_name_notice_info)) {
            db.execCmd(NoticeInfoSql.createTable());

            initTest();
        }
    }

    public static void initTest() {
        new NoticeInfo(3001,"这是一个公告1","jflkdasjflajsfljfjldksajfldkjfdklaj","李四",1388641580).save();
        new NoticeInfo(3002,"这是一个公告2","jflkdasjflajsfljfjldksajfldkjfdklaj","李四",1388641580).save();
        new NoticeInfo(3003,"这是一个公告3","jflkdasjflajsfljfjldksajfldkjfdklaj","李四",1388641580).save();
        showAll();
    }

    public static void showAll() {
        ArrayList<ContentValues> rows = db.queryCmd(NoticeInfoSql.showAll());
        SqlCmd.showRows(rows);
    }

    public NoticeInfo() {
    }

    public NoticeInfo(long id, String title, String content, String sendName, long sendTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.sendName = sendName;
        this.sendTime = SqlCmd.dateOfCol(sendTime);;
    }

    public Boolean exist() {
        return db.hasRow(NoticeInfoSql.exist(this));
    }

    public Boolean save() {
        if (exist()) {
            return db.execCmd(NoticeInfoSql.update(this));
        } else {
            return db.execCmd(NoticeInfoSql.insertInto(this));
        }
    }

    public static LocalDatabase getDb() {
        return db;
    }

    public static void setDb(LocalDatabase db) {
        NoticeInfo.db = db;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
