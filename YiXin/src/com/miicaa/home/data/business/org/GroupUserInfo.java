package com.miicaa.home.data.business.org;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.ContentValues;

import com.miicaa.common.base.Debugger;
import com.miicaa.common.base.JSONValue;
import com.miicaa.common.base.OnEachRow;
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
public class GroupUserInfo {
	private static LocalDatabase db;

	public static void init() {
		db = ConfigDatabase.instance();

		if (!db.isTableExsit(GroupUserInfoSql.tb_name_group_user_info)) {
			db.execCmd(GroupUserInfoSql.createTable());

			if (DataCenter.INIT_TEST) {
				initTest();
			}
		}
	}

	public static void initTest() {
		new GroupUserInfo(1111, "", "g1", "u1", 1).save();
		new GroupUserInfo(2222, "", "g2", "u2", 2).save();
		new GroupUserInfo(3333, "", "g2", "u3", 3).save();
		new GroupUserInfo(4444, "", "g3", "u4", 4).save();
		new GroupUserInfo(5555, "", "g3", "u5", 5).save();

		showAll();
	}

	public static void showAll() {
		ArrayList<ContentValues> rows = db.queryCmd(GroupUserInfoSql.showAll());
		SqlCmd.showRows(rows);
	}

	public static void cacheAll() {
		OrgRequest.requestGroupUsers(new OnBusinessResponse() {
			@Override
			public void onProgress(ProgressMessage msg) {

			}

			@Override
			public void onResponse(ResponseData data) {
				if (data.getResultState() == ResponseData.ResultState.eSuccess) {
					final JSONArray groupUserList = data.getJsonArray();
					db.onTransaction(new OnTransaction() {
						@Override
						public void transaction() {
							for (int i = 0; i < groupUserList.length(); i++) {
								JSONValue groupUser = JSONValue.from(groupUserList.optJSONObject(i));
								if (groupUser.hasValue()) {
									new GroupUserInfo(
											groupUser.getLong(GroupUserInfoSql.col_name_id),
											groupUser.getString(GroupUserInfoSql.col_name_unit_code),
											groupUser.getString(GroupUserInfoSql.col_name_group_code),
											groupUser.getString(GroupUserInfoSql.col_name_user_code),
											groupUser.getLong(GroupUserInfoSql.col_name_sort)).save();
								}
							}
						}
					});
				}
			}
		}, AccountInfo.instance().getLastOrgInfo());
	}

	public static void delete(UserInfo info) {
		db.execCmd(GroupUserInfoSql.delete(info));
	}

    public static void deleteByUserCode(String userCode) {
        db.execCmd(GroupUserInfoSql.deleteByUnitCode(userCode));
    }

	public static ArrayList<GroupInfo> groupsForUser(UserInfo info) {
		ArrayList<GroupInfo> groups = new ArrayList<GroupInfo>();
		db.queryCmd(GroupUserInfoSql.groupsForUser(info), new OnEachRow() {
			@Override
			public void eachRow(ContentValues row, Object cbData) {
				GroupInfo group = GroupInfoSql.fromRow(row);
				((ArrayList<GroupInfo>) cbData).add(group);
			}
		}, groups);
		return groups;
	}

    public static GroupUserInfo findById(Long id) {
        ContentValues row = db.firstRow(GroupUserInfoSql.findById(id));
        return GroupUserInfoSql.fromRow(row);
    }

    public static ArrayList<GroupUserInfo> findGroupUser(String unitCode)
    {
        ArrayList<GroupUserInfo> units = new ArrayList<GroupUserInfo>();
        db.queryCmd(GroupUserInfoSql.findGroupUser(unitCode), new OnEachRow() {
            @Override
            public void eachRow(ContentValues row, Object cbData) {
                GroupUserInfo unit = GroupUserInfoSql.fromRow(row);
                ((ArrayList<GroupUserInfo>) cbData).add(unit);
            }
        }, units);
        return units;
    }

    public static void deleteGroupUser(String groupCode)
    {
       db.queryCmd(GroupUserInfoSql.deleteGroupUser(groupCode));
    }

	public GroupUserInfo() {
	}

	public GroupUserInfo(long id,
						 String unitCode,
						 String groupCode,
						 String userCode,
						 long sort) {
		this.id = id;
		this.unitCode = unitCode;
		this.groupCode = groupCode;
		this.userCode = userCode;
		this.sort = sort;
	}

	public Boolean exist() {
		return db.hasRow(GroupUserInfoSql.exist(this));
	}

	public Boolean save() {
		if (exist()) {
			Debugger.d("GroupUserInfo Update", toString(false));
			return db.execCmd(GroupUserInfoSql.update(this));
		} else {
			Debugger.d("GroupUserInfo Insert", toString(false));
			return db.execCmd(GroupUserInfoSql.insertInto(this));
		}
	}

	public void delete() {
		Debugger.d("GroupUserInfo Delete", toString(false));
		db.execCmd(GroupUserInfoSql.delete(this));
	}

	public String toString(boolean isFull) {
		if (isFull) {
			return String.format("[%d][%s][%s][%s][%d]",
					id,
					unitCode,
					groupCode,
					userCode,
					sort);
		} else {
			return String.format("[%d][%s][%s]",
					id,
					groupCode,
					userCode);
		}
	}

	private long id;
	private String unitCode;
	private String groupCode;
	private String userCode;
	private long sort;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}
}
