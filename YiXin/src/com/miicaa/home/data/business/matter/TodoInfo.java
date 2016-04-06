package com.miicaa.home.data.business.matter;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;

import com.miicaa.home.data.storage.CacheDatabase;
import com.miicaa.home.data.storage.LocalDatabase;
import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by apple on 13-12-31.
 */
public class TodoInfo {
	private static LocalDatabase db;
	private String id;
	private String dataId;
	private Date createTime;
	private Date finishTime;
	private String status;
	private Date planTime;
	private long star;
	private String remindStr;
    private String operateGroup;

	public static void init() {
		db = CacheDatabase.instance();

		if (!db.isTableExsit(TodoInfoSql.tb_name_todo_info)) {
			db.execCmd(TodoInfoSql.createTable());

			initTest();
		}
	}

    public static void initTest() {
        showAll();
    }

	public static void showAll() {
		ArrayList<ContentValues> rows = db.queryCmd(TodoInfoSql.showAll());
		SqlCmd.showRows(rows);
	}

	public static ArrayList<ContentValues> todoList(String time) {
		return db.queryCmd(TodoInfoSql.todoInMatter(time));
	}

	public static ArrayList<ContentValues> todoneList(String time) {
		return db.queryCmd(TodoInfoSql.todoneInMatter(time));
	}

	public static ArrayList<ContentValues> toOverList() {
		return db.queryCmd(TodoInfoSql.toOverInMatter());
	}

    public static TodoInfo findByDataId(String dataId)
    {
        SqlCmd cmd = new SqlCmd(TodoInfoSql.tb_name_todo_info);
        SqlCmd sqlCmd = cmd.select("*").where("%s = ?", TodoInfoSql.col_name_data_id).ps(dataId);
        ArrayList<ContentValues> rows = db.queryCmd(sqlCmd);
        if (rows != null && rows.size() > 0)
        {
            return TodoInfoSql.fromRow(rows.get(0));
        }
        return null;
    }

    public TodoInfo(String id, String dataId, long createTime,
                    long finishTime, String status, long planTime,
                    long star, String remindStr, String operateGroup) {
        this.id = id;
        this.dataId = dataId;
        this.createTime = SqlCmd.dateOfCol(createTime);
        this.finishTime = SqlCmd.dateOfCol(finishTime);
        this.status = status;
        this.planTime = SqlCmd.dateOfCol(planTime);
        this.star = star;
        this.remindStr = remindStr;
        this.operateGroup = operateGroup;
    }
	public static ArrayList<ContentValues> toNoticeList() {
		return db.queryCmd(TodoInfoSql.toNoticeMatter());
	}

	public TodoInfo() {
	}

	public TodoInfo(String id, String dataId, long createTime,
                    long finishTime, String status, long planTime,
                    long star, String remindStr) {
		this.id = id;
		this.dataId = dataId;
		this.createTime = SqlCmd.dateOfCol(createTime);
		this.finishTime = SqlCmd.dateOfCol(finishTime);
		this.status = status;
		this.planTime = SqlCmd.dateOfCol(planTime);
		this.star = star;
		this.remindStr = remindStr;
	}

	public Boolean exist() {
		return db.hasRow(TodoInfoSql.exist(this));
	}

	public Boolean save() {
		if (exist()) {
			return db.execCmd(TodoInfoSql.update(this));
		} else {
			return db.execCmd(TodoInfoSql.insertInto(this));
		}
	}

	public static LocalDatabase getDb() {
		return db;
	}

	public static void setDb(LocalDatabase db) {
		TodoInfo.db = db;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	public long getStar() {
		return star;
	}

	public void setStar(long star) {
		this.star = star;
	}

	public String getRemindStr() {
		return remindStr;
	}

	public void setRemindStr(String remindStr) {
		this.remindStr = remindStr;
	}

    public String getOperateGroup()
    {
        return operateGroup;
    }

    public void setOperateGroup(String operateGroup)
    {
        this.operateGroup = operateGroup;
    }
}
