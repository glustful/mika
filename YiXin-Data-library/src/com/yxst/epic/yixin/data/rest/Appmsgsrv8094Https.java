package com.yxst.epic.yixin.data.rest;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.androidannotations.api.rest.RestClientRootUrl;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.yxst.epic.yixin.data.dto.request.AddOrRemoveContactRequest;
import com.yxst.epic.yixin.data.dto.request.CheckUpdateRequest;
import com.yxst.epic.yixin.data.dto.request.GetAppOperationListRequest;
import com.yxst.epic.yixin.data.dto.request.GetApplicationListRequest;
import com.yxst.epic.yixin.data.dto.request.GetMemberRequest;
import com.yxst.epic.yixin.data.dto.request.GetOrgInfoRequest;
import com.yxst.epic.yixin.data.dto.request.GetOrgUserListRequest;
import com.yxst.epic.yixin.data.dto.request.LoginRequest;
import com.yxst.epic.yixin.data.dto.request.SearchRequest;
import com.yxst.epic.yixin.data.dto.request.SetUserAvatarRequest;
import com.yxst.epic.yixin.data.dto.request.SetUserInfoRequest;
import com.yxst.epic.yixin.data.dto.response.AddOrRemoveContactResponse;
import com.yxst.epic.yixin.data.dto.response.CheckUpdateResponse;
import com.yxst.epic.yixin.data.dto.response.GetAppOperationListResponse;
import com.yxst.epic.yixin.data.dto.response.GetApplicationListResponse;
import com.yxst.epic.yixin.data.dto.response.GetMemberResponse;
import com.yxst.epic.yixin.data.dto.response.GetOrgInfoResponse;
import com.yxst.epic.yixin.data.dto.response.GetOrgUserListResponse;
import com.yxst.epic.yixin.data.dto.response.LoginResponse;
import com.yxst.epic.yixin.data.dto.response.SearchResponse;
import com.yxst.epic.yixin.data.dto.response.SetUserAvatarResponse;
import com.yxst.epic.yixin.data.dto.response.SetUserInfoResponse;

//@Rest(rootUrl = "http://115.29.107.77:8093/app/client/device", converters = { MappingJackson2HttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
@Rest(rootUrl = "https://10.180.128.127/app/client/device", converters = { MappingJackson2HttpMessageConverter.class, StringHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
// @Rest(rootUrl = "http://115.29.226.14:8093/app/client/device", converters = {
// MappingJackson2HttpMessageConverter.class }, interceptors = {
// HttpBasicAuthenticatorInterceptor.class })
//@Rest(rootUrl = "http://192.168.1.122:8093/app/client/device", converters = { MappingJackson2HttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
@Accept(MediaType.APPLICATION_JSON)
public interface Appmsgsrv8094Https extends RestClientErrorHandling,
		RestClientRootUrl, RestClientSupport {

//	void setHeader(String name, String value);
//
//	String getHeader(String name);
	
	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/login")
//	@RequiresHeader(value = {"Connection"})
	LoginResponse login(LoginRequest request);

	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/getOrgInfo")
	GetOrgInfoResponse getOrgInfo(GetOrgInfoRequest request);

	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/getOrgUserList")
	GetOrgUserListResponse getOrgUserList(GetOrgUserListRequest request);

	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/addOrRemoveContact")
	AddOrRemoveContactResponse addOrRemoveContact(
			AddOrRemoveContactRequest request);

	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/getMember")
	GetMemberResponse getMember(GetMemberRequest request);

	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/search")
	SearchResponse search(SearchRequest request);

	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/checkUpdate")
	CheckUpdateResponse checkUpdate(CheckUpdateRequest request);

	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/getApplicationList")
	GetApplicationListResponse getApplicationList(
			GetApplicationListRequest request);

	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/getAppOperationList")
	GetAppOperationListResponse getAppOperationList(
			GetAppOperationListRequest request);

	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/setUserAvatar")
	SetUserAvatarResponse setUserAvatar(SetUserAvatarRequest request);
	
	/**
	 * 8094
	 * @param request
	 * @return
	 */
	@Post("/setUserInfo")
	SetUserInfoResponse setUserInfo(SetUserInfoRequest request);
	
	@Accept(MediaType.TEXT_PLAIN)
	@Get("/getWelcomeImg")
	String getWelcomeImg();
}
