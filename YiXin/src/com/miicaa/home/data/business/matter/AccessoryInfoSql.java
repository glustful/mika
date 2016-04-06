package com.miicaa.home.data.business.matter;

import android.content.ContentValues;

import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by apple on 13-12-31.
 */
public class AccessoryInfoSql {
    // 用户表名
    public static final String tb_name_unit_info = "t_accessory_info";
    // 用户字段
    public static final String col_name_id = "id";
    public static final String col_name_title = "title";
    public static final String col_name_ext = "ext";
    public static final String col_name_size = "size";
    public static final String col_name_info_id = "info_id";
    public static final String col_name_file_id = "file_id";

    public static SqlCmd createTable() {
        SqlCmd cmd = new SqlCmd(tb_name_unit_info);
        cmd.col(col_name_id, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_title, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_ext, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_size, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_info_id, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_file_id, SqlCmd.COL_TYPE_INTEGER);

        return cmd.createTable();
    }

    public static SqlCmd insertInto(AccessoryInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_unit_info);
        cmd.col(col_name_id, info.getId());
        cmd.col(col_name_title, info.getTitle());
        cmd.col(col_name_ext, info.getExt());
        cmd.col(col_name_size, info.getSize());
        cmd.col(col_name_info_id, info.getInfoId());
        cmd.col(col_name_file_id, info.getFileId());

        return cmd.insertInto();
    }

    public static SqlCmd update(AccessoryInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_unit_info);
        cmd.col(col_name_id, info.getId());
        cmd.col(col_name_title, info.getTitle());
        cmd.col(col_name_ext, info.getExt());
        cmd.col(col_name_size, info.getSize());
        cmd.col(col_name_info_id, info.getInfoId());
        cmd.col(col_name_file_id, info.getFileId());

        return cmd.update().where("%s = ?", col_name_id).ps(info.getId());
    }

    public static SqlCmd showAll() {
        SqlCmd cmd = new SqlCmd(tb_name_unit_info);
        return cmd.select("*").orderBy("%s %s", col_name_title, "ASC");
    }

    public static SqlCmd exist(AccessoryInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_unit_info);
        return cmd.hasRow("%s = ?", col_name_id).ps(info.getId());
    }

    public static AccessoryInfo fromRow(ContentValues row) {
        AccessoryInfo info = new AccessoryInfo();
        info.setId(row.getAsString(col_name_id));
        info.setTitle(row.getAsString(col_name_title));
        info.setExt(row.getAsString(col_name_ext));
        info.setSize(row.getAsLong(col_name_size));
        info.setInfoId(row.getAsString(col_name_info_id));
        info.setFileId(row.getAsString(col_name_file_id));
        return info;
    }
}
