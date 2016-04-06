package com.yxst.epic.yixin.push.rest;

//import org.androidannotations.annotations.rest.Post;
//import org.androidannotations.annotations.rest.Rest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

//@Rest(rootUrl = "http://115.29.107.77:8091", converters = {StringHttpMessageConverter.class })
public interface IPush {

	// if you need to add some configuration to the Spring RestTemplate.
	RestTemplate getRestTemplate();

	void setRestTemplate(RestTemplate restTemplate);

	/**
	 * 推送消息给指定KEY(TAG)的用户 <br/>
	 * http://host:port/1/admin/push/private?key={key}&expire={expire}<br/>
	 * http://115.29.107.77:8091/1/admin/push/private?key=QXF&expire=600
	 * 
	 * @param key
	 *            推送目标的KEY
	 * @param expire
	 *            消息过期时间（秒）
	 * @return {"ret" : 0}<br/>
	 *         [ret:0 成功;65534 参数错误;65535 内部错误]
	 * @throws RestClientException
	 */
	// @Post("/1/admin/push/private?key={key}&expire={expire}")
	public String push(String key, long expire, String msg);
}
