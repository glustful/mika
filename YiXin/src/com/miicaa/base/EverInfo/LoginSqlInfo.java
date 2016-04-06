package com.miicaa.base.EverInfo;

import android.content.ContentValues;

import com.miicaa.base.data.SqlCmd;



public class LoginSqlInfo {
	// ��¼��־����
		public static final String tb_name_login_info = "t_login_info";
		// ��¼��־�ֶ�
		public static final String col_name_user_email = "user_email";
		public static final String col_name_user_password = "user_password";
		public static final String col_name_login_auto = "login_auto";
		public static final String col_name_login_type = "login_type";
		public static final String col_name_last_time = "last_time";
		public static final String col_name_last_org_code = "last_org_code";
		public static final String col_name_last_user_code = "last_user_code";
		public static final String col_name_last_user_name = "last_user_name";
		// ��¼����ֵ����
		public static final String col_val_login_type_net = "net";
		public static final String col_val_login_type_local = "local";

		public static SqlCmd createTable() {
			SqlCmd cmd = new SqlCmd(tb_name_login_info);
			cmd.col(col_name_user_email, SqlCmd.COL_TYPE_TEXT);
			cmd.col(col_name_user_password, SqlCmd.COL_TYPE_TEXT);
			cmd.col(col_name_login_auto, SqlCmd.COL_TYPE_TEXT);
			cmd.col(col_name_login_type, SqlCmd.COL_TYPE_TEXT);
			cmd.col(col_name_last_time, SqlCmd.COL_TYPE_INTEGER);
			cmd.col(col_name_last_org_code, SqlCmd.COL_TYPE_TEXT);
			cmd.col(col_name_last_user_code, SqlCmd.COL_TYPE_TEXT);
			cmd.col(col_name_last_user_name, SqlCmd.COL_TYPE_TEXT);
			return cmd.createTable();
		}

		public static SqlCmd insertInto(LoginUserInfo info) {
			SqlCmd cmd = new SqlCmd(tb_name_login_info);
			cmd.col(col_name_user_email, info.getUserEmail());
			cmd.col(col_name_user_password, info.getUserPassword());
//			cmd.col(col_name_login_auto, info.getLoginAuto());
			cmd.col(col_name_login_type, info.getLoginType());
			cmd.col(col_name_last_time, info.getLastTime());
//			cmd.col(col_name_last_org_code, info.getLastOrgInfo().getCode());
//			cmd.col(col_name_last_user_code, info.getLastUserInfo().getCode());
//			cmd.col(col_name_last_user_name, info.getLastUserInfo().getName());
			return cmd.insertInto();
		}

		public static SqlCmd showAll() {
			SqlCmd cmd = new SqlCmd(tb_name_login_info);
			return cmd.select("*").orderBy("%s %s", col_name_last_time, "DESC");
		}

		public static SqlCmd getLastLogin() {
			SqlCmd cmd = new SqlCmd(tb_name_login_info);
			return cmd.select("*").orderBy("%s %s", col_name_last_time, "DESC").limit(1);
		}
		
		public static LoginUserInfo fromRow(ContentValues row) {
			LoginUserInfo info = new LoginUserInfo();
			if (row != null) {
				info.setUserEmail(row.getAsString(col_name_user_email));
				info.setuserPassword(row.getAsString(col_name_user_password));
//				info.setLoginAuto(SqlCmd.boolOfCol(row.getAsString(col_name_login_auto)));
				info.setLoginType(row.getAsString(col_name_login_type));
				info.setLastTime(SqlCmd.dateOfCol(row.getAsLong(col_name_last_time)));
				//����ǵ�¼��ʱ���Ӧ�ĵ�λ��ƺ��û����
//				info.setLastOrgInfo(OrgInfo.findByCode(row.getAsString(col_name_last_org_code)));
//				info.setLastUserInfo(UserInfo.findByCode(row.getAsString(col_name_last_user_code)));
			}
			return info;
		}
}
