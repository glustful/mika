package com.miicaa.base.share.city;

import java.io.Serializable;
import java.util.ArrayList;

public class Cityinfo implements Serializable {

	public Cityinfo() {
		super();
		id = "test";
		city_name = "test";
	}

	private String id;
	private String city_name;
	private ArrayList<Cityinfo> children;
	public ArrayList<Cityinfo> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Cityinfo> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	@Override
	public String toString() {
		return "Cityinfo [id=" + id + ", city_name=" + city_name + "]";
	}

}
