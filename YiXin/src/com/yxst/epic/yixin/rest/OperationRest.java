package com.yxst.epic.yixin.rest;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.androidannotations.api.rest.RestClientRootUrl;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.yxst.epic.yixin.data.rest.HttpBasicAuthenticatorInterceptor;

@Rest(converters = { MappingJackson2HttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
public interface OperationRest extends RestClientErrorHandling,
		RestClientRootUrl, RestClientSupport {

	@Get("")
	@Accept(MediaType.APPLICATION_JSON)
	ServiceResult<Object> get(); 
	
	@Post("")
	@Accept(MediaType.APPLICATION_JSON)
	ServiceResult<Object> post(Object obj);
}
