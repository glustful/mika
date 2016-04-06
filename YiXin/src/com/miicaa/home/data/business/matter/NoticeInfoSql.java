package com.miicaa.home.data.business.matter;

import android.content.ContentValues;

import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by apple on 14-1-2.
 */
public class NoticeInfoSql {
    // 用户表名
    public static final String tb_name_notice_info = "t_notice_info";
    // 用户字段
    public static final String col_name_id = "id";
    public static final String col_name_title = "title";
    public static final String col_name_content = "content";
    public static final String col_name_send_name = "send_name";
    public static final String col_name_send_time = "send_time";



    public static SqlCmd createTable() {
        SqlCmd cmd = new SqlCmd(tb_name_notice_info);
        cmd.col(col_name_id, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_title, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_content, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_send_name, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_send_time, SqlCmd.COL_TYPE_INTEGER);

        return cmd.createTable();
    }

    public static SqlCmd insertInto(NoticeInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_notice_info);
        cmd.col(col_name_id, info.getId());
        cmd.col(col_name_title, info.getTitle());
        cmd.col(col_name_content, info.getContent());
        cmd.col(col_name_send_name, info.getSendName());
        cmd.col(col_name_send_time, info.getSendTime());

        return cmd.insertInto();
    }

    public static SqlCmd update(NoticeInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_notice_info);
        cmd.col(col_name_id, info.getId());
        cmd.col(col_name_title, info.getTitle());
        cmd.col(col_name_content, info.getContent());
        cmd.col(col_name_send_name, info.getSendName());
        cmd.col(col_name_send_time, info.getSendTime());

        return cmd.update().where("%s = ?", col_name_id).ps(info.getId());
    }

    public static SqlCmd showAll() {
        SqlCmd cmd = new SqlCmd(tb_name_notice_info);
        return cmd.select("*").orderBy("%s %s", col_name_title, "ASC");
    }

    public static SqlCmd exist(NoticeInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_notice_info);
        return cmd.hasRow("%s = ?", col_name_id).ps(info.getId());
    }

    public static NoticeInfo fromRow(ContentValues row) {
        NoticeInfo info = new NoticeInfo();
        info.setId(row.getAsLong(col_name_id));
        info.setTitle(row.getAsString(col_name_title));
        info.setContent(row.getAsString(col_name_content));
        info.setSendName(row.getAsString(col_name_send_name));
        info.setSendTime(SqlCmd.dateOfCol(row.getAsLong(col_name_send_time)));
        return info;
    }
}
