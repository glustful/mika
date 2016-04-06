//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.yxst.epic.yixin.data.rest;

import java.util.ArrayList;
import java.util.Collections;
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
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public final class Appmsgsrv8094_
    implements Appmsgsrv8094
{

    private RestTemplate restTemplate;
    private String rootUrl;
    private RestErrorHandler restErrorHandler;

    public Appmsgsrv8094_() {
        restTemplate = new RestTemplate();
        rootUrl = "http://10.180.120.63:8094/app/client/device";
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.setInterceptors(new ArrayList<ClientHttpRequestInterceptor>());
        restTemplate.getInterceptors().add(new HttpBasicAuthenticatorInterceptor());
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Override
    public void setRestTemplate(RestTemplate arg0) {
        this.restTemplate = arg0;
    }

    @Override
    public String getRootUrl() {
        return rootUrl;
    }

    @Override
    public void setRootUrl(String arg0) {
        this.rootUrl = arg0;
    }

    @Override
    public void setRestErrorHandler(RestErrorHandler arg0) {
        this.restErrorHandler = arg0;
    }

    @Override
    public String getWelcomeImg() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("text/plain")));
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/getWelcomeImg"), HttpMethod.GET, requestEntity, String.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public SetUserAvatarResponse setUserAvatar(SetUserAvatarRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<SetUserAvatarRequest> requestEntity = new HttpEntity<SetUserAvatarRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/setUserAvatar"), HttpMethod.POST, requestEntity, SetUserAvatarResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public GetAppOperationListResponse getAppOperationList(GetAppOperationListRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<GetAppOperationListRequest> requestEntity = new HttpEntity<GetAppOperationListRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/getAppOperationList"), HttpMethod.POST, requestEntity, GetAppOperationListResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public SetUserInfoResponse setUserInfo(SetUserInfoRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<SetUserInfoRequest> requestEntity = new HttpEntity<SetUserInfoRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/setUserInfo"), HttpMethod.POST, requestEntity, SetUserInfoResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public SearchResponse search(SearchRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<SearchRequest> requestEntity = new HttpEntity<SearchRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/search"), HttpMethod.POST, requestEntity, SearchResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public AddOrRemoveContactResponse addOrRemoveContact(AddOrRemoveContactRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<AddOrRemoveContactRequest> requestEntity = new HttpEntity<AddOrRemoveContactRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/addOrRemoveContact"), HttpMethod.POST, requestEntity, AddOrRemoveContactResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public CheckUpdateResponse checkUpdate(CheckUpdateRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<CheckUpdateRequest> requestEntity = new HttpEntity<CheckUpdateRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/checkUpdate"), HttpMethod.POST, requestEntity, CheckUpdateResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public GetMemberResponse getMember(GetMemberRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<GetMemberRequest> requestEntity = new HttpEntity<GetMemberRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/getMember"), HttpMethod.POST, requestEntity, GetMemberResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public GetOrgInfoResponse getOrgInfo(GetOrgInfoRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<GetOrgInfoRequest> requestEntity = new HttpEntity<GetOrgInfoRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/getOrgInfo"), HttpMethod.POST, requestEntity, GetOrgInfoResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<LoginRequest> requestEntity = new HttpEntity<LoginRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/login"), HttpMethod.POST, requestEntity, LoginResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public GetOrgUserListResponse getOrgUserList(GetOrgUserListRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<GetOrgUserListRequest> requestEntity = new HttpEntity<GetOrgUserListRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/getOrgUserList"), HttpMethod.POST, requestEntity, GetOrgUserListResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public GetApplicationListResponse getApplicationList(GetApplicationListRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("application/json")));
        HttpEntity<GetApplicationListRequest> requestEntity = new HttpEntity<GetApplicationListRequest>(request, httpHeaders);
        try {
            return restTemplate.exchange(rootUrl.concat("/getApplicationList"), HttpMethod.POST, requestEntity, GetApplicationListResponse.class).getBody();
        } catch (RestClientException e) {
            if (restErrorHandler!= null) {
                restErrorHandler.onRestClientExceptionThrown(e);
                return null;
            } else {
                throw e;
            }
        }
    }

}
