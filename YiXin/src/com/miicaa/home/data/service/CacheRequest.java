package com.miicaa.home.data.service;

import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-12-27.
 */
public class CacheRequest {
	/*
	<form name="test" action="http://localhost:8080/mobile/mobile/task/execute"  method="post">
		<input type="text" id="operType" name="operType" value="add"/>
		<input type="text" id="taskType" name="taskType" value="oug"/>
		<input type="text" id="param" name="param" value='{"dataType":"user","codes":["U=94267604627653","U=94267653284488"]}'/>
		<input type="text" id="dataId" name="dataId" value=""/>
		</form>
		这个是任务执行呢方法
		http://localhost:8080/mobile/mobile/task/undo?clientCode=11111111111&userCode=U%3Df0136f1e&seqs=11
		这个是获取任务列表呢方法


	http://10.180.120.147/mobile/mobile/task/undo?clientCode=11111111111&userCode=U%3Df0136f1e&seqs=11
	http://10.180.120.147/mobile/mobile/task/execute?
		operType=add&
		taskType=oug&
		param={%22dataType%22:%22user%22,%22codes%22:[%22U=94267604627653%22,%22U=94267653284488%22]}&
		dataId=
	 */

	public static Boolean getUndo(final OnBusinessResponse onResponse, String clientCode, String userCode, int seqs) {
		if (onResponse == null)
			return false;
		return new RequestAdpater() {
			@Override
			public void onReponse(ResponseData data) {
				onResponse.onResponse(data);
			}

			@Override
			public void onProgress(ProgressMessage msg) {
				onResponse.onProgress(msg);
			}
		}.setUrl("/mobile/mobile/task/undo")
				.addParam("clientCode", clientCode)
				.addParam("userCode", userCode)
				.addParam("seqs", String.valueOf(seqs))
				.notifyRequest();
	}

	public static Boolean execUndo(final OnBusinessResponse onResponse, TodoListItem item) {
		if (onResponse == null)
			return false;
		return new RequestAdpater() {
			@Override
			public void onReponse(ResponseData data) {
				onResponse.onResponse(data);
			}

			@Override
			public void onProgress(ProgressMessage msg) {
				onResponse.onProgress(msg);
			}
		}.setUrl("/mobile/mobile/task/execute")
				.addParam("operType", item.getOperType())
				.addParam("taskType", item.getDataType())
				.addParam("param", item.getParam())
				.addParam("dataId", item.getDataId())
				.notifyRequest();
	}
}
