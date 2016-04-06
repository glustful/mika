package com.miicaa.home.data.business.org;

import android.content.ContentValues;

import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by Administrator on 13-12-27.
 */
public class UnitUserInfoSql {
	// 用户表名
	public static final String tb_name_unit_user_info = "t_unit_user_info";
	// 用户字段
	public static final String col_name_id = "id";
	public static final String col_name_unit_code = "unitCode";
	public static final String col_name_user_code = "userCode";
	public static final String col_name_sort = "sort";
	public static final String col_name_status = "status";

	public static SqlCmd createTable() {
		SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
		cmd.col(col_name_id, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_unit_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_user_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_sort, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_status, SqlCmd.COL_TYPE_INTEGER);
		return cmd.createTable();
	}

	public static SqlCmd insertInto(UnitUserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
		cmd.col(col_name_id, info.getId());
		cmd.col(col_name_unit_code, info.getUnitCode());
		cmd.col(col_name_user_code, info.getUserCode());
		cmd.col(col_name_sort, info.getSort());
		cmd.col(col_name_status, info.getStatus());
		return cmd.insertInto();
	}

	public static SqlCmd update(UnitUserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
		cmd.col(col_name_id, info.getId());
		cmd.col(col_name_sort, info.getSort());
		cmd.col(col_name_status, info.getStatus());
		return cmd.update()
				.where("%s = ? AND %s = ?", col_name_unit_code, col_name_user_code)
				.ps(info.getUnitCode(), info.getUserCode());
	}

	public static SqlCmd delete(UnitUserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
		return cmd.delete()
				.where("%s = ?", col_name_id)
				.ps(info.getId());
	}

    public static SqlCmd deleteByUnitCode(String unitCode)
    {
        SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
        return cmd.delete()
                .where("%s = ?", col_name_unit_code)
                .ps(unitCode);
    }



	public static SqlCmd delete(UserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
		return cmd.delete()
				.where("%s = ?", col_name_user_code)
				.ps(info.getCode());
	}

    public static SqlCmd deleteByUserCode(String userCode) {
        SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
        return cmd.delete()
                .where("%s = ?", col_name_user_code)
                .ps(userCode);
    }

    public static SqlCmd unitsForUser(UserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
		return cmd.select("B.*")
				.join("INNER JOIN %s AS B ON B.%s = A.%s",
						UnitInfoSql.tb_name_unit_info,
						UnitInfoSql.col_name_code,
						col_name_unit_code)
				.where("%s = ?", col_name_user_code)
				.ps(info.getCode());
	}

	public static SqlCmd showAll() {
		SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
		return cmd.select("*").orderBy("%s %s", col_name_sort, "ASC");
	}

	public static SqlCmd exist(UnitUserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
		return cmd.hasRow("%s = ? AND %s = ?", col_name_unit_code, col_name_user_code)
				.ps(info.getUnitCode(), info.getUserCode());
	}

	public static UnitUserInfo fromRow(ContentValues row) {
		UnitUserInfo info = new UnitUserInfo();
		info.setId(row.getAsLong(col_name_id));
		info.setUnitCode(row.getAsString(col_name_unit_code));
		info.setUserCode(row.getAsString(col_name_user_code));
		info.setSort(row.getAsLong(col_name_sort));
		info.setStatus(row.getAsLong(col_name_status));
		return info;
	}

    public static SqlCmd findOnlyFormUnit(String unitCode)
    {
        SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
        return cmd.select("* as u").where(" where unitCode = ? and not EXISTS(" +
                " select 1 from %s as uu where u.userCode=uu.userCode and uu.unitCode != ? "
                , unitCode,tb_name_unit_user_info)
                .ps(unitCode,unitCode);

    }

    public static SqlCmd findOnlyMulUnit(String unitCode)
    {
        SqlCmd cmd = new SqlCmd(tb_name_unit_user_info);
        return cmd.select("* as u").where(" where unitCode = ? and EXISTS(" +
                " select 1 from %s as uu where u.userCode=uu.userCode and uu.unitCode != ? "
                , unitCode,tb_name_unit_user_info)
                .ps(unitCode,unitCode);

    }


}
