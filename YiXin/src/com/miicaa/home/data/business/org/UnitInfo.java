package com.miicaa.home.data.business.org;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;

import android.content.ContentValues;

import com.miicaa.common.base.DateHelper;
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
public class UnitInfo {
	private static LocalDatabase db;

	public static void init() {
		db = ConfigDatabase.instance();

		if (!db.isTableExsit(UnitInfoSql.tb_name_unit_info)) {
			db.execCmd(UnitInfoSql.createTable());

			if (DataCenter.INIT_TEST) {
				initTest();
			}
		}
	}

	public static void initTest() {
		new UnitInfo(1111, "u1", "rrrr", "haha rr", "O=ynyx", "", 1, 1).save();
		new UnitInfo(2222, "u2", "developer", "developer", "O=ynyx", "u1", 2, 2).save();
		new UnitInfo(3333, "u3", "customer servicce", "customer servicce", "O=ynyx", "u1", 3, 3).save();
		new UnitInfo(4444, "u4", "user servicce", "user servicce", "O=ynyx", "u1", 4, 4).save();
		new UnitInfo(5555, "u5", "mobile", "mobile", "O=ynyx", "u2", 5, 5).save();
		new UnitInfo(6666, "u6", "Implement", "Implement", "O=ynyx", "u3", 6, 6).save();

		showAll();
	}

	public static void showAll() {
		ArrayList<ContentValues> rows = db.queryCmd(UnitInfoSql.showAll());
		SqlCmd.showRows(rows);
	}

	public static void cacheAll() {
		OrgRequest.requestUnits(new OnBusinessResponse() {
			@Override
			public void onProgress(ProgressMessage msg) {

			}

			@Override
			public void onResponse(ResponseData data) {
				if (data.getResultState() == ResponseData.ResultState.eSuccess) {
					final JSONArray unitList = data.getJsonArray();
					db.onTransaction(new OnTransaction() {
						@Override
						public void transaction() {
							for (int i = 0; i < unitList.length(); i++) {
								JSONValue unit = JSONValue.from(unitList.optJSONObject(i));
								if (unit.hasValue()) {
									new UnitInfo(
											unit.getLong(UnitInfoSql.col_name_id),
											unit.getString(UnitInfoSql.col_name_code),
											unit.getString(UnitInfoSql.col_name_name),
											unit.getString(UnitInfoSql.col_name_full_name),
											unit.getString(UnitInfoSql.col_name_org_code),
											unit.getString(UnitInfoSql.col_name_parent_code),
											unit.getLong(UnitInfoSql.col_name_status),
											unit.getLong(UnitInfoSql.col_name_sort),
											unit.getLong(UnitInfoSql.col_name_create_time)
									).save();
								}
							}
						}
					});
				}
			}
		}, AccountInfo.instance().getLastOrgInfo());
	}

	public static ArrayList<ContentValues> unitsInOrg(OrgInfo info) {
		return db.queryCmd(UnitInfoSql.unitsInOrg(info));
	}

	public UnitInfo() {
	}

	private UnitInfo(long id,
					 String code,
					 String name,
					 String fullName,
					 String orgCode,
					 String parentCode,
					 long status,
					 long sort) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.fullName = fullName;
		this.orgCode = orgCode;
		this.parentCode = parentCode;
		this.status = status;
		this.sort = sort;
		this.createTime = DateHelper.getDate();
	}

	public UnitInfo(long id,
					String code,
					String name,
					String fullName,
					String orgCode,
					String parentCode,
					long status,
					long sort,
					long createTime) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.fullName = fullName;
		this.orgCode = orgCode;
		this.parentCode = parentCode;
		this.status = status;
		this.sort = sort;
		this.createTime = SqlCmd.dateOfCol(createTime);
	}

	public Boolean exist() {
		return db.hasRow(UnitInfoSql.exist(this));
	}

	public Boolean save() {
		if (exist()) {
			Debugger.d("UnitInfo Update", toString(false));
			return db.execCmd(UnitInfoSql.update(this));
		} else {
			Debugger.d("UnitInfo Insert", toString(false));
			return db.execCmd(UnitInfoSql.insertInto(this));
		}
	}

    public Boolean updateChild(String oldName) {

        return db.execCmd(UnitInfoSql.updateChild(this.getCode(),oldName,this.getName()));
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

    public static UnitInfo findById(Long id)
    {
        ContentValues row = db.firstRow(UnitInfoSql.findById(id));
        return UnitInfoSql.fromRow(row);
    }

    public static ArrayList<UnitInfo> findByParentCode(String parentCode)
    {
        ArrayList<UnitInfo> units = new ArrayList<UnitInfo>();
        db.queryCmd(UnitInfoSql.findByParentCode(parentCode), new OnEachRow() {
            @Override
            public void eachRow(ContentValues row, Object cbData) {
                UnitInfo unit = UnitInfoSql.fromRow(row);
                ((ArrayList<UnitInfo>) cbData).add(unit);
            }
        }, units);
        return units;
    }

    public Boolean deleteFormDb()
    {
        String parts[] = getCode().split("/");
        String rootUnitCode = parts[parts.length -1];

        ArrayList<UnitUserInfo> onlyUsers = UnitUserInfo.findOnlyFormUnit(getCode());
        for(int i =0; i < onlyUsers.size(); i++)
        {
            UnitUserInfo info = onlyUsers.get(i);
            info.delete();
            info.setUnitCode(rootUnitCode);
            info.save();
        }
        ArrayList<UnitUserInfo> mulUnitusers = UnitUserInfo.findOnlyFormUnit(getCode());
        for(int i =0; i < mulUnitusers.size(); i++)
        {
            UnitUserInfo info = mulUnitusers.get(i);
            info.delete();
        }

        ArrayList<UnitInfo> chidren =  findByParentCode(getParentCode());
        for(int i =0; i < chidren.size(); i++)
        {
            UnitInfo info = chidren.get(i);
            info.deleteFormDb();
        }
        delete();

        return true;
    }

    private void delete() {
        db.execCmd(UnitInfoSql.delete(this));
    }

    public static Boolean deleteByOrgCode(String orgCode){
        Debugger.d("UnitInfo Delete", orgCode);
        return db.execCmd(UnitInfoSql.deleteByOrgCode(orgCode));
    }
}
