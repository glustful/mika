package com.yxst.epic.yixin.push.rest;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

//@Rest(rootUrl = "http://115.29.107.77:8090", converters = { StringHttpMessageConverter.class })
public interface IMsg {

	// if you need to add some configuration to the Spring RestTemplate.
	RestTemplate getRestTemplate();

	void setRestTemplate(RestTemplate restTemplate);

	/**
	 * 获取离线消息 <br/>
	 * http://host:port/1/msg/get?k={k}&m={m}<br/>
	 * http://115.29.107.77:8090/1/msg/get?k=QXF&m=0
	 * 
	 * @param k
	 *            key string 订阅key
	 * @param m
	 *            mid int64 最新接收的私有消息ID
	 * @return 
	 *         {"data":{"msgs":[{"msg":自定义消息体,"mid":1407392306172234,"gid":0}]},"ret"
	 *         :0}<br/>
	 *         [ret:0 成功;65534 参数错误;65535 内部错误]
	 * @throws RestClientException
	 */
	// @Get("/1/msg/get?k={k}&m={m}")
	public String msg(String k, int m);
}
