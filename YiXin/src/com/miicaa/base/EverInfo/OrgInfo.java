package com.miicaa.base.EverInfo;

import java.util.Date;

public class OrgInfo {

	private long id;
	private String code;
	private String name;
	private long sort;
	private long status;
	private String desc;
	private Date createTime;
	/**
	 *
	 * Program ������Ϊ��λ�Ͳ��ŵ���������ʱ��Ὺ��
	 */
//	private OrgTreeElement unitTree = null;
//	private OrgTreeElement grupTree = null;

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

//	public OrgTreeElement getUnitTree() {
//		if (unitTree == null) {
//			createUnitTree();
//		}
//
//		return unitTree;
//	}
//
//	public OrgTreeElement getGrupTree() {
//		if (grupTree == null) {
//			createGroupTree();
//		}
//
//		return grupTree;
//	}
}
