package com.yxst.epic.yixin.data.rest;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.androidannotations.api.rest.RestClientRootUrl;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.yxst.epic.yixin.data.dto.request.AddOrRemoveContactRequest;
import com.yxst.epic.yixin.data.dto.request.AddQunMemberRequest;
import com.yxst.epic.yixin.data.dto.request.CancelShieldMsgRequest;
import com.yxst.epic.yixin.data.dto.request.CheckUpdateRequest;
import com.yxst.epic.yixin.data.dto.request.CreateQunRequest;
import com.yxst.epic.yixin.data.dto.request.DelQunMemberRequest;
import com.yxst.epic.yixin.data.dto.request.GetAppOperationListRequest;
import com.yxst.epic.yixin.data.dto.request.GetApplicationListRequest;
import com.yxst.epic.yixin.data.dto.request.GetMemberRequest;
import com.yxst.epic.yixin.data.dto.request.GetOrgInfoRequest;
import com.yxst.epic.yixin.data.dto.request.GetOrgUserListRequest;
import com.yxst.epic.yixin.data.dto.request.GetQunMembersRequest;
import com.yxst.epic.yixin.data.dto.request.IsUserShieldAppRequest;
import com.yxst.epic.yixin.data.dto.request.LoginRequest;
import com.yxst.epic.yixin.data.dto.request.PushRequest;
import com.yxst.epic.yixin.data.dto.request.SearchRequest;
import com.yxst.epic.yixin.data.dto.request.SetShieldMsgRequest;
import com.yxst.epic.yixin.data.dto.request.SetUserAvatarRequest;
import com.yxst.epic.yixin.data.dto.request.UpdateQunTopicRequest;
import com.yxst.epic.yixin.data.dto.response.AddOrRemoveContactResponse;
import com.yxst.epic.yixin.data.dto.response.AddQunMemberResponse;
import com.yxst.epic.yixin.data.dto.response.CancelShieldMsgResponse;
import com.yxst.epic.yixin.data.dto.response.CheckUpdateResponse;
import com.yxst.epic.yixin.data.dto.response.CreateQunResponse;
import com.yxst.epic.yixin.data.dto.response.DelQunMemberResponse;
import com.yxst.epic.yixin.data.dto.response.GetAppOperationListResponse;
import com.yxst.epic.yixin.data.dto.response.GetApplicationListResponse;
import com.yxst.epic.yixin.data.dto.response.GetMemberResponse;
import com.yxst.epic.yixin.data.dto.response.GetOrgInfoResponse;
import com.yxst.epic.yixin.data.dto.response.GetOrgUserListResponse;
import com.yxst.epic.yixin.data.dto.response.GetQunMembersResponse;
import com.yxst.epic.yixin.data.dto.response.IsUserShieldAppResponse;
import com.yxst.epic.yixin.data.dto.response.LoginResponse;
import com.yxst.epic.yixin.data.dto.response.PushResponse;
import com.yxst.epic.yixin.data.dto.response.SearchResponse;
import com.yxst.epic.yixin.data.dto.response.SetShieldMsgResponse;
import com.yxst.epic.yixin.data.dto.response.SetUserAvatarResponse;
import com.yxst.epic.yixin.data.dto.response.UpdateQunTopicResponse;

//@Rest(rootUrl = "http://115.29.107.77:8093/app/client/device", converters = { MappingJackson2HttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
// @Rest(rootUrl = "http://115.29.226.14:8093/app/client/device", converters = {
// MappingJackson2HttpMessageConverter.class }, interceptors = {
// HttpBasicAuthenticatorInterceptor.class })
//@Rest(rootUrl = "http://10.180.120.63:8093/app/client/device", converters = { MappingJackson2HttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
//@Rest(rootUrl = "http://192.168.1.22:8093/app/client/device", converters = { MappingJackson2HttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
//@Rest(rootUrl = "http://192.168.1.16:8093/app/client/device", converters = { MappingJackson2HttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
@Rest(rootUrl = "http://"+YixinHost.YixinHost+":"+YixinHost.YixinPort+"/app/client/device", converters = { MappingJackson2HttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
//@Rest(rootUrl = "http://http://10.180.120.157:8093/app/client/device", converters = { MappingJackson2HttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
@Accept(MediaType.APPLICATION_JSON)
public interface Appmsgsrv8093 extends RestClientErrorHandling,
		RestClientRootUrl, RestClientSupport {

//	void setHeader(String name, String value);
//
//	String getHeader(String name);
	
	@Post("/push")
//	@RequiresHeader(value = {"Connection"})
	PushResponse push(PushRequest request);

	@Post("/create-qun")
	CreateQunResponse createQun(CreateQunRequest request);

	@Post("/getQunMembers")
	GetQunMembersResponse getQunMembers(GetQunMembersRequest request);

	@Post("/updateQunTopic")
	UpdateQunTopicResponse updateQunTopic(UpdateQunTopicRequest request);

	@Post("/addQunMember")
	AddQunMemberResponse addQunMember(AddQunMemberRequest request);

	@Post("/delQunMember")
	DelQunMemberResponse delQunMember(DelQunMemberRequest request);

	@Post("/setShieldMsg")
	SetShieldMsgResponse setShieldMsg(SetShieldMsgRequest request);
	
	@Post("/cancelShieldMsg")
	CancelShieldMsgResponse cancelShieldMsg(CancelShieldMsgRequest request);
	
	@Post("/isUserShieldApp")
	IsUserShieldAppResponse isUserShieldApp(IsUserShieldAppRequest request);
}
