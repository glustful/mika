package com.miicaa.home.data.business.org;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;

import android.content.ContentValues;

import com.miicaa.common.base.DateHelper;
import com.miicaa.common.base.Debugger;
import com.miicaa.common.base.JSONValue;
import com.miicaa.common.base.OnTransaction;
import com.miicaa.home.data.DataCenter;
import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.storage.ConfigDatabase;
import com.miicaa.home.data.storage.LocalDatabase;
import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by Administrator on 13-12-27.
 */
public class GroupInfo {
	private static LocalDatabase db;

	public static void init() {
		db = ConfigDatabase.instance();

		if (!db.isTableExsit(GroupInfoSql.tb_name_group_info)) {
			db.execCmd(GroupInfoSql.createTable());

			if (DataCenter.INIT_TEST) {
				initTest();
			}
		}
	}

	public static void initTest() {
		new GroupInfo(1111, "", "g1", "boss", "big boss", "O=ynyx", 1, 1).save();
		new GroupInfo(2222, "g1", "g2", "dept", "dept", "O=ynyx", 2, 2).save();
		new GroupInfo(3333, "g1", "g3", "customer", "customer", "O=ynyx", 3, 3).save();
		new GroupInfo(4444, "g1", "g4", "user", "user", "O=ynyx", 4, 4).save();
		new GroupInfo(5555, "g2", "g5", "group", "group", "O=ynyx", 5, 5).save();
		new GroupInfo(6666, "g3", "g6", "liner", "liner", "O=ynyx", 6, 6).save();

		showAll();
	}

	public static void showAll() {
		ArrayList<ContentValues> rows = db.queryCmd(GroupInfoSql.showAll());
		SqlCmd.showRows(rows);
	}

	public static void cacheAll() {
		OrgRequest.requestGroups(new OnBusinessResponse() {
			@Override
			public void onProgress(ProgressMessage msg) {

			}

			@Override
			public void onResponse(ResponseData data) {
				if (data.getResultState() == ResponseData.ResultState.eSuccess) {
					final JSONArray groupList = data.getJsonArray();
					db.onTransaction(new OnTransaction() {
						@Override
						public void transaction() {
							for (int i = 0; i < groupList.length(); i++) {
								JSONValue group = JSONValue.from(groupList.optJSONObject(i));
								if (group.hasValue()) {
									new GroupInfo(
											group.getLong(GroupInfoSql.col_name_id),
											group.getString(GroupInfoSql.col_name_parent_code),
											group.getString(GroupInfoSql.col_name_code),
											group.getString(GroupInfoSql.col_name_name),
											group.getString(GroupInfoSql.col_name_full_name),
											group.getString(GroupInfoSql.col_name_org_code),
											group.getLong(GroupInfoSql.col_name_status),
											group.getLong(GroupInfoSql.col_name_sort),
											group.getLong(GroupInfoSql.col_name_create_time)
									).save();
								}
							}
						}
					});
				}
			}
		}, AccountInfo.instance().getLastOrgInfo());
	}

	public static ArrayList<ContentValues> groupsInOrg(OrgInfo info) {
		return db.queryCmd(GroupInfoSql.groupsInOrg(info));
	}

	public static GroupInfo findByCode(String code) {
		return GroupInfoSql.fromRow(db.firstRow(GroupInfoSql.findByCode(code)));
	}

    public static GroupInfo findById(Long id)
    {
        ContentValues row = db.firstRow(GroupInfoSql.findById(id));
        return GroupInfoSql.fromRow(row);
    }

    public static void deleteByCode(String code)
    {
        db.queryCmd(GroupInfoSql.deleteByCode(code));
    }

    public static Boolean deleteByOrgCode(String orgCode){
        Debugger.d("GroupInfo Delete", orgCode);
        return db.execCmd(GroupInfoSql.deleteByOrgCode(orgCode));
    }

	public GroupInfo() {
	}

	private GroupInfo(long id,
					  String parentCode,
					  String code,
					  String name,
					  String fullName,
					  String orgCode,
					  long status,
					  long sort) {
		this.id = id;
		this.parentCode = parentCode;
		this.code = code;
		this.name = name;
		this.fullName = fullName;
		this.orgCode = orgCode;
		this.status = status;
		this.sort = sort;
		this.createTime = DateHelper.getDate();
	}

	public GroupInfo(long id,
					 String parentCode,
					 String code,
					 String name,
					 String fullName,
					 String orgCode,
					 long status,
					 long sort,
					 long createTime) {
		this.id = id;
		this.parentCode = parentCode;
		this.code = code;
		this.name = name;
		this.fullName = fullName;
		this.orgCode = orgCode;
		this.status = status;
		this.sort = sort;
		this.createTime = SqlCmd.dateOfCol(createTime);
	}

	public Boolean exist() {
		return db.hasRow(GroupInfoSql.exist(this));
	}

	public Boolean save() {
		if (exist()) {
			Debugger.d("GroupInfo Update", toString(false));
			return db.execCmd(GroupInfoSql.update(this));
		} else {
			Debugger.d("GroupInfo Insert", toString(false));
			return db.execCmd(GroupInfoSql.insertInto(this));
		}
	}

    public Boolean updateChild(String oldName) {

        return db.execCmd(GroupInfoSql.updateChild(this.getCode(),oldName,this.getName()));
    }

	public String toString(boolean isFull) {
		if (isFull) {
			return String.format("[%d][%s][%s][%s][%s][%s][%d][%d][%s]",
					id,
					parentCode,
					code,
					name,
					fullName,
					orgCode,
					status,
					sort,
					createTime.toString());
		} else {
			return String.format("[%d][%s][%s]",
					id,
					code,
					name);
		}
	}

	private long id;
	private String parentCode;
	private String code;
	private String name;
	private String fullName;
	private String orgCode;
	private long status;
	private long sort;
	private Date createTime;

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getId() {

		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
