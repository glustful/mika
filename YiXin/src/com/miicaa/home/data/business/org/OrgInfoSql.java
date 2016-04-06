package com.miicaa.home.data.business.org;

import android.content.ContentValues;

import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by Administrator on 13-12-20.
 */
public class OrgInfoSql {
	// 单位表名
	public static final String tb_name_org_info = "t_org_info";
	// 单位字段
	public static final String col_name_id = "id";
	public static final String col_name_code = "code";
	public static final String col_name_name = "name";
	public static final String col_name_sort = "sort";
	public static final String col_name_status = "status";
	public static final String col_name_desc = "desc";
	public static final String col_name_create_time = "createtime";

	public static SqlCmd createTable() {
		SqlCmd cmd = new SqlCmd(tb_name_org_info);
		cmd.col(col_name_id, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_name, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_sort, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_status, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_desc, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_create_time, SqlCmd.COL_TYPE_INTEGER);
		return cmd.createTable();
	}

	public static SqlCmd insertInto(OrgInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_org_info);
		cmd.col(col_name_id, info.getId());
		cmd.col(col_name_code, info.getCode());
		cmd.col(col_name_name, info.getName());
		cmd.col(col_name_sort, info.getSort());
		cmd.col(col_name_status, info.getStatus());
		cmd.col(col_name_desc, info.getDesc());
		cmd.col(col_name_create_time, info.getCreateTime());
		return cmd.insertInto();
	}

	public static SqlCmd update(OrgInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_org_info);
		cmd.col(col_name_id, info.getId());
		cmd.col(col_name_name, info.getName());
		cmd.col(col_name_sort, info.getSort());
		cmd.col(col_name_status, info.getStatus());
		cmd.col(col_name_desc, info.getDesc());
		cmd.col(col_name_create_time, info.getCreateTime());
		return cmd.update().where("%s = ?", col_name_code).ps(info.getCode());
	}
	
	public static SqlCmd delete(OrgInfo info){
		SqlCmd cmd = new SqlCmd(tb_name_org_info);
		return cmd.delete().where("%s = ?", col_name_code).ps(info.getCode());
	}
	
	public static SqlCmd deleteAll(){
		SqlCmd cmd = new SqlCmd(tb_name_org_info);
		return cmd.delete();
	}
	
	

	public static SqlCmd showAll() {
		SqlCmd cmd = new SqlCmd(tb_name_org_info);
		return cmd.select("*").orderBy("%s %s", col_name_sort, "DESC");
	}

	public static SqlCmd exist(OrgInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_org_info);
		return cmd.hasRow("%s = ?", col_name_code).ps(info.getCode());
	}

	public static SqlCmd findByCode(String code) {
		SqlCmd cmd = new SqlCmd(tb_name_org_info);
		return cmd.select("*").where("%s = ?", col_name_code).ps(code);
	}

	public static OrgInfo fromRow(ContentValues row) {
		OrgInfo info = new OrgInfo();
		if (row != null) {
			info.setId(row.getAsLong(col_name_id));
			info.setCode(row.getAsString(col_name_code));
			info.setName(row.getAsString(col_name_name));
			info.setSort(row.getAsLong(col_name_sort));
			info.setStatus(row.getAsLong(col_name_status));
			info.setDesc(row.getAsString(col_name_desc));
			info.setCreateTime(SqlCmd.dateOfCol(row.getAsLong(col_name_create_time)));
		}
		return info;
	}
}
