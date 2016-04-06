package com.miicaa.home.data.business.org;

import android.content.ContentValues;

import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by Administrator on 13-12-20.
 */
public class UserInfoSql {
	// 用户表名
	public static final String tb_name_user_info = "t_user_info";
	// 用户字段
	public static final String col_name_id = "id";
	public static final String col_name_code = "code";
	public static final String col_name_name = "name";
	public static final String col_name_name_fpy = "name_fpy";
	public static final String col_name_name_py = "name_py";
	public static final String col_name_email = "email";
	public static final String col_name_cellphone = "cellphone";
	public static final String col_name_status = "status";
	public static final String col_name_avatar = "avatar";
	public static final String col_name_create_time = "createTime";
	public static final String col_name_org_code = "orgCode";
	public static final String col_name_gender = "gender";
	public static final String col_name_birthday = "birthday";
	public static final String col_name_qq = "qq";
	public static final String col_name_phone = "phone";
	public static final String col_name_addr = "addr";

	public static SqlCmd createTable() {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		cmd.col(col_name_id, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_name, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_name_fpy, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_name_py, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_email, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_cellphone, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_status, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_avatar, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_create_time, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_org_code, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_gender, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_birthday, SqlCmd.COL_TYPE_INTEGER);
		cmd.col(col_name_qq, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_phone, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_addr, SqlCmd.COL_TYPE_TEXT);
		return cmd.createTable();
	}

	public static SqlCmd insertInto(UserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		cmd.col(col_name_id, info.getId());
		cmd.col(col_name_code, info.getCode());
		cmd.col(col_name_name, info.getName());
		cmd.col(col_name_name_fpy, info.getNameFPY());
		cmd.col(col_name_name_py, info.getNamePY());
		cmd.col(col_name_email, info.getEmail());
		cmd.col(col_name_cellphone, info.getCellphone());
		cmd.col(col_name_status, info.getStatus());
		cmd.col(col_name_avatar, info.getAvatar());
		cmd.col(col_name_create_time, info.getCreateTime());
		cmd.col(col_name_org_code, info.getOrgCode());
		cmd.col(col_name_gender, info.getGender());
		cmd.col(col_name_birthday, info.getBirthday());
		cmd.col(col_name_qq, info.getQq());
		cmd.col(col_name_phone, info.getPhone());
		cmd.col(col_name_addr, info.getAddr());
		return cmd.insertInto();
	}

	public static SqlCmd update(UserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		cmd.col(col_name_id, info.getId());
		cmd.col(col_name_code, info.getCode());
		cmd.col(col_name_name, info.getName());
		cmd.col(col_name_name_fpy, info.getNameFPY());
		cmd.col(col_name_name_py, info.getNamePY());
		cmd.col(col_name_cellphone, info.getCellphone());
		cmd.col(col_name_status, info.getStatus());
		cmd.col(col_name_avatar, info.getAvatar());
		cmd.col(col_name_create_time, info.getCreateTime());
		cmd.col(col_name_gender, info.getGender());
		cmd.col(col_name_birthday, info.getBirthday());
		cmd.col(col_name_qq, info.getQq());
		cmd.col(col_name_phone, info.getPhone());
		cmd.col(col_name_addr, info.getAddr());
		return cmd.update().where("%s = ? AND %s = ?", col_name_email, col_name_org_code).ps(info.getEmail(), info.getOrgCode());
	}

	public static SqlCmd delete(UserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		return cmd.delete().where("%s = ?", col_name_code).ps(info.getCode());
	}

    public static SqlCmd deleteByOrgCode(String orgCode) {
        SqlCmd cmd = new SqlCmd(tb_name_user_info);
        return cmd.delete().where("%s = ?", col_name_org_code).ps(orgCode);
    }

    public static SqlCmd delete(long id) {
        SqlCmd cmd = new SqlCmd(tb_name_user_info);
        return cmd.delete().where("%s = ?", col_name_id).ps(id);
    }

	public static SqlCmd showAll() {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		return cmd.select("*").orderBy("%s %s", col_name_name, "ASC");
	}

	public static SqlCmd exist(UserInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		return cmd.hasRow("%s = ? AND %s = ?", col_name_email, col_name_org_code).ps(info.getEmail(), info.getOrgCode());
	}

	public static SqlCmd findByCode(String code) {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		return cmd.select("*").where("%s = ?", col_name_code).ps(code);
	}
	
	public static SqlCmd findByCodeForName(String code){
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		return cmd.select(col_name_name).where("%s = ?", col_name_code).ps(code);
	}

    public static SqlCmd findById(Long id) {
        SqlCmd cmd = new SqlCmd(tb_name_user_info);
        return cmd.select("*").where("%s = ?", col_name_id).ps(id);
    }

	public static SqlCmd findByEmailAndOrgCode(String email, String code) {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		return cmd.select("*").where("%s = ? AND %s = ?", col_name_email, col_name_org_code).ps(email, code);
	}

	public static SqlCmd usersInOrg(OrgInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		return cmd.select("*").where("%s = ?", col_name_org_code).ps(info.getCode());
	}

	public static SqlCmd usersInUnit(UnitInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		return cmd.select("A.*")
				.join("INNER JOIN %s AS B ON B.%s = A.%s",
						UnitUserInfoSql.tb_name_unit_user_info,
						UnitUserInfoSql.col_name_user_code,
						col_name_code)
				.where("B.%s = ?", UnitUserInfoSql.col_name_unit_code)
				.ps(info.getCode());
	}

	public static SqlCmd usersInGroup(GroupInfo info) {
		SqlCmd cmd = new SqlCmd(tb_name_user_info);
		return cmd.select("A.*")
				.join("INNER JOIN %s AS B ON B.%s = A.%s",
						GroupUserInfoSql.tb_name_group_user_info,
						GroupUserInfoSql.col_name_user_code,
						col_name_code)
				.where("B.%s = ?", GroupUserInfoSql.col_name_group_code)
				.ps(info.getCode());
	}

	public static UserInfo fromRow(ContentValues row) {
		UserInfo info = new UserInfo();
		if (row != null) {
			info.setId(row.getAsLong(col_name_id));
			info.setCode(row.getAsString(col_name_code));
			info.setNames(row.getAsString(col_name_name),
					row.getAsString(col_name_name_fpy),
					row.getAsString(col_name_name_py));
			info.setEmail(row.getAsString(col_name_email));
			info.setCellphone(row.getAsString(col_name_cellphone));
			info.setStatus(row.getAsLong(col_name_status));
			info.setAvatar(row.getAsString(col_name_avatar));
			info.setCreateTime(SqlCmd.dateOfCol(row.getAsLong(col_name_create_time)));
			info.setOrgCode(row.getAsString(col_name_org_code));
			info.setGender(row.getAsString(col_name_gender));
			info.setBirthday(SqlCmd.dateOfCol(row.getAsLong(col_name_birthday)));
			info.setQq(row.getAsString(col_name_qq));
			info.setPhone(row.getAsString(col_name_phone));
			info.setAddr(row.getAsString(col_name_addr));
		}
		return info;
	}
}
