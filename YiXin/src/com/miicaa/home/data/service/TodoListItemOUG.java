package com.miicaa.home.data.service;

import org.json.JSONArray;
import org.json.JSONObject;

import com.miicaa.common.base.JSONValue;
import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.business.org.GroupUserInfo;
import com.miicaa.home.data.business.org.UnitUserInfo;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-12-16.
 */
public class TodoListItemOUG extends TodoListItem {
	public TodoListItemOUG(JSONValue item) {
		super(item);
	}

	@Override
	protected void exec() {
		CacheRequest.execUndo(new OnBusinessResponse() {
			@Override
			public void onProgress(ProgressMessage msg) {

			}

			@Override
			public void onResponse(ResponseData data) {
				if (data.getResultState() == ResponseData.ResultState.eSuccess) {
					JSONObject d = data.getJsonObject();

					JSONArray users = d.optJSONArray("users");
					execUsers(users);

					JSONArray groupUser = d.optJSONArray("groupUser");
					execGroupUser(groupUser);

					JSONArray unitUser = d.optJSONArray("unitUser");
					execUnitUser(unitUser);
				}
			}
		}, this);
	}

	private void execUsers(JSONArray users) {
        if(users == null)
        {
            return;
        }
		for (int i = 0; i < users.length(); i++) {
			JSONValue item = JSONValue.from(users.optJSONObject(i));
			if (item.hasValue()) {
				UserInfo userInfo = new UserInfo(
						item.getLong("id"),
						item.getString("code"),
						item.getString("name"),
						item.getString("email"),
						item.getString("cellphone"),
						item.getLong("status"),
						item.getString("avatar"),
						item.getLong("createTime"),
						item.getString("orgCode"),
						item.getString("gender"),
						item.getLong("birthday"),
						item.getString("qq"),
						item.getString("phone"),
						item.getString("addr"));
				if (getOperType().equals(OPER_TYPE_ADD)) {
					userInfo.save();
				} else if (getOperType().equals(OPER_TYPE_DELETE)) {
					userInfo.delete();
				}
			}
		}
	}

	private void execGroupUser(JSONArray groupUser) {
        if(groupUser == null)
        {
            return;
        }
		for (int i = 0; i < groupUser.length(); i++) {
			JSONValue item = JSONValue.from(groupUser.optJSONObject(i));
			if (item.hasValue()) {
				GroupUserInfo groupUserInfo = new GroupUserInfo(
						item.getLong("id"),
						item.getString("unitCode"),
						item.getString("groupCode"),
						item.getString("userCode"),
						item.getLong("sort"));
				if (getOperType().equals(OPER_TYPE_ADD)) {
					groupUserInfo.save();
				} else if (getOperType().equals(OPER_TYPE_DELETE)) {
					groupUserInfo.delete();
				}
			}
		}
	}

	private void execUnitUser(JSONArray unitUser) {
        if(unitUser == null)
        {
            return;
        }
		for (int i = 0; i < unitUser.length(); i++) {
			JSONValue item = JSONValue.from(unitUser.optJSONObject(i));
			if (item.hasValue()) {
				UnitUserInfo unitUserInfo = new UnitUserInfo(
						item.getLong("id"),
						item.getString("unitCode"),
						item.getString("userCode"),
						item.getLong("sort"),
						item.getLong("status"));
				if (getOperType().equals(OPER_TYPE_ADD)) {
					unitUserInfo.save();
				} else if (getOperType().equals(OPER_TYPE_DELETE)) {
					unitUserInfo.delete();
				}
			}
		}
	}
}
