package com.miicaa.home.data.business.org;

import java.util.ArrayList;

/**
 * Created by Administrator on 13-12-30.
 */
public class OrgTreeElement extends TreeElement {
	public enum Type {ORG, UNIT, GROUP, USER}

	private Type type;
	private OrgInfo dataOrg = null;
	private UnitInfo dataUnit = null;
	private GroupInfo dataGroup = null;
	private UserInfo dataUser = null;
	private ArrayList<OrgTreeElement> children;

	public boolean hasChild() {
		return children.size() > 0;
	}

	public void appendChild(OrgTreeElement child) {
		if (child != null) {
			child.setId(String.format("%s/%d", getId(), children.size()));
			child.setLevel(getLevel() + 1);
			children.add(child);
		}
	}

	public ArrayList<OrgTreeElement> getChildren() {
		return children;
	}

	public OrgTreeElement() {
		this("", "", 0);
	}

	public OrgTreeElement(String id, String code, int level) {
		super(id, code, level);

		children = new ArrayList<OrgTreeElement>();
	}

	public String getName() {
		switch (type) {
			case ORG: return dataOrg.getName();
			case UNIT: return dataUnit.getName();
			case GROUP: return dataGroup.getName();
			case USER: return dataUser.getName();
			default: return "";
		}
	}

	public Type getType() {
		return type;
	}

	public OrgInfo getDataOrg() {
		return dataOrg;
	}

	public void setDataOrg(OrgInfo dataOrg) {
		this.type = Type.ORG;
		this.dataOrg = dataOrg;
	}

	public UnitInfo getDataUnit() {
		return dataUnit;
	}

	public void setDataUnit(UnitInfo dataUnit) {
		this.type = Type.UNIT;
		this.dataUnit = dataUnit;
	}

	public GroupInfo getDataGroup() {
		return dataGroup;
	}

	public void setDataGroup(GroupInfo dataGroup) {
		this.type = Type.GROUP;
		this.dataGroup = dataGroup;
	}

	public UserInfo getDataUser() {
		return dataUser;
	}

	public void setDataUser(UserInfo dataUser) {
		this.type = Type.USER;
		this.dataUser = dataUser;
	}
}
