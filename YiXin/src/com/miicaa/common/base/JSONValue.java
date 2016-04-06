package com.miicaa.common.base;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 获取JSON对象属性值的工具
 * <p/>
 * Created by yayowd on 14-1-6.
 */
public class JSONValue {
	private JSONObject jo = null;

	public static JSONValue from(JSONObject jo) {
		return new JSONValue(jo);
	}

	protected JSONValue(JSONObject jo) {
		this.jo = jo;
	}

	public boolean hasValue() {
		return (jo != null);
	}

	public boolean getBool(String name) {
		if (jo != null) {
			return jo.optBoolean(name);
		}
		return false;
	}

	public int getInt(String name) {
		if (jo != null) {
			return jo.optInt(name);
		}
		return 0;
	}

	public long getLong(String name) {
		if (jo != null) {
			return jo.optLong(name);
		}
		return 0;
	}

	public Date getDate(String name) {
		return new Date(getLong(name));
	}

	public double getDouble(String name) {
		if (jo != null) {
			return jo.optDouble(name);
		}
		return 0;
	}

	public String getString(String name) {
		if (jo != null) {
			if (jo.has(name) && !jo.isNull(name)) {
				return jo.optString(name);
			}
		}
		return "";
	}

	public JSONValue getJsonValue(String name) {
		if (jo != null) {
			return JSONValue.from(jo.optJSONObject(name));
		}
		return JSONValue.from(null);
	}

	public JSONArray getJsonArray(String name) {
		if (jo != null) {
			return jo.optJSONArray(name);
		}
		return null;
	}
}
