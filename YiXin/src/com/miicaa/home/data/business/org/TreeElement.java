package com.miicaa.home.data.business.org;

/**
 * Created by Administrator on 13-12-30.
 */
public class TreeElement {
	private String id;
	private String code;
	private int level;
	private boolean select;

	public TreeElement() {
		this("", "", 0);
	}

	public TreeElement(String id, String code, int level) {
		this.id = id;
		this.code = code;
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}
}
