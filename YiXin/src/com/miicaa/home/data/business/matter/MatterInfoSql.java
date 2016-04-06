package com.miicaa.home.data.business.matter;

import android.content.ContentValues;

import com.miicaa.home.data.storage.ConfigDatabase;
import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by apple on 13-12-31.
 */
public class MatterInfoSql {
    // 用户表名
    public static final String tb_name_matter_info = "t_matter_info";
    // 用户字段
    public static final String col_name_id = "id";
    public static final String col_name_content = "content";
    public static final String col_name_arrange_type = "arrangeType";
    public static final String col_name_people_num = "peopleNum";
    public static final String col_name_start_time = "startTime";
    public static final String col_name_end_time = "endTime";
    public static final String col_name_secrecy = "secrecy";
    public static final String col_name_creator_code = "creatorCode";
    public static final String col_name_creator_name = "creatorName";
    public static final String col_name_data_version = "dataVersion";
    public static final String col_name_create_time = "createTime";
    public static final String col_name_src_name = "srcName";
    public static final String col_name_last_update_time = "updateTime";
    public static final String col_name_data_type = "dataType";
    public static final String col_name_client_name = "clientName";
    public static final String col_name_status = "status";
    public static final String col_name_approve_type_name = "approveTypeName";
    public static final String col_name_last_approve_status = "lastApproveStatus";
    public static final String col_name_orgcode = "orgcode";
	public static final String col_name_repeat_id = "repeatId";
	public static final String col_name_repeat_str = "repeatStr";
    public static final  String col_name_hasObserved = "hasObserved";

    public static SqlCmd createTable() {
        SqlCmd cmd = new SqlCmd(tb_name_matter_info);
        cmd.col(col_name_id, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_content, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_arrange_type, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_people_num, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_start_time, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_end_time, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_secrecy, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_creator_code, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_creator_name, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_data_version, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_create_time, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_src_name, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_last_update_time, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_data_type, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_client_name, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_status, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_approve_type_name, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_last_approve_status, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_orgcode, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_repeat_id, SqlCmd.COL_TYPE_TEXT);
		cmd.col(col_name_repeat_str, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_hasObserved, SqlCmd.COL_TYPE_TEXT);
        return cmd.createTable();
    }

    public static SqlCmd insertInto(MatterInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_matter_info);
        cmd.col(col_name_id, info.getId());
        cmd.col(col_name_content, info.getContent());
        cmd.col(col_name_arrange_type,info.getArrangeType());
        cmd.col(col_name_people_num, info.getPeopleNum());
        cmd.col(col_name_start_time, info.getStartTime());
        cmd.col(col_name_end_time, info.getEndTime());
        cmd.col(col_name_secrecy, info.getSecrecy());
        cmd.col(col_name_creator_code, info.getCreatorCode());
        cmd.col(col_name_creator_name, info.getCreatorName());
        cmd.col(col_name_data_version, info.getDataVersion());
        cmd.col(col_name_create_time, info.getCreateTime());
        cmd.col(col_name_src_name, info.getSrcName());
        cmd.col(col_name_last_update_time, info.getLastUpdateTime());
        cmd.col(col_name_data_type, info.getDataType());
        cmd.col(col_name_client_name, info.getClientName());
        cmd.col(col_name_status, info.getStatus());
        cmd.col(col_name_approve_type_name, info.getApproveTypeName());
        cmd.col(col_name_last_approve_status, info.getLastApproveStatus());
		cmd.col(col_name_orgcode, info.getOrgcode());
		cmd.col(col_name_repeat_id, info.getRepeatId());
        cmd.col(col_name_repeat_str, info.getRepeatStr());
        cmd.col(col_name_hasObserved, info.getHasObserved());
        return cmd.insertInto();
    }

    public static SqlCmd update(MatterInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_matter_info);
        cmd.col(col_name_id, info.getId());
        cmd.col(col_name_content, info.getContent());
        cmd.col(col_name_arrange_type,info.getArrangeType());
        cmd.col(col_name_people_num, info.getPeopleNum());
        cmd.col(col_name_start_time, info.getStartTime());
        cmd.col(col_name_end_time, info.getEndTime());
        cmd.col(col_name_secrecy, info.getSecrecy());
        cmd.col(col_name_creator_code, info.getCreatorCode());
        cmd.col(col_name_creator_name, info.getCreatorName());
        cmd.col(col_name_data_version, info.getDataVersion());
        cmd.col(col_name_create_time, info.getCreateTime());
        cmd.col(col_name_src_name, info.getSrcName());
        cmd.col(col_name_last_update_time, info.getLastUpdateTime());
        cmd.col(col_name_data_type, info.getDataType());
        cmd.col(col_name_client_name, info.getClientName());
        cmd.col(col_name_status, info.getStatus());
        cmd.col(col_name_approve_type_name, info.getApproveTypeName());
        cmd.col(col_name_last_approve_status, info.getLastApproveStatus());
		cmd.col(col_name_orgcode, info.getOrgcode());
		cmd.col(col_name_repeat_id, info.getRepeatId());
        cmd.col(col_name_repeat_str, info.getRepeatStr());
        cmd.col(col_name_hasObserved, info.getHasObserved());
        return cmd.update().where("%s = ?", col_name_id).ps(info.getId());
    }

    public static SqlCmd showAll() {
        SqlCmd cmd = new SqlCmd(tb_name_matter_info);
        return cmd.select("*").orderBy("%s %s", col_name_content, "ASC");
    }

    public static SqlCmd findById(MatterInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_matter_info);
        return cmd.select("*").where("%s = ?", col_name_id).ps(info.getId());
    }

    public static MatterInfo findById(long id)
    {
        SqlCmd cmd = new SqlCmd(tb_name_matter_info);
        SqlCmd sqlCmd = cmd.select("*");
       //SqlCmd sqlCmd = cmd.select("*").where("%s = ?", col_name_id).ps(id);
        ContentValues value = ConfigDatabase.instance().firstRow(sqlCmd);
        return fromRow(value);
    }



    public static SqlCmd exist(MatterInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_matter_info);
        return cmd.hasRow("%s = ?", col_name_id).ps(info.getId());
    }

    public static MatterInfo fromRow(ContentValues row) {
        MatterInfo info = new MatterInfo();
        info.setId(row.getAsString(col_name_id));
        info.setContent(row.getAsString(col_name_content));
        info.setArrangeType(row.getAsString(col_name_arrange_type));
        info.setPeopleNum(row.getAsLong(col_name_people_num));
        info.setStartTime(SqlCmd.dateOfCol(row.getAsLong(col_name_start_time)));
        info.setEndTime(SqlCmd.dateOfCol(row.getAsLong(col_name_end_time)));
        info.setSecrecy(row.getAsString(col_name_secrecy));
        info.setCreatorCode(row.getAsString(col_name_creator_code));
        info.setCreatorName(row.getAsString(col_name_creator_name));
        info.setDataVersion(row.getAsString(col_name_data_version));
        info.setCreateTime(SqlCmd.dateOfCol(row.getAsLong(col_name_create_time)));
        info.setSrcName(row.getAsString(col_name_src_name));
        info.setLastUpdateTime(SqlCmd.dateOfCol(row.getAsLong(col_name_last_update_time)));
        info.setDataType(row.getAsString(col_name_data_type));
        info.setClientName(row.getAsString(col_name_client_name));
        info.setStatus(row.getAsString(col_name_status));
        info.setApproveTypeName(row.getAsString(col_name_approve_type_name));
        info.setLastApproveStatus(row.getAsString(col_name_last_approve_status));
		info.setOrgcode(row.getAsString(col_name_orgcode));
		info.setRepeatId(row.getAsString(col_name_repeat_id));
        info.setRepeatStr(row.getAsString(col_name_repeat_str));
        info.setHasObserved(row.getAsString(col_name_hasObserved));
        return info;
    }
}
