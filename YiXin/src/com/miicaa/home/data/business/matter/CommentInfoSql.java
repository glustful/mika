package com.miicaa.home.data.business.matter;

import android.content.ContentValues;

import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by apple on 13-12-31.
 */
public class CommentInfoSql {
    // 用户表名
    public static final String tb_name_comment_info = "t_comment_info";
    // 用户字段
    public static final String col_name_id = "id";
    public static final String col_name_data_id = "data_id";
    public static final String col_name_content = "content";
    public static final String col_name_create_time = "create_time";
    public static final String col_name_end_time = "end_time";
    public static final String col_name_user_code = "user_code";
    public static final String col_name_user_name = "user_name";
    public static final String col_name_status = "status";
    public static final String col_name_data_type = "data_type";

    public static SqlCmd createTable() {
        SqlCmd cmd = new SqlCmd(tb_name_comment_info);
        cmd.col(col_name_id, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_data_id, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_content, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_create_time, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_end_time, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_user_code, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_user_name, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_status, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_data_type, SqlCmd.COL_TYPE_TEXT);

        return cmd.createTable();
    }

    public static SqlCmd insertInto(CommentInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_comment_info);
        cmd.col(col_name_id, info.getId());
        cmd.col(col_name_data_id, info.getDataId());
        cmd.col(col_name_content, info.getContent());
        cmd.col(col_name_create_time, info.getCreateTime());
        cmd.col(col_name_end_time, info.getEndTime());
        cmd.col(col_name_user_code, info.getUserCode());
        cmd.col(col_name_user_name, info.getUserName());
        cmd.col(col_name_status, info.getStatus());
        cmd.col(col_name_data_type, info.getDataType());

        return cmd.insertInto();
    }

    public static SqlCmd update(CommentInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_comment_info);
        cmd.col(col_name_id, info.getId());
        cmd.col(col_name_data_id, info.getDataId());
        cmd.col(col_name_content, info.getContent());
        cmd.col(col_name_create_time, info.getCreateTime());
        cmd.col(col_name_end_time, info.getEndTime());
        cmd.col(col_name_user_code, info.getUserCode());
        cmd.col(col_name_user_name, info.getUserName());
        cmd.col(col_name_status, info.getStatus());
        cmd.col(col_name_data_type, info.getDataType());

        return cmd.update().where("%s = ?", col_name_id).ps(info.getId());
    }

    public static SqlCmd showAll() {
        SqlCmd cmd = new SqlCmd(tb_name_comment_info);
        return cmd.select("*").orderBy("%s %s", col_name_content, "ASC");
    }

    public static SqlCmd exist(CommentInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_comment_info);
        return cmd.hasRow("%s = ?", col_name_id).ps(info.getId());
    }

    public static SqlCmd findById(long dataId,long dataType) {
        SqlCmd cmd = new SqlCmd(tb_name_comment_info);
        return cmd.select("*").where("col_name_data_id = ? AND col_name_data_type = ?")
                .ps(dataId,dataType).orderBy("%s %s", col_name_end_time, "ASC");
    }

    public static CommentInfo fromRow(ContentValues row) {
        CommentInfo info = new CommentInfo();
        info.setId(row.getAsString(col_name_id));
        info.setDataId(row.getAsString(col_name_data_id));
        info.setContent(row.getAsString(col_name_content));
        info.setCreateTime(SqlCmd.dateOfCol(row.getAsLong(col_name_create_time)));
        info.setEndTime(SqlCmd.dateOfCol(row.getAsLong(col_name_end_time)));
        info.setUserCode(row.getAsString(col_name_user_code));
        info.setUserName(row.getAsString(col_name_user_name));
        info.setStatus(row.getAsString(col_name_status));
        info.setDataType(row.getAsString(col_name_data_type));
        return info;
    }
}
