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
public class UnitUserInfo {
	private static LocalDatabase db;

	public static void init() {
		db = ConfigDatabase.instance();

		if (!db.isTableExsit(UnitUserInfoSql.tb_name_unit_user_info)) {
			db.execCmd(UnitUserInfoSql.createTable());

			if (DataCenter.INIT_TEST) {
				initTest();
			}
		}
	}

	public static void initTest() {
		new UnitUserInfo(1111, "u1", "u1", 1, 1).save();
		new UnitUserInfo(2222, "u2", "u2", 2, 2).save();
		new UnitUserInfo(3333, "u2", "u3", 3, 3).save();
		new UnitUserInfo(4444, "u3", "u4", 4, 4).save();
		new UnitUserInfo(5555, "u3", "u5", 5, 5).save();

		showAll();
	}

	public static void showAll() {
		ArrayList<ContentValues> rows = db.queryCmd(UnitUserInfoSql.showAll());
		SqlCmd.showRows(rows);
	}

	public static void cacheAll() {
		OrgRequest.requestUnitUsers(new OnBusinessResponse() {
			@Override
			public void onProgress(ProgressMessage msg) {

			}

			@Override
			public void onResponse(ResponseData data) {
				if (data.getResultState() == ResponseData.ResultState.eSuccess) {
					final JSONArray unitUserList = data.getJsonArray();
					db.onTransaction(new OnTransaction() {
						@Override
						public void transaction() {
							for (int i = 0; i < unitUserList.length(); i++) {
								JSONValue unitUser = JSONValue.from(unitUserList.optJSONObject(i));
								if (unitUser.hasValue()) {
									new UnitUserInfo(
											unitUser.getLong(UnitUserInfoSql.col_name_id),
											unitUser.getString(UnitUserInfoSql.col_name_unit_code),
											unitUser.getString(UnitUserInfoSql.col_name_user_code),
											unitUser.getLong(UnitUserInfoSql.col_name_sort),
											0).save();
								}
							}
						}
					});
				}
			}
		}, AccountInfo.instance().getLastOrgInfo());
	}

	public static void delete(UserInfo info) {
		db.execCmd(UnitUserInfoSql.delete(info));
	}

    public static void deleteByUnitCode(String unitCode) {
        db.execCmd(UnitUserInfoSql.deleteByUnitCode(unitCode));
    }

    public static void deleteByUserCode(String userCode) {
        db.execCmd(UnitUserInfoSql.deleteByUnitCode(userCode));
    }

	public static ArrayList<UnitInfo> unitsForUser(UserInfo info) {
		ArrayList<UnitInfo> units = new ArrayList<UnitInfo>();
		db.queryCmd(UnitUserInfoSql.unitsForUser(info), new OnEachRow() {
			@Override
			public void eachRow(ContentValues row, Object cbData) {
				UnitInfo unit = UnitInfoSql.fromRow(row);
				((ArrayList<UnitInfo>) cbData).add(unit);
			}
		}, units);
		return units;
	}

	public UnitUserInfo() {
	}

	public UnitUserInfo(long id,
						String unitCode,
						String userCode,
						long sort,
						long status) {
		this.id = id;
		this.unitCode = unitCode;
		this.userCode = userCode;
		this.sort = sort;
		this.status = status;
	}

	public Boolean exist() {
		return db.hasRow(UnitUserInfoSql.exist(this));
	}

	public Boolean save() {
		if (exist()) {
			Debugger.d("UnitUserInfo Update", toString(false));
			return db.execCmd(UnitUserInfoSql.update(this));
		} else {
			Debugger.d("UnitUserInfo Insert", toString(false));
			return db.execCmd(UnitUserInfoSql.insertInto(this));
		}
	}

	public void delete() {
		Debugger.d("UnitUserInfo Delete", toString(false));
		db.execCmd(UnitUserInfoSql.delete(this));
	}

    public static ArrayList<UnitUserInfo> findOnlyFormUnit(String unitCode)
    {
        ArrayList<UnitUserInfo> units = new ArrayList<UnitUserInfo>();
        db.queryCmd(UnitUserInfoSql.findOnlyFormUnit(unitCode), new OnEachRow() {
            @Override
            public void eachRow(ContentValues row, Object cbData) {
                UnitUserInfo unit = UnitUserInfoSql.fromRow(row);
                ((ArrayList<UnitUserInfo>) cbData).add(unit);
            }
        }, units);
        return units;
    }

    public static ArrayList<UnitUserInfo> findOnlyMulUnit(String unitCode)
    {
        ArrayList<UnitUserInfo> units = new ArrayList<UnitUserInfo>();
        db.queryCmd(UnitUserInfoSql.findOnlyMulUnit(unitCode), new OnEachRow() {
            @Override
            public void eachRow(ContentValues row, Object cbData) {
                UnitUserInfo unit = UnitUserInfoSql.fromRow(row);
                ((ArrayList<UnitUserInfo>) cbData).add(unit);
            }
        }, units);
        return units;
    }

	public String toString(boolean isFull) {
		if (isFull) {
			return String.format("[%d][%s][%s][%d][%d]",
					id,
					unitCode,
					userCode,
					sort,
					status);
		} else {
			return String.format("[%d][%s][%s]",
					id,
					unitCode,
					userCode);
		}
	}

	private long id;
	private String unitCode;
	private String userCode;
	private long sort;
	private long status;

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

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}
}
