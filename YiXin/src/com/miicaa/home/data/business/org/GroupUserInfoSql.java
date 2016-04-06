package com.miicaa.home.data.business.org;

import android.content.ContentValues;

import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by Administrator on 13-12-27.
 */
public class GroupUserInfoSql {
	// 用户表名
	public static final String tb_name_group_user_info = "t_group_user_info";
	// 用户字段
	public static final String col_name_id = "id";
	public static final String col_name_unit_code = "unitCode";
	public static final String col_name_group_code = "groupCode";
	public static final String col_name_user_code = "userCode";
	public static final String col_name_sort = "sort";

	public static SqlCmd createTable() {
		SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
		cmd.col(col_name_id, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_unit_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_group_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_user_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_sort, SqlCmd.COL_TYPE_INTEGER);
		return cmd.createTable();
	}

	public static SqlCmd insertInto(GroupUserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
		cmd.col(col_name_id, info.getId());
		cmd.col(col_name_unit_code, info.getUnitCode());
		cmd.col(col_name_group_code, info.getGroupCode());
		cmd.col(col_name_user_code, info.getUserCode());
		cmd.col(col_name_sort, info.getSort());
		return cmd.insertInto();
	}

	public static SqlCmd update(GroupUserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
		cmd.col(col_name_id, info.getId());
		cmd.col(col_name_unit_code, info.getUnitCode());
		cmd.col(col_name_sort, info.getSort());
		return cmd.update()
				.where("%s = ? AND %s = ?", col_name_group_code, col_name_user_code)
				.ps(info.getGroupCode(), info.getUserCode());
	}

    public static SqlCmd findById(Long id) {
        SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
        return cmd.select("%s = ?", col_name_id).ps(id);
    }

    public static SqlCmd findGroupUser(String groupCode)
    {
        SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
        return cmd.select("* as u").where(" where groupCode like #{%s} "
                ,groupCode);

    }

    public static SqlCmd deleteGroupUser(String groupCode)
    {
        SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
        return cmd.delete().where(" where groupCode like #{%s} "
                ,groupCode);

    }

	public static SqlCmd delete(GroupUserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
		return cmd.delete()
				.where("%s = ?", col_name_id)
				.ps(info.getId());
	}

	public static SqlCmd delete(UserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
		return cmd.delete()
				.where("%s = ?", col_name_user_code)
				.ps(info.getCode());
	}

    public static SqlCmd deleteByUnitCode(String userCode) {
        SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
        return cmd.delete()
                .where("%s = ?", col_name_user_code)
                .ps(userCode);
    }

	public static SqlCmd groupsForUser(UserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
		return cmd.select("B.*")
				.join("INNER JOIN %s AS B ON B.%s = A.%s",
						GroupInfoSql.tb_name_group_info,
						GroupInfoSql.col_name_code,
						col_name_group_code)
				.where("%s = ?", col_name_user_code)
				.ps(info.getCode());
	}

	public static SqlCmd showAll() {
		SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
		return cmd.select("*").orderBy("%s %s", col_name_sort, "ASC");
	}

	public static SqlCmd exist(GroupUserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_group_user_info);
		return cmd.hasRow("%s = ? AND %s = ?", col_name_group_code, col_name_user_code)
				.ps(info.getGroupCode(), info.getUserCode());
	}

	public static GroupUserInfo fromRow(ContentValues row) {
		GroupUserInfo info = new GroupUserInfo();
		info.setId(row.getAsLong(col_name_id));
		info.setUnitCode(row.getAsString(col_name_unit_code));
		info.setGroupCode(row.getAsString(col_name_group_code));
		info.setUserCode(row.getAsString(col_name_user_code));
		info.setSort(row.getAsLong(col_name_sort));
		return info;
	}
}
