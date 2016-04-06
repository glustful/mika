package com.miicaa.home.data.business.matter;

import java.util.Date;

import android.content.ContentValues;

import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by apple on 13-12-31.
 */
public class TodoInfoSql {

    private long id;
    private long dataId;
    private Date createTime;
    private Date finishTime;
    private long status;
    private long planTime;
    private long star;
    private String remindStr;
    // 用户表名
    public static final String tb_name_todo_info = "t_todo_info";
    // 用户字段
    public static final String col_name_id = "id";
    public static final String col_name_data_id = "data_id";
    public static final String col_name_create_time = "createTime";
    public static final String col_name_finish_time = "finishTime";
    public static final String col_name_status = "status";
    public static final String col_name_plan_time = "planTime";
    public static final String col_name_star = "star";
    public static final String col_name_remind_str = "remindStr";
    public static final  String col_name_operateGroup = "operateGroup";


    public static SqlCmd createTable() {
        SqlCmd cmd = new SqlCmd(tb_name_todo_info);
        cmd.col(col_name_id, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_data_id, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_create_time, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_finish_time, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_status, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_plan_time, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_star, SqlCmd.COL_TYPE_INTEGER);
        cmd.col(col_name_remind_str, SqlCmd.COL_TYPE_TEXT);
        cmd.col(col_name_operateGroup, SqlCmd.COL_TYPE_TEXT);

        return cmd.createTable();
    }

    public static SqlCmd insertInto(TodoInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_todo_info);
        cmd.col(col_name_id, info.getId());
        cmd.col(col_name_data_id, info.getDataId());
        cmd.col(col_name_create_time, info.getCreateTime());
        cmd.col(col_name_finish_time, info.getFinishTime());
        cmd.col(col_name_status, info.getStatus());
        cmd.col(col_name_plan_time, info.getPlanTime());
        cmd.col(col_name_star, info.getStar());
        cmd.col(col_name_remind_str, info.getRemindStr());
        cmd.col(col_name_operateGroup, info.getOperateGroup());
        return cmd.insertInto();
    }

    public static SqlCmd update(TodoInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_todo_info);
        cmd.col(col_name_id, info.getId());
        cmd.col(col_name_data_id, info.getDataId());
        cmd.col(col_name_create_time, info.getCreateTime());
        cmd.col(col_name_finish_time, info.getFinishTime());
        cmd.col(col_name_status, info.getStatus());
        cmd.col(col_name_plan_time, info.getPlanTime());
        cmd.col(col_name_star, info.getStar());
        cmd.col(col_name_remind_str, info.getRemindStr());
        cmd.col(col_name_operateGroup, info.getOperateGroup());
        return cmd.update().where("%s = ?", col_name_id).ps(info.getId());
    }

    public static SqlCmd showAll() {
        SqlCmd cmd = new SqlCmd(tb_name_todo_info);
        return cmd.select("*").orderBy("%s %s", col_name_id, "ASC");
    }

    public static SqlCmd exist(TodoInfo info) {
        SqlCmd cmd = new SqlCmd(tb_name_todo_info);
        return cmd.hasRow("%s = ?", col_name_id).ps(info.getId());
    }

    public static SqlCmd todoInMatter(String time) {
        if(time.equals("all")){
            SqlCmd cmd = new SqlCmd(tb_name_todo_info);
            return cmd.select("B.*,A.planTime,A.remindStr,B.updateTime")
                    .join(" INNER JOIN %s AS B ON B.%s = A.%s",
                            MatterInfoSql.tb_name_matter_info,
                            MatterInfoSql.col_name_id,
                            col_name_data_id).where("A.%s = ?", col_name_status)
                    .ps("01").orderBy("B.%s %s", MatterInfoSql.col_name_last_update_time, "ASC");
        }else if(time.equals("today")){
            SqlCmd cmd = new SqlCmd(tb_name_todo_info);
            return cmd.select("B.*,A.planTime,A.remindStr")
                    .join(" INNER JOIN %s AS B ON B.%s = A.%s",
                            MatterInfoSql.tb_name_matter_info,
                            MatterInfoSql.col_name_id,
                            col_name_data_id).where("A.%s = ? and date(A.planTime,'unixepoch','localtime') BETWEEN date('now','-1 day') AND date('now','+1 day') ", col_name_status)
                    .ps("01").orderBy("%s %s", col_name_plan_time, "ASC");
        }else if(time.equals("tomorrow")){
            SqlCmd cmd = new SqlCmd(tb_name_todo_info);
            return cmd.select("B.*,A.planTime,A.remindStr")
                    .join(" INNER JOIN %s AS B ON B.%s = A.%s",
                            MatterInfoSql.tb_name_matter_info,
                            MatterInfoSql.col_name_id,
                            col_name_data_id).where("A.%s = ? and date(A.planTime,'unixepoch','localtime') BETWEEN date('now','+1 day') AND date('now','+2 day') ", col_name_status,col_name_plan_time)
                    .ps("01").orderBy("%s %s", col_name_plan_time, "ASC");
        }else if(time.equals("afterTomorrow")){
            SqlCmd cmd = new SqlCmd(tb_name_todo_info);
            return cmd.select("B.*,A.planTime,A.remindStr")
                    .join(" INNER JOIN %s AS B ON B.%s = A.%s",
                            MatterInfoSql.tb_name_matter_info,
                            MatterInfoSql.col_name_id,
                            col_name_data_id).where("A.%s = ?  and date(A.planTime,'unixepoch','localtime') > date('now','+2 day') ", col_name_status,col_name_plan_time)
                    .ps("01").orderBy("%s %s", col_name_plan_time, "ASC");
        }else{
            SqlCmd cmd = new SqlCmd(tb_name_todo_info);
            return cmd;
        }
    }
    public static SqlCmd todoneInMatter(String time) {
        if(time.equals("all")){
            SqlCmd cmd = new SqlCmd(tb_name_todo_info);
            return cmd.select("B.*,A.planTime,A.remindStr")
                    .join(" INNER JOIN %s AS B ON B.%s = A.%s",
                            MatterInfoSql.tb_name_matter_info,
                            MatterInfoSql.col_name_id,
                            col_name_data_id).where("A.%s = ?", col_name_status)
                    .ps("02").orderBy("%s %s", col_name_plan_time, "ASC");
        }else if(time.equals("today")){
            SqlCmd cmd = new SqlCmd(tb_name_todo_info);
            return cmd.select("B.*,A.planTime,A.remindStr")
                    .join(" INNER JOIN %s AS B ON B.%s = A.%s",
                            MatterInfoSql.tb_name_matter_info,
                            MatterInfoSql.col_name_id,
                            col_name_data_id).where("A.%s = ? and date(A.planTime,'unixepoch','localtime') BETWEEN date('now','-1 day') AND date('now','+1 day') ", col_name_status)
                    .ps("02").orderBy("%s %s", col_name_plan_time, "ASC");
        }else if(time.equals("tomorrow")){
            SqlCmd cmd = new SqlCmd(tb_name_todo_info);
            return cmd.select("B.*,A.planTime,A.remindStr")
                    .join(" INNER JOIN %s AS B ON B.%s = A.%s",
                            MatterInfoSql.tb_name_matter_info,
                            MatterInfoSql.col_name_id,
                            col_name_data_id).where("A.%s = ? and date(A.planTime,'unixepoch','localtime') BETWEEN date('now','+1 day') AND date('now','+2 day') ", col_name_status,col_name_plan_time)
                    .ps("02").orderBy("%s %s", col_name_plan_time, "ASC");
        }else if(time.equals("afterTomorrow")){
            SqlCmd cmd = new SqlCmd(tb_name_todo_info);
            return cmd.select("B.*,A.planTime,A.remindStr")
                    .join(" INNER JOIN %s AS B ON B.%s = A.%s",
                            MatterInfoSql.tb_name_matter_info,
                            MatterInfoSql.col_name_id,
                            col_name_data_id).where("A.%s = ?  and date(A.planTime,'unixepoch','localtime') > date('now','+2 day') ", col_name_status,col_name_plan_time)
                    .ps("02").orderBy("%s %s", col_name_plan_time, "ASC");
        }else{
            SqlCmd cmd = new SqlCmd(tb_name_todo_info);
            return cmd;
        }
    }
    public static SqlCmd toOverInMatter() {
        SqlCmd cmd = new SqlCmd(tb_name_todo_info);
        return cmd.select("B.*,A.planTime,A.remindStr")
                .join(" INNER JOIN %s AS B ON B.%s = A.%s",
                        MatterInfoSql.tb_name_matter_info,
                        MatterInfoSql.col_name_id,
                        col_name_data_id).where("A.%s == ? OR A.%s == ?", col_name_status,col_name_status)
                .ps("03","04").orderBy("%s %s", col_name_plan_time, "ASC");
    }
    public static SqlCmd toNoticeMatter() {
        SqlCmd cmd = new SqlCmd(tb_name_todo_info);
        return cmd.select("B.*,A.planTime,A.remindStr")
                .join(" INNER JOIN %s AS B ON B.%s = A.%s",
                        NoticeInfoSql.tb_name_notice_info,
                        NoticeInfoSql.col_name_id,
                        col_name_data_id).orderBy("%s %s", col_name_plan_time, "ASC");
    }
    public static TodoInfo fromRow(ContentValues row) {
        TodoInfo info = new TodoInfo();
        info.setId(row.getAsString(col_name_id));
        info.setDataId(row.getAsString(col_name_data_id));
        info.setCreateTime(SqlCmd.dateOfCol(row.getAsLong(col_name_create_time)));
        info.setFinishTime(SqlCmd.dateOfCol(row.getAsLong(col_name_finish_time)));
        info.setStatus(row.getAsString(col_name_status));
        info.setPlanTime(SqlCmd.dateOfCol(row.getAsLong(col_name_plan_time)));
        info.setStar(row.getAsLong(col_name_star));
        info.setRemindStr(row.getAsString(col_name_remind_str));
        info.setOperateGroup(row.getAsString(col_name_operateGroup));
        return info;
    }
}
