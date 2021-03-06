package com.miicaa.home.data.business.org;

import android.content.ContentValues;

import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by Administrator on 13-12-27.
 * 查询类
 */
public class UnitInfoSql {
	// 用户表名
	public static final String tb_name_unit_info = "t_unit_info";
	// 用户字段
	public static final String col_name_id = "id";
	public static final String col_name_code = "code";
	public static final String col_name_name = "name";
	public static final String col_name_full_name = "fullName";
	public static final String col_name_org_code = "orgCode";
	public static final String col_name_parent_code = "parentCode";
	public static final String col_name_status = "status";
	public static final String col_name_sort = "sort";
	public static final String col_name_create_time = "createTime";

	public static SqlCmd createTable() {
		SqlCmd cmd = new SqlCmd(tb_name_unit_info);
		cmd.col(col_name_id, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_name, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_full_name, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_org_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_parent_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_status, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_sort, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_create_time, SqlCmd.COL_TYPE_INTEGER);
		return cmd.createTable();
	}

	public static SqlCmd insertInto(UnitInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_unit_info);
		cmd.col(col_name_id, info.getId());
		cmd.col(col_name_code, info.getCode());
		cmd.col(col_name_name, info.getName());
		cmd.col(col_name_full_name, info.getFullName());
		cmd.col(col_name_org_code, info.getOrgCode());
		cmd.col(col_name_parent_code, info.getParentCode());
		cmd.col(col_name_status, info.getStatus());
		cmd.col(col_name_sort, info.getSort());
		cmd.col(col_name_create_time, info.getCreateTime());
		return cmd.insertInto();
	}

	public static SqlCmd update(UnitInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_unit_info);
		cmd.col(col_name_id, info.getId());
		cmd.col(col_name_name, info.getName());
		cmd.col(col_name_full_name, info.getFullName());
		cmd.col(col_name_org_code, info.getOrgCode());
		cmd.col(col_name_parent_code, info.getParentCode());
		cmd.col(col_name_status, info.getStatus());
		cmd.col(col_name_sort, info.getSort());
		cmd.col(col_name_create_time, info.getCreateTime());
		return cmd.update().where("%s = ?", col_name_code).ps(info.getCode());
	}

    public static SqlCmd updateChild(String unitCode, String oldName, String newName)
    {
        SqlCmd cmd = new SqlCmd(tb_name_unit_info);
        return cmd.replace(col_name_full_name,oldName,newName).where("%s like #{?}", unitCode).ps(unitCode);
    }

	public static SqlCmd showAll() {
		SqlCmd cmd = new SqlCmd(tb_name_unit_info);
		return cmd.select("*").orderBy("%s %s", col_name_name, "ASC");
	}

	public static SqlCmd exist(UnitInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_unit_info);
		return cmd.hasRow("%s = ?", col_name_code).ps(info.getCode());
	}

    public static SqlCmd findById(Long id) {
        SqlCmd cmd = new SqlCmd(tb_name_unit_info);
        return cmd.select("%s = ?", col_name_id).ps(id);
    }

    public static SqlCmd findByParentCode(String parentCode) {
        SqlCmd cmd = new SqlCmd(tb_name_unit_info);
        return cmd.select("%s = ?", col_name_parent_code).ps(parentCode);
    }

	public static SqlCmd unitsInOrg(OrgInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_unit_info);
		return cmd.select("*").where("%s = ?", col_name_org_code).ps(info.getCode());
	}

	public static UnitInfo fromRow(ContentValues row) {
		UnitInfo info = new UnitInfo();
		info.setId(row.getAsLong(col_name_id));
		info.setCode(row.getAsString(col_name_code));
		info.setName(row.getAsString(col_name_name));
		info.setFullName(row.getAsString(col_name_full_name));
		info.setOrgCode(row.getAsString(col_name_org_code));
		info.setParentCode(row.getAsString(col_name_parent_code));
		info.setStatus(row.getAsLong(col_name_status));
		info.setSort(row.getAsLong(col_name_sort));
		info.setCreateTime(SqlCmd.dateOfCol(row.getAsLong(col_name_create_time)));
		return info;
	}

    public static SqlCmd delete(UnitInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_unit_info);
        return cmd.delete()
                .where("%s = ?", col_name_id)
                .ps(info.getId());
    }
    public static SqlCmd deleteByOrgCode(String orgCode) {
        SqlCmd cmd = new SqlCmd(tb_name_unit_info);
        return cmd.delete().where("%s = ?", col_name_org_code).ps(orgCode);
    }
}
