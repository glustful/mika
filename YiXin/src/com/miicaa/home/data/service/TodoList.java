package com.miicaa.home.data.service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.miicaa.common.base.Debugger;
import com.miicaa.common.base.JSONValue;
import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-12-16.
 */
public class TodoList {
	private static final String TAG = "Cache_Service";
	private com.miicaa.home.data.OnFinish finish = null;
	private ArrayList<TodoListItem> list = null;

	public TodoList(com.miicaa.home.data.OnFinish onFinish) {
		finish = onFinish;
		list = new ArrayList<TodoListItem>();
	}

	public void getTodoList() {
		Debugger.d(TAG, "I'm going to read todolist.");

		_getTodoList(new com.miicaa.home.data.OnFinish() {
			@Override
			public void onSuccess(JSONObject res) {
				doTodoList();
			}

			@Override
			public void onFailed(String msg) {

			}
		});
	}

	private void _getTodoList(final com.miicaa.home.data.OnFinish onFinish) {
		Debugger.d(TAG, "now, todolist has readed.");
		list.clear();

		if (AccountInfo.instance().getLastUserInfo() != null) {
			CacheRequest.getUndo(new OnBusinessResponse() {
				@Override
				public void onProgress(ProgressMessage msg) {

				}

				@Override
				public void onResponse(ResponseData data) {
					if (data.getResultState() == ResponseData.ResultState.eSuccess) {
						JSONArray undoList = data.getJsonArray();
						for (int i = 0; i < undoList.length(); i++) {
							JSONValue item = JSONValue.from(undoList.optJSONObject(i));
							if (item.hasValue()) {
								String dataType = item.getString("dataType");
								if (dataType.equals(TodoListItem.DATA_TYPE_OUG)) {
									list.add(new TodoListItemOUG(item));
								}
							}
						}
						onFinish.onSuccess(null);
					}
				}
			}, "11111111111", AccountInfo.instance().getLastUserInfo().getCode(), 11);
		}
	}

	public Boolean doTodoList() {
		Debugger.d(TAG, "I'm going to run each todolist item.");
		for (TodoListItem item : list) {
			item.run();
		}
		if (finish != null) {
			finish.onSuccess(null);
		}
		return true;
	}

	public void run() {
		Debugger.d(TAG, "run todolist update.");
		getTodoList();
	}
}
