package com.miicaa.home.data.business.org;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;

import com.miicaa.common.base.DateHelper;
import com.miicaa.common.base.Debugger;
import com.miicaa.common.base.OnEachRow;
import com.miicaa.home.data.DataCenter;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.storage.ConfigDatabase;
import com.miicaa.home.data.storage.LocalDatabase;
import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by Administrator on 13-12-20.
 */
public class OrgInfo {
	private static LocalDatabase db;

	public static void init() {
		db = ConfigDatabase.instance();

		if (!db.isTableExsit(OrgInfoSql.tb_name_org_info)) {
			db.execCmd(OrgInfoSql.createTable());

			if (DataCenter.INIT_TEST) {
			}
		}
	}


	public static void showAll() {
		ArrayList<ContentValues> rows = db.queryCmd(OrgInfoSql.showAll());
		SqlCmd.showRows(rows);
	}

	public static OrgInfo findByCode(String code) {
		ContentValues row = db.firstRow(OrgInfoSql.findByCode(code));
		return OrgInfoSql.fromRow(row);
	}

	public OrgInfo() {
	}

	private OrgInfo(long id,
					String code,
					String name,
					long sort,
					long status,
					String desc) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.sort = sort;
		this.status = status;
		this.desc = desc;
		this.createTime = DateHelper.getDate();
	}

	public OrgInfo(long id,
				   String code,
				   String name,
				   long sort,
				   long status,
				   String desc,
				   long createTime) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.sort = sort;
		this.status = status;
		this.desc = desc;
		this.createTime = SqlCmd.dateOfCol(createTime);
	}

	public Boolean exist() {
		return db.hasRow(OrgInfoSql.exist(this));
	}

	public Boolean isCurrent() {
		OrgInfo curOrg = AccountInfo.instance().getLastOrgInfo();
		return (curOrg != null && curOrg.getCode().equals(code));
	}

	public Boolean save() {
		if (exist()) {
			Debugger.d("OrgInfo Update", toString(false));
			return db.execCmd(OrgInfoSql.update(this));
		} else {
			Debugger.d("OrgInfo Insert", toString(false));
			return db.execCmd(OrgInfoSql.insertInto(this));
		}
	}
	
	public Boolean delete(){
		/*
		 * 删除公司
		 */
		return db.execCmd(OrgInfoSql.delete(this));
	}
	
	public Boolean deleteAll(){
		/*
		 * 删除所有公司
		 */
		return db.execCmd(OrgInfoSql.deleteAll());
	}

	private void findChildren(ArrayList<ContentValues> rows,
							  String colPCode,
							  OrgTreeElement parent,
							  OnEachElement onEachElement) {
		for (ContentValues row : rows) {
			String pcode = row.getAsString(colPCode);
			if (pcode.equals(parent.getCode())) {
				OrgTreeElement child = (OrgTreeElement) onEachElement.eachElement(row);
				parent.appendChild(child);

				findChildren(rows, colPCode, child, onEachElement);

				onEachElement.afterAddToParent(child);
			}
		}
	}

	public void createUnitTree() {
		unitTree = new OrgTreeElement(String.valueOf(AccountInfo.instance().getLastOrgInfo().getId()),"null", 0);
		unitTree.setDataOrg(this);

		ArrayList<ContentValues> units = UnitInfo.unitsInOrg(this);
		findChildren(units, UnitInfoSql.col_name_parent_code, unitTree, new OnEachElement() {
			@Override
			public TreeElement eachElement(ContentValues row) {
				UnitInfo unitInfo = UnitInfoSql.fromRow(row);
				OrgTreeElement unit = new OrgTreeElement();
				unit.setCode(unitInfo.getCode());
				unit.setDataUnit(unitInfo);
				return unit;
			}

			@Override
			public void afterAddToParent(TreeElement node) {
				OrgTreeElement unit = (OrgTreeElement) node;
				UserInfo.usersInUnit(unit.getDataUnit(), new OnEachRow() {
					@Override
					public void eachRow(ContentValues row, Object cbData) {
						UserInfo userInfo = UserInfoSql.fromRow(row);
						OrgTreeElement user = new OrgTreeElement();
						user.setCode(userInfo.getCode());
						user.setDataUser(userInfo);
						((OrgTreeElement) cbData).appendChild(user);
					}
				}, unit);
			}
		});
	}

	public void createGroupTree() {
		grupTree = new OrgTreeElement(String.valueOf(AccountInfo.instance().getLastOrgInfo().getId()), "null", 0);
		grupTree.setDataOrg(this);
		ArrayList<ContentValues> groups = GroupInfo.groupsInOrg(this);
		findChildren(groups, GroupInfoSql.col_name_parent_code, grupTree, new OnEachElement() {
			@Override
			public TreeElement eachElement(ContentValues row) {
				GroupInfo groupInfo = GroupInfoSql.fromRow(row);
				OrgTreeElement group = new OrgTreeElement();
				group.setCode(groupInfo.getCode());
				group.setDataGroup(groupInfo);
				return group;
			}

			@Override
			public void afterAddToParent(TreeElement node) {
				OrgTreeElement group = (OrgTreeElement) node;
				UserInfo.usersInGroup(group.getDataGroup(), new OnEachRow() {
					@Override
					public void eachRow(ContentValues row, Object cbData) {
						UserInfo userInfo = UserInfoSql.fromRow(row);
						OrgTreeElement user = new OrgTreeElement();
						user.setCode(userInfo.getCode());
						user.setDataUser(userInfo);
						((OrgTreeElement) cbData).appendChild(user);
					}
				}, group);
			}
		});
	}

	public String toString(boolean isFull) {
		if (isFull) {
			return String.format("[%d][%s][%s][%d][%d][%s][%s]",
					id,
					code,
					name,
					sort,
					status,
					desc,
					createTime.toString());
		} else {
			return String.format("[%d][%s][%s]",
					id,
					code,
					name);
		}
	}

	private long id;
	private String code;
	private String name;
	private long sort;
	private long status;
	private String desc;
	private Date createTime;
	private OrgTreeElement unitTree = null;
	private OrgTreeElement grupTree = null;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public OrgTreeElement getUnitTree() {
		if (unitTree == null) {
			createUnitTree();
		}

		return unitTree;
	}

	public OrgTreeElement getGrupTree() {
		if (grupTree == null) {
			createGroupTree();
		}

		return grupTree;
	}
}
